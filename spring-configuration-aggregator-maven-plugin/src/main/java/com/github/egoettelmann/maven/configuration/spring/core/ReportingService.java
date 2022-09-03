package com.github.egoettelmann.maven.configuration.spring.core;

import com.github.egoettelmann.maven.configuration.spring.core.dto.OutputReport;
import com.github.egoettelmann.maven.configuration.spring.core.model.AggregatedPropertyMetadata;
import com.github.egoettelmann.maven.configuration.spring.core.model.ArtifactMetadata;

import java.util.List;

public interface ReportingService {

    ArtifactMetadata report(final List<AggregatedPropertyMetadata> aggregate);

    void save(final ArtifactMetadata metadata, final OutputReport outputReport);

}
