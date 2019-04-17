package com.eresearch.elsevier.scopus.consumer.repository;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.codahale.metrics.Timer;
import com.eresearch.elsevier.scopus.consumer.dao.ScopusConsumerDao;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerDto;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusConsumerSearchViewDto;
import com.eresearch.elsevier.scopus.consumer.metrics.entries.RepositoryLayerMetricEntry;
import com.eresearch.elsevier.scopus.consumer.repository.extractor.LinkExtractor;
import com.eresearch.elsevier.scopus.consumer.repository.extractor.LinkExtractorRefIdentifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository
public class ScopusConsumerRepositoryImpl implements ScopusConsumerRepository {

    @Autowired
    private Clock clock;

    @Qualifier("elsevierObjectMapper")
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("dbOperationsExecutor")
    private ExecutorService dbOperationsExecutor;

    @Autowired
    private RepositoryLayerMetricEntry repositoryLayerMetricEntry;

    @Autowired
    private ScopusConsumerDao scopusConsumerDao;

    @Autowired
    private LinkExtractor linkExtractor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Qualifier("transactionTemplate")
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void save(ElsevierScopusConsumerDto elsevierScopusConsumerDto, ElsevierScopusConsumerResultsDto elsevierScopusConsumerResultsDto) {

        Runnable task = saveTask(elsevierScopusConsumerDto, elsevierScopusConsumerResultsDto);
        CompletableFuture.runAsync(task, dbOperationsExecutor);
    }

    private Runnable saveTask(final ElsevierScopusConsumerDto elsevierScopusConsumerDto,
                              final ElsevierScopusConsumerResultsDto elsevierScopusConsumerResultsDto) {

        return () -> {

            Timer.Context context = repositoryLayerMetricEntry.getRepositoryLayerTimer().time();
            try {

                final String sql = scopusConsumerDao.getInsertQueryForSearchResultsTable();

                final ArrayList<ScopusConsumerResultsDto> resultsToStore = new ArrayList<>(elsevierScopusConsumerResultsDto.getResults());

                final String authorIdentifier = objectMapper.writeValueAsString(elsevierScopusConsumerDto);

                final Timestamp creationTimestamp = Timestamp.from(Instant.now(clock));

                this.executeSaveStatements(sql, resultsToStore, authorIdentifier, creationTimestamp);

                log.info("ScopusConsumerRepositoryImpl#save --- operation completed successfully.");

            } catch (JsonProcessingException e) {

                log.error("ScopusConsumerRepositoryImpl#save --- error occurred --- not even tx initialized.", e);

            } finally {
                context.stop();
            }
        };
    }

    private void executeSaveStatements(final String sql,
                                       final ArrayList<ScopusConsumerResultsDto> resultsToStore,
                                       final String authorIdentifier,
                                       final Timestamp creationTimestamp) {
        for (ScopusConsumerResultsDto scopusConsumerResultsDto : resultsToStore) {
            executeSaveStatement(sql, authorIdentifier, creationTimestamp, scopusConsumerResultsDto);
        }
    }

    private void executeSaveStatement(final String sql,
                                      final String authorIdentifier,
                                      final Timestamp creationTimestamp,
                                      final ScopusConsumerResultsDto scopusConsumerResultsDto) {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus txStatus) {

                try {
                    List<String> linksConsumedPerEntry = linkExtractor.extractLinksConsumedFromElsevierApi(
                            scopusConsumerResultsDto,
                            LinkExtractorRefIdentifier.SELF);
                    String linksConsumedPerEntryStr = objectMapper.writeValueAsString(linksConsumedPerEntry);

                    String firstLink = linkExtractor.extractLinkConsumedFromElsevierApi(scopusConsumerResultsDto,
                            LinkExtractorRefIdentifier.FIRST);

                    String lastLink = linkExtractor.extractLinkConsumedFromElsevierApi(scopusConsumerResultsDto,
                            LinkExtractorRefIdentifier.LAST);

                    ScopusConsumerSearchViewDto scopusConsumerSearchViewDto = scopusConsumerResultsDto.getScopusConsumerSearchViewDto();
                    String scopusConsumerSearchViewDtoStr = objectMapper.writeValueAsString(scopusConsumerSearchViewDto);

                    jdbcTemplate.update(sql,
                            authorIdentifier,
                            scopusConsumerSearchViewDtoStr,
                            linksConsumedPerEntryStr,
                            firstLink,
                            lastLink,
                            creationTimestamp);

                } catch (DataAccessException | JsonProcessingException e) {

                    log.error("ScopusConsumerRepositoryImpl#save --- error occurred --- proceeding with rollback.", e);
                    txStatus.setRollbackOnly();

                }
            }
        });

    }
}
