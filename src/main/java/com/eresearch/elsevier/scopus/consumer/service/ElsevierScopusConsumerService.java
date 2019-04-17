package com.eresearch.elsevier.scopus.consumer.service;

import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerDto;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;

public interface ElsevierScopusConsumerService {

    ElsevierScopusConsumerResultsDto elsevierScopusConsumerOperation(ElsevierScopusConsumerDto elsevierScopusConsumerDto) throws BusinessProcessingException;

    void scopusNonBlockConsumption(String transactionId, ElsevierScopusConsumerDto elsevierScopusConsumerDto);
}
