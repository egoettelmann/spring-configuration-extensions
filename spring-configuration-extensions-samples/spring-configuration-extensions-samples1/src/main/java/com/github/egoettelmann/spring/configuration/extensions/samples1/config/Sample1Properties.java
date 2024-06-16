package com.github.egoettelmann.spring.configuration.extensions.samples1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sample1.app")
@Data
public class Sample1Properties {

   /**
    * Sample app title injected through @ConfigurationProperties
    */
   private String title;

   /**
    * Boolean value without any default value
    */
   private boolean bool;

}
