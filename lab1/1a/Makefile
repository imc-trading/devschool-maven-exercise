all: clean compile run

clean:
	rm *.class

compile:
	javac HelloWorld.java

test-compile:
	javac HelperTest.java

test: test-compile
	java HelperTest

run: compile
	java HelloWorld John Smith

