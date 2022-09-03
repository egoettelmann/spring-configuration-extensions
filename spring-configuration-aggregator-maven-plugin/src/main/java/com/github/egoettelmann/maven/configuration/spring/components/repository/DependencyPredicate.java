package com.github.egoettelmann.maven.configuration.spring.components.repository;

import com.github.egoettelmann.maven.configuration.spring.core.dto.DependencyMatcher;
import org.eclipse.aether.graph.Dependency;

import java.util.List;
import java.util.function.Predicate;

public class DependencyPredicate implements Predicate<Dependency> {

    private List<DependencyMatcher> includeDependencies;

    private List<DependencyMatcher> excludeDependencies;

    public DependencyPredicate includeDependencies(List<DependencyMatcher> includeDependencies) {
        this.includeDependencies = includeDependencies;
        return this;
    }

    public DependencyPredicate excludeDependencies(List<DependencyMatcher> excludeDependencies) {
        this.excludeDependencies = excludeDependencies;
        return this;
    }

    @Override
    public boolean test(final Dependency dependency) {
        // Checking if dependencies to include are defined
        if (this.includeDependencies != null) {
            for (final DependencyMatcher matcher : this.includeDependencies) {
                // If artifact matches, it is considered
                if (matcher.matches(dependency)) {
                    return true;
                }
            }

            // No filter matches, artifact ignored
            return false;
        }

        // Checking if dependencies to exclude are defined
        if (this.excludeDependencies != null) {
            for (final DependencyMatcher matcher : this.excludeDependencies) {
                // If artifact matches, it is ignored
                if (matcher.matches(dependency)) {
                    return false;
                }
            }
        }

        return true;
    }

}
