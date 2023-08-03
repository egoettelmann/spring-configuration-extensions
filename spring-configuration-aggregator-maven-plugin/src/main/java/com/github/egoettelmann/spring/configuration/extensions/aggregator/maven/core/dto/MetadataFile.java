package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import lombok.Data;

@Data
public class MetadataFile {

    private String path;

    public MetadataFile() {
    }

    public MetadataFile(String path) {
        this.path = path;
    }

    public void set(final String path) {
        this.path = path;
    }

}
