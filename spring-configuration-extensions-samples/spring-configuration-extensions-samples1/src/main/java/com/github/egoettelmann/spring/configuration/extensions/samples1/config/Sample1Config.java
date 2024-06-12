package com.github.egoettelmann.spring.configuration.extensions.samples1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Sample1Config {

    @Autowired
    private Sample1Properties sample1Properties;

    /**
     * Custom config value injected by @Value.
     */
    @Value("${sample1.custom.conf}")
    private String customConfig;

    /**
     * Config including unicode characters.
     */
    @Value("${sample1.unicode.chars}")
    private String unicodeChars;

    /**
     * Custom config value injected by @Value.
     */
    @Value("${sample1.new.conf}")
    private String newConfig;

}
