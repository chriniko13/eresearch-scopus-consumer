package com.eresearch.elsevier.scopus.consumer.dto;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScopusConsumerSearchViewDto {

    @JsonProperty("opensearch:totalResults")
    private String totalResults;

    @JsonProperty("opensearch:startIndex")
    private String startIndex;

    @JsonProperty("opensearch:itemsPerPage")
    private String itemsPerPage;

    @JsonProperty("opensearch:Query")
    private ScopusSearchViewQuery query;

    @JsonProperty("link")
    private Collection<ScopusSearchViewLink> links;

    @JsonProperty("entry")
    private Collection<ScopusSearchViewEntry> entries;
}
