run: package
	java -classpath lib/guava.jar:target/helloworld.jar com.imc.school.maven.HelloWorld John Smith

run-with-manifest: package-with-manifest
	java -jar target/helloworld.jar John Smith

test: compile-tests
	java -jar lib/junit-platform.jar --classpath target/classes:target/test-classes --scan-classpath

resolve-dependencies:
	mkdir -p lib/
	[ -f lib/guava.jar ] || curl https://repo1.maven.org/maven2/com/google/guava/guava/31.1-jre/guava-31.1-jre.jar -o lib/guava.jar
	[ -f lib/junit-platform.jar ] || curl https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/junit-platform-console-standalone-1.9.3.jar -o lib/junit-platform.jar

compile: resolve-dependencies
	javac -sourcepath src/main/ -classpath lib/guava.jar -d target/classes/ src/main/com/imc/school/maven/HelloWorld.java

compile-tests: resolve-dependencies
	javac -sourcepath src/test -classpath lib/junit-platform.jar:target/classes -d target/test-classes/ src/test/com/imc/school/util/HelperTest.java

package: compile test
	jar --create --file=target/helloworld.jar -C target/classes .

package-with-manifest: compile
	jar --create --file=target/helloworld.jar --manifest=resources/Manifest.txt -C target/classes .

clean:
	rm -rf target/

