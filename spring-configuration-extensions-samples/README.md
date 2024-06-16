Spring Configuration Extensions: Samples
==========

This module provides sample configurations for using:
 - [spring-value-annotation-processor](../spring-value-annotation-processor)
 - [spring-configuration-aggregator-maven-plugin](../spring-configuration-aggregator-maven-plugin)

Each submodule showcases the various configuration options through its `pom.xml`.

The projects are configured through their `pom.xml` files as follows:
- [samples1](./spring-configuration-extensions-samples1/pom.xml)
  - includes only configuration metadata from dependencies with same 'groupId' as current project (see `includeDependencies`)
  - computes metadata changes against a fixed based version (see `changes`)
  - generates reports in all available formats (see `outputReports`)
- [samples2](./spring-configuration-extensions-samples2/pom.xml)
  - configures the annotation processor to fail on error  (see `failOnError`)
  - defines a custom metadata output file to generate (`metadataOutputFile`)
  - specifies additional configuration metadata to take into account (see `additionalMetadataFiles`)
  - generates a json report into a specific file location (see `outputReports`)
- [samples3](./spring-configuration-extensions-samples3/pom.xml),
  - computes metadata changes against a custom json file (see `changes`)

