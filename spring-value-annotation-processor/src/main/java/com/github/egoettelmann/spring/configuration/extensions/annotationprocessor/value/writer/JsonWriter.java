package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.writer;

import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core.ValueAnnotationMetadata;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Utility class to write JSON.
 * This class is mainly used to prevent relying on any external dependency.
 */
public class JsonWriter {

    private final Writer writer;

    /**
     * Instantiates the writer.
     *
     * @param writer the writer to write the output to
     */
    public JsonWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Naive JSON writer to prevent using any external dependency.
     *
     * @param metadataList the list of metadata
     */
    public void write(final List<ValueAnnotationMetadata> metadataList) {
        if (metadataList.isEmpty()) {
            return;
        }
        try {
            writer.write("{\"properties\":[");
            int i = 0;
            for (ValueAnnotationMetadata metadata : metadataList) {
                if (i > 0) {
                    writer.write(",");
                }
                writer.write("{");
                this.write("name", metadata.getName());
                writer.write(",");
                this.write("type", metadata.getType());
                writer.write(",");
                this.write("description", metadata.getDescription());
                writer.write(",");
                this.write("sourceType", metadata.getSourceType());
                writer.write(",");
                this.write("defaultValue", metadata.getDefaultValue());
                writer.write("}");
                i++;
            }
            writer.write("]}");
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Naive JSON writer to prevent using any external dependency.
     *
     * @param key the key
     * @param value the value
     * @throws IOException thrown on any writing failure
     */
    private void write(final String key, final String value) throws IOException {
        if (value == null) {
            this.writer.write(String.format("\"%s\": null", key));
            return;
        }
        this.writer.write(String.format("\"%s\": \"%s\"", key, JsonWriter.sanitize(value)));
    }

    private static String sanitize(String s){
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\"", "\\\"")
                .trim();
    }

}
