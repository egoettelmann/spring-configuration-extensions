Spring Configuration Extensions
==========

[![Maven Central](https://img.shields.io/maven-central/v/com.github.egoettelmann/spring-configuration-extensions?style=flat-square&label=Maven%20Central)](https://search.maven.org/artifact/com.github.egoettelmann/spring-configuration-extensions)
[![CircleCI build (develop)](https://img.shields.io/circleci/build/github/egoettelmann/spring-configuration-extensions/develop?label=Develop&style=flat-square)](https://app.circleci.com/pipelines/github/egoettelmann/spring-configuration-extensions?branch=develop)

This project is intended to provide some tools around Spring configurations.

You will find following modules:
 - [spring-value-annotation-processor](./spring-value-annotation-processor):
   an annotation processor to extract metadata for configuration properties from `@Value` annotations.
 - [spring-configuration-aggregator-maven-plugin](./spring-configuration-aggregator-maven-plugin):
   a Maven plugin to aggregate all metadata files of a Spring project and generate reports out of it.

Goal of this project
----------

Most of the time, documentation of possible configuration properties is difficult to keep up to date and becomes easily outdated.
However, configuration properties are an important part of your project (as stated by the [The Twelve-Factor App](https://12factor.net/config)).
So these should not only be well documented, but this documentation should also be part of your artifacts/deliverables.

This project provides some tools intended to cover following goals:
 - add auto-completion of configuration files in your IDE
 - expose possible configuration properties through your app like Spring Boot Actuator does
 - analyze the project's dependencies on configuration properties
 - generate documentation

Project structure
------------

The project is structured in different modules.
You will find the documentation for each module in the corresponding sub-folder.

In addition to the main modules, you can find the following :
 - [spring-configuration-extensions-samples](./spring-configuration-extensions-samples):
   a module that provides some samples to illustrate the usage of the different tools
   and on which all integration tests are performed. 
