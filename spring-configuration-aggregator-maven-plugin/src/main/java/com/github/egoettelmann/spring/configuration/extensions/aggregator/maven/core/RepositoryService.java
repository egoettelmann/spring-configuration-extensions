package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.OperationFailedException;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.artifact.Artifact;

import java.util.List;

/**
 * The repository service
 */
public interface RepositoryService {

    /**
     * Resolves the dependencies of the given Maven artifact.
     *
     * @param project the Maven artifact to resolves the dependencies from
     * @return the list of dependencies
     * @throws OperationFailedException thrown if resolution fails
     */
    List<Artifact> resolveDependencies(final MavenProject project) throws OperationFailedException;

    /**
     * Resolves the previous stable version of a Maven artifact.
     *
     * @param project the Maven artifact to resolve the version from
     * @return the previous stable version
     * @throws OperationFailedException thrown if resolution fails
     */
    Artifact resolvePreviousStableVersion(final MavenProject project) throws OperationFailedException;

    /**
     * Resolves a version defined as string value for the given Maven artifact.
     *
     * @param project the Maven artifact to resolve the version from
     * @param version the version to resolve
     * @return the resolved version
     * @throws OperationFailedException thrown if resolution fails
     */
    Artifact resolveVersion(final MavenProject project, final String version) throws OperationFailedException;

}
