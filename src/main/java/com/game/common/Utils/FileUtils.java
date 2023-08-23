package com.game.common.Utils;

import java.io.InputStream;
import java.util.Scanner;

public class FileUtils {

    //https://stackoverflow.com/questions/39685671/filesystem-not-found-exception-when-using-getresources
    public static String loadShaderFromResources(String fileName) {
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
        StringBuilder text = new StringBuilder();
        assert is != null;
        try (Scanner scanner = new Scanner(is)) {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine()).append("\n");
            }
        }
        return text.toString();
    }

//    public static void saveFile(int[] data,String file) {
//        try {
//            FileWriter writer = new FileWriter(file);
//            for (int y = 0; y < CHUNK_HEIGHT; y++) {
//                for (int z = 0; z <CHUNK_SIZE; z++) {
//                    for (int x = 0; x < CHUNK_SIZE; x++) {
//                        writer.append(String.valueOf(x)).append(",").append(String.valueOf(y)).append(",").append(String.valueOf(z)).append(",");
//                        writer.append(Integer.toBinaryString(data[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))]));
//                        writer.append("\n");
//                    }
//                }
//            }
//
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

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
