package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.reporting.writers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.OutputReport;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.ArtifactMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonReportWriter extends AbstractReportWriter {

    private final ObjectMapper objectMapper;

    public JsonReportWriter(
            final Log log,
            final MavenProject project,
            final ObjectMapper objectMapper
    ) {
        super(log, project);
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(final OutputReport outputReport) {
        return "json".equalsIgnoreCase(outputReport.getType());
    }

    @Override
    public void write(final OutputReport outputReport, final ArtifactMetadata metadata) throws Exception {
        final File outputFile = this.getOutputFile(outputReport.getOutputFile());
        if (outputFile.getParentFile().mkdirs()) {
            this.log.debug("Created folder " + outputFile.getParentFile());
        }

        final ArtifactMetadata.Wrapper wrapper = new ArtifactMetadata.Wrapper();
        final List<ArtifactMetadata> metadataList = new ArrayList<>();
        metadataList.add(metadata);
        wrapper.setArtifacts(metadataList);

        this.objectMapper.writeValue(outputFile, wrapper);
    }

    private File getOutputFile(final String outputFile) {
        if (StringUtils.isNotBlank(outputFile)) {
            return new File(outputFile);
        }

        final String fileName = this.project.getArtifactId() + "-" + this.project.getVersion() + "-configurations.json";
        return new File(this.project.getBuild().getDirectory() + "/" + fileName);
    }

}
