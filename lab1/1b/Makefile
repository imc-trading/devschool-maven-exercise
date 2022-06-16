all: clean compile run

clean:
	rm -rf target/

compile:
	javac -sourcepath src/main -d target/classes/ src/main/com/imc/school/maven/HelloWorld.java

run: compile
	java -classpath target/classes/ com.imc.school.maven.HelloWorld John Smith

test-compile: compile
	javac -sourcepath src/test -classpath target/classes -d target/test-classes/ src/test/com/imc/school/util/HelperTest.java

test: test-compile
	java -classpath target/classes/:target/test-classes/ com.imc.school.util.HelperTest

