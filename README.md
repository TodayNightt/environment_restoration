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
javac -d ./out -classpath ./out;./src/main;./libs/* src/main/Main.java
java -cp ./out;./libs/* Main
```
