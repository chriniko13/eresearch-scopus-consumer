package com.eresearch.elsevier.scopus.consumer.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScopusConsumerResultsDto {

    @JsonProperty("search-results")
    private ScopusConsumerSearchViewDto scopusConsumerSearchViewDto;
}
