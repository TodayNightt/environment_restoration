# Environment Restoration

Hi There, We are Year 2 undergraduate student from Teikyo University in Japan.
<br>
We are currently building a game called Environment Restoration.
<br>
This game was inspired by both Minecraft and Tetris.

## makefileがある場合(おすすめ)
```
make Main
```

## makefileがない場合
```make
set classpath='./out;./src/main;./libs/core.jar;./libs/joml-1.10.5.jar;./libs/jogl-all.jar;./libs/gluegen-rt.jar;./libs/core'
javac -d ./out -sourcepath ./src/main src/main/Main.java
java -cp ./out;./libs/* Main
```
