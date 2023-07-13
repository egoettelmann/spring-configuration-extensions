package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("string value")
                .build();
        Assertions.assertTrue(metadata.isEmpty(), "Metadata should be empty");
    }

    /**
     * <code>@Value("${value.from.file}")</code>
     */
    @Test
    public void testComplianceBasic2() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${value.from.file}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("value.from.file", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("${systemValue}")</code>
     */
    @Test
    public void testComplianceBasic3() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${systemValue}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("systemValue", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("${priority}")</code>
     */
    @Test
    public void testComplianceBasic4() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${priority}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("priority", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("${listOfValues}")</code>
     */
    @Test
    public void testComplianceBasic5() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("${listOfValues}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("listOfValues", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{systemProperties['priority']}")</code>
     * <p>
     * Note: nothing to configure from a configuration file, no no configuration value to extract.
     */
    @Test
    public void testComplianceSpel1() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{systemProperties['priority']}")
                .build();
        Assertions.assertTrue(metadata.isEmpty(), "Metadata should be empty");
    }

    /**
     * <code>@Value("#{systemProperties['unknown'] ?: 'some default'}")</code>
     * <p>
     * Note: nothing to configure from a configuration file, no no configuration value to extract.
     */
    @Test
    public void testComplianceSpel2() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{systemProperties['unknown'] ?: 'some default'}")
                .build();
        Assertions.assertTrue(metadata.isEmpty(), "Metadata should be empty");
    }

    /**
     * <code>@Value("#{someBean.someValue}")</code>
     * <p>
     * Note: nothing to configure from a configuration file, no no configuration value to extract.
     */
    @Test
    public void testComplianceSpel3() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{someBean.someValue}")
                .build();
        Assertions.assertTrue(metadata.isEmpty(), "Metadata should be empty");
    }

    /**
     * <code>@Value("#{'${listOfValues}'.split(',')}")</code>
     */
    @Test
    public void testComplianceSpel4() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{'${listOfValues}'.split(',')}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("listOfValues", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{${valuesMap}}")</code>
     */
    @Test
    public void testComplianceMap1() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("valuesMap", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{${valuesMap}.key1}")</code>
     * <p>
     * Note: SpEL expression should be parsed to ensure that '${valuesMap}' resolves to a map.
     * It could be more complex (like a constructor), in this case 'key1' would be class field.
     */
    @Test
    public void testComplianceMap2() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}.key1}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("valuesMap", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{${valuesMap}['unknownKey']}")</code>
     * <p>
     * Note: SpEL expression should be parsed to ensure that '${valuesMap}' resolves to a map.
     * See {@link #testComplianceMap2()}
     */
    @Test
    public void testComplianceMap3() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}['unknownKey']}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("valuesMap", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    /**
     * <code>@Value("#{${unknownMap : {key1: '1', key2: '2'}}}")</code>
     */
    @Test
    public void testComplianceMap4() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${unknownMap : {key1: '1', key2: '2'}}}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("unknownMap", metadata.get(0).getName(), "Wrong name");
        Assertions.assertEquals("{key1: '1', key2: '2'}", metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testComplianceMap5() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}['unknownKey'] ?: 5}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("valuesMap", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testComplianceMap6() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{${valuesMap}.?[value>'1']}")
                .build();
        Assertions.assertEquals(1, metadata.size(), "Metadata has wrong size");
        Assertions.assertEquals("valuesMap", metadata.get(0).getName(), "Wrong name");
        Assertions.assertNull(metadata.get(0).getDefaultValue(), "Wrong default value");
    }

    @Test
    public void testComplianceMap7() {
        final List<ValueAnnotationMetadata> metadata = ValueAnnotationMetadataBuilder
                .of("#{systemProperties}")
                .build();
        Assertions.assertTrue(metadata.isEmpty(), "Metadata should be empty");
    }

}
