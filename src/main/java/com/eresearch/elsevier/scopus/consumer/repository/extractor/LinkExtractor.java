package com.eresearch.elsevier.scopus.consumer.repository.extractor;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eresearch.elsevier.scopus.consumer.dto.ScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusSearchViewLink;

@Component
public class LinkExtractor {

    private static final String DEFAULT_VALUE = "NO_VALUE";

    public List<String> extractLinksConsumedFromElsevierApi(ScopusConsumerResultsDto scopusConsumerResultsDto,
                                                            LinkExtractorRefIdentifier linkExtractorRefIdentifier) {

        if (linkExtractorRefIdentifier != LinkExtractorRefIdentifier.SELF) {
            throw new IllegalArgumentException("This method can only used with 'SELF' strategy extraction");
        }

        return scopusConsumerResultsDto
                .getScopusConsumerSearchViewDto()
                .getLinks()
                .stream()
                .filter(link -> link.getRef().equals(linkExtractorRefIdentifier.getValue()))
                .map(ScopusSearchViewLink::getHref)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    public String extractLinkConsumedFromElsevierApi(ScopusConsumerResultsDto scopusConsumerResultsDto,
                                                     LinkExtractorRefIdentifier linkExtractorRefIdentifier) {


        if (linkExtractorRefIdentifier != LinkExtractorRefIdentifier.FIRST
                && linkExtractorRefIdentifier != LinkExtractorRefIdentifier.LAST) {
            throw new IllegalArgumentException("This method can only used with 'FIRST' || 'LAST' strategy extraction");
        }

        List<String> links = scopusConsumerResultsDto
                .getScopusConsumerSearchViewDto()
                .getLinks()
                .stream()
                .filter(link -> link.getRef().equals(linkExtractorRefIdentifier.getValue()))
                .map(ScopusSearchViewLink::getHref)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());


        return Optional.ofNullable(links)
                .filter(l -> !l.isEmpty())
                .filter(l -> l.size() == 1)
                .map(l -> l.get(0))
                .orElse(DEFAULT_VALUE);
    }
}