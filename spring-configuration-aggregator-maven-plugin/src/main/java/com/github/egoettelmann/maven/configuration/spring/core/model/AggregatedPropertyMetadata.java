package com.github.egoettelmann.maven.configuration.spring.core.model;

import lombok.Data;

import java.util.List;

@Data
public class AggregatedPropertyMetadata {

    private String name;
    private String type;
    private String description;
    private String defaultValue;
    private List<Source> sourceTypes;

    @Data
    public static class Wrapper {
        private List<AggregatedPropertyMetadata> properties;
    }

    @Data
    public static class Source {
        private String groupId;
        private String artifactId;
        private String sourceType;
    }

}
