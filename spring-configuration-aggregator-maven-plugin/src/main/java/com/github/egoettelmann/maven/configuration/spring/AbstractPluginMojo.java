package com.github.egoettelmann.maven.configuration.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.egoettelmann.maven.configuration.spring.components.repository.DependencyPredicate;
import com.github.egoettelmann.maven.configuration.spring.core.dto.DependencyMatcher;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;

import java.util.List;

/**
 * Aggregates all Spring Configuration Properties metadata into a single file.
 */
public abstract class AbstractPluginMojo extends AbstractMojo {

    /**
     * The project dependencies resolver
     */
    @Component
    protected ProjectDependenciesResolver projectDependenciesResolver;

    /**
     * The repository system
     */
    @Component
    protected RepositorySystem repoSystem;

    /**
     * Repository system session to resolve dependencies
     */
    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
    protected RepositorySystemSession repoSession;

    /**
     * Current project model
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

    /**
     * Defines if the plugin execution should be skipped.
     */
    @Parameter(defaultValue = "false", required = true)
    protected boolean skip;

    /**
     * Defines if errors during aggregation should make the build fail. By default: 'true'.
     */
    @Parameter(defaultValue = "true", required = true)
    protected boolean failOnError;

    /**
     * The dependencies for which to consider configuration properties metadata.
     */
    @Parameter()
    private List<DependencyMatcher> includeDependencies;

    /**
     * The dependencies for which to ignore configuration properties metadata.
     */
    @Parameter()
    private List<DependencyMatcher> excludeDependencies;

    /**
     * Instantiates a new dependency predicate.
     * It takes into account the included/excluded dependency options.
     *
     * @return the dependency predicate
     */
    protected DependencyPredicate dependencyPredicate() {
        return new DependencyPredicate()
                .includeDependencies(this.includeDependencies)
                .excludeDependencies(this.excludeDependencies);
    }

    /**
     * The default object mapper to serialize/deserialize JSON.
     *
     * @return the default object mapper
     */
    protected ObjectMapper defaultObjectMapper() {
        return new ObjectMapper()
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

}
