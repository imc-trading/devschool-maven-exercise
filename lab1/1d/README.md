# Lab 1d - Dependencies

Now that we know how JAR files are packaged for release, we can see how we can depend on other JARs in our code.

## Guava

[Guava](https://github.com/google/guava) is a library released by Google, with a lot of helpful utilities. It is widely
used throughout the Java community, and is considered to be an extension of the standard library.

## Downloading Guava

One way that Guava is released is as a JAR, where the release information is available on
[Github](https://github.com/google/guava/releases/tag/v31.1). This includes a link to download the JAR.

We download it into `lib/`.
```
$ curl https://repo1.maven.org/maven2/com/google/guava/guava/31.1-jre/guava-31.1-jre.jar -o lib/guava.jar
```

## Using Guava

Guava has a `Joiner` class that is a bit easier to use than the `StringJoiner` from the standard library.

We replaced the `name` method in `HelloWorld.java` with the more concise:

```java
private static String name(String[] args) {
    return (args.length != 0) ? Joiner.on(" ").join(args) : "Guest";
}
```

## Compile

We build as before, except we need to tell `javac` that we are depending on `guava.jar`, by passing in `-classpath lib/guava.jar`.

```
$ javac -sourcepath src/main/ -classpath lib/guava.jar -d target/classes/ src/main/com/imc/school/maven/HelloWorld.java
```

> ### What happens if you don't pass in `-classpath` into the `javac` call?
> 
> ```shell
> $ javac -sourcepath src/main/ -d target/classes/ src/main/com/imc/school/maven/HelloWorld.java
> src/com/imc/school/maven/HelloWorld.java:3: error: package com.google.common.base does not exist
> import com.google.common.base.Joiner;
>                               ^
> src/com/imc/school/maven/HelloWorld.java:12: error: cannot find symbol
> return Joiner.on(" ").join(args);
>        ^
> ```
>
> You need the `-classpath` argument to tell javac that we expect to be able to call the Guava code.
> 
> However, note that if you inspect the contents of this jar, it still has the same files as before, and doesn't have anything
> about guava.
>
>```shell
>$ jar --list --file=target/helloworld.jar
>com/
>com/imc/
>com/imc/school/
>com/imc/school/util/
>com/imc/school/util/Helper.class
>com/imc/school/maven/
>com/imc/school/maven/HelloWorld.class
>```
>
>Passing in `-classpath lib/guava.jar` just tells `javac` to look at the contents of `guava.jar` to figure out what
>classes/methods it has, so that it can emit the correct errors if you try to call a class or method that doesn't exist.

## Packaging

```shell
$ jar --create --file=target/helloworld.jar -C target/classes/ .
```

## Running with Java

Like with compiling, we can't simply execute the JAR we created by calling:
```
$ java -classpath target/helloworld.jar com.imc.school.maven.HelloWorld John Smith    
Exception in thread "main" java.lang.NoClassDefFoundError: com/google/common/base/Joiner
        at com.imc.school.maven.HelloWorld.name(HelloWorld.java:12)
        at com.imc.school.maven.HelloWorld.main(HelloWorld.java:8)
Caused by: java.lang.ClassNotFoundException: com.google.common.base.Joiner
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:522)
        ... 2 more
```

`java` knows that we need to call the Guava code, but it doesn't know where it is.

```shell
$ java -classpath lib/guava.jar:target/helloworld.jar com.imc.school.maven.HelloWorld John Smith
```

> #### Question: 
> Why doesn't Java simply put the guava.jar inside helloworld.jar, so we don't need to pass in each dependency
> into the classpath when `java` is called?
> 
> Hint: What happens if you have another different dependency that also depends on Guava?

## Manifest

Once again, we can add this information to the Manifest to make it simpler to run with `java`.

If we add this line to the manifest:
```
Class-Path: ../lib/guava.jar
```

and then build the jar the same as before with
```shell
$ jar --create --file=target/helloworld.jar --manifest=resources/Manifest.txt -C target/classes/ .
```

Then we don't need to pass in the main class or the class path when calling `java`.

```shell
$ java -jar target/helloworld.jar John Smith
```

> Note that the Class-Path is relative to the location of the jar.

## Downsides

- If we have more than one dependency, how annoying will it be to find download links for all of them?
  - Guava helpfully includes a link to the JAR on their website. Not everyone else does.
- How do we know what versions are available for dependencies?
  - Every project/website is slightly different about how they show previous versions.
- What if our dependency has dependencies? How do we download those?
  - The Joiner class in Guava doesn't depend on any code outside of Guava, so we didn't need to download any. However,
    if we share our `helloworld.jar`, then whoever calls it will need to make sure Guava is also on the classpath.

If only there was a standard way to index all available dependencies, the versions that are available for them, and the
dependencies of those dependencies!

Lab 2 will discuss how Maven solves these problems. However, exercise 1e will first go over how dependencies can
potentially go wrong.

## Bonus - Test Dependencies

Let's download JUnit and convert our test to use JUnit.

```shell
curl https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/junit-platform-console-standalone-1.9.3.jar -o lib/junit-platform.jar
```

Then, we change `HelperTest` to:

```java
package com.imc.school.util;

import com.imc.school.util.Helper;
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

We can compile all test files with:

```shell
javac -sourcepath src/test -classpath lib/junit-platform.jar:target/classes -d target/test-classes/ $(find src/test -name "*.java")
```

Finally, we can run the tests with:

```shell
java -jar lib/junit-platform.jar --classpath target/classes:target/test-classes --scan-classpath
```