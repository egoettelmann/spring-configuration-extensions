package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.MetadataFile;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.PropertiesFile;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.MetadataFileNotFoundException;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import org.eclipse.aether.artifact.Artifact;

import java.util.List;
import java.util.Set;

public interface AggregationService {

    /**
     * Aggregates all configuration properties metadata of the current project.
     * Combines all metadata files from the project and of its dependencies.
     *
     * @param additionalMetadataFiles the list of json files to retrieve additional configuration metadata from
     * @param propertiesFiles the list of properties files to extract default values from
     * @param profiles the list of spring profiles to extract default values for (can be null to accept all)
     * @return the list of aggregated properties metadata
     */
    List<AggregatedPropertyMetadata> aggregate(final List<MetadataFile> additionalMetadataFiles, final List<PropertiesFile> propertiesFiles, final Set<String> profiles);

    /**
     * Loads the aggregated configuration properties metadata of the current project.
     * Retrieves them from the existing aggregation file.
     *
     * @return the list of aggregated properties metadata
     * @throws MetadataFileNotFoundException if the aggregation file could not be found
     */
    List<AggregatedPropertyMetadata> load() throws MetadataFileNotFoundException;

    /**
     * Loads the aggregate configuration properties metadata of the given artifact.
     * Retrieves them from the existing aggregation file.
     *
     * @param artifact the artifact to load the metadata from
     * @return the list of aggregated properties metadata
     * @throws MetadataFileNotFoundException if the aggregation file could not be found
     */
    List<AggregatedPropertyMetadata> load(final Artifact artifact) throws MetadataFileNotFoundException;

    /**
     * Loads the aggregate configuration properties metadata from the given file.
     * Retrieves them from the existing aggregation file.
     *
     * @param filePath the file to load the metadata from
     * @return the list of aggregated properties metadata
     * @throws MetadataFileNotFoundException if the aggregation file could not be found
     */
    List<AggregatedPropertyMetadata> load(final String filePath) throws MetadataFileNotFoundException;

    /**
     * Saves the given list of aggregated configuration properties metadata for the current project.
     *
     * @param aggregatedProperties the aggregated properties to save
     */
    void save(final List<AggregatedPropertyMetadata> aggregatedProperties);

}
