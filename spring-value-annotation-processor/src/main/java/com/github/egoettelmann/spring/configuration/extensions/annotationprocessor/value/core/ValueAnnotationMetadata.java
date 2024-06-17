package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core;

/**
 * Data holder for value annotation metadata
 */
public class ValueAnnotationMetadata {

    private String name;
    private String type;
    private String description;
    private String sourceType;
    private String defaultValue;

    /**
     * Instantiates the value annotation metadata
     */
    public ValueAnnotationMetadata() {
    }

    /**
     * Get the name of the property.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the property.
     *
     * @param name the property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the type of the property.
     *
     * @return the property name
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the property.
     *
     * @param type the property name
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the description of the property.
     *
     * @return the property description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the property.
     *
     * @param description the property description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the source type of the property.
     *
     * @return the property source type
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * Set the source type of the property.
     *
     * @param sourceType the property source type
     */
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * Get the default value of the property.
     *
     * @return the property default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Set the default value of the property.
     *
     * @param defaultValue the property default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
