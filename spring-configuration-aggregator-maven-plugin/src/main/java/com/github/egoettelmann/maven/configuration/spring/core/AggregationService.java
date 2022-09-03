package com.github.egoettelmann.maven.configuration.spring.core;

import com.github.egoettelmann.maven.configuration.spring.core.dto.PropertiesFiles;
import com.github.egoettelmann.maven.configuration.spring.core.exceptions.MetadataFileNotFoundException;
import com.github.egoettelmann.maven.configuration.spring.core.model.AggregatedPropertyMetadata;
import org.eclipse.aether.artifact.Artifact;

import java.util.List;

public interface AggregationService {

    /**
     * Aggregates all configuration properties metadata of the current project.
     * Combines all metadata files from the project and of its dependencies.
     *
     * @param propertiesFiles the list of properties files to extract default values from
     * @return the list of aggregated properties metadata
     */
    List<AggregatedPropertyMetadata> aggregate(final PropertiesFiles propertiesFiles);

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
     * Saves the given list of aggregated configuration properties metadata for the current project.
     *
     * @param aggregatedProperties the aggregated properties to save
     */
    void save(final List<AggregatedPropertyMetadata> aggregatedProperties);

}
