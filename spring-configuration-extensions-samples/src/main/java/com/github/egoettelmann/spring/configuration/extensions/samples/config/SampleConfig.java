package com.github.egoettelmann.spring.configuration.extensions.samples.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleConfig {

    @Autowired
    private SampleProperties sampleProperties;

    @Value("${sample.custom.conf}")
    private String customConfig;

    @Value("${sample.unicode.chars}")
    private String unicodeChars;

}
