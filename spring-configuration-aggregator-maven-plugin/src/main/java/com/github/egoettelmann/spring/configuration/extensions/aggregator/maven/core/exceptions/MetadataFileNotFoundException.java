package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions;

public class MetadataFileNotFoundException extends Exception {

    public MetadataFileNotFoundException(String message) {
        super(message);
    }

    public MetadataFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
