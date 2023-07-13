package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ValueAnnotationParserTest {

    private final ValueAnnotationParser parser = new ValueAnnotationParser();

    @Test
    public void testSimpleDefinition() {
        final Map<String, String> values = this.parser.parse("${custom.property}");
        Assertions.assertEquals(1, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property"), "Wrong parsed value");
        Assertions.assertNull(values.get("custom.property"), "Wrong parsed default value");
    }

    @Test
    public void testSimpleDefinitionWithDefaultValue() {
        final Map<String, String> values = this.parser.parse("${custom.property:defaultVal}");
        Assertions.assertEquals(1, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property"), "Wrong parsed value");
        Assertions.assertEquals("defaultVal", values.get("custom.property"), "Wrong parsed default value");
    }

    @Test
    public void testNestedDefinition() {
        final Map<String, String> values = this.parser.parse("${custom.property1:${custom.property2}}");
        Assertions.assertEquals(2, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property1"), "Parsed values should contain 'a'");
        Assertions.assertEquals("${custom.property2}", values.get("custom.property1"), "Wrong parsed default value for 'a'");
        Assertions.assertTrue(values.containsKey("custom.property2"), "Parsed values should contain 'b'");
        Assertions.assertNull(values.get("custom.property2"), "Wrong parsed default value for 'b'");
    }

    @Test
    public void testDoubleNestedDefinition() {
        final Map<String, String> values = this.parser.parse("${a:${b:${c}}}");
        Assertions.assertEquals(3, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("a"), "Parsed values should contain 'a'");
        Assertions.assertEquals("${b:${c}}", values.get("a"), "Wrong parsed default value for 'a'");
        Assertions.assertTrue(values.containsKey("b"), "Parsed values should contain 'b'");
        Assertions.assertEquals("${c}", values.get("b"), "Wrong parsed default value for 'b'");
        Assertions.assertTrue(values.containsKey("c"), "Parsed values should contain 'c'");
        Assertions.assertNull(values.get("c"), "Wrong parsed default value for 'c'");
    }

    @Test
    public void testDoubleNestedDefinitionWithDefaultValue() {
        final Map<String, String> values = this.parser.parse("${a:${b:${c:defaultVal}}}");
        Assertions.assertEquals(3, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("a"), "Parsed values should contain 'a'");
        Assertions.assertEquals("${b:${c:defaultVal}}", values.get("a"), "Wrong parsed default value for 'a'");
        Assertions.assertTrue(values.containsKey("b"), "Parsed values should contain 'b'");
        Assertions.assertEquals("${c:defaultVal}", values.get("b"), "Wrong parsed default value for 'b'");
        Assertions.assertTrue(values.containsKey("c"), "Parsed values should contain 'c'");
        Assertions.assertEquals("defaultVal", values.get("c"), "Wrong parsed default value for 'c'");
    }

    @Test
    public void testDefinitionWithSpecialCharacters() {
        final Map<String, String> values = this.parser.parse("${custom.property-path.upperCase.with_underscores.dot}");
        Assertions.assertEquals(1, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property-path.upperCase.with_underscores.dot"), "Wrong parsed value");
        Assertions.assertNull(values.get("custom.property-path.upperCase.with_underscores.dot"), "Wrong parsed default value");
    }

    @Test
    public void testSpelDefinition() {
        final Map<String, String> values = this.parser.parse("#{${custom.property}}");
        Assertions.assertEquals(1, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property"), "Wrong parsed value");
        Assertions.assertNull(values.get("custom.property"), "Wrong parsed default value");
    }

    @Test
    public void testSpelDefinitionWithMethodCall() {
        final Map<String, String> values = this.parser.parse("#{'${custom.property}'.split(',')}");
        Assertions.assertEquals(1, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property"), "Wrong parsed value");
        Assertions.assertNull(values.get("custom.property"), "Wrong parsed default value");
    }

    @Test
    public void testSpelDefinitionWithSpace() {
        final Map<String, String> values = this.parser.parse("#{new ${custom.property}()}");
        Assertions.assertEquals(1, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property"), "Wrong parsed value");
        Assertions.assertNull(values.get("custom.property"), "Wrong parsed default value");
    }

    @Test
    public void testSpelDefinitionWithMethodCallAndDefaultValue() {
        final Map<String, String> values = this.parser.parse("#{'${custom.property:defaultVal}'.split(',')}");
        Assertions.assertEquals(1, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property"), "Wrong parsed value");
        Assertions.assertEquals("defaultVal", values.get("custom.property"), "Wrong parsed default value");
    }

    @Test
    public void testSpelDefinitionWithSpaceAndDefaultValue() {
        final Map<String, String> values = this.parser.parse("#{new ${custom.property:defaultVal}()}");
        Assertions.assertEquals(1, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property"), "Wrong parsed value");
        Assertions.assertEquals("defaultVal", values.get("custom.property"), "Wrong parsed default value");
    }

    @Test
    public void testSpelDefinitionWithMultipleValues() {
        final Map<String, String> values = this.parser.parse("#{new FakeConstructor(${custom.property1:defaultVal}, ${custom.property2:defaultVal})}");
        Assertions.assertEquals(2, values.size(), "Wrong number of parsed values");
        Assertions.assertTrue(values.containsKey("custom.property1"), "Wrong parsed value");
        Assertions.assertEquals("defaultVal", values.get("custom.property1"), "Wrong parsed default value");
        Assertions.assertTrue(values.containsKey("custom.property2"), "Wrong parsed value");
        Assertions.assertEquals("defaultVal", values.get("custom.property2"), "Wrong parsed default value");
    }

    @Test
    public void testInvalidDefinition() {
        final IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.parser.parse("${${a}}");
        });
    }

    @Test
    public void testInvalidDefinition2() {
        final IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.parser.parse("${{a}}");
        });
    }

    @Test
    public void testInvalidDefinition3() {
        final IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.parser.parse("${{a}");
        });
    }

}
