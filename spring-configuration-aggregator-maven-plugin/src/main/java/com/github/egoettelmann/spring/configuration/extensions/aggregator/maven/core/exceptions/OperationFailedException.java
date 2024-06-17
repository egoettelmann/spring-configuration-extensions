package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions;

/**
 * Exception thrown when a plugin operation failed
 */
public class OperationFailedException extends RuntimeException {

    /**
     * Instantiates the exception.
     *
     * @param message the message
     */
    public OperationFailedException(String message) {
        super(message);
    }

    /**
     * Instantiates the exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
