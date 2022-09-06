package com.github.egoettelmann.spring.configuration.extensions.samples;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;

public class SpringConfigurationPropertiesTest {

    @Test
    public void testSpringConfigurationProperties() throws Exception {
        final File generatedFile = ResourceUtils.getFile("classpath:META-INF/spring-configuration-metadata.json");
        Assertions.assertTrue(generatedFile.exists(), "Configuration metadata file should exist");
        final JsonNode generatedJsonNode = new ObjectMapper().readTree(generatedFile);
        Assertions.assertTrue(generatedJsonNode.has("properties"), "No field 'properties' found");
        final JsonNode generatedProperties = generatedJsonNode.get("properties");

        final File expectedFile = ResourceUtils.getFile("classpath:spring-configuration-metadata-test-1.json");
        final JsonNode expectedJsonNode = new ObjectMapper().readTree(expectedFile);
        final JsonNode expectedProperties = expectedJsonNode.get("properties");

        final JsonNode jsonPatch = JsonDiff.asJson(expectedProperties, generatedProperties);
        Assertions.assertTrue(jsonPatch.isArray(), "Patch should be an array");
        Assertions.assertEquals(0, jsonPatch.size(), "Path should be empty");
    }

}
