package com.eresearch.elsevier.scopus.consumer.connector.guard;

import java.util.Collection;

import com.eresearch.elsevier.scopus.consumer.dto.ScopusSearchViewEntry;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusSearchViewQuery;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = {"totalUniqueResults", "scopusSearchViewQuery"})
@Getter
@Setter
class MemoGuard {

    private Integer totalUniqueResults;
    private ScopusSearchViewQuery scopusSearchViewQuery;
    private Collection<ScopusSearchViewEntry> scopusSearchViewEntries;

    MemoGuard(Integer totalUniqueResults, ScopusSearchViewQuery scopusSearchViewQuery, Collection<ScopusSearchViewEntry> scopusSearchViewEntries) {
        this.totalUniqueResults = totalUniqueResults;
        this.scopusSearchViewQuery = scopusSearchViewQuery;
        this.scopusSearchViewEntries = scopusSearchViewEntries;
    }
}