package com.github.egoettelmann.annotationprocessor.spring.value.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Tests defined in this class are based on following examples:
 * <a href="https://www.baeldung.com/spring-value-annotation">https://www.baeldung.com/spring-value-annotation</a>
 */
public class ValueAnnotationMetadataBuilderComplianceTest {

    /**
     * <code>@Value("string value")</code>
     * <p>
     * Note: nothing to configure, so no configuration property to extract.
     */
    @Test
    public void testComplianceBasic1() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("string value")
                .build();
        Assertions.assertFalse(metadata.isPresent(), "Metadata should be null");
    }

    /**
     * <code>@Value("${value.from.file}")</code>
     */
    @Test
    public void testComplianceBasic2() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${value.from.file}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("value.from.file", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("${systemValue}")</code>
     */
    @Test
    public void testComplianceBasic3() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${systemValue}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("systemValue", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("${priority}")</code>
     */
    @Test
    public void testComplianceBasic4() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${priority}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("priority", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("${listOfValues}")</code>
     */
    @Test
    public void testComplianceBasic5() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${listOfValues}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("listOfValues", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{systemProperties['priority']}")</code>
     * <p>
     * Note: nothing to configure from a configuration file, no no configuration value to extract.
     */
    @Test
    public void testComplianceSpel1() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{systemProperties['priority']}")
                .build();
        Assertions.assertFalse(metadata.isPresent(), "Metadata should be null");
    }

    /**
     * <code>@Value("#{systemProperties['unknown'] ?: 'some default'}")</code>
     * <p>
     * Note: nothing to configure from a configuration file, no no configuration value to extract.
     */
    @Test
    public void testComplianceSpel2() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{systemProperties['unknown'] ?: 'some default'}")
                .build();
        Assertions.assertFalse(metadata.isPresent(), "Metadata should be null");
    }

    /**
     * <code>@Value("#{someBean.someValue}")</code>
     * <p>
     * Note: nothing to configure from a configuration file, no no configuration value to extract.
     */
    @Test
    public void testComplianceSpel3() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{someBean.someValue}")
                .build();
        Assertions.assertFalse(metadata.isPresent(), "Metadata should be null");
    }

    /**
     * <code>@Value("#{'${listOfValues}'.split(',')}")</code>
     */
    @Test
    public void testComplianceSpel4() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{'${listOfValues}'.split(',')}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("listOfValues", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{${valuesMap}}")</code>
     */
    @Test
    public void testComplianceMap1() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("valuesMap", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{${valuesMap}.key1}")</code>
     * <p>
     * Note: SpEL expression should be parsed to ensure that '${valuesMap}' resolves to a map.
     * It could be more complex (like a constructor), in this case 'key1' would be class field.
     */
    @Test
    public void testComplianceMap2() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}.key1}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("valuesMap", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{${valuesMap}['unknownKey']}")</code>
     * <p>
     * Note: SpEL expression should be parsed to ensure that '${valuesMap}' resolves to a map.
     * See {@link #testComplianceMap2()}
     */
    @Test
    public void testComplianceMap3() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}['unknownKey']}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("valuesMap", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{${unknownMap : {key1: '1', key2: '2'}}}")</code>
     */
    @Disabled("Disabled until value parser has been reviewed")
    @Test
    public void testComplianceMap4() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${unknownMap : {key1: '1', key2: '2'}}}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("unknownMap", metadata.get().getName(), "Wrong name");
        Assertions.assertEquals("{key1: '1', key2: '2'}", metadata.get().getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testComplianceMap5() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}['unknownKey'] ?: 5}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("valuesMap", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testComplianceMap6() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}.?[value>'1']}")
                .build();
        Assertions.assertTrue(metadata.isPresent(), "Metadata should not be null");
        Assertions.assertEquals("valuesMap", metadata.get().getName(), "Wrong name");
        Assertions.assertNull(metadata.get().getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testComplianceMap7() {
        final Optional<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{systemProperties}")
                .build();
        Assertions.assertFalse(metadata.isPresent(), "Metadata should be null");
    }

}
