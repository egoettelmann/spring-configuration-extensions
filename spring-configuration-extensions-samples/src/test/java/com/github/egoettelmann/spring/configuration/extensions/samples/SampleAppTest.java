package com.github.egoettelmann.spring.configuration.extensions.samples;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;

public class SampleAppTest {

    @Test
    public void testSpringConfigurationProperties() throws Exception {
        final File aggregationFile = ResourceUtils.getFile("classpath:META-INF/spring-configuration-metadata.json");
        Assertions.assertTrue(aggregationFile.exists(), "Configuration metadata file should exist");
        final JsonNode jsonNode = new ObjectMapper().readTree(aggregationFile);
        Assertions.assertTrue(jsonNode.has("properties"), "No field 'properties' found");
        final JsonNode properties = jsonNode.get("properties");
        Assertions.assertTrue(properties.isArray(), "Field 'properties' should be an array");
        final ArrayNode propertiesList = (ArrayNode) properties;
        Assertions.assertEquals(1, propertiesList.size(), "Wrong properties size");
        final JsonNode element1 = propertiesList.get(0);
        Assertions.assertEquals("sample.app.title", element1.get("name").asText(), "Wrong configuration name for idx 0");
    }

    @Test
    public void testAdditionalConfigurationProperties() throws Exception {
        final File aggregationFile = ResourceUtils.getFile("classpath:META-INF/additional-spring-configuration-metadata.json");
        Assertions.assertTrue(aggregationFile.exists(), "Additional configuration metadata file should exist");
        final JsonNode jsonNode = new ObjectMapper().readTree(aggregationFile);
        Assertions.assertTrue(jsonNode.has("properties"), "No field 'properties' found");
        final JsonNode properties = jsonNode.get("properties");
        Assertions.assertTrue(properties.isArray(), "Field 'properties' should be an array");
        final ArrayNode propertiesList = (ArrayNode) properties;
        Assertions.assertEquals(1, propertiesList.size(), "Wrong properties size");
        final JsonNode element1 = propertiesList.get(0);
        Assertions.assertEquals("sample.custom.conf", element1.get("name").asText(), "Wrong configuration name for idx 0");
    }

}
