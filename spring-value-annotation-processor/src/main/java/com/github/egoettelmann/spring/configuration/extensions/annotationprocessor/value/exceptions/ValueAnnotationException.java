package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.exceptions;

public class ValueAnnotationException extends RuntimeException {

    public ValueAnnotationException(String message) {
        super(message);
    }

    public ValueAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

}
