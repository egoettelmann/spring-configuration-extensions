package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.ChangesOptions;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.OutputReport;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.ArtifactMetadata;

import java.util.List;

/**
 * The reporting service.
 */
public interface ReportingService {

    /**
     * Generates a report for the given list of aggregated property metadata.
     *
     * @param aggregate the list of metadata
     * @param options the options for the change computation
     * @return the report
     */
    ArtifactMetadata report(final List<AggregatedPropertyMetadata> aggregate, final ChangesOptions options);

    /**
     * Saves the provided metadata into the given output report.
     *
     * @param metadata the metadata to save
     * @param outputReport the output report
     */
    void save(final ArtifactMetadata metadata, final OutputReport outputReport);

}
