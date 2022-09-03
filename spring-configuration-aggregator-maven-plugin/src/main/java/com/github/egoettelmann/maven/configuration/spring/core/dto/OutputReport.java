package com.github.egoettelmann.maven.configuration.spring.core.dto;

import lombok.Data;

@Data
public class OutputReport {

    private String type;

    private String templateFile;

    private String outputFile;

}
