package com.github.egoettelmann.maven.configuration.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.egoettelmann.maven.configuration.spring.components.aggregation.DefaultAggregationService;
import com.github.egoettelmann.maven.configuration.spring.components.reporting.DefaultReportingService;
import com.github.egoettelmann.maven.configuration.spring.components.reporting.writers.AbstractReportWriter;
import com.github.egoettelmann.maven.configuration.spring.components.reporting.writers.HtmlReportWriter;
import com.github.egoettelmann.maven.configuration.spring.components.reporting.writers.JsonReportWriter;
import com.github.egoettelmann.maven.configuration.spring.components.reporting.writers.PdfReportWriter;
import com.github.egoettelmann.maven.configuration.spring.components.repository.DefaultRepositoryService;
import com.github.egoettelmann.maven.configuration.spring.core.AggregationService;
import com.github.egoettelmann.maven.configuration.spring.core.ReportingService;
import com.github.egoettelmann.maven.configuration.spring.core.RepositoryService;
import com.github.egoettelmann.maven.configuration.spring.core.dto.OutputReport;
import com.github.egoettelmann.maven.configuration.spring.core.model.AggregatedPropertyMetadata;
import com.github.egoettelmann.maven.configuration.spring.core.model.ArtifactMetadata;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates a report of all aggregated Spring Configuration Properties metadata.
 */
@Mojo(name = "report", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class ReporterMojo extends AbstractPluginMojo {

    /**
     * Defines the list of output reports to generate.
     */
    @Parameter()
    private List<OutputReport> outputReports;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.skip) {
            this.getLog().info("Configuration properties metadata report generation is skipped.");
            return;
        }
        this.getLog().info("Starting configuration properties metadata report generation.");

        try {
            // Building required components
            final ObjectMapper objectMapper = this.defaultObjectMapper();
            final RepositoryService repositoryService = new DefaultRepositoryService(
                    this.getLog(),
                    this.projectDependenciesResolver,
                    this.repoSystem,
                    this.repoSession,
                    this.dependencyPredicate()
            );
            final AggregationService aggregationService = new DefaultAggregationService(
                    this.getLog(),
                    this.project,
                    repositoryService,
                    objectMapper
            );
            final List<AbstractReportWriter> reportWriters = new ArrayList<>();
            reportWriters.add(new JsonReportWriter(this.getLog(), this.project, objectMapper));
            reportWriters.add(new HtmlReportWriter(this.getLog(), this.project));
            reportWriters.add(new PdfReportWriter(this.getLog(), this.project));
            final ReportingService reportingService = new DefaultReportingService(
                    this.getLog(),
                    reportWriters,
                    this.project,
                    repositoryService,
                    aggregationService
            );

            // Generating reports
            final List<AggregatedPropertyMetadata> aggregate = aggregationService.load();
            final ArtifactMetadata metadata = reportingService.report(aggregate);

            // Generating reports
            for (final OutputReport outputReport : this.getOutputReports()) {
                reportingService.save(metadata, outputReport);
                this.getLog().info("Generated '" + outputReport.getType() + "' report");
            }
        } catch (final Exception e) {
            this.getLog().error("Goal 'report' failed, but error is ignored", e);
            if (!this.failOnError) {
                return;
            }
            throw new RuntimeException("Goal 'report' failed", e);
        }
    }

    private List<OutputReport> getOutputReports() {
        if (this.outputReports != null) {
            return this.outputReports;
        }
        final OutputReport jsonReport = new OutputReport();
        jsonReport.setType("json");
        return Collections.singletonList(jsonReport);
    }

}
