package com.github.egoettelmann.spring.configuration.extensions.samples;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.diff.JsonDiff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

public class AggregatedConfigurationPropertiesIT {

    @Test
    public void testAggregatedConfigurationProperties() throws Exception {
        try (
                final InputStream generatedIS = new ClassPathResource("META-INF/aggregated-spring-configuration-metadata.json").getInputStream();
                final InputStream expectedIS = new ClassPathResource("aggregated-spring-configuration-metadata-test-1.json").getInputStream()
        ) {
            Assertions.assertNotNull(generatedIS, "Aggregated configuration metadata file should exist");
            final JsonNode generatedJsonNode = new ObjectMapper().readTree(generatedIS);
            Assertions.assertTrue(generatedJsonNode.has("properties"), "No field 'properties' found");
            final JsonNode generatedProperties = generatedJsonNode.get("properties");

            final JsonNode expectedJsonNode = new ObjectMapper().readTree(expectedIS);
            final JsonNode expectedProperties = expectedJsonNode.get("properties");

            final JsonNode jsonPatch = JsonDiff.asJson(expectedProperties, generatedProperties);
            Assertions.assertTrue(jsonPatch.isArray(), "Patch should be an array");
            Assertions.assertEquals(0, jsonPatch.size(), "Patch should be empty");
        }
    }

}
