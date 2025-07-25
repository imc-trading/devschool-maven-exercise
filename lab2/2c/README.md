# Tests and build phases

Let's take the code from 2b, but add unit tests.

## Unit Test Library

First, we need a unit test library. JUnit is the most widely used library, so we will use that as an example.

Add it as a dependency:

```xml
<dependency>
  <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.3</version>
    <scope>test</scope>
  </dependency>
</dependency>
```

### Scopes

The `test` scope means that this dependency will be on the classpath when compiling and running tests, but not for 
compiling or running non-test code. The default scope is `compile`, which means a dependency will be on the classpath 
when compiling and running production code as well as test code.

## Test Code

Test code goes in `src/test/java`, so put the `HelperTest.java` from 1d in `src/test/java/com/imc/school/util/`.

> By convention, unit tests for a class are named the same as the class with the suffix `Test`, and go in the same
> package in the test sources directory. This gives them access to the package-private methods of the class to be
> tested.

```java
package com.imc.school.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelperTest {
  @Test
  void test() {
    String string = Helper.appendExclamationMark("Hello");
    assertEquals(string, "Hello!");
  }
}
```

## Upgrade Surefire

The Maven Surefire Plugin is responsible for running tests. JUnit 5 requires at least version 2.22.0 of Surefire, so we
need to specify the version of Surefire in the POM.

> What happens if we don't set this? What version of surefire gets used when we run `mvn test`?

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.22.0</version>
            </plugin>
        </plugins>
    </build>
```

## Running Tests

We can now run tests with `mvn test`.

```shell
$ mvn test
...
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ maven-helloworld ---
[INFO] Surefire report directory: /Users/jsmith/code/maven-exercise/lab2/2c/target/surefire-reports

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.imc.school.util.HelperTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.034 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

```

## Build phases

You've now run `mvn` in multiple ways, but they fall in two general camps:

* Run a build phase. The build phases are built into maven, and are the various steps in the 
  [build lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html). The most
  common phases to use are `clean`, `compile`, `test`, `verify`, `package`, and `install`.
* Run the goal of a plugin. `mvn exec:java` ran the `java` goal of the `exec-maven-plugin` plugin.

For now, we will focus on the build phases. The default build lifecycle has the following phases:

* `validate` - validate the POM is correct and all necessary information is available
* `compile` - compile the source code of the project
* `test` - test the compiled source code using a suitable unit testing framework. These tests should not require the code be packaged or deployed
* `package` - take the compiled code and package it in its distributable format, such as a JAR.
* `verify` - run any checks on results of integration tests to ensure quality criteria are met
* `install` - install the package into the local repository, for use as a dependency in other projects locally
* `deploy` - done in the build environment, copies the final package to the remote repository for sharing with other developers and projects.

Calling maven with any of these phases will sequentially run every phase up to that the one requested.

When you request ```mvn package```, Maven will first validate, then compile, then test, and then package the JAR.

The `clean` phase is not part of the default build lifecycle, so you need to run `mvn clean package` if you want it to
do a clean.

## Maven Plugins

As we saw earlier, the functionality that is run in the `test` phase is provided by the Maven Surefire Plugin.

The functionality of most of the build phases are provided by plugins. Additionally, Maven provides some plugins by
default in order to minimize the configuration necessary.

* `validate` - Default POM validation functionality built into Maven.
* `compile` - `maven-compiler-plugin` - Knows how to compile Java code
* `test` - `maven-surefire-plugin` - Knows how to run the most common Java Unit Testing libraries. JUnit 5 is relatively new, so we needed a new version of the plugin.
* `package` - `javen-jar-plugin` - Knows how to build JAR files.
* `verify` - No plugin is bound to this phase by default. `maven-failsafe-plugin` is often used to run integration tests.
* `install` - `maven-install-plugin`
* `deploy` - `maven-deploy-plugin`

If you want to custom functionality, such as building other languages, or anything else, you can define new plugins in
the POM. For example, you can use the `scala-maven-plugin` to be able to build Scala code, or
the `spotless-maven-plugin` to be able to format your code. You can bind any plugin's goal(s) to any phase of the Maven
lifecycle to customize what your build does, or call the plugin's goals directory.

The `exec-maven-plugin` we used in `2b` is an example of a custom plugin made by [Mojohaus](http://mojohaus.org/) for
ease of use. In that example, we did not bind the `exec:java` goal to any phase, but called it directly.

