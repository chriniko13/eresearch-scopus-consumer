package com.eresearch.elsevier.scopus.consumer.resource;


import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerDto;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusFinderImmediateResultDto;
import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;
import com.eresearch.elsevier.scopus.consumer.exception.DataValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface ElsevierScopusConsumerResource {

    DeferredResult<ElsevierScopusConsumerResultsDto> elsevierScopusConsumerOperation(ElsevierScopusConsumerDto elsevierScopusConsumerDto) throws BusinessProcessingException;

    ResponseEntity<ScopusFinderImmediateResultDto> authorFinderNonBlockConsumption(String transactionId, ElsevierScopusConsumerDto elsevierScopusConsumerDto) throws DataValidationException;
}
