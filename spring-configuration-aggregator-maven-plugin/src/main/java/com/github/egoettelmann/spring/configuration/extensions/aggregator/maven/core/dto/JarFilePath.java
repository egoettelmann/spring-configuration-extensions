package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JarFilePath {

    private String jar;

    private String path;

    public static String getPath(String jar) {
        return getPath(jar, null);
    }

    public static String getPath(String jar, String path) {
        return new JarFilePath(jar, path).toString();
    }

    public String toString() {
        return "jar:file:" + getJar() + "!" + getPath();
    }

}
