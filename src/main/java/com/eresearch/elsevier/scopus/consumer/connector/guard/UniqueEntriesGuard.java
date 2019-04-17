package com.eresearch.elsevier.scopus.consumer.connector.guard;

import com.eresearch.elsevier.scopus.consumer.dto.ScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusConsumerSearchViewDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusSearchViewEntry;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusSearchViewQuery;
import com.eresearch.elsevier.scopus.consumer.error.EresearchElsevierScopusConsumerError;
import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j
@Component
public class UniqueEntriesGuard {

    @Value("${perform.unique.entries.guard.reporting}")
    private String performUniqueEntriesGuardReporting;

    @Autowired
    private ObjectFactory<MemoGuardStack> memoGuardStackObjectFactory;

    @Autowired
    private ObjectFactory<MemoResultsReporter> memoResultsReporterObjectFactory;

    public void apply(List<ScopusConsumerResultsDto> results) throws BusinessProcessingException {

        if (noResultsExist(results)) return;

        final MemoGuardStack memoGuardStack = memoGuardStackObjectFactory.getObject();
        final MemoResultsReporter memoResultsReporter = memoResultsReporterObjectFactory.getObject();

        for (int i = 0; i < results.size(); i++) {

            boolean isLastResult = (i == results.size() - 1);

            ScopusConsumerResultsDto result = results.get(i);
            Integer totalUniqueResults = extractTotalUniqueResultsElsevierScopusProvides(result);
            ScopusSearchViewQuery authorSearchViewQuery = result.getScopusConsumerSearchViewDto().getQuery();

            Optional<MemoGuard> memoPeek = memoGuardStack.peek();
            if (memoPeek.isPresent()) { //if stack is not empty.

                Collection<ScopusSearchViewEntry> notUniqueAuthorSearchViewEntries = getNotUniqueAuthorSearchViewEntries(result);
                MemoGuard memoToTestAgainst = new MemoGuard(totalUniqueResults, authorSearchViewQuery, notUniqueAuthorSearchViewEntries);

                if (memoPeek.get().equals(memoToTestAgainst)) {

                    //add the entry...
                    memoGuardStack.push(memoToTestAgainst);

                } else {
                    applyGuard(memoGuardStack, totalUniqueResults, memoResultsReporter, authorSearchViewQuery);

                    //add the new entry...
                    memoGuardStack.push(memoToTestAgainst);
                }

            } else { //if stack is empty.

                //add new entry...
                Collection<ScopusSearchViewEntry> notUniqueAuthorSearchViewEntries = getNotUniqueAuthorSearchViewEntries(result);
                MemoGuard memoGuard = new MemoGuard(totalUniqueResults, authorSearchViewQuery, notUniqueAuthorSearchViewEntries);
                memoGuardStack.push(memoGuard);
            }

            if (isLastResult) {
                applyGuard(memoGuardStack, totalUniqueResults, memoResultsReporter, authorSearchViewQuery);
            }
        }
    }

    private boolean noResultsExist(List<ScopusConsumerResultsDto> results) {
        if (results.size() == 1) {
            ScopusConsumerSearchViewDto scopusConsumerSearchViewDto = results.get(0).getScopusConsumerSearchViewDto();

            boolean noResults = "0".equals(scopusConsumerSearchViewDto.getTotalResults())
                    && "0".equals(scopusConsumerSearchViewDto.getStartIndex())
                    && "0".equals(scopusConsumerSearchViewDto.getItemsPerPage())
                    && scopusConsumerSearchViewDto.getEntries() != null
                    && scopusConsumerSearchViewDto.getEntries().size() == 1
                    && scopusConsumerSearchViewDto.getEntries().iterator().next().getError().equals("Result set was empty");

            if (noResults) return true;
        }
        return false;
    }

    private void applyGuard(MemoGuardStack memoGuardStack,
                            Integer totalUniqueResults,
                            MemoResultsReporter memoResultsReporter,
                            ScopusSearchViewQuery authorSearchViewQuery) throws BusinessProcessingException {

        //time to accumulate memos...
        Integer accumulateUniqueEntriesSize = memoGuardStack.accumulateUniqueEntriesSize();

        //see if we have the proper results...
        String infoMessage = String.format("UniqueEntriesGuard#apply, accumulateUniqueEntriesSize = %s , elsevierTotalResults = %s.",
                accumulateUniqueEntriesSize, totalUniqueResults);
        log.info(infoMessage);

        if (Boolean.valueOf(performUniqueEntriesGuardReporting)) {
            memoResultsReporter.reportResults(accumulateUniqueEntriesSize, totalUniqueResults, authorSearchViewQuery);
        }

        if (!Objects.equals(accumulateUniqueEntriesSize, totalUniqueResults)) {
            throw new BusinessProcessingException(EresearchElsevierScopusConsumerError.BUSINESS_PROCESSING_ERROR, EresearchElsevierScopusConsumerError.BUSINESS_PROCESSING_ERROR.getMessage());
        }

        //clean stack...
        memoGuardStack.clean();
    }

    private Collection<ScopusSearchViewEntry> getNotUniqueAuthorSearchViewEntries(ScopusConsumerResultsDto result) {
        return result.getScopusConsumerSearchViewDto().getEntries();
    }

    private Integer extractTotalUniqueResultsElsevierScopusProvides(ScopusConsumerResultsDto result) throws BusinessProcessingException {
        return Integer.valueOf(
                Optional.ofNullable(result)
                        .map(ScopusConsumerResultsDto::getScopusConsumerSearchViewDto)
                        .map(ScopusConsumerSearchViewDto::getTotalResults)
                        .orElseThrow(() -> new BusinessProcessingException(EresearchElsevierScopusConsumerError.BUSINESS_PROCESSING_ERROR, EresearchElsevierScopusConsumerError.BUSINESS_PROCESSING_ERROR.getMessage())));
    }


}
