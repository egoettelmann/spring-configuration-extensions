package com.github.egoettelmann.spring.configuration.extensions.samples2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Sample2Config {

    @Autowired
    private Sample2Properties sample2Properties;

    /**
     * Custom config value injected by @Value.
     */
    @Value("${sample2.custom.conf}")
    private String customConfig;

    /**
     * Config including unicode characters.
     */
    @Value("${sample2.unicode.chars}")
    private String unicodeChars;

}
