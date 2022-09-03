package com.github.egoettelmann.maven.configuration.spring.components.aggregation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.egoettelmann.maven.configuration.spring.core.exceptions.MetadataFileNotFoundException;
import com.github.egoettelmann.maven.configuration.spring.core.model.AggregatedPropertyMetadata;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

class AggregatedPropertiesMetadataReader {

    private final Log log;

    private final ObjectMapper objectMapper;

    public AggregatedPropertiesMetadataReader(final Log log, final ObjectMapper objectMapper) {
        this.log = log;
        this.objectMapper = objectMapper;
    }

    public List<AggregatedPropertyMetadata> read(final String filePath) throws MetadataFileNotFoundException {
        // Parsing filePath as URL
        final URL metadataUrl;
        try {
            metadataUrl = new URL(filePath);
        } catch (MalformedURLException e) {
            throw new MetadataFileNotFoundException("Invalid file path " + filePath, e);
        }

        // Reading file content
        try (final InputStream fileContent = metadataUrl.openStream()) {
            this.log.debug("Found aggregated configuration properties metadata file " + metadataUrl.getFile() + " in " + metadataUrl.getPath() + "");

            // Parsing content
            final AggregatedPropertyMetadata.Wrapper wrapper = this.objectMapper.readValue(fileContent, AggregatedPropertyMetadata.Wrapper.class);
            if (wrapper.getProperties() == null || wrapper.getProperties().isEmpty()) {
                this.log.debug("Ignoring " + metadataUrl + ": no aggregated properties found");
                return Collections.emptyList();
            }

            // Returning parsed properties
            return wrapper.getProperties();
        } catch (final IOException e) {
            throw new MetadataFileNotFoundException("Failed reading " + metadataUrl, e);
        }
    }

}
