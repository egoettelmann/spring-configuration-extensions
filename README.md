Spring Value Annotation Processor
==========

[![Maven Central](https://img.shields.io/maven-central/v/com.github.egoettelmann/spring-value-annotation-processor?style=flat-square&label=Maven%20Central)](https://search.maven.org/artifact/com.github.egoettelmann/spring-value-annotation-processor)
[![CircleCI build (develop)](https://img.shields.io/circleci/build/github/egoettelmann/spring-value-annotation-processor/develop?label=Develop&style=flat-square)](https://app.circleci.com/pipelines/github/egoettelmann/spring-value-annotation-processor?branch=develop)

An annotation processor to extract all configuration properties injected through Spring `@Value` annotations.

It generates a `additional-spring-configuration-metadata.json` conforming to the [Configuration Metadata](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-configuration-metadata.html) format defined by Spring Boot.

Goal of this project
----------

### Background

Most of the time, possible configuration properties for legacy Spring projects are maintained manually.
Documentation is difficult to keep up to date and becomes easily outdated.

This annotation processor allows to automatically extract all configuration properties at build time.
The generated file(s) can be used to:
 - add auto-completion of configuration files in your IDE
 - expose possible configuration properties through your app like Spring Boot Actuator does
 - analyze the project's dependencies on configuration properties
 - generate documentation

### Generated metadata

For every class attribute annotated with `@Value`, the processor will generate:
 - the `name` of the configuration property used
 - the `type` of the attribute
 - the `description` taken from the JavaDoc defined on the attribute (if any)
 - the `sourceType` of the enclosing class
 - the `defaultValue` specified in the annotation (if any)

Additionally, the processor recognizes **SPEL** expressions and extracts properties out of them.
In this case the `type` will not correspond to the property itself, but to the attribute's one.
Currently, if multiple properties are used within the same expression, only the first one is extracted.


Usage
------------

If using Maven, you can simply add the project as a `provided` dependency.
```xml
<dependency>
    <groupId>com.github.egoettelmann</groupId>
    <artifactId>spring-value-annotation-processor</artifactId>
    <version>0.0.3-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

If you are using the `maven-compiler-plugin` for your build, you will have to declare it in the `annotationProcessorPaths`.
The plugin's configuration should look to something like the following:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <annotationProcessorPaths>
            <!-- in addition to any other annotation processor (like Lombok, etc.) -->
            <annotationProcessorPath>
                <groupId>com.github.egoettelmann</groupId>
                <artifactId>spring-value-annotation-processor</artifactId>
                <version>0.0.3-SNAPSHOT</version>
            </annotationProcessorPath>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

After executing `mvn clean package`, you can find the `additional-spring-configuration-metadata.json` file under `target/classes/META-INF/`.
