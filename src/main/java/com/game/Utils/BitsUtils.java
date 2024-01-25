package com.game.Utils;

import org.joml.Vector3f;

import java.nio.IntBuffer;
import java.util.Arrays;

import static java.lang.Integer.parseInt;
import static java.lang.Math.*;

public class BitsUtils {

    public static int[] floatsToBits(float[] array) {
        int[] arr = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            float value = array[i];
            String[] valueComponent = Float.toString(value).split("\\.");
            arr[i] = (isNegative(value) ? 0x80 : 0x00) | abs(parseInt(valueComponent[0])) << 4 | parseInt(valueComponent[1]);
        }
        return arr;
    }

    public static int floatToBits(float value) {
        String[] valueComponent = Float.toString(value).split("\\.");
        return (isNegative(value) ? 0x80 : 0x00) | abs(parseInt(valueComponent[0])) << 4 | parseInt(valueComponent[1]);
    }

    private static boolean isNegative(float value) {
        return value < 0.f;
    }

    private static String[] intToBinary(int[] array) {
        String[] arr = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = Integer.toBinaryString(array[i]);
        }
        return arr;
    }

    public static IntBuffer threeIntsToOne(int[] array) {
        IntBuffer buffer = IntBuffer.allocate(array.length / 3);
        for (int i = 0; i < array.length; i += 3) {
            buffer.put(array[i] << 16 | array[i + 1] << 8 | array[i + 2]);
        }
        return buffer;
    }

    public static IntBuffer fiveFloatsToOneInt(float[] array) {
        IntBuffer buffer = IntBuffer.allocate(array.length / 5);
        for (int i = 0; i < array.length; i += 5) {
            buffer.put(floatToBits(array[i]) << 18 | floatToBits(array[i + 1]) << 10 | floatToBits(array[i + 2]) << 2 | (int) array[i + 3] << 1 | (int) array[i + 4]);
        }
        return buffer;
    }

    public static void main(String[] args) {
        float[] values = {
                -1.0f, 1.0f, 0.5f, 1f, 0f, 1.0f, 1.0f, 0.5f, 1.0f, 1.0f, -1.0f, -1.0f, 0.5f, 0f, 0f,
                1.0f, -1.0f, 0.5f, 0f, 1f, -1.0f, 1.0f, -0.5f, 1f, 0f, 1.0f, 1.0f, -0.5f, 1f, 1f, -1.0f,
                -1.0f, -0.5f, 0f, 0f, 1.0f, -1.0f, -0.5f, 0f, 1f
        };

        float[] value = {0.5f, 0.7f, 0f, 1f, 1f};


//
//        System.out.println(Arrays.toString(values));
//        int[] valueInt = floatsToBits(values);
//        System.out.println(Arrays.toString(valueInt));
//        String [] valueStr = intToBinary(valueInt);
//        System.out.println(Arrays.toString(valueStr));
//        int[] valueBuffer = fiveFloatsToOneInt(values).array();
//        System.out.println(Arrays.toString(valueBuffer));

//        double[] values = {4, 77, 234, 4563, 13467, 635789};
//        for(int i = 0; i < values.length; i++)
//        {
//            double tenthPower = floor(log10(values[i]));
//            double place = Math.pow(10, tenthPower);
//            System.out.println(place);
//        }
//
//        float[] values = {
//                -1.15f,1.5f,0.5f,1f,0f
//        };

        System.out.println(Arrays.toString(values));

        int[] valueBuffer = fiveFloatsToOneInt(value).array();

        System.out.println(Arrays.toString(valueBuffer));
        long first = System.nanoTime();
        VertexData[] data = create(valueBuffer);
        System.out.println((double) (System.nanoTime() - first) / 1_000_000_000);

        System.out.println(Arrays.toString(data));
    }

    public static VertexData[] create(int[] values) {
        VertexData[] datas = new VertexData[values.length];
        for (int i = 0; i < values.length; i++) {
            datas[i] = convert(values[i]);
        }
        return datas;
    }

    public static VertexData convert(int data) {
        Vector3f position = new Vector3f();
        int byteSize = 0xFF;
        position.x = num_to_decimal((data >> 18) & byteSize);
        position.y = num_to_decimal((data >> 10) & byteSize);
        position.z = num_to_decimal((data >> 2) & byteSize);
        int uv = data & 0x3;
        return new VertexData(position, uv);
    }

    public static float num_to_decimal(int data) {
        boolean negativeBit = ((data >> 7) & 0x1) == 0x1;
        float whole_number = (data >> 4) & 0x7;
        float decimal_place = data & 0xF;
        float decimal_place_float = 0.f;
        if (decimal_place > 0f) {
            float tenthPower = (float) floor(log_ten(decimal_place));
            float denominator = (float) (10f * pow(10.0f, tenthPower));
            decimal_place_float = (1.0f / denominator) * decimal_place;
        }

        return (negativeBit ? -1.0f : 1.0f) * (whole_number + decimal_place_float);
    }

    public static float log_ten(float x) {
        return (float) ((1.0f / Math.log(10.0)) * log(x));
    }

    public record VertexData(Vector3f position, int uv) {
    }
}
