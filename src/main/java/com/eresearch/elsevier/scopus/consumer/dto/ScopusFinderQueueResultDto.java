package com.eresearch.elsevier.scopus.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScopusFinderQueueResultDto {
    private String transactionId;
    private String exceptionMessage;
    private ElsevierScopusConsumerResultsDto scopusConsumerResultsDto;
}
