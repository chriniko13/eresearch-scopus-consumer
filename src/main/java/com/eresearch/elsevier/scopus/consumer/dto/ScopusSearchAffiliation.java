package com.eresearch.elsevier.scopus.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class ScopusSearchAffiliation {

    @JsonProperty("@_fa")
    private String fa;

    @JsonProperty("affilname")
    private String affiliationName;

    @JsonProperty("affiliation-city")
    private String affiliationCity;

    @JsonProperty("affiliation-country")
    private String affiliationCountry;

    @JsonProperty("afid")
    private String affiliationId;

    @JsonProperty("affiliation-url")
    private String affiliationUrl;

    @JsonProperty("name-variant")
    private Collection<ScopusSearchAffiliationNameVariant> affiliationNameVariant;
}
