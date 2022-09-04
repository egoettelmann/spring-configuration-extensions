package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model;

import lombok.Data;

import java.util.List;

@Data
public class ArtifactMetadata {

    private String groupId;
    private String artifactId;
    private String version;
    private String name;
    private String description;
    private List<AggregatedPropertyMetadata> properties;
    private Changes changes;

    @Data
    public static class Wrapper {
        private List<ArtifactMetadata> artifacts;
    }

    @Data
    public static class Changes {

        private String baseVersion;
        private List<String> added;
        private List<String> updated;
        private List<String> deleted;

    }

}
