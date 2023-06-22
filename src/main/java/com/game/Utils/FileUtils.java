package com.game.Utils;

import java.io.InputStream;
import java.util.Scanner;

public class FileUtils {

    //https://stackoverflow.com/questions/39685671/filesystem-not-found-exception-when-using-getresources
    public static CharSequence loadShaderFromResources(String fileName) {
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
        CharSequence text = "";
        assert is != null;
        try (Scanner scanner = new Scanner(is)) {
            while(scanner.hasNextLine()){
                text+= scanner.nextLine() + "\n";
            }
        }
       return text;
    }

    //https://stackoverflow.com/questions/45171816/lwjgl-3-stbi-load-from-memory-not-working-when-in-jar
//    public static ByteBuffer loadFromResources(String fileName) throws IOException {
//        byte[] imageByte= Objects.requireNonNull(FileUtils.class.getClassLoader().getResourceAsStream(fileName)).readAllBytes();
//        ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageByte.length);
//        imageBuffer.put(imageByte);
//        imageBuffer.flip();
//        return imageBuffer;
//    }
    //https://github.com/microsoft/wslg/wiki/GPU-selection-in-WSLg
}
