package com.github.egoettelmann.annotationprocessor.spring.value.writer;

import com.github.egoettelmann.annotationprocessor.spring.value.core.ValueAnnotationMetadata;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class JsonWriter {

    private final Writer writer;

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

    public void write(final String key, final String value) throws IOException {
        if (value == null) {
            this.writer.write(String.format("\"%s\": null", key));
            return;
        }
        this.writer.write(String.format("\"%s\": \"%s\"", key, value));
    }

}
