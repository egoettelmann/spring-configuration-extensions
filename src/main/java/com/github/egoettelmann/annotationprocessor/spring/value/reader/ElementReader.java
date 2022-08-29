package com.github.egoettelmann.annotationprocessor.spring.value.reader;

import com.github.egoettelmann.annotationprocessor.spring.value.core.ValueAnnotationMetadata;
import com.github.egoettelmann.annotationprocessor.spring.value.core.ValueAnnotationMetadataBuilder;
import com.github.egoettelmann.annotationprocessor.spring.value.exceptions.ValueAnnotationException;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;
import java.util.Map;
import java.util.Optional;

public class ElementReader {

    public static final String VALUE_ANNOTATION_CLASS = "org.springframework.beans.factory.annotation.Value";

    private static final String VALUE_METHOD = "value";

    private final Elements elementUtils;

    public ElementReader(final Elements elementUtils) {
        this.elementUtils = elementUtils;
    }

    public Optional<ValueAnnotationMetadata> read(final Element element) {
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
                if (VALUE_METHOD.equals(entry.getKey().getSimpleName().toString())) {
                    Object value = entry.getValue().getValue();
                    return String.valueOf(value);
                }
            }
        }

        throw new ValueAnnotationException("No @Value annotation with property found on element: " + element.getSimpleName().toString());
    }

}
