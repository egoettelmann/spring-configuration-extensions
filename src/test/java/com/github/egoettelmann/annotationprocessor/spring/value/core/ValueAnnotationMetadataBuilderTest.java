package com.github.egoettelmann.annotationprocessor.spring.value.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class ValueAnnotationMetadataBuilderTest {

    @Test
    public void testSimpleDefinition() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${custom.property}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("custom.property", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSimpleDefinitionWithDefaultValue() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${custom.property:defaultVal}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("custom.property", metadata.get().getName(), "Wrong name");
        Assertions.assertEquals("defaultVal", metadata.get().getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testDefinitionWithSpecialCharacters() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${custom.property-path.upperCase.with_underscores.dot}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("custom.property-path.upperCase.with_underscores.dot", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSpelDefinition() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${custom.property}}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("custom.property", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSpelDefinitionWithMethodCall() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{'${custom.property}'.split(',')}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("custom.property", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSpelDefinitionWithSpace() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{new ${custom.property}()}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("custom.property", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSpelDefinitionWithMethodCallAndDefaultValue() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{'${custom.property:defaultVal}'.split(',')}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("custom.property", metadata.get().getName(), "Wrong name");
        Assertions.assertEquals("defaultVal", metadata.get().getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testSpelDefinitionWithSpaceAndDefaultValue() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{new ${custom.property:defaultVal}()}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("custom.property", metadata.get().getName(), "Wrong name");
        Assertions.assertEquals("defaultVal", metadata.get().getDefaultValue(), "Wrong default value");
    }

    @Disabled("Disabled until value parser supports extracting multiple values")
    @Test
    public void testSpelDefinitionWithMultipleValues() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{new FakeConstructor(${custom.property1:defaultVal}, ${custom.property2:defaultVal})}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("custom.property1", metadata.get().getName(), "Wrong name");
        Assertions.assertEquals("custom.property2", metadata.get().getName(), "Wrong name");
        Assertions.assertEquals("defaultVal", metadata.get().getDefaultValue(), "Wrong default value");
    }

}
