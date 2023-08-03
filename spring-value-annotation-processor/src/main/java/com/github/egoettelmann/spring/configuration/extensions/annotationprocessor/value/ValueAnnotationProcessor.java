package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value;

import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core.ValueAnnotationMetadata;
import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.exceptions.ValueAnnotationException;
import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.reader.ElementReader;
import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.writer.JsonWriter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes(ElementReader.VALUE_ANNOTATION_CLASS)
@SupportedOptions({ValueAnnotationProcessor.ARG_FAIL_ON_ERROR, ValueAnnotationProcessor.ARG_METADATA_OUTPUT_FILE})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ValueAnnotationProcessor extends AbstractProcessor {

    public static final String ARG_FAIL_ON_ERROR = "failOnError";

    public static final String ARG_METADATA_OUTPUT_FILE = "metadataOutputFile";

    private static final String TARGET_PACKAGE = "";

    private boolean failOnError = false;

    private String metadataOutputFile = "META-INF/additional-spring-configuration-metadata.json";

    private ElementReader elementReader;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementReader = new ElementReader(processingEnv.getElementUtils());
        final String failOnError = processingEnv.getOptions().get(ValueAnnotationProcessor.ARG_FAIL_ON_ERROR);
        this.failOnError = Boolean.parseBoolean(failOnError);
        this.metadataOutputFile = processingEnv.getOptions().getOrDefault(ValueAnnotationProcessor.ARG_METADATA_OUTPUT_FILE, this.metadataOutputFile);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        final List<ValueAnnotationMetadata> metadataList = new ArrayList<>();
        for (final TypeElement annotation : annotations) {
            for (final Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                try {
                    final List<ValueAnnotationMetadata> results = this.elementReader.read(element);
                    for (final ValueAnnotationMetadata metadata : results) {
                        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found @Value annotation with property '" + metadata.getName() + "'");
                        metadataList.add(metadata);
                    }
                } catch (final Exception e) {
                    final String errorMessage = "Error while processing @Value annotations on " + element.getSimpleName();
                    if (this.failOnError) {
                        throw new ValueAnnotationException(errorMessage, e);
                    }
                    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, errorMessage);
                }
            }
        }
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found a total of " + metadataList.size() + " @Value annotations");
        this.writeToTargetFile(metadataList);
        return false;
    }

    private void writeToTargetFile(final List<ValueAnnotationMetadata> metadata) {
        try (final Writer writer = this.getFileWriter()) {
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.write(metadata);
        } catch (IOException ioe) {
            throw new ValueAnnotationException("Could not create target resource file", ioe);
        }
    }

    private Writer getFileWriter() throws IOException {
        return this.processingEnv.getFiler()
                .createResource(
                        StandardLocation.CLASS_OUTPUT,
                        TARGET_PACKAGE,
                        this.metadataOutputFile
                ).openWriter();
    }
}
