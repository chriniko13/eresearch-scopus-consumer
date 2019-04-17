package com.eresearch.elsevier.scopus.consumer.validator;

import com.eresearch.elsevier.scopus.consumer.exception.DataValidationException;

public interface Validator<T> {

    void validate(T data) throws DataValidationException;
}
