package com.github.egoettelmann.spring.configuration.extensions.samples2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sample2.app")
@Data
public class Sample2Properties {

   /**
    * Sample app title injected through @ConfigurationProperties
    */
   private String title;

}
