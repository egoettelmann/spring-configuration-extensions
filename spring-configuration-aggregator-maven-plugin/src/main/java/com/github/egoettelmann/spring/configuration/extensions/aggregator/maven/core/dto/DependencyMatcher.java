package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import org.eclipse.aether.graph.Dependency;

public class DependencyMatcher {

    private String groupId;

    private String artifactId;

    private String scope;

    public boolean matches(final Dependency dependency) {
        // Filter by groupId is defined and it does not match
        if (this.groupId != null && !this.groupId.equals(dependency.getArtifact().getGroupId())) {
            return false;
        }

        // Filter by artifactId is defined and it does not match
        if (this.artifactId != null && !this.artifactId.equals(dependency.getArtifact().getArtifactId())) {
            return false;
        }

        // Filter by scope is defined and it does not match
        if (this.scope != null && !this.scope.equals(dependency.getScope())) {
            return false;
        }

        // Artifact matches filter
        return true;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
