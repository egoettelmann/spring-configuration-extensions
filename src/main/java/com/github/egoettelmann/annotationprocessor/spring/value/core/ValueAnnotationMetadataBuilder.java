package com.github.egoettelmann.annotationprocessor.spring.value.core;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueAnnotationMetadataBuilder {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]*)}");

    private static final String DEFAULT_VALUE_SEPARATOR = ":";

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

    public Optional<ValueAnnotationMetadata> build() {
        ValueAnnotationMetadata metadata = new ValueAnnotationMetadata();

        // Adding metadata
        metadata.setType(this.type);
        metadata.setDescription(this.description);
        metadata.setSourceType(this.sourceType);

        // Extracting property name
        final Optional<String> definition = extractPropertyDefinition(this.rawValue);
        if (!definition.isPresent()) {
            return Optional.empty();
        }
        final String definitionValue = definition.get();
        final String[] values = definitionValue.split(DEFAULT_VALUE_SEPARATOR, 2);
        metadata.setName(values[0].trim());

        // Extracting default value of defined
        if (values.length > 1) {
            metadata.setDefaultValue(values[1].trim());
        }

        return Optional.of(metadata);
    }

    private static Optional<String> extractPropertyDefinition(final String rawValue) {
        final Matcher matcher = PATTERN.matcher(rawValue);
        if (!matcher.find()) {
            return Optional.empty();
        }

        final String match = matcher.group(1);
        if (match == null) {
            return Optional.empty();
        }

        return Optional.of(match.trim());
    }

}
