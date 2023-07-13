package com.github.egoettelmann.spring.configuration.extensions.samples.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleConfig2 {

    /**
     * Custom config value injected by @Value, with default value referencing another config.
     */
    @Value("${sample.custom.conf2:${sample.custom.conf}}")
    private String customConfig2;

}
