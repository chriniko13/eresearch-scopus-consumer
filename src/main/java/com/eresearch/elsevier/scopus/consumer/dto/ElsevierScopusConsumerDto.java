package com.eresearch.elsevier.scopus.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ElsevierScopusConsumerDto {

    @JsonProperty("au-id")
    private String scopusAuthorIdentifierNumber;
}
