package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import lombok.Data;

@Data
public class ChangesOptions {

    private Boolean skip;

    private String baseVersion;

    private String baseFilePath;

}
