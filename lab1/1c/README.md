# Lab 1c - JARs

For releasing and sharing code, we don't want to have to share a bunch of class files. It would be easier if we could
just zip them up and share a single file.

Java supports JAR files - Java ARchives, which is basically just that. It is a ZIP archive of a bunch of class files,
along any other resources that you might need at runtime.

In this lab, we haven't changed the code or directory structure at all from the previous one.

## Compiling with Javac

We compile the same way as before:
```
$ javac -sourcepath src/main -d target/classes/ src/main/com/imc/school/maven/HelloWorld.java 
```

## Packaging the JAR

We then package the class files into `helloworld.jar` using the `jar` command.

```
$ jar --create --file=target/helloworld.jar -C target/classes/ .
```

## Inspecting the JAR files

We can inspect the contents of the JAR with the `jar --list` command. 
```
$ jar --list --file=target/helloworld.jar 
com/
com/imc/
com/imc/school/
com/imc/school/util/
com/imc/school/util/Helper.class
com/imc/school/maven/
com/imc/school/maven/HelloWorld.class
```

## Running with Java

Now, we can call `java`, giving it the `jar` we created, instead of a target directory full of class files:

```shell
$ java --class-path=target/helloworld.jar com.imc.school.maven.HelloWorld John Smith
```

## Manifest

Additionally, the JAR format supports a manifest file that describes how it should be called, which allows you to pass
in fewer command-line arguments:

If we add a `resources/Manifest.txt` with the following contents:
```
Main-Class: com.imc.school.maven.HelloWorld
```

and then build the jar with
```shell
$ jar --create --file=target/helloworld.jar --manifest=resources/Manifest.txt -C target/classes .
```

Then we don't need to pass in the Main class when calling `java`.

```shell
$ java -jar target/helloworld.jar John Smith
```

## Summary

With this, we've created a single file, `helloword.jar`, which we can easily share with anyone who wants to use it.

In the next lab, we'll see what that looks like.

## Resources
 
- https://en.wikipedia.org/wiki/JAR_(file_format)
- https://docs.oracle.com/javase/tutorial/deployment/jar/manifestindex.html