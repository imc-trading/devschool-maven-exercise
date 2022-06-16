# Maven Training

This repository contains a series of exercises that should build a basic understanding of how Java libraries are
distributed, how archives such as JARs were distributed prior to Maven, how Maven improves on that process, and
how to use Maven.

## Getting started

Download the repository with exercises:
```shell
git clone https://github.com/imc-trading/devschool-maven-exercise.git
```

### Dependencies

You'll need to have Maven 3.5+ and Java 11+ higher to run the exercises.

#### Mac

On a Mac, if you have [Homebrew](https://brew.sh/) installed, then you can install Maven with

```shell
brew install maven
```

This will install Java (`openjdk`) as a dependency.

#### Linux (Ubuntu)

```shell
sudo apt-get install maven
```

This will install Java (`openjdk`) as a dependency.

#### Docker

If you have Docker, you can run the exercises within a Docker image for a consistent environment:
```shell
docker run -it \
  --mount type=bind,source=$(pwd),target=/usr/src/maven-training \
  -w /usr/src/maven-training \
  maven:latest \
  /bin/bash -l
```

#### Other

In other environments, it is up to you to install these
dependencies. [Here](https://www.baeldung.com/install-maven-on-windows-linux-mac) is one tutorial that provides some
instructions.

## Exercise

This exercise is broken up into two labs, with multiple exercises in each section.

For each exercise, you don't need to write any code, or modify any `pom.xml` files. Just run the commands and observe
what they do.

### Lab 1

How Java libraries are compiled and packaged, without Maven.

Start [here](lab1/README.md).

### Lab 2

Introduction to using Maven to make everything easier.

Start [here](lab2/README.md).
