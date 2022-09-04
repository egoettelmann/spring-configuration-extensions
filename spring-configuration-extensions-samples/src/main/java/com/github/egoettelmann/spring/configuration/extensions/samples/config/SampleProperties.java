package com.github.egoettelmann.spring.configuration.extensions.samples.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sample.app")
@Data
public class SampleProperties {

   private String title;

}
