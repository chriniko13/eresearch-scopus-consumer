package com.eresearch.elsevier.scopus.consumer.service;

import com.codahale.metrics.Timer;
import com.eresearch.elsevier.scopus.consumer.application.configuration.JmsConfiguration;
import com.eresearch.elsevier.scopus.consumer.connector.ScopusSearchConnector;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerDto;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusFinderQueueResultDto;
import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;
import com.eresearch.elsevier.scopus.consumer.metrics.entries.ServiceLayerMetricEntry;
import com.eresearch.elsevier.scopus.consumer.repository.ScopusConsumerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@Log4j
public class ElsevierScopusConsumerServiceImpl implements ElsevierScopusConsumerService {

    @Value("${enable.persistence.results}")
    private String enablePersistenceForResults;

    @Autowired
    private ScopusSearchConnector scopusSearchConnector;

    @Autowired
    private Clock clock;

    @Autowired
    private ScopusConsumerRepository scopusConsumerRepository;

    @Autowired
    private ServiceLayerMetricEntry serviceLayerMetricEntry;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("elsevierObjectMapper")
    private ObjectMapper objectMapper;

    @Override
    public ElsevierScopusConsumerResultsDto elsevierScopusConsumerOperation(ElsevierScopusConsumerDto elsevierScopusConsumerDto) throws BusinessProcessingException {

        Timer.Context context = serviceLayerMetricEntry.getServiceLayerTimer().time();
        try {

            ElsevierScopusConsumerResultsDto result = new ElsevierScopusConsumerResultsDto();

            String authorIdentifierNumber = elsevierScopusConsumerDto.getScopusAuthorIdentifierNumber();

            String queryToSearchFor = constructQueryToSearchFor(authorIdentifierNumber);

            List<ScopusConsumerResultsDto> scopusConsumerResultsDtos
                    = scopusSearchConnector.searchScopusExhaustive(queryToSearchFor);

            result.setRequestedElsevierScopusConsumerDto(elsevierScopusConsumerDto);
            result.setResultsSize(scopusConsumerResultsDtos.size());
            result.setResults(scopusConsumerResultsDtos);
            result.setOperationResult(Boolean.TRUE);
            result.setProcessFinishedDate(Instant.now(clock));

            if (Boolean.valueOf(enablePersistenceForResults)) {
                scopusConsumerRepository.save(elsevierScopusConsumerDto, result);
            }

            return result;

        } catch (BusinessProcessingException ex) {
            log.error("ElsevierScopusConsumerServiceImpl#elsevierScopusConsumerOperation --- error occurred.", ex);
            throw ex;
        } finally {
            context.stop();
        }
    }

    @Override
    public void scopusNonBlockConsumption(String transactionId, ElsevierScopusConsumerDto elsevierScopusConsumerDto) {

        ScopusFinderQueueResultDto scopusFinderQueueResultDto = null;
        try {

            ElsevierScopusConsumerResultsDto scopusResults
                    = this.elsevierScopusConsumerOperation(elsevierScopusConsumerDto);

            scopusFinderQueueResultDto = new ScopusFinderQueueResultDto(transactionId, null, scopusResults);

        } catch (BusinessProcessingException e) {

            log.error("ElsevierScopusConsumerServiceImpl#scopusNonBlockConsumption --- error occurred.", e);

            ElsevierScopusConsumerResultsDto scopusResults = new ElsevierScopusConsumerResultsDto();
            scopusResults.setOperationResult(false);

            scopusFinderQueueResultDto = new ScopusFinderQueueResultDto(transactionId, e.toString(), scopusResults);

        } finally {

            try {
                String resultForQueue = objectMapper.writeValueAsString(scopusFinderQueueResultDto);
                jmsTemplate.convertAndSend(JmsConfiguration.QUEUES.SCOPUS_RESULTS_QUEUE, resultForQueue);
            } catch (JsonProcessingException e) {
                //we can't do much things for the moment here...
                log.error("ElsevierScopusConsumerServiceImpl#scopusNonBlockConsumption --- error occurred.", e);
            }

        }

    }

    private String constructQueryToSearchFor(String authorIdentifierNumber) {
        return "AU-ID(" + authorIdentifierNumber + ")&view=COMPLETE";
    }
}
