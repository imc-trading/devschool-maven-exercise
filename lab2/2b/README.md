# Dependencies

Let's add the dependency from exercise 1d.

## POM

Add the following dependency to the pom.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <!-- Previous elements like coordinates, properties, etc.  -->
    
    <dependencies>
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>31.0.1-jre</version>
        </dependency>
    </dependencies>
</project>
```

## Source code:

Once again, we copy `HelloWorld.java` to `src/main/java/com/imc/school/maven`
and `Helper.java` to `src/main/java/com/imc/school/util/` from Exercise 1d.

## Building

Compile and package the jar by running. Maven handles setting the class-path for `javac`.

```shell
$ mvn package
```

## Running

We could call `java` directly like previously, but since we need our dependencies on the classpath, we need to ask
Maven where the dependencies are.

> Note:
> Maven is primarily a build tool, so actually executing code isn't its core functionality. Neither of the following 
> methods are how we run code at IMC. IMC internal and industry standard solutions for running code are out of the scope
> of this training.

### Using dependency:build-classpath
We can get the classpath of with the dependencies by asking `mvn`:
```shell
mvn dependency:build-classpath -DincludeScope=runtime -Dmdep.outputFile=target/dependencies-classpath.txt
...
[INFO] --- dependency:3.6.1:build-classpath (default-cli) @ maven-helloworld ---
[INFO] Wrote classpath file '/Users/jsmith/code/maven-exercise/lab2/2b/target/dependencies-classpath.txt'.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
...
```

This outputs the dependencies to `target/dependencies-classpath.txt`. We can then pass it to `java` with:
```shell
$ java --class-path=target/maven-helloworld-0.1.jar:$(<target/dependencies-classpath.txt)  com.imc.school.maven.HelloWorld John Smith 
Hello, John Smith!
```

### Using exec:java
Another option is to invoke a different plugin, the exec-maven-plugin to execute our code.

We can configure the plugin in our POM so it knows the correct main to call.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <!-- Previous elements like coordinates, properties, etc.  -->

    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>1.6.0</version>
                <configuration>
                    <mainClass>com.imc.school.maven.HelloWorld</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

```shell
$ mvn exec:java -Dexec.args="John Smith"
```

## Transitive dependencies

If you inspect the `target/dependencies-classpath.txt` file, or list the dependencies with:

```shell
$ mvn dependency:list
[INFO] --- maven-dependency-plugin:2.8:list (default-cli) @ maven-helloworld ---
[INFO] 
[INFO] The following files have been resolved:
[INFO]    com.google.code.findbugs:jsr305:jar:3.0.2:compile
[INFO]    com.google.guava:guava:jar:31.0.1-jre:compile
[INFO]    com.google.guava:failureaccess:jar:1.0.1:compile
[INFO]    com.google.errorprone:error_prone_annotations:jar:2.7.1:compile
[INFO]    com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile
[INFO]    org.checkerframework:checker-qual:jar:3.12.0:compile
[INFO]    com.google.j2objc:j2objc-annotations:jar:1.3:compile
```

You'll notice that there are a few other dependencies. If you look at the Guava
[Release Notes](https://github.com/google/guava/releases) you'll see that one of those is actually required at
runtime: failureaccess-1.0.1.jar. We got lucky in our previous examples where we didn't actually try to call the
code that depended on failureaccess. The other dependencies are not actually necessary at runtime.

> Question: Where did those additional dependencies come from?
>
> They are transitive dependencies of Guava. If you look at Guava's
> [POM](https://github.com/google/guava/blob/master/guava/pom.xml), you will see that it has those dependencies.
> Maven will recursively look at the dependencies of your dependencies, and their dependencies, and so on, when
> building the class-path.
 
## Maven Repository

If you inspect the `target/dependencies-classpath.txt` file, you'll see the that the Guava jar (and the rest of the
dependencies) are actually not in your project, but rather in `~/.m2/repository`. This is your local Maven Repository.

Whenever a Maven build tries to resolve dependencies, it first looks in your local repository for the dependency. If it
isn't there, then it will try to download it from the remote repository.

Running `mvn dependency:list-repositories` will show the repositories that are configured for your build. By default,
Maven uses Maven Central, so you get that if you don't configure any custom repositories. Many companies, including IMC,
use private internal repositories for their code.

```shell
$ mvn dependency:list-repositories
[INFO] --- maven-dependency-plugin:2.8:list-repositories (default-cli) @ maven-helloworld ---
[INFO] Project remote repositories used by this build:
 * central (https://repo.maven.apache.org/maven2, default, releases)
```

When we ran `mvn package`, we built the jar, but just left it in the `target/` directory. You can install any project
to your local repository with `mvn install`.

```shell
$ mvn install
[INFO] --- maven-install-plugin:2.4:install (default-install) @ maven-helloworld ---
[INFO] Installing /Users/jsmith/code/maven-exercise/lab2/2b/target/maven-helloworld-0.1.jar to /Users/jsmith/.m2/repository/com/imc/school/maven/maven-helloworld/0.1/maven-helloworld-0.1.jar
[INFO] Installing /Users/jsmith/code/maven-exercise/lab2/2b/pom.xml to /Users/jsmith/.m2/repository/com/imc/school/maven/maven-helloworld/0.1/maven-helloworld-0.1.pom
```

Then, any other Maven project will be able to depend on that artifact.

The `mvn deploy` command is used to upload an artifact to the remote repository. However, you need special configuration
for that - and in general it is best practice to only deploy to remote repositories from a Continuous Deployment server.

## Addendum: Dependency Management

One optional section in the POM that you see in many projects is a `dependencyManagement` section.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <!-- Previous elements like coordinates, properties, etc.  -->

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.google.guava</groupId>
                <artifactId>guava</artifactId>
                <version>31.0.1-jre</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
    <dependencies>
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
        </dependency>
    </dependencies>
</project>
```

For our project this section doesn't really make a difference, but `dependencyManagement` can be convenient for
multi-module projects. Any version or other properties set for a dependency in `dependencyManagement` is inherited
by all modules. This allows you to define all of your dependency versions in one place, but define actual dependencies
in the modules themselves.

## Addendum: Properties

You can refer to properties elsewhere in the pom with `${property.name}`. This is often used for version numbers,
which makes it easy to ensure that a group of dependencies from the same framework all have the same version.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <!-- Previous elements like coordinates, etc.  -->
    <properties>
        <maven.compiler.target>11</maven.compiler.target>
        <maven.compiler.source>11</maven.compiler.source>
        <guava.version>31.0.1-jre</guava.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.google.guava</groupId>
                <artifactId>guava</artifactId>
                <version>${guava.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```
