package com.eresearch.elsevier.scopus.consumer.connector.guard;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.eresearch.elsevier.scopus.consumer.dto.ScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusSearchViewEntry;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class NoResultsAvailableGuard implements Predicate<ScopusConsumerResultsDto> {

    @Override
    public boolean test(ScopusConsumerResultsDto scopusConsumerResultsDto) {
        return noResultsAvailable(scopusConsumerResultsDto);
    }

    private boolean noResultsAvailable(ScopusConsumerResultsDto scopusConsumerResultsDto) {

        final String forceArrayValueWhenNoResults = "true";
        final String errorValueWhenNoResults = "Result set was empty";

        ScopusSearchViewEntry authorSearchViewEntry = scopusConsumerResultsDto
                .getScopusConsumerSearchViewDto()
                .getEntries()
                .stream()
                .findFirst()
                .get(); //note: here we should not have any problem.

        if (forceArrayValueWhenNoResults.equals(authorSearchViewEntry.getForceArray())
                && errorValueWhenNoResults.equals(authorSearchViewEntry.getError())) {
            log.info("NoResultsAvailableGuard#noResultsAvailable --- no results for provided info.");
            return true;
        }
        return false;
    }
}
