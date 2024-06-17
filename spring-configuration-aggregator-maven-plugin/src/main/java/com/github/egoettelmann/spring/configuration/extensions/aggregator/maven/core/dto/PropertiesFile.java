package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import lombok.Data;

/**
 * Holds a reference to a properties file
 */
@Data
public class PropertiesFile {

    private String path;

    /**
     * Instantiates the properties file reference
     */
    public PropertiesFile() {
    }

    /**
     * Instantiates the properties file reference with a path
     *
     * @param path the path to the properties file
     */
    public PropertiesFile(String path) {
        this.path = path;
    }

    /**
     * Set the properties file path.
     *
     * @param path the path to the properties file
     */
    public void set(final String path) {
        this.path = path;
    }

}
