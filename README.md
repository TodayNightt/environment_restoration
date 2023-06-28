# Environment Restoration

Hi There, We are Year 2 undergraduate student from Teikyo University in Japan.
<br>
We are currently building a game called Environment Restoration.
<br>
This game was inspired by both Minecraft and Tetris.

## makefileがある場合(おすすめ)

```
make
```

## makefileがない場合

```make
//Mac
chmod +x ./gradlew
./gradlew shadowJar
java -XstartOnFirstThread -jar ./build/libs/game.jar

javac -d ./out -cp './libs/*' -sourcepath ./src/main/ src/main/Main.java
java -cp './out;./libs/*;./src/main/resources' Main
```
