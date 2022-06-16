all: clean compile package run

clean:
	rm -rf target/

compile:
	javac -sourcepath src/main src/main/com/imc/school/maven/HelloWorld.java -d target/classes/

package:
	jar --create --file=target/helloworld.jar -C target/classes/ .

run:
	java --class-path=target/helloworld.jar com.imc.school.maven.HelloWorld John Smith

package-with-manifest: compile
	jar --create --file=target/helloworld-manifest.jar --manifest=resources/Manifest.txt -C target/classes .

run-with-manifest: package-with-manifest
	java -jar target/helloworld-manifest.jar John Smith