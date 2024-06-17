package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.reader;

import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core.ValueAnnotationMetadata;
import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core.ValueAnnotationMetadataBuilder;
import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core.ValueAnnotationParser;
import com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.exceptions.ValueAnnotationException;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A reader class to extract the value annotation metadata from a given {@link Element}.
 */
public class ElementReader {

    /**
     * The value annotation class to process
     */
    public static final String VALUE_ANNOTATION_CLASS = "org.springframework.beans.factory.annotation.Value";

    private static final String VALUE_ANNOTATION_PARAM = "value";

    private final Elements elementUtils;

    /**
     * Instantiates the reader.
     *
     * @param elementUtils the utils class to interact with the element context
     */
    public ElementReader(final Elements elementUtils) {
        this.elementUtils = elementUtils;
    }

    /**
     * Extracts the value annotation metadata from the given element.
     *
     * @param element the element to read
     * @return the list of value annotation metadata
     */
    public List<ValueAnnotationMetadata> read(final Element element) {
        try {
            return ValueAnnotationMetadataBuilder.of(this.extractValue(element))
                    .type(element.asType().toString())
                    .sourceType(element.getEnclosingElement().toString())
                    .description(this.elementUtils.getDocComment(element))
                    .build();
        } catch (Exception e) {
            throw new ValueAnnotationException("Could not build metadata", e);
        }

    }

    /**
     * Extracts the value of the annotation.
     * <p>
     * Uses reflection to prevent explicit dependency on Spring.
     *
     * @param element the element to extract the value from
     * @return the value field
     * @throws ValueAnnotationException any exception that may occur during extraction
     */
    private String extractValue(final Element element) {

        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            String annotation = annotationMirror.getAnnotationType().toString();
            if (!VALUE_ANNOTATION_CLASS.equals(annotation)) {
                continue;
            }
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
                if (VALUE_ANNOTATION_PARAM.equals(entry.getKey().getSimpleName().toString())) {
                    Object value = entry.getValue().getValue();
                    return String.valueOf(value);
                }
            }
        }

        throw new ValueAnnotationException("No @Value annotation with property found on element: " + element.getSimpleName().toString());
    }

}
