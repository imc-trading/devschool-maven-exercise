# Lab 1e - Conflicts

Now that we know how Java depends on the Classpath to determine what classes are available at runtime, let's explore
how that can go wrong.

## What happens if a dependency jar passed into `--class-path` at run time is different than the one that was used at compile time?

Let's build two versions of the same `Greeting` class:

We'll put one of them in `greeting1/`
```java
public class Greeting {
    public static String message() {
        return "Hello";
    }
}
```

And the other in `greeting2/`
```java
public class Greeting {
    public static String message() {
        return "Bonjour";
    }
}
```

Then, we can build them both into `jars`.

```shell
$ javac greeting1/Greeting.java -d target/greeting1/
$ jar --create --file=target/greeting1.jar -C target/greeting1/ .
$ javac greeting2/Greeting.java -d target/greeting2/
$ jar --create --file=target/greeting2.jar -C target/greeting2/ .
```

Now, we create a `HelloWorld` class that depends on `Greeting`, in `src/`.
```java
public class HelloWorld {
    public static void main(String args[]) {
        System.out.println(Greeting.message() + " World!");
    }
}
```

We can compile, jar, and run `HelloWorld`, with it depending on `greeting1.jar`.

```
$ javac src/HelloWorld.java --class-path=target/greeting1.jar -d target/classes/
$ jar --create --file=target/helloworld.jar -C target/classes .
$ java --class-path=target/greeting1.jar:target/helloworld.jar HelloWorld
Hello World!
```

Now, if we change it to have `greeting2.jar` on the class-path at run time...
```shell
$ java --class-path=target/greeting2.jar:target/helloworld.jar HelloWorld 
Bonjour World!
```

It happily runs, but has different behavior due to a different `Greeting` class being called with a different
implementation of `message()`.

## What happens if multiple `jars` on the classpath declare the same class?

```shell
$ java --class-path=target/greeting1.jar:target/greeting2.jar:target/helloworld.jar HelloWorld 
Hello World!
$ java --class-path=target/greeting2.jar:target/greeting1.jar:target/helloworld.jar HelloWorld 
Bonjour World!
```

If the same class exists in multiple jars, then the version that is first in the classpath is the one that gets loaded.

## What happens if you load another version of the class that doesn't have the same method?

Let's make a third version of the `Greeting` class and put it in `greeting3`, but change it so that `message` takes an
argument.
```
public class Greeting {
    public static String message(String suffix) {
        return "Hello" + suffix;
    }
}
```

```shell
$ javac greeting3/Greeting.java -d target/greeting3/
$ jar --create --file=target/greeting3.jar -C target/greeting3/ .
```

Now, when if we run `HelloWorld` with this version on the classpath...

```shell
$ java --class-path=target/greeting3.jar:target/helloworld.jar HelloWorld 
Exception in thread "main" java.lang.NoSuchMethodError: 'java.lang.String Greeting.message()'
        at HelloWorld.main(HelloWorld.java:3)
```

We get a `NoSuchMethodError`, because there is no version of `Greeting` with a `message()` method that takes no
arguments.

> Question: What happens if you include `greeting1.jar` on the classpath after `greeting3.jar`?
>  
> ```
> $ java --class-path=target/greeting3.jar:target/greeting1.jar:target/helloworld.jar HelloWorld
> Exception in thread "main" java.lang.NoSuchMethodError: 'java.lang.String Greeting.message()'
> at HelloWorld.main(HelloWorld.java:3)
> ```
