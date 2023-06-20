default:
	javac -d ./out -cp './libs/*' -sourcepath ./src/main/ src/main/Main.java
	java -cp './out;./libs/*;./src/main/resources' Main
