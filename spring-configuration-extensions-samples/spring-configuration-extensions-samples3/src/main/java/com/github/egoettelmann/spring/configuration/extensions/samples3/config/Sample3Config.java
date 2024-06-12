package com.github.egoettelmann.spring.configuration.extensions.samples3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Sample3Config {

    @Autowired
    private Sample3Properties sample3Properties;

    /**
     * Custom config value injected by @Value.
     */
    @Value("${sample3.custom.conf}")
    private String customConfig;

}
