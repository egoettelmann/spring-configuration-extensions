package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model;

import lombok.Data;

import java.util.List;

/**
 * Data holder for the metadata of a property.
 */
@Data
public class PropertyMetadata {

    private String name;
    private String type;
    private String description;
    private Object defaultValue;
    private String sourceType;

    /**
     * Wrapper to holder a list of metadata
     */
    @Data
    public static class Wrapper {
        private List<PropertyMetadata> properties;
    }

}
