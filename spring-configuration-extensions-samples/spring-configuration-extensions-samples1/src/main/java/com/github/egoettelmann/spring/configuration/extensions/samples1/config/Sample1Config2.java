package com.github.egoettelmann.spring.configuration.extensions.samples1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Sample1Config2 {

    /**
     * Custom config value injected by @Value, with default value referencing another config.
     */
    @Value("${sample1.custom.conf2:${sample1.custom.conf}}")
    private String customConfig2;

}
