package com.eresearch.elsevier.scopus.consumer.connector;


import com.eresearch.elsevier.scopus.consumer.dto.ScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;

import java.util.List;

public interface ScopusSearchConnector {

    List<ScopusConsumerResultsDto> searchScopusExhaustive(String query) throws BusinessProcessingException;

}
