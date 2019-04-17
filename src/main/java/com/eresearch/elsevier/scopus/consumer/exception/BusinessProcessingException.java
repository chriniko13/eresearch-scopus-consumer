package com.eresearch.elsevier.scopus.consumer.exception;

import com.eresearch.elsevier.scopus.consumer.error.EresearchElsevierScopusConsumerError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class BusinessProcessingException extends Exception {

	private static final long serialVersionUID = -4352150800142237666L;

	private final EresearchElsevierScopusConsumerError eresearchElsevierScopusConsumerError;

	public BusinessProcessingException(EresearchElsevierScopusConsumerError eresearchElsevierScopusConsumerError, String message, Throwable cause) {
		super(message, cause);
		this.eresearchElsevierScopusConsumerError = eresearchElsevierScopusConsumerError;
	}

	public BusinessProcessingException(EresearchElsevierScopusConsumerError eresearchElsevierScopusConsumerError, String message) {
		super(message);
		this.eresearchElsevierScopusConsumerError = eresearchElsevierScopusConsumerError;
	}

	public EresearchElsevierScopusConsumerError getEresearchElsevierScopusConsumerError() {
		return eresearchElsevierScopusConsumerError;
	}
}
