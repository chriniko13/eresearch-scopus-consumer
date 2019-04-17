package com.eresearch.elsevier.scopus.consumer.connector.pagination;

import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;

import java.util.List;

public interface ScopusSearchPaginationResourcesCalculator {

    List<String> calculateStartPageQueryParams(String firstResourcePage,
                                               String lastResourcePage,
                                               Integer resourcePageCount) throws BusinessProcessingException;
}
