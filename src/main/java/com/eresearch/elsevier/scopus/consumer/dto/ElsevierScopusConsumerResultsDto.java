package com.eresearch.elsevier.scopus.consumer.dto;


import java.time.Instant;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ElsevierScopusConsumerResultsDto {

    @JsonProperty("operation-result")
    private Boolean operationResult;

    @JsonProperty("process-finished-date")
    private Instant processFinishedDate;

    @JsonProperty("requested-elsevier-scopus-consumer-dto")
    private ElsevierScopusConsumerDto requestedElsevierScopusConsumerDto;

    @JsonProperty("fetched-results-size")
    private Integer resultsSize;

    @JsonProperty("fetched-results")
    private Collection<ScopusConsumerResultsDto> results;
}
