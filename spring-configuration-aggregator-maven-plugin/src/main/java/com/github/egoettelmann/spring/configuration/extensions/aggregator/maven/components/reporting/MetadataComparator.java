package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.reporting;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.AggregatedPropertyMetadata;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.ArtifactMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetadataComparator {

    private final Log log;

    private final Map<String, AggregatedPropertyMetadata> current;

    public MetadataComparator(final Log log, final List<AggregatedPropertyMetadata> current) {
        this.log = log;
        this.current = current.stream()
                .collect(Collectors.toMap(AggregatedPropertyMetadata::getName, m -> m));
    }

    public ArtifactMetadata.Changes compare(
            final List<AggregatedPropertyMetadata> previous,
            final String previousVersion
    ) {
        final ArtifactMetadata.Changes changes = new ArtifactMetadata.Changes();
        changes.setBaseVersion(previousVersion);
        changes.setAdded(new ArrayList<>());
        changes.setUpdated(new ArrayList<>());
        changes.setDeleted(new ArrayList<>());

        final Map<String, AggregatedPropertyMetadata> previousMap = previous.stream()
                .collect(Collectors.toMap(AggregatedPropertyMetadata::getName, m -> m));

        // Looping over current map to find new and updated values
        for (final Map.Entry<String, AggregatedPropertyMetadata> entry : this.current.entrySet()) {
            final String key = entry.getKey();
            final AggregatedPropertyMetadata value = entry.getValue();

            // Checking if key was present in previous map
            if (!previousMap.containsKey(key)) {
                // Found new key
                this.log.debug("Configuration key '" + key + "' added");
                changes.getAdded().add(key);
                continue;
            }

            // Checking if key has been updated
            final AggregatedPropertyMetadata previousValue = previousMap.get(key);
            if (this.hasChanged(value, previousValue)) {
                this.log.debug("Configuration key '" + key + "' updated");
                changes.getUpdated().add(key);
            }
        }

        // Looping over previous map to find deleted values
        for (Map.Entry<String, AggregatedPropertyMetadata> entry : previousMap.entrySet()) {
            final String key = entry.getKey();
            final AggregatedPropertyMetadata value = entry.getValue();

            // Checking if key was present in previous map
            if (!this.current.containsKey(key)) {
                // Found deleted key
                this.log.debug("Configuration key '" + key + "' deleted");
                changes.getDeleted().add(key);
            }
        }

        return changes;
    }

    private boolean hasChanged(final AggregatedPropertyMetadata current, final AggregatedPropertyMetadata previous) {
        if (!StringUtils.equalsIgnoreCase(current.getType(), previous.getType())) {
            return true;
        }
        if (!StringUtils.equalsIgnoreCase(current.getDefaultValue(), previous.getDefaultValue())) {
            return true;
        }
        return false;
    }

}
