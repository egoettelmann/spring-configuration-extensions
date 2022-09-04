package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions;

public class OperationFailedException extends RuntimeException {

    public OperationFailedException(String message) {
        super(message);
    }

    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
