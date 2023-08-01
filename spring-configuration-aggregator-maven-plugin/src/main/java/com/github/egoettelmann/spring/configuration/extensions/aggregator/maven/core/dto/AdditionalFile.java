package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import lombok.Data;

@Data
public class AdditionalFile {

    private String path;

    public AdditionalFile() {
    }

    public AdditionalFile(String path) {
        this.path = path;
    }

    public void set(final String path) {
        this.path = path;
    }

}
