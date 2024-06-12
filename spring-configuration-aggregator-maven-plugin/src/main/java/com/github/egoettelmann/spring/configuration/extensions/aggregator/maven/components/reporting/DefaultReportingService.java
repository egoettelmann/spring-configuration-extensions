package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.reporting;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.reporting.writers.AbstractReportWriter;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.AggregationService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.ReportingService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.RepositoryService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.ChangesOptions;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.OutputReport;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.MetadataFileNotFoundException;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.OperationFailedException;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.ArtifactMetadata;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
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
    public ArtifactMetadata report(final List<AggregatedPropertyMetadata> aggregate, final ChangesOptions options) {
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
            metadata.setChanges(this.computeChanges(aggregate, options));
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
            throw new OperationFailedException("Failed to write report " + outputReport.getType(), e);
        }

        // No appropriate writer found for report
        throw new OperationFailedException("No report generator found for " + outputReport.getType());
    }

    private ArtifactMetadata.Changes computeChanges(final List<AggregatedPropertyMetadata> aggregate, final ChangesOptions options) {
        // Checking if changes computation marked as skipped
        if (options != null && BooleanUtils.isTrue(options.getSkip())) {
            this.log.info("Changes computation is skipped");
            return null;
        }

        // Resolving previous aggregated metadata
        final String baseVersion;
        final List<AggregatedPropertyMetadata> baseAggregation;
        try {
            if (options != null && StringUtils.isNotBlank(options.getBaseVersion())) {
                // Base version provided: loading from it
                baseVersion = options.getBaseVersion();
                if (StringUtils.isNotBlank(options.getBaseFilePath())) {
                    // Base path provided: loading from file
                    this.log.info("Computing changes against file '" + options.getBaseFilePath() + "' (" + baseVersion + ")");
                    baseAggregation = this.aggregationService.load(options.getBaseFilePath());
                } else {
                    // No base file path provided: loading from artifact
                    this.log.info("Computing changes against version '" + baseVersion + "'");
                    final Artifact previousArtifact = this.repositoryService.resolveVersion(this.project, options.getBaseVersion());
                    baseAggregation = this.aggregationService.load(previousArtifact);
                }
            } else {
                // No base version provided: loading from previous stable version
                final Artifact previousArtifact = this.repositoryService.resolvePreviousStableVersion(this.project);
                if (previousArtifact == null) {
                    this.log.info("No previous stable version found to compare");
                    return null;
                }
                baseVersion = previousArtifact.getVersion();
                this.log.info("Computing changes against latest stable version: '" + baseVersion + "'");
                baseAggregation = this.aggregationService.load(previousArtifact);
            }
        } catch (final MetadataFileNotFoundException e) {
            this.log.warn("No aggregation file found for previous version, skipping comparison");
            this.log.debug(e);
            return null;
        }

        // Comparing metadata with previous aggregated metadata
        try {
            this.log.info("Comparing with previous stable version: " + baseVersion);
            final MetadataComparator comparator = new MetadataComparator(this.log, aggregate);
            return comparator.compare(baseAggregation, baseVersion);
        }catch (final Exception e) {
            this.log.warn("Failed to compare with previous version");
            this.log.debug(e);
        }

        return null;
    }

}
