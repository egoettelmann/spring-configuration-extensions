Spring Configuration Extensions: Value Annotation Processor
==========

[![Maven Central](https://img.shields.io/maven-central/v/com.github.egoettelmann/spring-value-annotation-processor?style=flat-square&color=blue&label=Maven%20Central&logo=Apache%20Maven&logoColor=orange)](https://search.maven.org/artifact/com.github.egoettelmann/spring-value-annotation-processor)
[![CircleCI build (develop)](https://img.shields.io/circleci/build/github/egoettelmann/spring-configuration-extensions/develop?label=develop&logo=circleci&style=flat-square)](https://app.circleci.com/pipelines/github/egoettelmann/spring-configuration-extensions?branch=develop)

An annotation processor to extract all configuration properties injected through Spring `@Value` annotations.

It generates a `additional-spring-configuration-metadata.json` conforming to the [Configuration Metadata](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-configuration-metadata.html) format defined by Spring Boot.

Usage
------------

If using Maven, you can simply add the project as a `provided` dependency.
```xml
<dependency>
    <groupId>com.github.egoettelmann</groupId>
    <artifactId>spring-value-annotation-processor</artifactId>
    <version>0.1.3</version>
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
                <version>0.1.3</version>
            </annotationProcessorPath>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

After executing `mvn clean package`, you can find the `additional-spring-configuration-metadata.json` file under `target/classes/META-INF/`.

### Additional compiler options

Following additional option is available: `failOnError`.
This option is by default `false`.
If defined at `true`, the annotation processor will fail if a property cannot be parsed.

Configuration would be as follows:
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
                <version>0.1.3</version>
            </annotationProcessorPath>
        </annotationProcessorPaths>
        <compilerArgs>
            <arg>-AfailOnError=true</arg>
        </compilerArgs>
    </configuration>
</plugin>
```
