package com.github.egoettelmann.maven.configuration.spring.core.exceptions;

public class MetadataFileNotFoundException extends Exception {

    public MetadataFileNotFoundException(String message) {
        super(message);
    }

    public MetadataFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
