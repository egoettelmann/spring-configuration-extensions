package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValueAnnotationMetadataBuilder {

    private final String rawValue;
    private String type;
    private String description;
    private String sourceType;

    private ValueAnnotationMetadataBuilder(String rawValue) {
        this.rawValue = rawValue;
    }

    public static ValueAnnotationMetadataBuilder of(final String rawValue) {
        return new ValueAnnotationMetadataBuilder(rawValue);
    }

    public ValueAnnotationMetadataBuilder type(final String type) {
        this.type = type;
        return this;
    }

    public ValueAnnotationMetadataBuilder description(final String description) {
        this.description = description;
        return this;
    }

    public ValueAnnotationMetadataBuilder sourceType(final String sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public List<ValueAnnotationMetadata> build() {
        final List<ValueAnnotationMetadata> results = new ArrayList<>();

        final ValueAnnotationParser parser = new ValueAnnotationParser();
        final Map<String, String> properties = parser.parse(this.rawValue);

        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            final ValueAnnotationMetadata metadata = new ValueAnnotationMetadata();
            metadata.setType(this.type);
            metadata.setDescription(this.description);
            metadata.setSourceType(this.sourceType);
            metadata.setName(entry.getKey());
            metadata.setDefaultValue(entry.getValue());
            results.add(metadata);
        }

        return results;
    }

}
