package com.eresearch.elsevier.scopus.consumer.repository;


import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerDto;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerResultsDto;

public interface ScopusConsumerRepository {


    void save(ElsevierScopusConsumerDto elsevierScopusConsumerDto, ElsevierScopusConsumerResultsDto result);
}
