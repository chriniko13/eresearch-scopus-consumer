package com.eresearch.elsevier.scopus.consumer.exception;

import com.eresearch.elsevier.scopus.consumer.error.EresearchElsevierScopusConsumerError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DataValidationException extends Exception {

    private static final long serialVersionUID = -4807690929767265288L;

    private final EresearchElsevierScopusConsumerError eresearchElsevierScopusConsumerError;

    public DataValidationException(EresearchElsevierScopusConsumerError eresearchElsevierScopusConsumerError, String message) {
        super(message);
        this.eresearchElsevierScopusConsumerError = eresearchElsevierScopusConsumerError;
    }

    public EresearchElsevierScopusConsumerError getEresearchElsevierScopusConsumerError() {
        return eresearchElsevierScopusConsumerError;
    }
}
