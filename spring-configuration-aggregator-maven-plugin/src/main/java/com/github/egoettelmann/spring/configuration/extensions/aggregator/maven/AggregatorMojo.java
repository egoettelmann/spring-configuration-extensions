package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.aggregation.DefaultAggregationService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.repository.DefaultRepositoryService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.AggregationService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.RepositoryService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.MetadataFile;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.PropertiesFile;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Aggregates all Spring Configuration Properties metadata into a single file.
 */
@Mojo(name = "aggregate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class AggregatorMojo extends AbstractPluginMojo {

    /**
     * The list of json files to retrieve additional configuration metadata from.
     */
    @Parameter()
    private List<MetadataFile> additionalMetadataFiles;

    /**
     * The list of properties files to retrieve defaultValues from.
     */
    @Parameter()
    private List<PropertiesFile> propertiesFiles;

    /**
     * Comma separated list of spring profiles to include for reading values ('*' for all, '-' for none).
     * Default value: '*'.
     */
    @Parameter()
    private String profiles;

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
            final List<AggregatedPropertyMetadata> properties = aggregationService.aggregate(
                    this.getAdditionalMetadataFiles(),
                    this.getPropertiesFiles(),
                    this.getProfiles()
            );

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

    private List<PropertiesFile> getPropertiesFiles() {
        if (this.propertiesFiles != null) {
            return this.propertiesFiles;
        }

        // Defining default files
        final List<PropertiesFile> files = new ArrayList<>();
        final String resourcesFolder = this.project.getBasedir() + "/src/main/resources";
        files.add(new PropertiesFile(resourcesFolder + "/application.yml"));
        files.add(new PropertiesFile(resourcesFolder + "/application.yaml"));
        files.add(new PropertiesFile(resourcesFolder + "/application.properties"));
        return files;
    }

    private List<MetadataFile> getAdditionalMetadataFiles() {
        if (this.additionalMetadataFiles != null) {
            return this.additionalMetadataFiles;
        }

        return Collections.emptyList();
    }

    private Set<String> getProfiles() {
        // Default value: all found profiles (null)
        if (this.profiles == null || this.profiles.equals("*")) {
            return null;
        }

        // No profile included: empty set
        if (this.profiles.equals("-")) {
            return Collections.emptySet();
        }

        // Split and trim values
        return Arrays.stream(this.profiles.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}
