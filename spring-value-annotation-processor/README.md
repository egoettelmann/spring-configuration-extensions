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
    <version>0.2.2-SNAPSHOT</version>
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
                <version>0.2.2-SNAPSHOT</version>
            </annotationProcessorPath>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

After executing `mvn clean package`, you can find the `additional-spring-configuration-metadata.json` file under `target/classes/META-INF/`.

### Additional compiler options

Following additional options are available:

| Property              | Type      | Description                                                                                         |
|-----------------------|-----------|-----------------------------------------------------------------------------------------------------|
| `failOnError`         | `boolean` | Specifies if errors during the processing should make the build fail. Default: `false`.             |
| `metadataOutputFile`  | `String`  | Defines a different output file. Default: `META-INF/additional-spring-configuration-metadata.json`. |

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
                <version>0.2.2-SNAPSHOT</version>
            </annotationProcessorPath>
        </annotationProcessorPaths>
        <compilerArgs>
            <arg>-AfailOnError=true</arg>
            <arg>-AmetadataOutputFile=META-INF/additional4value-spring-configuration-metadata.json</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

Known issues
------------

List of known issues and how to avoid/fix them.

### Using custom additional spring configuration metadata [#10](https://github.com/egoettelmann/spring-configuration-extensions/issues/10)

If defining a custom `additional-spring-configuration-metadata.json`
in your resources (as described by the [Spring documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#appendix.configuration-metadata.annotation-processor.adding-additional-metadata)),
in combination with the [spring-boot-configuration-processor](https://central.sonatype.com/artifact/org.springframework.boot/spring-boot-configuration-processor/),
you will get following error during the build:
```
javax.annotation.processing.FilerException: Attempt to reopen a file for path /META-INF/additional-spring-configuration-metadata.json
```

This is because both annotation processors, `spring-boot-configuration-processor` and `spring-value-annotation-processor` try to create the same file. 
As Java specifications [JSR-269](https://jcp.org/en/jsr/detail?id=269) for annotation processing does not allow reopening an already written file, the execution fails.

The solution is to specify a different output file using the `-AmetadataOutputFile` compiler option, to prevent any conflict.

