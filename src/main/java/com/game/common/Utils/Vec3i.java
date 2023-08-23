package com.game.common.Utils;


public class Vec3i {
    public int x() {
        return x;
    }

    public Vec3i setX(int x) {
        this.x = x;
        return this;
    }

    public int y() {
        return y;
    }

    public Vec3i setY(int y) {
        this.y = y;
        return this;
    }

    public int z() {
        return z;
    }

    public Vec3i setZ(int z) {
        this.z = z;
        return this;
    }

    private int x, y, z;

    public Vec3i(int x, int y, int z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public Vec3i(float x, float y, float z) {
        setX((int) x);
        setY((int) y);
        setZ((int) z);
    }

    public Vec3i() {
        setX(0);
        setY(0);
        setZ(0);
    }

    public Vec3i negate() {
        return setX(-x())
                .setY(-y())
                .setZ(-x());
    }

//    public float dot(Vec3i other) {
//        //https://www.mathsisfun.com/algebra/vectors-dot-product.html
//        return x() * other.x() + y() * other.y() + z() * other.z();
//    }

//    public Vec3i cross(Vec3i other) {
//        //https://www.mathsisfun.com/algebra/vectors-cross-product.html
//        return new Vec3i((y() * other.z()) - (z() * other.y()),
//                (z() * other.x()) - (x() * other.z()),
//                (x() * other.y()) - (y() * other.x()));
//    }


    public Vec3i addX(int amount) {
        return setX(x() + amount);
    }

    public Vec3i addY(int amount) {
        return setY(y() + amount);
    }

    public Vec3i addZ(int amount) {
        return setZ(z() + amount);
    }


    public Vec3i(Vec3i other) {
        setX(other.x());
        setY(other.y());
        setZ(other.z());
    }

    public Vec3i set(int x, int y, int z) {
        return setX(x)
                .setY(y)
                .setZ(z);
    }

    public Vec3i set(float x, float y, float z) {
        return setX((int) x)
                .setY((int) y)
                .setZ((int) z);
    }


    public Vec3i add(Vec3i other) {
        return setX(x() + other.x())
                .setY(y() + other.y())
                .setZ(z() + other.z());
    }

    public Vec3i sub(Vec3i other) {
        return setX(x() - other.x())
                .setY(y() - other.y())
                .setZ(z() - other.z());
    }

//    public Vec3i mulDirection(Mat4f mat) {
//        float x = x(), y = y(), z = z();
//        return setX(fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z)))
//                .setY(fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z)))
//                .setZ(fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z)));
//    }

//    public Vec3i normalize() {
//        //https://en.wikipedia.org/wiki/Fast_inverse_square_root
//        float factor = (float) (1f / sqrt(x() * x() + y() * y() + z() * z()));
//        return setX(x() * factor)
//                .setY(y() * factor)
//                .setZ(z() * factor);
//    }

    public boolean equals(int x, int y, int z) {
        return x == x() && y == y() && z == z();
    }

    public Vec3i mul(int factor) {
        return setX(x() * factor)
                .setY(y() * factor)
                .setZ(z() * factor);
    }

    @Override
    public String toString() {
        return String.format("(%s) | %d %d %d |", getClass().getSimpleName(), x(), y(), z());
    }

}
