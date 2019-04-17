package com.eresearch.elsevier.scopus.consumer.dto;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(of = {"dcIdentifier", "dcTitle"})
@Getter
@Setter
@NoArgsConstructor
public class ScopusSearchViewEntry {

    @JsonProperty("@force-array")
    private String forceArray;

    @JsonProperty("error")
    private String error;

    @JsonProperty("@_fa")
    private String fa;

    @JsonProperty("link")
    private Collection<ScopusSearchViewLink> links;

    @JsonProperty("prism:url")
    private String prismUrl;

    @JsonProperty("dc:identifier")
    private String dcIdentifier;

    @JsonProperty("eid")
    private String eid;

    @JsonProperty("dc:title")
    private String dcTitle;

    @JsonProperty("prism:aggregationType")
    private String prismAggregationType;

    @JsonProperty("citedby-count")
    private String citedByCount;

    @JsonProperty("prism:publicationName")
    private String prismPublicationName;

    @JsonProperty("prism:isbn")
    private String prismIsbn;

    @JsonProperty("prism:issn")
    private String prismIssn;

    @JsonProperty("prism:eIssn")
    private String prismEissn;

    @JsonProperty("prism:volume")
    private String prismVolume;

    @JsonProperty("prism:issueIdentifier")
    private String prismIssueIdentifier;

    @JsonProperty("prism:pageRange")
    private String prismPageRange;

    @JsonProperty("prism:coverDate")
    private String prismCoverDate;

    @JsonProperty("prism:coverDisplayDate")
    private String prismCoverDisplayDate;

    @JsonProperty("prism:doi")
    private String prismDoi;

    @JsonProperty("pii")
    private String pii;

    @JsonProperty("pubmed-id")
    private String pubmedId;

    @JsonProperty("orcid")
    private String orcId;

    @JsonProperty("dc:creator")
    private String dcCreator;

    @JsonProperty("affiliation")
    private Collection<ScopusSearchAffiliation> affiliations;

    @JsonProperty("author")
    private Collection<ScopusSearchAuthor> authors;

    @JsonProperty("author-count")
    private ScopusSearchAuthorCount authorCount;

    @JsonProperty("dc:description")
    private String dcDescription;

    @JsonProperty("authkeywords")
    private String authorKeywords;

    @JsonProperty("article-number")
    private String articleNumber;

    @JsonProperty("subtype")
    private String subtype;

    @JsonProperty("subtypeDescription")
    private String subtypeDescription;

    @JsonProperty("source-id")
    private String sourceId;

    @JsonProperty("fund-acr")
    private String fundingAgencyAcronym;

    @JsonProperty("fund-no")
    private String fundingAgencyIdentification;

    @JsonProperty("fund-sponsor")
    private String fundingAgencyName;

    @JsonProperty("message")
    private String message;

    @JsonProperty("openaccess")
    private String openAccess;

    @JsonProperty("openaccessFlag")
    private String openAccessFlag;
}
