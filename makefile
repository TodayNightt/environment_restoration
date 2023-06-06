%:
	javac -d ./out -classpath './;./src/main;./libs/*' 'src/main/$@.java'
	java -classpath './out;./libs/*' $@
