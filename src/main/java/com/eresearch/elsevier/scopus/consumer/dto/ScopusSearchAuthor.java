package com.eresearch.elsevier.scopus.consumer.dto;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScopusSearchAuthor {

    @JsonProperty("@_fa")
    private String fa;

    @JsonProperty("@seq")
    private String sequenceInEntry;

    @JsonProperty("author-url")
    private String authorUrl;

    @JsonProperty("authid")
    private String authorId;

    @JsonProperty("orcid")
    private String orcId;

    @JsonProperty("authname")
    private String authorName;

    @JsonProperty("given-name")
    private String givenName;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("initials")
    private String initials;

    @JsonProperty("afid")
    private Collection<ScopusSearchAuthorAffiliationId> affiliationIds;

}
