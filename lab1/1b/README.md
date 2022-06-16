# Lab 1b - Organizing Code into Packages

In this exercise, we'll still build and run Java code with `javac` and `java`, but set up our source code with slightly
better organization.

Then, we can run `javac` to put the class files in the `target/` directory, so they aren't cluttering up the source
directory. This significantly simplifies the cleaning up process.

## Adding Packages

We can add packages to our source files, and move them into directories that match the package structure. This
`HelloWorld` example doesn't take advantage of it, but we can now have better encapsulation by allowing
`package-private` classes/methods.

We declare packages for `HelloWorld` and `Helper` by adding `package` statements at the top of the file:

`HelloWorld.java`
```
package com.imc.school.maven;
```

`Helper.java`
```
package com.imc.school.util;
```

Additionally, we'll move them into `com/imc/school/maven/` and `com/imc/school/util/` directories, respectively.

## `src/` directory

We'll put our code in the `src/` directory to separate our source code from the compiled class files.

## `main/` and `test/` directories

Additionally, we can separate our production code from our test code. We'll put the production code in `src/main/`, and
the test code in `src/test/`.

We'll put HelperTest in the same package as Helper:

`HelperTest.java`
```
package com.imc.school.util;
```

but put it in the `src/test/` directory instead, under `com/imc/school/util/`.

**Look in the `src/` directory to confirm that everything is as you expect.** 

## Building with Javac

To build the code with this new package structure, we can use `javac` like before, but tell `javac` to put the class
files into `target/classes/`, to keep them separate.

This time, since `Helper` and `HelloWorld` are in different packages, we need to run `javac` with `-sourcepath` to tell
it where to search for code.

Thus, we use the following arguments:

- `-sourcepath src/main` to tell it that the production sources are in `src/main`
- `-d target/classes/` to tell it to put the class files in `target/classes`
- The source file(s) to start compilation at.

```
$ javac -sourcepath src/main -d target/classes/ src/main/com/imc/school/maven/HelloWorld.java
```

We can observe the built class files, which are in the same directory structure based on their packages.

(You may need to install `tree` if you want to use this exact command, but you can also just use the file explorer)

```shell
$ tree target/classes
target/classes
└── com
    └── imc
        └── school
            ├── maven
            │   └── HelloWorld.class
            └── util
                └── Helper.class
```

## Running with Java

To invoke `java`, we need to tell it:
* The `classpath` - the directory(s) where all the class files are. If we don't set this, `javac` assumes they are
all in the current directory (which is what we did in the previous example.)
* The class that has `public static void main(String[] args)` method, which is the entrypoint.
* Any arguments to the program.

```
$ java -classpath target/classes/ com.imc.school.maven.HelloWorld John Smith
Hello, John Smith!
```

## Building Tests

To build the tests, we give it the following arguments:

- `-sourcepath src/test` to tell it that the test sources are in `src/test`. (This isn't strictly necessary in this
  example, because we only have one test file, but normally it would be.)
- `-classpath target/classes` to tell it where the previously compiled production class files are
- `-d target/test-classes` to tell it to put the test class files in `target/test-classes`
- The source files to start compilation at.

```shell
$ javac -sourcepath src/test -classpath target/classes -d target/test-classes src/test/com/imc/school/util/HelperTest.java 
```

```shell
$ tree target/test-classes
target/test-classes
└── com
    └── imc
        └── school
            └── util
                └── HelperTest.class
```

Note: Javac still works fine without the `-classpath` argument if you do `-sourcepath src/test:src/main`. Make sure you
understand what is different between these two alternatives!

## Running Tests

To run the test, we need to put both `target/classes` and `target/test-classes` on the classpath. (The **classpath** is
the list of all files/directories in which to look for class files.) Java and Javac use a colon `:` to delimit multiple
paths.

```shell
$ java -classpath target/classes/:target/test-classes/ com.imc.school.util.HelperTest
HelperTest passes
```

## Cleaning up

Putting everything in the `target/` directory makes cleanup a lot simpler.

```
$ rm -rf target/
```

## Summary

By using packages, and specifying the sourcepaths and classpaths, we can improve code organization, separation of
production and test code, encapsulation, and the organization of the compiled class files.

However, you need the entire `target/` directory to run the code. This makes it very difficult to share your code with
someone else, or to use code that someone else wrote.