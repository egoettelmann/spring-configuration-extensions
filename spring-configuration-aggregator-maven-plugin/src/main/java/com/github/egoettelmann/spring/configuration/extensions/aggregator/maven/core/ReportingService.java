package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.ChangesOptions;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.OutputReport;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.ArtifactMetadata;

import java.util.List;

public interface ReportingService {

    ArtifactMetadata report(final List<AggregatedPropertyMetadata> aggregate, final ChangesOptions options);

    void save(final ArtifactMetadata metadata, final OutputReport outputReport);

}
