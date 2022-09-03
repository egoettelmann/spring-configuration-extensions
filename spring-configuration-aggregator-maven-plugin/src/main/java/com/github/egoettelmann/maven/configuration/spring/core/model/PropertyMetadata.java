package com.github.egoettelmann.maven.configuration.spring.core.model;

import lombok.Data;

import java.util.List;

@Data
public class PropertyMetadata {

    private String name;
    private String type;
    private String description;
    private String defaultValue;
    private String sourceType;

    @Data
    public static class Wrapper {
        private List<PropertyMetadata> properties;
    }

}
