package co.com.bancolombia.model;

import co.com.bancolombia.TechnicalException;

public class CustomRetryExhaustedException extends TechnicalException {

    public CustomRetryExhaustedException(String technicalMessage) {
        super(technicalMessage);
    }
}