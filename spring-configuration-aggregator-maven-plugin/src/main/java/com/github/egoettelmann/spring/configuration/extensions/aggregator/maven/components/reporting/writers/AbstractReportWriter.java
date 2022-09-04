package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.reporting.writers;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.OutputReport;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.ArtifactMetadata;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public abstract class AbstractReportWriter {

    protected Log log;

    protected MavenProject project;

    public AbstractReportWriter(final Log log, final MavenProject project) {
        this.log = log;
        this.project = project;
    }

    public abstract boolean supports(final OutputReport outputReport);

    public abstract void write(final OutputReport outputReport, final ArtifactMetadata metadata) throws Exception;

}
