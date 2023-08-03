package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.aggregation;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.exceptions.MetadataFileNotFoundException;
import org.apache.commons.io.FilenameUtils;
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

    public List<Properties> read(final String filePath) throws MetadataFileNotFoundException {
        // Parsing filePath as URL
        final URL propertiesUrl;
        try {
            propertiesUrl = new URL(filePath);
        } catch (final MalformedURLException e) {
            throw new MetadataFileNotFoundException("Invalid file path " + filePath, e);
        }
        final String extension = FilenameUtils.getExtension(propertiesUrl.getFile());
        this.log.debug("Reading file '" + filePath + "' with extension '" + extension + "'");

        // Parsing as properties
        if ("properties".equalsIgnoreCase(extension)) {
            return Collections.singletonList(this.readAsProperties(propertiesUrl));
        }

        // Parsing as yaml
        if ("yml".equalsIgnoreCase(extension) || "yaml".equalsIgnoreCase(extension)) {
            return this.readAsYaml(propertiesUrl);
        }

        // Not a valid extension
        throw new MetadataFileNotFoundException("Unsupported file extension " + extension + " for " + propertiesUrl.getFile());
    }

    private Properties readAsProperties(final URL propertiesUrl) throws MetadataFileNotFoundException {
        try (final InputStream fileContent = propertiesUrl.openStream()) {
            this.log.debug("Found property values file " + propertiesUrl.getFile());

            // Parsing content
            final Properties properties = new Properties();
            properties.load(fileContent);

            // Logging all found properties for debug
            if (this.log.isDebugEnabled()) {
                this.log.debug("Found " + properties.size() + " property values in file " + propertiesUrl.getFile());
                for (final Map.Entry<Object, Object> property : properties.entrySet()) {
                    this.log.debug("Found '" + property.getKey() + "=" + property.getValue() + "'");
                }
            }

            // Returning parsed properties
            return properties;
        } catch (final IOException e) {
            throw new MetadataFileNotFoundException("Failed reading " + propertiesUrl.getFile(), e);
        }
    }

    private List<Properties> readAsYaml(final URL propertiesUrl) throws MetadataFileNotFoundException {
        final List<Properties> propertiesList = new ArrayList<>();

        try (final InputStream fileContent = propertiesUrl.openStream()) {
            this.log.debug("Found property values file " + propertiesUrl.getFile());

            // Parsing content: yaml file may contain multiple documents
            final Yaml yaml = new Yaml();
            for (final Object group : yaml.loadAll(fileContent)) {
                final Map<String, String> flattened = this.flatten((Map<String, Object>) group);
                final Properties properties = new Properties();
                properties.putAll(flattened);
                propertiesList.add(properties);
            }

            // Logging all found properties for debug
            if (this.log.isDebugEnabled()) {
                for (final Properties properties : propertiesList) {
                    this.log.debug("Found " + properties.size() + " property values in file " + propertiesUrl.getFile());
                    for (Map.Entry<Object, Object> property : properties.entrySet()) {
                        this.log.debug("Found '" + property.getKey() + "=" + property.getValue() + "'");
                    }
                }
            }

            // Returning parsed properties
            return propertiesList;
        } catch (final IOException e) {
            throw new MetadataFileNotFoundException("Failed reading " + propertiesUrl.getFile(), e);
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
                flatMap.put(key, Objects.toString(value, ""));
            }
        }
        return flatMap;
    }

}
