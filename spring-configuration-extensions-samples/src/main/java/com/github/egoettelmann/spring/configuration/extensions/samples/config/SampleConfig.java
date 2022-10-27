package com.github.egoettelmann.spring.configuration.extensions.samples.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleConfig {

    @Autowired
    private SampleProperties sampleProperties;

    /**
     * Custom config value injected by @Value.
     */
    @Value("${sample.custom.conf}")
    private String customConfig;

    /**
     * Config including unicode characters.
     */
    @Value("${sample.unicode.chars}")
    private String unicodeChars;

}
