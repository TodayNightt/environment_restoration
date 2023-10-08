package com.game.Utils;

import org.joml.Matrix4f;

import static org.joml.Math.cos;
import static org.joml.Math.sin;

public final class MatrixCalc {
    public static Matrix4f createModelMatrix(Matrix4f rotationX, Matrix4f rotationY, Matrix4f rotationZ,
                                             Matrix4f translation, Matrix4f scale) {
        return new Matrix4f().mul(rotationX).mul(rotationY).mul(rotationZ).mul(translation).mul(scale);
    }

    // https://github.com/OneLoneCoder/Javidx9/blob/master/ConsoleGameEngine/BiggerProjects/Engine3D/OneLoneCoder_olcEngine3D_Part3.cpp
    public static Matrix4f rotationMatrix(float angle, byte mode) {
        float angleRad = (float) Math.toRadians(angle);
        return switch (mode) {
            case 1 -> new Matrix4f()
                    .m00(1.0f)
                    .m11(cos(angleRad))
                    .m12(-sin(angleRad))
                    .m21(sin(angleRad))
                    .m22(cos(angleRad))
                    .m33(1.0f);
            case 2 -> new Matrix4f()
                    .m00(cos(angleRad))
                    .m02(sin(angleRad))
                    .m20(-sin(angleRad))
                    .m11(1.0f)
                    .m22(cos(angleRad))
                    .m33(1.0f);
            case 3 -> new Matrix4f()
                    .m00(cos(angleRad))
                    .m01(-sin(angleRad))
                    .m10(sin(angleRad))
                    .m11(cos(angleRad))
                    .m22(1.0f)
                    .m33(1.0f);
            default -> null;
        };
    }
}
