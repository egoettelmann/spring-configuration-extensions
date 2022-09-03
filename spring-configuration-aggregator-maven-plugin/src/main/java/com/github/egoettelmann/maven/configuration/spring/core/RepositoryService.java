package com.github.egoettelmann.maven.configuration.spring.core;

import com.github.egoettelmann.maven.configuration.spring.core.exceptions.OperationFailedException;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.artifact.Artifact;

import java.util.List;

public interface RepositoryService {

    List<Artifact> resolveDependencies(final MavenProject project) throws OperationFailedException;

    Artifact resolvePreviousStableVersion(final MavenProject project) throws OperationFailedException;

}
