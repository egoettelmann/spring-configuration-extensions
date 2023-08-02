package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value;

import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core.ValueAnnotationMetadata;
import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.exceptions.ValueAnnotationException;
import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.reader.ElementReader;
import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.writer.JsonWriter;

import javax.annotation.processing.*;
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
@SupportedOptions({"failOnError", "targetFilePathForAdditionalSpringValueConfiguration"})
public class ValueAnnotationProcessor extends AbstractProcessor {

    private static final String TARGET_PACKAGE = "";
    private static final String TARGET_FILE_NAME = "META-INF/additional-spring-configuration-metadata.json";
    private static final String TARGET_FILE_PATH_ARG_NAME = "targetFilePathForAdditionalSpringValueConfiguration";

    private ElementReader elementReader;

    private boolean failOnError = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementReader = new ElementReader(processingEnv.getElementUtils());
        final String failOnError = processingEnv.getOptions().get("failOnError");
        this.failOnError = Boolean.parseBoolean(failOnError);
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
                    this.processingEnv.getOptions().getOrDefault(TARGET_FILE_PATH_ARG_NAME, TARGET_FILE_NAME)
                ).openWriter();
    }
}
