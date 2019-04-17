package com.eresearch.elsevier.scopus.consumer.error;

public enum EresearchElsevierScopusConsumerError {

    // --- business errors ---
    BUSINESS_PROCESSING_ERROR("Could not perform business operation."),

    // --- validation errors ---
    DATA_VALIDATION_ERROR("Provided data is invalid"),

    // --- connection errors ---
    CONNECTOR_CONNECTION_ERROR("Connector connection error.");

    private String message;

    EresearchElsevierScopusConsumerError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
