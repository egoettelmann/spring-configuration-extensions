package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilePath {

    private String path;

    public static String getPath(String path) {
        return new FilePath(path).get();
    }

    public String get() {
        return "file:///" + this.path;
    }

}
