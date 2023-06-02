# Enviroment Restoration

Hi There, We are Year 2 undergraduate student from Teikyo University in Japan.
<br>
We are currently building a game called Enviroment Restoration.
<br>
This game was inpired by both Minecraft and Tetris.

## makefileがある場合(おすすめ)
```
cd src/main
make Main
```

## makefileがない場合
```
cd src/main
javac -d ../../out -cp './;../../libs/core.jar;../../libs/gluegen-rt.jar;../../libs/jogl-all.jar;../../libs/joml-1.10.5.jar' Main.java
java -cp '../../out;../../libs/*' Main
```
