# Simple Maven Example

Let's convert the simple example from exercise 1c to a Maven project.

# POM

The simplest POM just describes the Maven coordinates (aside from some required boilerplate).

In practice, we also need to define at least a couple of properties - by default the `maven-compiler-plugin`
uses Java 5 source/target, which is no longer supported by the newer versions of the JDK.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.imc.school.maven</groupId>
    <artifactId>maven-helloworld</artifactId>
    <version>0.1</version>

    <properties>
        <maven.compiler.target>11</maven.compiler.target>
        <maven.compiler.source>11</maven.compiler.source>
    </properties>
</project>
```

# Directory structure

Source code goes into `src/main/java`. So we copy `HelloWorld.java` to `src/main/java/com/imc/school/maven`
and `Helper.java` to `src/main/java/com/imc/school/util/`.

# Compile

Compile the code by running:

```shell
$ mvn compile
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------< com.imc.school.maven:maven-helloworld >----------------
[INFO] Building maven-helloworld 0.1
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
...
[INFO] --- compiler:3.11.0:compile (default-compile) @ maven-helloworld ---
[INFO] Changes detected - recompiling the module! :source
[INFO] Compiling 2 source files with javac [debug target 11] to target/classes
```

If you inspect the `target/classes` directory, you can see that it compiled everything the same as when we did it
with `javac`:

```shell
$ tree target/classes
target/classes
└── com
    └── imc
        └── school
            ├── maven
            │   └── HelloWorld.class
            └── util
                └── Helper.class
```

# Package

Package the jar by running:

```shell
$ mvn package
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------< com.imc.school.maven:maven-helloworld >----------------
[INFO] Building maven-helloworld 0.1
[INFO] --------------------------------[ jar ]---------------------------------
... 
[INFO] --- compiler:3.11.0:compile (default-compile) @ maven-helloworld ---
[INFO] Nothing to compile - all classes are up to date
...
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ maven-helloworld ---
[INFO] Building jar: /Users/jsmith/code/maven-exercise/lab2/2a/target/maven-helloworld-0.1.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.645 s
[INFO] Finished at: 2022-06-15T21:12:32-05:00
[INFO] ------------------------------------------------------------------------
```

This builds the `jar` to your `target/` directory, as we can see from the output above. You can now run the jar
like before:

```shell
$ java --class-path=target/maven-helloworld-0.1.jar com.imc.school.maven.HelloWorld John Smith
Hello, John Smith!
```

> Note: If you run `mvn package` again, you will see different behavior:
> ```shell
> [INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ maven-helloworld ---
> [INFO] Nothing to compile - all classes are up to date
> ...
> [INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ maven-helloworld ---
> [INFO] ------------------------------------------------------------------------
> [INFO] BUILD SUCCESS
> [INFO] ------------------------------------------------------------------------
>```
> 
> This is because Maven has some incremental build support - it won't recompile and re-package the jar if it detects
> that there have been no changes to the source files in question. 
> 
> Use `mvn clean package` to delete the `target/` directory and force a re-compile.
