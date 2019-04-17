package com.eresearch.elsevier.scopus.consumer.validator;

import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerDto;
import com.eresearch.elsevier.scopus.consumer.error.EresearchElsevierScopusConsumerError;
import com.eresearch.elsevier.scopus.consumer.exception.DataValidationException;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/*
Note:   ElsevierScopusConsumerDto->scopusAuthorIdentifierNumber should NOT be null,
        but other attributes can.
 */
@Component
@Log4j
public class ElsevierScopusConsumerDtoValidator implements Validator<ElsevierScopusConsumerDto> {

    private static final String EMPTY_STRING = "";

    @Override
    public void validate(ElsevierScopusConsumerDto elsevierScopusConsumerDto) throws DataValidationException {

        // first validation...
        if (Objects.isNull(elsevierScopusConsumerDto)) {
            log.error("ElsevierScopusConsumerDtoValidator#validate --- error occurred (first validation) --- elsevierScopusConsumerDto = " + elsevierScopusConsumerDto);
            throw new DataValidationException(
                    EresearchElsevierScopusConsumerError.DATA_VALIDATION_ERROR,
                    EresearchElsevierScopusConsumerError.DATA_VALIDATION_ERROR.getMessage());
        }

        // second validation...
        if (Objects.isNull(elsevierScopusConsumerDto.getScopusAuthorIdentifierNumber())
                || EMPTY_STRING.equals(elsevierScopusConsumerDto.getScopusAuthorIdentifierNumber())) {
            log.error("ElsevierScopusConsumerDtoValidator#validate --- error occurred (second validation) --- elsevierScopusConsumerDto = " + elsevierScopusConsumerDto);
            throw new DataValidationException(EresearchElsevierScopusConsumerError.DATA_VALIDATION_ERROR, EresearchElsevierScopusConsumerError.DATA_VALIDATION_ERROR.getMessage());
        }

    }
}
