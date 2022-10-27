package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.aggregation;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.MetadataFileNotFoundException;
import org.apache.maven.plugin.logging.Log;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

class PropertiesValueReader {

    private final Log log;

    public PropertiesValueReader(final Log log) {
        this.log = log;
    }

    public Properties read(final String filePath) throws MetadataFileNotFoundException {
        // Parsing filePath as URL
        final URL propertiesUrl;
        try {
            propertiesUrl = new URL(filePath);
        } catch (final MalformedURLException e) {
            throw new MetadataFileNotFoundException("Invalid file path " + filePath, e);
        }

        final String extension = filePath.substring(filePath.lastIndexOf("."));
        if (".properties".equalsIgnoreCase(extension)) {
            return this.readAsProperties(propertiesUrl);
        }
        if (".yml".equalsIgnoreCase(extension) || ".yaml".equalsIgnoreCase(extension)) {
            return this.readAsYaml(propertiesUrl);
        }

        throw new MetadataFileNotFoundException("Unsupported file extension " + extension + " for " + propertiesUrl);
    }

    private Properties readAsProperties(final URL propertiesUrl) throws MetadataFileNotFoundException {
        try (final InputStream fileContent = propertiesUrl.openStream()) {
            this.log.debug("Found property values file " + propertiesUrl.getFile());

            // Parsing content
            final Properties properties = new Properties();
            properties.load(fileContent);
            this.log.debug("Found " + properties.size() + " property values in file " + propertiesUrl);

            // Returning parsed properties
            return properties;
        } catch (final IOException e) {
            throw new MetadataFileNotFoundException("Failed reading " + propertiesUrl, e);
        }
    }

    private Properties readAsYaml(final URL propertiesUrl) throws MetadataFileNotFoundException {
        try (final InputStream fileContent = propertiesUrl.openStream()) {
            this.log.debug("Found property values file " + propertiesUrl.getFile());

            // Parsing content
            final Properties properties = new Properties();
            final Yaml yaml = new Yaml();
            final Map<String, String> flattened = this.flatten(yaml.load(fileContent));
            properties.putAll(flattened);
            this.log.debug("Found " + properties.size() + " property values in file " + propertiesUrl);
            for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
                this.log.debug("Found '" + entry.getKey() + "=" + entry.getValue() + "'");
            }

            // Returning parsed properties
            return properties;
        } catch (final IOException e) {
            throw new MetadataFileNotFoundException("Failed reading " + propertiesUrl, e);
        }
    }

    /**
     * Values extracted from YAML files are flattened, as the hierarchical map structure does not fit well.
     * The structure is traversed recursively to put all properties into a flat map.
     * <p>
     * Note: as we have no hint which exact level will be mapped to a configuration property, we keep all paths.
     *
     * @param hierarchicalMap the hierarchical map of configuration values
     * @return the flattened map
     */
    private Map<String, String> flatten(final Map<String, Object> hierarchicalMap) {
        final Map<String, String> flatMap = new HashMap<>();
        for (final Map.Entry<String, Object> entry : hierarchicalMap.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            flatMap.put(key, value.toString());
            if (value instanceof Map) {
                final Map<String, Object> nested = (Map<String, Object>) value;
                final Map<String, String> flatNested = this.flatten(nested);
                for (final Map.Entry<String, String> nestedEntry : flatNested.entrySet()) {
                    flatMap.put(key + "." + nestedEntry.getKey(), nestedEntry.getValue());
                }
            } else if (value instanceof Collection) {
                final Object[] nested = ((Collection<Object>) value).toArray();
                for (int i = 0; i < nested.length; i++) {
                    final Object nestedValue = nested[i];
                    final String nestedKey = key + "[" + i + "]";
                    final Map<String, String> flatNested = this.flatten(Collections.singletonMap(nestedKey, nestedValue));
                    flatMap.putAll(flatNested);
                }
            } else {
                flatMap.put(key, value.toString());
            }
        }
        return flatMap;
    }

}
