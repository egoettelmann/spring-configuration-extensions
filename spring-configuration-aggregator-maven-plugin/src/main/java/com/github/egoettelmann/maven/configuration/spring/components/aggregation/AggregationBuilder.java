package com.github.egoettelmann.maven.configuration.spring.components.aggregation;

import com.github.egoettelmann.maven.configuration.spring.core.model.AggregatedPropertyMetadata;
import com.github.egoettelmann.maven.configuration.spring.core.model.PropertyMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.plugin.logging.Log;

import java.util.*;
import java.util.stream.Collectors;

public class AggregationBuilder {

    private final Log log;

    private final Map<Pair<String, String>, List<PropertyMetadata>> artifactProperties;

    private final Properties defaultValues;

    public AggregationBuilder(final Log log) {
        this.log = log;
        this.artifactProperties = new HashMap<>();
        this.defaultValues = new Properties();
    }

    public AggregationBuilder add(final List<PropertyMetadata> metadata, final String groupId, final String artifactId) {
        // Building map key
        final Pair<String, String> key = Pair.of(groupId, artifactId);

        // Initializing entry if key not registered yet
        if (!this.artifactProperties.containsKey(key)) {
            this.artifactProperties.put(key, new ArrayList<>());
        }

        // Adding all properties
        this.artifactProperties.get(key).addAll(metadata);

        return this;
    }

    public AggregationBuilder put(final Properties defaultValues) {
        this.defaultValues.putAll(defaultValues);
        return this;
    }

    public List<AggregatedPropertyMetadata> build() {
        final Map<String, AggregatedPropertyMetadata> aggregationMap = new HashMap<>();

        // Adding all properties for each artifact entry
        for (final Map.Entry<Pair<String, String>, List<PropertyMetadata>> entry : this.artifactProperties.entrySet()) {
            // Retrieving values for current entry
            final String groupId = entry.getKey().getLeft();
            final String artifactId = entry.getKey().getRight();
            final List<PropertyMetadata> properties = entry.getValue();

            // Adding each property one by one
            for (final PropertyMetadata property : properties) {
                this.log.debug("Aggregating configuration property " + property);

                // Property does not exist yet: create it and add it
                if (!aggregationMap.containsKey(property.getName())) {
                    final AggregatedPropertyMetadata aggregate = this.create(property, groupId, artifactId);
                    aggregationMap.put(property.getName(), aggregate);
                    continue;
                }

                // Property exists already: merging
                final AggregatedPropertyMetadata aggregate = aggregationMap.get(property.getName());
                this.merge(aggregate, property, groupId, artifactId);
            }
        }

        // Returning sorted list
        return aggregationMap.values().stream()
                .sorted(Comparator.comparing(AggregatedPropertyMetadata::getName))
                .collect(Collectors.toList());
    }

    private AggregatedPropertyMetadata create(final PropertyMetadata property, final String groupId, final String artifactId) {
        final AggregatedPropertyMetadata aggregate = new AggregatedPropertyMetadata();
        aggregate.setName(property.getName());
        aggregate.setDefaultValue(property.getDefaultValue());
        // If a default value is defined, it overrides the already defined one
        if (this.defaultValues.containsKey(property.getName())) {
            aggregate.setDefaultValue(this.defaultValues.getProperty(property.getName()));
        }
        aggregate.setType(property.getType());
        aggregate.setDescription(property.getDescription());
        aggregate.setSourceTypes(new ArrayList<>());
        aggregate.getSourceTypes().add(this.create(property.getSourceType(), groupId, artifactId));
        return aggregate;
    }

    private AggregatedPropertyMetadata.Source create(final String sourceType, final String groupId, final String artifactId) {
        final AggregatedPropertyMetadata.Source source = new AggregatedPropertyMetadata.Source();
        source.setSourceType(sourceType);
        source.setArtifactId(artifactId);
        source.setGroupId(groupId);
        return source;
    }

    private void merge(
            final AggregatedPropertyMetadata aggregate,
            final PropertyMetadata property,
            final String groupId,
            final String artifactId
    ) {
        // Appending description if no overlap in documentation
        this.mergeDescription(aggregate, property);

        // Checking if source type exists
        final Optional<AggregatedPropertyMetadata.Source> existing = aggregate.getSourceTypes().stream()
                .filter(st -> st.getSourceType() != null)
                .filter(st -> st.getSourceType().equalsIgnoreCase(property.getSourceType()))
                .findFirst();
        if (!existing.isPresent()) {
            final AggregatedPropertyMetadata.Source source = this.create(property.getSourceType(), groupId, artifactId);
            aggregate.getSourceTypes().add(source);
        }

        // Checking that type matches
        if (!Objects.equals(aggregate.getType(), property.getType())) {
            // TODO: depending on config, following actions could be taken:
            //  - throw error
            //  - keep existing
            //  - override
        }

        // Checking that type matches
        if (!Objects.equals(aggregate.getDefaultValue(), property.getDefaultValue())) {
            // TODO: depending on config, following actions could be taken:
            //  - throw error
            //  - keep existing
            //  - override
        }
    }

    private void mergeDescription(
            final AggregatedPropertyMetadata aggregate,
            final PropertyMetadata property
    ) {
        // No description to add: ignoring
        if (StringUtils.isBlank(property.getDescription())) {
            return;
        }

        // No existing description: using provided one
        if (StringUtils.isBlank(aggregate.getDescription())) {
            aggregate.setDescription(StringUtils.trim(property.getDescription()));
            return;
        }

        // Description already contained in existing: ignoring
        if (aggregate.getDescription().contains(property.getDescription())) {
            return;
        }

        // Appending description
        final String newDescription = aggregate.getDescription() +
                System.lineSeparator() +
                StringUtils.trim(property.getDescription());
        aggregate.setDescription(newDescription);
    }
}
