package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Data
public class AggregatedPropertyMetadata {

    private String name;
    private String type;
    private String description;
    private Object defaultValue;
    private Map<String, String> profiles;
    private Set<Source> sourceTypes;

    @Data
    public static class Wrapper {
        private List<AggregatedPropertyMetadata> properties;
    }

    @Data
    public static class Source implements Comparable<Source> {
        private String groupId;
        private String artifactId;
        private String sourceType;

        @Override
        public int compareTo(final Source o) {
            return Comparator.comparing(defaultString(Source::getGroupId))
                .thenComparing(defaultString(Source::getArtifactId))
                .thenComparing(defaultString(Source::getSourceType))
                .compare(this, o);
        }

        private Function<Source, String> defaultString(Function<Source, String> function) {
            return source -> StringUtils.defaultString(function.apply(source));
        }
    }

}
