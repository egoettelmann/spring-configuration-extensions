package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.aggregation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.AggregationService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.RepositoryService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.PropertiesFile;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.MetadataFileNotFoundException;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.OperationFailedException;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.PropertyMetadata;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.artifact.Artifact;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DefaultAggregationService implements AggregationService {

    private static final List<String> METADATA_FILE_SET = Arrays.asList(
            "/META-INF/spring-configuration-metadata.json",
            "/META-INF/additional-spring-configuration-metadata.json"
    );

    private static final String AGGREGATED_FILE = "/META-INF/aggregated-spring-configuration-metadata.json";

    private final Log log;

    private final RepositoryService repositoryService;

    private final MavenProject project;

    private final PropertiesMetadataReader propertiesMetadataReader;

    private final AggregatedPropertiesMetadataReader aggregatedPropertiesMetadataReader;

    private final AggregatedPropertiesMetadataWriter aggregatedPropertiesMetadataWriter;

    private final PropertiesValueReader propertiesValueReader;

    public DefaultAggregationService(
            final Log log,
            final MavenProject project,
            final RepositoryService repositoryService,
            final ObjectMapper objectMapper
    ) {
        this.log = log;
        this.repositoryService = repositoryService;
        this.project = project;
        this.propertiesMetadataReader = new PropertiesMetadataReader(this.log, objectMapper);
        this.aggregatedPropertiesMetadataReader = new AggregatedPropertiesMetadataReader(this.log, objectMapper);
        this.aggregatedPropertiesMetadataWriter = new AggregatedPropertiesMetadataWriter(this.log, objectMapper);
        this.propertiesValueReader = new PropertiesValueReader(this.log);
    }

    @Override
    public List<AggregatedPropertyMetadata> aggregate(final List<PropertiesFile> propertiesFiles) {
        final AggregationBuilder builder = new AggregationBuilder(this.log);

        // Resolving from current project
        this.log.debug("Retrieving configuration properties metadata from current project");
        final String projectPath = "file:///" + this.project.getBuild().getOutputDirectory();
        final List<PropertyMetadata> projectProperties = this.readPropertiesFromPath(projectPath);
        builder.add(projectProperties, this.project.getGroupId(), this.project.getArtifactId());

        // Resolving from dependencies
        final List<Artifact> dependencies = this.repositoryService.resolveDependencies(this.project);
        this.log.debug("Retrieving configuration properties metadata from " + dependencies.size() + " dependencies");
        for (final Artifact dependency : dependencies) {
            final String filePath = "jar:file:" + dependency.getFile().getAbsolutePath() + "!";
            final List<PropertyMetadata> properties = this.readPropertiesFromPath(filePath);
            builder.add(properties, dependency.getGroupId(), dependency.getArtifactId());
        }

        // Adding default values
        if (propertiesFiles != null) {
            for (final PropertiesFile propertiesFile : propertiesFiles) {
                try {
                    final Properties properties = this.propertiesValueReader.read("file:///" + propertiesFile.getPath());
                    builder.put(properties);
                } catch (final MetadataFileNotFoundException e) {
                    this.log.warn("No properties found in " + propertiesFile.getPath());
                    this.log.debug(e);
                }
            }
        }

        // Aggregating
        final List<AggregatedPropertyMetadata> aggregate = builder.build();
        this.log.debug("Resolved metadata for " + aggregate.size() + " configuration properties");
        return aggregate;
    }

    @Override
    public List<AggregatedPropertyMetadata> load() throws MetadataFileNotFoundException {
        final String aggregatedFilePath = "file:///" + this.project.getBuild().getOutputDirectory() + AGGREGATED_FILE;
        return this.aggregatedPropertiesMetadataReader.read(aggregatedFilePath);
    }

    @Override
    public List<AggregatedPropertyMetadata> load(final Artifact artifact) throws MetadataFileNotFoundException {
        final String aggregatedFilePath = "jar:file:" + artifact.getFile().getAbsolutePath() + "!" + AGGREGATED_FILE;
        return this.aggregatedPropertiesMetadataReader.read(aggregatedFilePath);
    }

    @Override
    public void save(final List<AggregatedPropertyMetadata> aggregatedProperties) throws OperationFailedException {
        final String aggregatedFilePath = "file:///" + this.project.getBuild().getOutputDirectory() + AGGREGATED_FILE;

        try {
            // Writing to file
            final URL url = new URL(aggregatedFilePath);
            this.aggregatedPropertiesMetadataWriter.write(aggregatedProperties, url);
        } catch (IOException e) {
            throw new OperationFailedException("Failed to write to file " + aggregatedFilePath, e);
        }
    }

    private List<PropertyMetadata> readPropertiesFromPath(final String path) {
        final List<PropertyMetadata> properties = new ArrayList<>();

        // Checking each file
        for (final String metadataFile : METADATA_FILE_SET) {
            final String filePath = path + metadataFile;
            try {
                // Parsing file
                final List<PropertyMetadata> metadata = this.propertiesMetadataReader.read(filePath);
                this.log.debug("Found metadata for " + metadata.size() + " configuration properties in " + filePath);
                properties.addAll(metadata);
            } catch (final MetadataFileNotFoundException e) {
                this.log.debug("No metadata for configuration properties found in " + filePath);
            }
        }

        this.log.debug("Found metadata for " + properties.size() + " configuration properties in " + path);
        return properties;
    }

}
