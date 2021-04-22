package com.github.egoettelmann.annotationprocessor.spring.value.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValueAnnotationMetadataBuilderTest {

    @Test
    public void testSimpleDefinition() {
        ValueAnnotationMetadata metadata = ValueAnnotationMetadataBuilder
                .of("${custom.property}")
                .build();
        Assertions.assertEquals("custom.property", metadata.getName(), "Wrong name");
        Assertions.assertNull(metadata.getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSimpleDefinitionWithDefaultValue() {
        ValueAnnotationMetadata metadata = ValueAnnotationMetadataBuilder
                .of("${custom.property:defaultVal}")
                .build();
        Assertions.assertEquals("custom.property", metadata.getName(), "Wrong name");
        Assertions.assertEquals("defaultVal", metadata.getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testDefinitionWithSpecialCharacters() {
        ValueAnnotationMetadata metadata = ValueAnnotationMetadataBuilder
                .of("${custom.property-path.upperCase.with_underscores.dot}")
                .build();
        Assertions.assertEquals("custom.property-path.upperCase.with_underscores.dot", metadata.getName(), "Wrong name");
        Assertions.assertNull(metadata.getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSpelDefinition() {
        ValueAnnotationMetadata metadata = ValueAnnotationMetadataBuilder
                .of("#{${custom.property}}")
                .build();
        Assertions.assertEquals("custom.property", metadata.getName(), "Wrong name");
        Assertions.assertNull(metadata.getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSpelDefinitionWithMethodCall() {
        ValueAnnotationMetadata metadata = ValueAnnotationMetadataBuilder
                .of("#{'${custom.property}'.split(',')}")
                .build();
        Assertions.assertEquals("custom.property", metadata.getName(), "Wrong name");
        Assertions.assertNull(metadata.getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSpelDefinitionWithSpace() {
        ValueAnnotationMetadata metadata = ValueAnnotationMetadataBuilder
                .of("#{new ${custom.property}()}")
                .build();
        Assertions.assertEquals("custom.property", metadata.getName(), "Wrong name");
        Assertions.assertNull(metadata.getDefaultValue(), "Default value should not be defined");
    }

    @Test
    public void testSpelDefinitionWithMethodCallAndDefaultValue() {
        ValueAnnotationMetadata metadata = ValueAnnotationMetadataBuilder
                .of("#{'${custom.property:defaultVal}'.split(',')}")
                .build();
        Assertions.assertEquals("custom.property", metadata.getName(), "Wrong name");
        Assertions.assertEquals("defaultVal", metadata.getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testSpelDefinitionWithSpaceAndDefaultValue() {
        ValueAnnotationMetadata metadata = ValueAnnotationMetadataBuilder
                .of("#{new ${custom.property:defaultVal}()}")
                .build();
        Assertions.assertEquals("custom.property", metadata.getName(), "Wrong name");
        Assertions.assertEquals("defaultVal", metadata.getDefaultValue(), "Wrong default value");
    }

}
