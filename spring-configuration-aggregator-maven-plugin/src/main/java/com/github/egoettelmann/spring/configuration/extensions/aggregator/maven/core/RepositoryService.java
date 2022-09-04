package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.OperationFailedException;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.artifact.Artifact;

import java.util.List;

public interface RepositoryService {

    List<Artifact> resolveDependencies(final MavenProject project) throws OperationFailedException;

    Artifact resolvePreviousStableVersion(final MavenProject project) throws OperationFailedException;

}
