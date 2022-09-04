package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.reporting;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.reporting.writers.AbstractReportWriter;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.AggregationService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.ReportingService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.RepositoryService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.OutputReport;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.MetadataFileNotFoundException;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.OperationFailedException;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.ArtifactMetadata;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.artifact.Artifact;

import java.util.List;

public class DefaultReportingService implements ReportingService {

    private final Log log;

    private final List<? extends AbstractReportWriter> writers;

    private final MavenProject project;

    private final RepositoryService repositoryService;

    private final AggregationService aggregationService;

    public DefaultReportingService(
            final Log log,
            final List<? extends AbstractReportWriter> writers,
            final MavenProject project,
            final RepositoryService repositoryService,
            final AggregationService aggregationService
    ) {
        this.log = log;
        this.writers = writers;
        this.project = project;
        this.repositoryService = repositoryService;
        this.aggregationService = aggregationService;
    }

    @Override
    public ArtifactMetadata report(final List<AggregatedPropertyMetadata> aggregate) {
        try {
            // Building artifact metadata
            final ArtifactMetadata metadata = new ArtifactMetadata();
            metadata.setGroupId(this.project.getGroupId());
            metadata.setArtifactId(this.project.getArtifactId());
            metadata.setVersion(this.project.getVersion());
            metadata.setName(this.project.getName());
            metadata.setDescription(this.project.getDescription());
            metadata.setProperties(aggregate);

            // TODO: combine multiple aggregation files (from 'merged') ?

            // Comparing with previous version
            try {
                final Artifact previousArtifact = this.repositoryService.resolvePreviousStableVersion(this.project);
                if (previousArtifact != null) {
                    final MetadataComparator comparator = new MetadataComparator(this.log, aggregate);
                    final List<AggregatedPropertyMetadata> previous = this.aggregationService.load(previousArtifact);
                    final ArtifactMetadata.Changes changes = comparator.compare(previous, previousArtifact.getVersion());
                    metadata.setChanges(changes);
                }
            } catch (final MetadataFileNotFoundException e) {
                this.log.warn("No aggregation file found for previous version, skipping comparison");
                this.log.debug(e);
            } catch (final Exception e) {
                this.log.warn("Failed to compare with previous version", e);
                this.log.debug(e);
            }

            return metadata;
        } catch (final Exception e) {
            throw new OperationFailedException("Failed to generate report", e);
        }
    }

    @Override
    public void save(final ArtifactMetadata metadata, final OutputReport outputReport) {
        try {
            for (final AbstractReportWriter writer : this.writers) {
                if (!writer.supports(outputReport)) {
                    continue;
                }

                writer.write(outputReport, metadata);
                return;
            }
        } catch (final Exception e) {
            throw new OperationFailedException("Failed to write report " + outputReport.getType());
        }

        // No appropriate writer found for report
        throw new OperationFailedException("No report generator found for " + outputReport.getType());
    }

}
