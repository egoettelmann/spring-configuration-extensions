package com.github.egoettelmann.spring.configuration.extensions.samples;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.InputStream;

public class SampleAppIT {

    @Test
    public void testAggregatedConfigurationProperties() throws Exception {
        try (final InputStream inputStream = new ClassPathResource("META-INF/aggregated-spring-configuration-metadata.json").getInputStream()) {
            Assertions.assertNotNull(inputStream, "Aggregated configuration metadata file should exist");
            final JsonNode jsonNode = new ObjectMapper().readTree(inputStream);
            Assertions.assertTrue(jsonNode.has("properties"), "No field 'properties' found");
            final JsonNode properties = jsonNode.get("properties");
            Assertions.assertTrue(properties.isArray(), "Field 'properties' should be an array");
            final ArrayNode propertiesList = (ArrayNode) properties;
            Assertions.assertEquals(2, propertiesList.size(), "Wrong properties size");
            final JsonNode element1 = propertiesList.get(0);
            Assertions.assertEquals("sample.app.title", element1.get("name").asText(), "Wrong configuration name for idx 0");
            final JsonNode element2 = propertiesList.get(1);
            Assertions.assertEquals("sample.custom.conf", element2.get("name").asText(), "Wrong configuration name for idx 1");
        }
    }

}
