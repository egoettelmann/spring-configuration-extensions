package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builder for {@link ValueAnnotationMetadata}.
 */
public class ValueAnnotationMetadataBuilder {

    private final String rawValue;
    private String type;
    private String description;
    private String sourceType;

    /**
     * Instantiates the builder.
     *
     * @param rawValue the raw value
     */
    private ValueAnnotationMetadataBuilder(String rawValue) {
        this.rawValue = rawValue;
    }

    /**
     * Creates a builder instance.
     *
     * @param rawValue the raw value
     * @return the builder instance
     */
    public static ValueAnnotationMetadataBuilder of(final String rawValue) {
        return new ValueAnnotationMetadataBuilder(rawValue);
    }

    /**
     * Defines the type.
     *
     * @param type the type
     * @return the builder instance
     */
    public ValueAnnotationMetadataBuilder type(final String type) {
        this.type = type;
        return this;
    }

    /**
     * Defines the description.
     *
     * @param description the description
     * @return the builder instance
     */
    public ValueAnnotationMetadataBuilder description(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Defines the source type.
     *
     * @param sourceType the source type
     * @return the builder instance
     */
    public ValueAnnotationMetadataBuilder sourceType(final String sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    /**
     * Builds a list of value annotation metadata from the instance.
     *
     * @return the list of value annotation metadata
     */
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
