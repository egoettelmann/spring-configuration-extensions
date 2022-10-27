package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import lombok.Data;

@Data
public class PropertiesFile {

    private String path;

    public PropertiesFile() {
    }

    public PropertiesFile(String path) {
        this.path = path;
    }

    public void set(final String path) {
        this.path = path;
    }

}
