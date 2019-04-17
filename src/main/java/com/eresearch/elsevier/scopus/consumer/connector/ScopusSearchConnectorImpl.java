package com.eresearch.elsevier.scopus.consumer.connector;


import com.codahale.metrics.Timer;
import com.eresearch.elsevier.scopus.consumer.connector.communicator.Communicator;
import com.eresearch.elsevier.scopus.consumer.connector.guard.NoResultsAvailableGuard;
import com.eresearch.elsevier.scopus.consumer.connector.guard.UniqueEntriesGuard;
import com.eresearch.elsevier.scopus.consumer.connector.pagination.ScopusSearchPaginationResourcesCalculator;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusSearchViewLink;
import com.eresearch.elsevier.scopus.consumer.error.EresearchElsevierScopusConsumerError;
import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;
import com.eresearch.elsevier.scopus.consumer.metrics.entries.ConnectorLayerMetricEntry;
import com.eresearch.elsevier.scopus.consumer.worker.WorkerDispatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Log4j
public class ScopusSearchConnectorImpl implements ScopusSearchConnector {

    private static final String API_KEY = "apikey";
    private static final String QUERY = "query";

    private static final Integer DEFAULT_RESOURCE_PAGE_COUNT = 25;
    private static final String START_QUERY_PARAM = "start";
    private static final String COUNT_QUERY_PARAM = "count";

    private static final String FIRST_LINK_REF_VALUE = "first";
    private static final String LAST_LINK_REF_VALUE = "last";

    @Value("${scopus.apikey.consumer}")
    private String apiKey;

    @Value("${scopus-author-search.url}")
    private String scopusAuthorSearchUrl;

    @Qualifier("elsevierObjectMapper")
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScopusSearchPaginationResourcesCalculator scopusSearchPaginationResourcesCalculator;

    @Autowired
    private WorkerDispatcher workerDispatcher;

    @Autowired
    @Qualifier("basicCommunicator")
    private Communicator communicator;

    @Autowired
    private UniqueEntriesGuard uniqueEntriesGuard;

    @Autowired
    private ConnectorLayerMetricEntry connectorLayerMetricEntry;

    @Autowired
    private NoResultsAvailableGuard noResultsAvailableGuard;

    @Override
    public List<ScopusConsumerResultsDto> searchScopusExhaustive(String query) throws BusinessProcessingException {

        Timer.Context context = connectorLayerMetricEntry.getConnectorLayerTimer().time();
        try {

            List<ScopusConsumerResultsDto> results = new ArrayList<>();

            URI uri = constructUri(query);

            ScopusConsumerResultsDto firstResult = pullAndFetchInfo(uri);
            results.add(firstResult);

            //check if we have results for the provided query...
            if (noResultsAvailableGuard.test(firstResult)) return Collections.emptyList();

            List<URI> allResourcesToHit = calculateAllResources(firstResult, query);

            if (workerDispatcher.shouldDispatch(allResourcesToHit.size())) { //if we need to split the load then...

                results.addAll(workerDispatcher.performTask(allResourcesToHit, extractInfoWithProvidedResource()));

            } else {

                for (URI resourceToHit : allResourcesToHit) {
                    ScopusConsumerResultsDto result = pullAndFetchInfo(resourceToHit);
                    results.add(result);
                }

            }

            uniqueEntriesGuard.apply(results);
            return results;

        } catch (BusinessProcessingException e) {

            log.error("ScopusSearchConnectorImpl#searchScopusExhaustive --- error occurred.", e);
            throw e;

        } catch (RestClientException | IOException e) {

            log.error("ScopusSearchConnectorImpl#searchScopusExhaustive --- error occurred.", e);
            throw new BusinessProcessingException(
                    EresearchElsevierScopusConsumerError.BUSINESS_PROCESSING_ERROR,
                    EresearchElsevierScopusConsumerError.BUSINESS_PROCESSING_ERROR.getMessage(),
                    e);

        } catch (CompletionException e) {

            log.error("ScopusSearchConnectorImpl#searchScopusExhaustive --- error occurred.", e.getCause());
            throw new BusinessProcessingException(
                    EresearchElsevierScopusConsumerError.BUSINESS_PROCESSING_ERROR,
                    EresearchElsevierScopusConsumerError.BUSINESS_PROCESSING_ERROR.getMessage(),
                    e.getCause());

        } finally {
            context.stop();
        }
    }

    private Function<URI, ScopusConsumerResultsDto> extractInfoWithProvidedResource() {
        return resource -> {
            try {
                return pullAndFetchInfo(resource);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        };
    }

    private List<URI> calculateAllResources(ScopusConsumerResultsDto firstResult, String query) throws BusinessProcessingException {

        try {
            // Note: if we do not have a last resource page, we get out of here!
            Optional<ScopusSearchViewLink> firstResourcePage = extractFirstResourcePage(firstResult);
            if (!firstResourcePage.isPresent()) {
                return Collections.emptyList();
            }

            // Note: if we do not have a last resource page, we get out of here!
            Optional<ScopusSearchViewLink> lastResourcePage = extractLastResourcePageLink(firstResult);
            if (!lastResourcePage.isPresent()) {
                return Collections.emptyList();
            }

            List<String> startPageQueryParamsOfAllResources
                    = scopusSearchPaginationResourcesCalculator.calculateStartPageQueryParams(firstResourcePage.get().getHref(),
                    lastResourcePage.get().getHref(),
                    DEFAULT_RESOURCE_PAGE_COUNT);

            return startPageQueryParamsOfAllResources.stream()
                    .map(startPageQueryParam -> constructUri(query,
                            startPageQueryParam,
                            String.valueOf(DEFAULT_RESOURCE_PAGE_COUNT)))
                    .collect(Collectors.toList());

        } catch (BusinessProcessingException ex) {
            log.error("ScopusSearchConnectorImpl#calculateAllResources --- error occurred.", ex);
            throw ex;
        }

    }

    private Optional<ScopusSearchViewLink> extractLastResourcePageLink(ScopusConsumerResultsDto firstResult) {
        return firstResult
                .getScopusConsumerSearchViewDto()
                .getLinks()
                .stream()
                .filter(link -> link.getRef().contains(LAST_LINK_REF_VALUE))
                .findAny();
    }

    private Optional<ScopusSearchViewLink> extractFirstResourcePage(ScopusConsumerResultsDto firstResult) throws BusinessProcessingException {
        return firstResult
                .getScopusConsumerSearchViewDto()
                .getLinks()
                .stream()
                .filter(link -> link.getRef().contains(FIRST_LINK_REF_VALUE))
                .findAny();
    }

    private ScopusConsumerResultsDto pullAndFetchInfo(URI uri) throws IOException {

        log.info("ScopusSearchConnectorImpl#pullAndFetchInfo, will hit url: " + uri.toString());

        String resultInString = communicator.communicateWithElsevier(uri);
        return objectMapper.readValue(resultInString, ScopusConsumerResultsDto.class);
    }

    private URI constructUri(String query, String startQueryParam, String countQueryParam) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(scopusAuthorSearchUrl);
        builder.queryParam(API_KEY, apiKey);
        builder.queryParam(QUERY, query);
        builder.queryParam(START_QUERY_PARAM, startQueryParam);
        builder.queryParam(COUNT_QUERY_PARAM, countQueryParam);

        return builder.build().toUri();
    }

    private URI constructUri(String query) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(scopusAuthorSearchUrl);
        builder.queryParam(API_KEY, apiKey);
        builder.queryParam(QUERY, query);

        return builder.build().toUri();
    }
}
