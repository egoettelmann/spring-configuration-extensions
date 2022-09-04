package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.aggregation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

class AggregatedPropertiesMetadataWriter {

    private final Log log;

    private final ObjectMapper objectMapper;

    public AggregatedPropertiesMetadataWriter(final Log log, final ObjectMapper objectMapper) {
        this.log = log;
        this.objectMapper = objectMapper;
    }

    public void write(final List<AggregatedPropertyMetadata> aggregate, final URL aggregatedMetadataUrl) throws IOException {
        final File outputFile = new File(aggregatedMetadataUrl.getFile());
        if (outputFile.getParentFile().mkdirs()) {
            this.log.debug("Created folder " + outputFile.getParentFile());
        }

        final AggregatedPropertyMetadata.Wrapper wrapper = new AggregatedPropertyMetadata.Wrapper();
        wrapper.setProperties(aggregate);

        this.objectMapper.writeValue(outputFile, wrapper);
    }

}
