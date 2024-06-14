package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.repository;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.RepositoryService;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.OperationFailedException;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.*;
import org.apache.maven.project.DependencyResolutionException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.*;
import org.eclipse.aether.version.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DefaultRepositoryService implements RepositoryService {

    private static final List<String> KNOWN_EXTENSIONS = Arrays.asList("jar", "war", "pom", "zip");

    private final Log log;

    private final ProjectDependenciesResolver projectDependenciesResolver;

    private final RepositorySystem repoSystem;

    private final RepositorySystemSession repoSession;

    private final DependencyPredicate dependencyPredicate;

    public DefaultRepositoryService(
            final Log log,
            final ProjectDependenciesResolver projectDependenciesResolver,
            final RepositorySystem repoSystem,
            final RepositorySystemSession repoSession,
            final DependencyPredicate dependencyPredicate
    ) {
        this.log = log;
        this.projectDependenciesResolver = projectDependenciesResolver;
        this.repoSystem = repoSystem;
        this.repoSession = repoSession;
        this.dependencyPredicate = dependencyPredicate;
    }

    @Override
    public List<Artifact> resolveDependencies(final MavenProject project) throws OperationFailedException {
        final DefaultDependencyResolutionRequest request = new DefaultDependencyResolutionRequest(project, this.repoSession);
        final DependencyResolutionResult result;
        try {
            result = this.projectDependenciesResolver.resolve(request);
        } catch (final DependencyResolutionException e) {
            throw new OperationFailedException("Failed to resolve project dependencies", e);
        }
        final List<Dependency> dependencies = result.getDependencies();
        this.log.debug("Scanning " + dependencies.size() + " dependencies");

        final List<Artifact> artifacts = new ArrayList<>();
        for (final Dependency dependency : dependencies) {
            final Artifact artifact = dependency.getArtifact();

            // Checking if artifact should be considered
            if (!this.dependencyPredicate.test(dependency)) {
                this.log.debug("Dependency '" + dependency + "' is ignored");
                continue;
            }

            // Checking that artifact file exists
            final File artifactFile = artifact.getFile();
            if (artifactFile == null) {
                this.log.debug("Dependency '" + dependency + "' has no related file");
                continue;
            }

            // Checking that artifact file is not a folder
            if (artifactFile.isDirectory()) {
                this.log.debug("Ignoring " + artifactFile.getName() + ", directory");
                continue;
            }

            // Artifact matches filters
            artifacts.add(artifact);
        }

        return artifacts;
    }

    @Override
    public Artifact resolvePreviousStableVersion(final MavenProject project) throws OperationFailedException {
        final DefaultArtifact defaultArtifact = this.defaultArtifact(project, "[0,)");
        final VersionRangeRequest versionRangeRequest = new VersionRangeRequest(defaultArtifact, project.getRemoteProjectRepositories(), null);
        try {
            this.log.debug("Resolving version range for " + defaultArtifact + " with " + versionRangeRequest);
            final VersionRangeResult versionRangeResult = this.repoSystem.resolveVersionRange(
                    this.repoSession,
                    versionRangeRequest
            );
            final List<Version> versions = versionRangeResult.getVersions();
            final Optional<Version> latestStable = this.findLatestStable(versions, project);
            if (!latestStable.isPresent()) {
                this.log.debug("No stable version before current (" + project.getVersion() + ") found");
                return null;
            }
            this.log.debug("Found latest stable version before current: " + latestStable.get());

            final DefaultArtifact previousArtifact = this.defaultArtifact(project, latestStable.get().toString());
            final ArtifactRequest artifactRequest = new ArtifactRequest(previousArtifact, project.getRemoteProjectRepositories(), null);
            final ArtifactResult artifactResult = this.repoSystem.resolveArtifact(this.repoSession, artifactRequest);
            return artifactResult.getArtifact();
        } catch (final VersionRangeResolutionException | ArtifactResolutionException e) {
            throw new OperationFailedException("Failed to retrieve previous release version", e);
        }
    }

    @Override
    public Artifact resolveVersion(MavenProject project, String version) throws OperationFailedException {
        try {
            final DefaultArtifact previousArtifact = this.defaultArtifact(project, version);
            final ArtifactRequest artifactRequest = new ArtifactRequest(previousArtifact, project.getRemoteProjectRepositories(), null);
            final ArtifactResult artifactResult = this.repoSystem.resolveArtifact(this.repoSession, artifactRequest);
            return artifactResult.getArtifact();
        } catch (final ArtifactResolutionException e) {
            throw new OperationFailedException("Failed to retrieve artifact for version " + version, e);
        }
    }

    private Optional<Version> findLatestStable(final List<Version> versions, final MavenProject project) {
        this.log.debug("Finding latest stable version before " + project.getVersion());
        final Semver projectVersion = new Semver(project.getVersion(), Semver.SemverType.LOOSE);

        Version latestVersion = null;
        for (final Version version : versions) {
            this.log.debug("Comparing with version '" + version + "'");
            if (StringUtils.isBlank(version.toString())) {
                this.log.debug("Version " + version + " is blank");
                continue;
            }
            final Semver semVersion;
            try {
                semVersion = new Semver(version.toString(), Semver.SemverType.LOOSE);
            }  catch (final SemverException e) {
                this.log.warn("Version " + version + " is not a valid semver: " + e.getMessage());
                continue;
            }
            if (!semVersion.isStable()) {
                // Version not stable: ignoring
                this.log.debug("Version " + version + " is not stable");
                continue;
            }
            if (semVersion.isGreaterThanOrEqualTo(projectVersion)) {
                // Version is higher than current project version: ignoring
                this.log.debug("Version " + version + " is greater or equal");
                continue;
            }
            if (latestVersion == null) {
                // No latest version yet: saving and continuing
                this.log.debug("Version " + version + " matches (first match)");
                latestVersion = version;
                continue;
            }
            final Semver latestSemVersion = new Semver(latestVersion.toString(), Semver.SemverType.LOOSE);
            if (semVersion.isGreaterThan(latestSemVersion)) {
                // Version is higher than saved one: saving
                this.log.debug("Version " + version + " matches");
                latestVersion = version;
            }
        }

        return Optional.ofNullable(latestVersion);
    }

    private DefaultArtifact defaultArtifact(final MavenProject mavenProject, String version) {
        String extension = mavenProject.getArtifact().getType();
        if (!KNOWN_EXTENSIONS.contains(extension)) {
            extension = "jar";
        }
        return new DefaultArtifact(
                mavenProject.getArtifact().getGroupId(),
                mavenProject.getArtifact().getArtifactId(),
                mavenProject.getArtifact().getClassifier(),
                extension,
                version
        );
    }

}
