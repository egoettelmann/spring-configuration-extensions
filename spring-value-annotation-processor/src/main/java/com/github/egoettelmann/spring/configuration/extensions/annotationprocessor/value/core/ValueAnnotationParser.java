package com.github.egoettelmann.spring.configuration.extensions.annotationprocessor.value.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser to resolve property placeholders within value annotations
 */
public class ValueAnnotationParser {

    private final String prefix = "${";
    private final String suffix = "}";
    private final String separator = ":";
    private final String opposingSuffix = "{";

    /**
     * Parses a value and returns a map of properties found, with:
     * - the key being the property
     * - the value being the default value (can be null)
     *
     * @param value the value to parse
     * @return the map of parse properties and their default values
     * @throws IllegalArgumentException if the provided value is invalid and cannot be parsed
     */
    public Map<String, String> parse(final String value) throws IllegalArgumentException {
        final Map<String, String> values = new HashMap<>();

        // Searching first occurrence of PREFIX
        int startIdx = value.indexOf(this.prefix);
        if (startIdx == -1) {
            // No occurrence found
            return values;
        }
        startIdx += this.prefix.length();

        // Searching end indexes (matching SEPARATOR and SUFFIX positions)
        final String afterPrefix = value.substring(startIdx);
        int[] indexes = this.findIndexes(afterPrefix);
        int separatorIdx = indexes[0];
        int endIdx = indexes[1];

        // Retrieving property and default value
        String property = afterPrefix.substring(0, endIdx).trim();
        String defaultValue = null;

        // Separator found: splitting to retrieve values
        if (separatorIdx > -1) {
            property = afterPrefix.substring(0, separatorIdx).trim();
            defaultValue = afterPrefix.substring(separatorIdx + this.separator.length(), endIdx).trim();
        }
        values.put(property, defaultValue);

        // Parsing default value
        if (defaultValue != null) {
            values.putAll(this.parse(defaultValue));
        }

        // Parsing remainder recursively
        if (endIdx + this.suffix.length() < afterPrefix.length()) {
            final String remainder = afterPrefix.substring(endIdx + this.suffix.length());
            values.putAll(this.parse(remainder));
        }

        // Returning values
        return values;
    }

    /**
     * Finds the indexes of the separator and the suffix within the provided string, with:
     * - at position 0, the index of the separator (can be -1)
     * - at position 1, the index of the ending suffix
     *
     * @param string the string to search
     * @return the indexes
     * @throws IllegalArgumentException if no matching ending suffix has been found
     */
    private int[] findIndexes(final String string) throws IllegalArgumentException {
        int[] result = {-1, -1};
        int depth = 0;

        int index = 0;
        while (index < string.length()) {
            final String substring = string.substring(index);

            // Prefix encountered: incrementing depth
            if (substring.startsWith(this.prefix)) {
                // Checking that separator has been encountered
                if (result[0] < 0) {
                    throw new IllegalArgumentException(String.format("Prefix encountered before separator in '%s'", string));
                }

                // Incrementing depth
                depth++;
                index += this.prefix.length();
                continue;
            }

            // Opposing suffix encountered
            if (substring.startsWith(this.opposingSuffix)) {
                // Checking that separator has been encountered
                if (result[0] < 0) {
                    throw new IllegalArgumentException(String.format("Opposing suffix encountered before separator in '%s'", string));
                }

                // Incrementing depth
                depth++;
                index += this.opposingSuffix.length();
                continue;
            }

            // Separator encountered: checking depth
            if (substring.startsWith(this.separator)) {
                // No separator found yet: saving index
                if (result[0] == -1) {
                    result[0] = index;
                }

                // Continuing
                index += this.separator.length();
                continue;
            }

            // Suffix encountered: checking depth
            if (substring.startsWith(this.suffix)) {
                // Depth not 0: decrementing depth, ignoring and continuing
                if (depth > 0) {
                    depth--;
                    index += this.suffix.length();
                    continue;
                }

                // End found
                result[1] = index;
                return result;
            }

            // Neither prefix nor suffix found: continuing
            index++;
        }

        // No end found
        throw new IllegalArgumentException(String.format("No ending suffix found in '%s'", string));
    }

}
