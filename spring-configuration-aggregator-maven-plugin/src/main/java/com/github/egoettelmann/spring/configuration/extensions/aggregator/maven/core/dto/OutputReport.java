package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto;

import lombok.Data;

@Data
public class OutputReport {

    private String type;

    private String templateFile;

    private String outputFile;

}
