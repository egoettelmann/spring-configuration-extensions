package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.exceptions;

/**
 * Exception thrown on annotation processing failures
 */
public class ValueAnnotationException extends RuntimeException {

    /**
     * Instantiates the exception.
     *
     * @param message the exception message
     */
    public ValueAnnotationException(String message) {
        super(message);
    }

    /**
     * Instantiates the exception.
     *
     * @param message the exception message
     * @param cause the cause
     */
    public ValueAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

}
