package com.game.Utils;

import java.util.Arrays;

import static java.lang.Math.pow;

/**
 * In this implementation the matrix in shown below<br/>
 * m00 m01 m02 m03<br/>
 * m10 m11 m12 m13<br/>
 * m20 m21 m22 m23<br/>
 * m30 m31 m32 m33<br/>
 * However, in OpenGL's implementation it is needed to be as shown below <br/>
 * m00 m10 m20 m30<br/>
 * m01 m11 m21 m31<br/>
 * m02 m12 m22 m32<br/>
 * m03 m13 m23 m33<br/>
 */
public class Mat4f {


    public Mat4f setValue(int row, int column, float value) {
        matrix[column + row * 4] = value;
        return this;
    }

    public float getValue(int row, int column) {
        return matrix[column + row * 4];
    }

    public float m00() {
        return matrix[0];
    }

    public float m01() {
        return matrix[1];
    }

    public float m02() {
        return matrix[2];
    }

    public float m03() {
        return matrix[3];
    }

    public float m10() {
        return matrix[4];
    }

    public float m11() {
        return matrix[5];
    }

    public float m12() {
        return matrix[6];
    }

    public float m13() {
        return matrix[7];
    }

    public float m20() {
        return matrix[8];
    }

    public float m21() {
        return matrix[9];
    }

    public float m22() {
        return matrix[10];
    }

    public float m23() {
        return matrix[11];
    }

    public float m30() {
        return matrix[12];
    }

    public float m31() {
        return matrix[13];
    }

    public float m32() {
        return matrix[14];
    }

    public float m33() {
        return matrix[15];
    }


    private final float[] matrix;


    /**
     * Create a new Mat4 and set it to identity
     */
    public Mat4f() {
        this.matrix = new float[16];
        identity();
    }

    public Mat4f(float[] src) {
        this.matrix = src;
    }


    public Mat4f identity() {
        Arrays.fill(matrix, 0f);
        return setValue(0, 0, 1f).setValue(1, 1, 1f).setValue(2, 2, 1f).setValue(3, 3, 1f);
    }

    public Mat4f mul(Mat4f other) {
        return setValue(0, 0, fma(m00(), other.m00(), fma(m10(), other.m01(), fma(m20(), other.m02(), m30() * other.m03()))))
                .setValue(0, 1, fma(m01(), other.m00(), fma(m11(), other.m01(), fma(m21(), other.m02(), m31() * other.m03()))))
                .setValue(0, 2, fma(m02(), other.m00(), fma(m12(), other.m01(), fma(m22(), other.m02(), m32() * other.m03()))))
                .setValue(0, 3, fma(m03(), other.m00(), fma(m13(), other.m01(), fma(m23(), other.m02(), m33() * other.m03()))))
                .setValue(1, 0, fma(m00(), other.m10(), fma(m10(), other.m11(), fma(m20(), other.m12(), m30() * other.m13()))))
                .setValue(1, 1, fma(m01(), other.m10(), fma(m11(), other.m11(), fma(m21(), other.m12(), m31() * other.m13()))))
                .setValue(1, 2, fma(m02(), other.m10(), fma(m12(), other.m11(), fma(m22(), other.m12(), m32() * other.m13()))))
                .setValue(1, 3, fma(m03(), other.m10(), fma(m13(), other.m11(), fma(m23(), other.m12(), m33() * other.m13()))))
                .setValue(2, 0, fma(m00(), other.m20(), fma(m10(), other.m21(), fma(m20(), other.m22(), m30() * other.m23()))))
                .setValue(2, 1, fma(m01(), other.m20(), fma(m11(), other.m21(), fma(m21(), other.m22(), m31() * other.m23()))))
                .setValue(2, 2, fma(m02(), other.m20(), fma(m12(), other.m21(), fma(m22(), other.m22(), m32() * other.m23()))))
                .setValue(2, 3, fma(m03(), other.m20(), fma(m13(), other.m21(), fma(m23(), other.m22(), m33() * other.m23()))))
                .setValue(3, 0, fma(m00(), other.m30(), fma(m10(), other.m31(), fma(m20(), other.m32(), m30() * other.m33()))))
                .setValue(3, 1, fma(m01(), other.m30(), fma(m11(), other.m31(), fma(m21(), other.m32(), m31() * other.m33()))))
                .setValue(3, 2, fma(m02(), other.m30(), fma(m12(), other.m31(), fma(m22(), other.m32(), m32() * other.m33()))))
                .setValue(3, 3, fma(m03(), other.m30(), fma(m13(), other.m31(), fma(m23(), other.m32(), m33() * other.m33()))));
    }

    public Mat4f mul(float factor) {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] *= factor;
        }
        return this;
    }


    public Mat4f scale(float factor) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                matrix[col + row * 4] *= factor;
            }
        }
        return this;
    }

    /**
     * @param FOV         in radians
     * @param aspectRatio the screen aspect ratio
     * @param Z_NEAR      the near plane
     * @param Z_FAR       the far plane
     */

    public Mat4f setPerspective(float FOV, float aspectRatio, float Z_NEAR, float Z_FAR) {
        //https://www.youtube.com/watch?v=ih20l3pJoeU
        //https://www.songho.ca/opengl/gl_projectionmatrix.html#ortho
        float fFov = (float) Math.tan(FOV * 0.5); // f = tan(angle/2)
        return setValue(0, 0, 1f / (fFov * aspectRatio))
                .setValue(1, 1, 1f / fFov)
                .setValue(2, 2, -(Z_FAR + Z_NEAR) / (Z_FAR - Z_NEAR))
                .setValue(3, 2, (-2 * (Z_FAR * Z_NEAR)) / (Z_FAR - Z_NEAR))
                .setValue(2, 3, -1f)
                .setValue(3, 3, 0f);
    }

    public Mat4f ortho(float left, float right, float bottom, float top, float near, float far) {
        //https://www.songho.ca/opengl/gl_matrix.html#example2
        return setValue(0, 0, 2 / (right - left))
                .setValue(1, 1, 2 / (top - bottom))
                .setValue(2, 2, -2 / (far - near))
                .setValue(3, 0, -(right + left) / (right - left))
                .setValue(3, 1, -(top + bottom) / (top - bottom))
                .setValue(3, 2, -(far + near) / (far - near))
                .setValue(3, 3, 1);
    }

    // https://github.com/OneLoneCoder/Javidx9/blob/master/ConsoleGameEngine/BiggerProjects/Engine3D/OneLoneCoder_olcEngine3D_Part3.cpp
//    public Mat4f pointAt(Vec3f pos, Vec3f target, Vec3f up) {
//        Vec3f forward = new Vec3f(target).sub(pos).normalize();
////        Vec3f a = new Vec3f(newForward).mul(up.dot(newForward));
//        Vec3f s = new Vec3f(up).cross(forward).normalize();
//        Vec3f right = new Vec3f(s).cross(forward);
//
//        return setValue(0, 0, s.x())
//                .setValue(1, 0, s.y())
//                .setValue(2, 0, s.z())
//                .setValue(3, 0, 0.0f)
//                .setValue(0, 1, right.x())
//                .setValue(1, 1, right.y())
//                .setValue(2, 1, right.z())
//                .setValue(3, 1, 0.0f)
//                .setValue(0, 2, -forward.x())
//                .setValue(1, 2, -forward.y())
//                .setValue(2, 2, -forward.z())
//                .setValue(3, 2, 0.0f);
//    }

    public Mat4f lookAlong(Vec3f dir, Vec3f up) {
        //https://github.com/JOML-CI/JOML/blob/main/src/main/java/org/joml/Matrix4f.java
        Vec3f forward = dir.normalize().mul(-1f);

        Vec3f left = up.cross(forward).normalize();


        return setValue(0, 0, left.x())
                .setValue(0, 1, forward.y() * left.z() - forward.z() * left.y())
                .setValue(0, 2, forward.x())
                .setValue(0, 3, 0f)
                .setValue(1, 0, left.y())
                .setValue(1, 1, forward.z() * left.x() - forward.x() * left.z())
                .setValue(1, 2, forward.y())
                .setValue(1, 3, 0f)
                .setValue(2, 0, left.z())
                .setValue(2, 1, forward.x() * left.y() - forward.y() * left.x())
                .setValue(2, 2, forward.z())
                .setValue(2, 3, 0f)
                .setValue(3, 0, 0f)
                .setValue(3, 1, 0f)
                .setValue(3, 2, 0f)
                .setValue(3, 3, 1f);
    }

    public Mat4f translation(Vec3f pos) {
        return setValue(3, 0, pos.x())
                .setValue(3, 1, pos.y())
                .setValue(3, 2, pos.z());
    }


    public Mat4f translation(float x, float y, float z) {
        return setValue(3, 0, x)
                .setValue(3, 1, y)
                .setValue(3, 2, z);
    }

    public Mat4f translate(Vec3f pos) {
        //https://github.com/JOML-CI/JOML/blob/main/src/main/java/org/joml/Matrix4f.java
        return setValue(3, 0, fma(m00(), pos.x(), fma(m10(), pos.y(), fma(m20(), pos.z(), m30()))))
                .setValue(3, 1, fma(m01(), pos.x(), fma(m11(), pos.y(), fma(m21(), pos.z(), m31()))))
                .setValue(3, 2, fma(m02(), pos.x(), fma(m12(), pos.y(), fma(m22(), pos.z(), m32()))))
                .setValue(3, 3, fma(m03(), pos.x(), fma(m13(), pos.y(), fma(m23(), pos.z(), m33()))));
    }


    /**
     * * m00 m01 m02 m03<br/>
     * * m10 m11 m12 m13<br/>
     * * m20 m21 m22 m23<br/>
     * * m30 m31 m32 m33<br/>
     */
    public Mat4f invert() {
        //https://semath.info/src/inverse-cofactor-ex4.html
        float determinant = (m00() * calculateDet3(m11(), m12(), m13(), m21(), m22(), m23(), m31(), m32(), m33()))
                - (m10() * calculateDet3(m01(), m02(), m03(), m21(), m22(), m23(), m31(), m32(), m33()))
                + (m20() * calculateDet3(m01(), m02(), m03(), m11(), m12(), m13(), m31(), m32(), m33()))
                - (m30() * calculateDet3(m01(), m02(), m03(), m11(), m12(), m13(), m21(), m22(), m23()));
        return getAdjugate().mul(1f / determinant);
    }

//    public void invert(Mat4f destination) {
//        float determinant = (m00() * calculateDet3(m11(), m12(), m13(), m21(), m22(), m23(), m31(), m32(), m33()))
//                - (m10() * calculateDet3(m01(), m02(), m03(), m21(), m22(), m23(), m31(), m32(), m33()))
//                + (m20() * calculateDet3(m01(), m02(), m03(), m11(), m12(), m13(), m31(), m32(), m33()))
//                - (m30() * calculateDet3(m01(), m02(), m03(), m11(), m12(), m13(), m21(), m22(), m23()));
//
//        destination = new Mat4f(getAdjugate().mul(1f / determinant).get());
//    }

    private Mat4f getAdjugate() {
        float[] matrix = new float[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int i = row + 1;
                int j = col + 1;
                matrix[col + row * 4] = (float) pow(-1, i + j) * calculateDet3(getSubMatrix3(row, col));
            }
        }
        return new Mat4f(matrix);
    }

    public float[] get() {
        return new float[]{
                m00(), m01(), m02(), m03(),
                m10(), m11(), m12(), m13(),
                m20(), m21(), m22(), m23(),
                m30(), m31(), m32(), m33()
        };
    }

    private float[] getSubMatrix3(int excludeRow, int excludeCol) {
        float[] sub = new float[9];
        int indexRow = 0, indexCol = 0;
        for (int row = 0; row < 4; row++) {
            if (row == excludeRow) continue;
            for (int col = 0; col < 4; col++) {
                if (col == excludeCol) continue;
                sub[indexCol + indexRow * 3] = getValue(row, col);
                indexCol++;
            }
            indexCol = 0;
            indexRow++;
        }
        return sub;
    }

    /**
     * Calculate a 3X3 determinant the matrix arrangement is as shown below<br/><br/>
     * |m00 m01 m02|<br/>
     * |m10 m11 m12|<br/>
     * |m20 m21 m22|<br/>
     */
    private float calculateDet3(float m00, float m01, float m02,
                                float m10, float m11, float m12,
                                float m20, float m21, float m22) {
        //https://semath.info/src/determinant-four-by-four.html
        return (m00 * m11 * m22) + (m01 * m12 * m20) + (m02 * m10 * m21) - (m02 * m11 * m20) - (m01 * m10 * m22) - (m00 * m12 * m21);
    }

    private float calculateDet3(float[] mat) {
        //https://semath.info/src/determinant-four-by-four.html
        return (mat[0] * mat[4] * mat[8]) + (mat[1] * mat[5] * mat[6]) + (mat[2] * mat[3] * mat[7]) - (mat[2] * mat[4] * mat[6]) - (mat[1] * mat[3] * mat[8]) - (mat[0] * mat[5] * mat[7]);
    }

    @Override
    public String toString() {
        float[] m = get();
        StringBuilder buf = new StringBuilder();
        for (int col = 0; col < 4; col++) {
            buf.append("| ");
            for (int row = 0; row < 4; row++) {
                buf.append(String.format("%f ", m[col + row * 4]));
            }
            buf.append("|\n");
        }
        return buf.toString();
    }

    private float fma(float a, float b, float c) {
        return a * b + c;
    }

    public static void main(String[] args) {
    }


}
