package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.aggregation.DefaultAggregationService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.repository.DefaultRepositoryService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.AggregationService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.RepositoryService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.PropertiesFiles;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates all Spring Configuration Properties metadata into a single file.
 */
@Mojo(name = "aggregate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class AggregatorMojo extends AbstractPluginMojo {

    /**
     * The list of properties files from which to retrieve defaultValues from.
     */
    @Parameter()
    private PropertiesFiles propertiesFiles;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.skip) {
            this.getLog().info("Configuration properties metadata aggregation is skipped.");
            return;
        }
        this.getLog().info("Starting configuration properties metadata aggregation.");

        try {
            // Building required components
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
                    this.defaultObjectMapper()
            );

            // Retrieving properties
            final List<AggregatedPropertyMetadata> properties = aggregationService.aggregate(this.getPropertiesFiles());

            // Saving properties
            aggregationService.save(properties);
            this.getLog().info("Aggregated metadata for " + properties.size() + " configuration properties.");
        } catch (final Exception e) {
            this.getLog().error("Goal 'aggregate' failed, but error is ignored", e);
            if (!this.failOnError) {
                return;
            }
            throw new RuntimeException("Goal 'aggregate' failed", e);
        }
    }

    private PropertiesFiles getPropertiesFiles() {
        if (this.propertiesFiles != null) {
            return this.propertiesFiles;
        }

        // Defining default files
        final PropertiesFiles files = new PropertiesFiles();
        files.setPropertiesFile(new ArrayList<>());
        final String resourcesFolder = this.project.getBasedir() + "/src/main/resources";
        files.getPropertiesFile().add(resourcesFolder + "/application.yml");
        files.getPropertiesFile().add(resourcesFolder + "/application.properties");
        return files;
    }

}
