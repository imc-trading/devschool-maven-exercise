run: package
	java --class-path=target/greeting1.jar:target/helloworld.jar HelloWorld

run-with-greeting2: package
	java --class-path=target/greeting2.jar:target/helloworld.jar HelloWorld

run-with-greeting3: package
	java --class-path=target/greeting3.jar:target/helloworld.jar HelloWorld

run-with-greeting1-then-2: package
	java --class-path=target/greeting1.jar:target/greeting2.jar:target/helloworld.jar HelloWorld

run-with-greeting2-then-1: package
	java --class-path=target/greeting2.jar:target/greeting1.jar:target/helloworld.jar HelloWorld

run-with-greeting3-then-1: package
	java --class-path=target/greeting3.jar:target/greeting1.jar:target/helloworld.jar HelloWorld

compile-dependencies: greeting1 greeting2 greeting3
	javac greeting1/Greeting.java -d target/greeting1/
	javac greeting2/Greeting.java -d target/greeting2/
	javac greeting3/Greeting.java -d target/greeting3/

target/greeting1.jar:
	jar --create --file=target/greeting1.jar -C target/greeting1/ .
target/greeting2.jar:
	jar --create --file=target/greeting2.jar -C target/greeting2/ .
target/greeting3.jar:
	jar --create --file=target/greeting3.jar -C target/greeting3/ .

package-dependencies: target/greeting1.jar target/greeting2.jar target/greeting3.jar

target/classes: package-dependencies
	javac src/HelloWorld.java --class-path=target/greeting1.jar -d target/classes

package: target/helloworld.jar

target/helloworld.jar: target/classes
	jar --create --file=target/helloworld.jar -C target/classes .

clean:
	rm -rf target/

