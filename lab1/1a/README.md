# Lab 1 - Compiling and Running Class Files

You can build and run `java` code with minimal complexity by just using `javac` and `java`.

## Code

This directory contains the following 2 classes:

`HelloWorld.java`
```java
import java.util.StringJoiner;

class HelloWorld {
    public static void main(String[] args) {
        System.out.println(Helper.appendExclamationMark("Hello, " + name(args)));
    }

    private static String name(String[] args) {
        if (args.length > 0) {
            StringJoiner joiner = new StringJoiner(" ");
            for (String arg : args) {
                joiner.add(arg);
            }
            return joiner.toString();
        } else {
            return "Guest";
        }
    }
}
```

`Helper.java`
```java
public class Helper {
    public static String appendExclamationMark(String arg) {
        return arg + "!";
    }
}
```

As well as a simple test class:
`HelperTest.java`
```java
class HelperTest {
    public static void main(String[] args) {
        String string = Helper.appendExclamationMark("Hello");
        if (string.equals("Hello!")) {
            System.out.println("HelperTest passes");
        } else {
            System.err.println("HelperTest failed");
        }
    }
}

```

## Building with Javac

From this directory, you can compile `HelloWorld.java` with:
```
$ javac HelloWorld.java
```

Observe that this creates following class files:

```
$ ls *.class
HelloWorld.class Helper.class
```

Note that it automatically compiled `Helper` because `HelloWorld` relies on `Helper`.

## Running with Java

You can now run this program `HelloWorld` by calling the `java` command:
```
$ java HelloWorld John Smith
Hello, John Smith!
```

Note: You can call `java` on any class file (a `.class` file output by `javac`). It will look for
a `public static void` method named `main` and invoke that.

## Building and running the test

Our test, in this example, is a different `.java` file with some test code in a `public static void main` function of
its own.

We can compile and run it with:

```
$ javac HelperTest.java
$ java HelperTest
HelperTest passes
```

## Cleaning up

Optionally, we can clean up our previously build class files with:

```
$ rm *.class
```

## Summary

You can compile and run Java code by with only the `javac` and `java` programs.

However, this has several downsides:

**No code organization** - All the code has to live in one directory. This works fine for small programs, but for larger
codebases you can imagine how this quickly becomes unwieldy.

**No separation of test and production code** - Test code is all in the same place as production codek, making it
difficult to tell them apart.

**No encapsulation** - All classes are visible to every other class, since all code is in the default package.

**Organization of compilation output** - All class files are dumped into the same directory as the code, while makes it
annoying to delete class files and reset, or to navigate the directory.

**Dependencies** - How can you use code that someone else has written? You would need to copy their source code into
your code directory.

## Optional Extension - Makefiles

We can do simple automation with [Makefiles](https://makefiletutorial.com). Makefiles were one of the earliest
incarnations of build systems. We won't go into any details about how they are done, but each directory in this exercise
includes a `Makefile` that allows you to run the exercise using `make all`.

Note: IMC does not heavily make use of Makefiles, so don't spend too much time learning this.

## Resources

- https://en.wikipedia.org/wiki/Java_class_file
- https://www.baeldung.com/javac
