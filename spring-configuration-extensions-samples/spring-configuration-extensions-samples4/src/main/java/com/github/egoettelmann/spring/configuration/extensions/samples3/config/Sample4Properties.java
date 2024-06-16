package com.github.egoettelmann.spring.configuration.extensions.samples3.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sample4.app")
@Data
public class Sample4Properties {

   private boolean bool;

}
