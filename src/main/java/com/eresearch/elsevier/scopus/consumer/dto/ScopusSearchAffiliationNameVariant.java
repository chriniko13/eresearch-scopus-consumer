package com.eresearch.elsevier.scopus.consumer.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScopusSearchAffiliationNameVariant {

    @JsonProperty("@_fa")
    private String fa;

    @JsonProperty("$")
    private String value;
}
