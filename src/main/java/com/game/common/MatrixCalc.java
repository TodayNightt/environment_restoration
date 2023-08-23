package com.game.common;

import com.game.common.Utils.Mat4f;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MatrixCalc {
    public static Mat4f createModelMatrix(Mat4f rotationX, Mat4f rotationY, Mat4f rotationZ,
                                          Mat4f translation, Mat4f scale) {
        return new Mat4f().mul(rotationX).mul(rotationY).mul(rotationZ).mul(translation).mul(scale);
    }

    // https://github.com/OneLoneCoder/Javidx9/blob/master/ConsoleGameEngine/BiggerProjects/Engine3D/OneLoneCoder_olcEngine3D_Part3.cpp
    public static Mat4f rotationMatrix(float angle, byte mode) {
        float angleRad = (float) Math.toRadians(angle);
        return switch (mode) {
            case 1 -> new Mat4f()
                    .setValue(0, 0, 1.0f)
                    .setValue(1, 1, (float) cos(angleRad))
                    .setValue(1, 2, (float) sin(angleRad))
                    .setValue(2, 1, (float) -sin(angleRad))
                    .setValue(2, 2, (float) cos(angleRad))
                    .setValue(3, 3, 1.0f);
            case 2 -> new Mat4f()
                    .setValue(0, 0, (float) cos(angleRad))
                    .setValue(0, 2, (float) sin(angleRad))
                    .setValue(2, 0, (float) -sin(angleRad))
                    .setValue(1, 1, 1.0f)
                    .setValue(2, 2, (float) cos(angleRad))
                    .setValue(3, 3, 1.0f);
            case 3 -> new Mat4f()
                    .setValue(0, 0, (float) cos(angleRad))
                    .setValue(0, 1, (float) sin(angleRad))
                    .setValue(1, 0, (float) -sin(angleRad))
                    .setValue(1, 1, (float) cos(angleRad))
                    .setValue(2, 2, 1.0f)
                    .setValue(3, 3, 1.0f);
            default -> null;
        };
    }
//
//    public static Matrix4f createModelMatrix(Matrix4f rotationX, Matrix4f rotationY, Matrix4f rotationZ,
//                                             Matrix4f translation, Matrix4f scale) {
//        return new Matrix4f().identity().mul(rotationX).mul(rotationY).mul(rotationZ).mul(translation).mul(scale);
//    }
//
//
//    public static Matrix4f rotation(float angle, byte mode) {
//        float angleRad = (float) Math.toRadians(angle);
//        return switch (mode) {
//            case 1 -> new Matrix4f()
//                    .m00(1.0f)
//                    .m11(cos(angleRad))
//                    .m12(-sin(angleRad))
//                    .m21(sin(angleRad))
//                    .m22(cos(angleRad))
//                    .m33(1.0f);
//            case 2 -> new Matrix4f()
//                    .m00(cos(angleRad))
//                    .m02(sin(angleRad))
//                    .m20(-sin(angleRad))
//                    .m11(1.0f)
//                    .m22(cos(angleRad))
//                    .m33(1.0f);
//            case 3 -> new Matrix4f()
//                    .m00(cos(angleRad))
//                    .m01(-sin(angleRad))
//                    .m10(sin(angleRad))
//                    .m11(cos(angleRad))
//                    .m22(1.0f)
//                    .m33(1.0f);
//            default -> null;
//        };
//    }
}
