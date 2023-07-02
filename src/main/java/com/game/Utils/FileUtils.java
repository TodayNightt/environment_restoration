package com.game.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

import static com.game.Graphics.Chunk.*;

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

    public static void saveFile(int[] data,String file) {
        try {
            FileWriter writer = new FileWriter(file);
            for (int y = 0; y < CHUNK_HEIGHT; y++) {
                for (int z = 0; z <CHUNK_SIZE; z++) {
                    for (int x = 0; x < CHUNK_SIZE; x++) {
                        writer.append(String.valueOf(x)).append(",").append(String.valueOf(y)).append(",").append(String.valueOf(z)).append(",");
                        writer.append(Integer.toBinaryString(data[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))]));
                        writer.append("\n");
                    }
                }
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
