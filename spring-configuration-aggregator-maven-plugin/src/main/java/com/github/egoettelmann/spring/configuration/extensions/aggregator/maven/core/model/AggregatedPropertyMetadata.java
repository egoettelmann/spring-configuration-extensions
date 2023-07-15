package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AggregatedPropertyMetadata {

    private String name;
    private String type;
    private String description;
    private String defaultValue;
    private Map<String, String> profiles;
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
