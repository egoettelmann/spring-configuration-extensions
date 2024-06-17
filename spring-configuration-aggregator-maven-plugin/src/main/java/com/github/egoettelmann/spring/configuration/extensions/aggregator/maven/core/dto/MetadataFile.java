package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import lombok.Data;

/**
 * Holds a reference to a metadata file
 */
@Data
public class MetadataFile {

    private String path;

    /**
     * Instantiates the metadata file reference
     */
    public MetadataFile() {
    }

    /**
     * Instantiates the metadata file reference with a path
     *
     * @param path the path to the metadata file
     */
    public MetadataFile(String path) {
        this.path = path;
    }

    /**
     * Set the metadata file path.
     *
     * @param path the path to the metadata file
     */
    public void set(final String path) {
        this.path = path;
    }

}
