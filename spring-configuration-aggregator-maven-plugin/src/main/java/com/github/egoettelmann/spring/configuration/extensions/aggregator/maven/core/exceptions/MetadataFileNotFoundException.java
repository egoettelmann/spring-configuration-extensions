package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions;

/**
 * Exception thrown when a metadata file reference could not be found
 */
public class MetadataFileNotFoundException extends Exception {

    /**
     * Instantiates the exception.
     *
     * @param message the exception message
     */
    public MetadataFileNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates the exception.
     *
     * @param message the exception message
     * @param cause   the cause
     */
    public MetadataFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
