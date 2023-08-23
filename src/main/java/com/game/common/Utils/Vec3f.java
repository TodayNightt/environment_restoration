package com.game.common.Utils;

import static java.lang.Math.sqrt;

public class Vec3f {
    public float x() {
        return x;
    }

    public Vec3f setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public Vec3f setY(float y) {
        this.y = y;
        return this;
    }

    public float z() {
        return z;
    }

    public Vec3f setZ(float z) {
        this.z = z;
        return this;
    }

    private float x, y, z;

    public Vec3f(float x, float y, float z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public Vec3f() {
        setX(0f);
        setY(0f);
        setZ(0f);
    }

    public Vec3f negate() {
        return setX(-x())
                .setY(-y())
                .setZ(-x());
    }

    public float dot(Vec3f other) {
        //https://www.mathsisfun.com/algebra/vectors-dot-product.html
        return x() * other.x() + y() * other.y() + z() * other.z();
    }

    public Vec3f cross(Vec3f other) {
        //https://www.mathsisfun.com/algebra/vectors-cross-product.html
        return new Vec3f((y() * other.z()) - (z() * other.y()),
                (z() * other.x()) - (x() * other.z()),
                (x() * other.y()) - (y() * other.x()));
    }


    public Vec3f addX(float amount) {
        return setX(x() + amount);
    }

    public Vec3f addY(float amount) {
        return setY(y() + amount);
    }

    public Vec3f addZ(float amount) {
        return setZ(z() + amount);
    }


    public Vec3f(Vec3f other) {
        setX(other.x());
        setY(other.y());
        setZ(other.z());
    }

    public Vec3f set(float x, float y, float z) {
        return setX(x)
                .setY(y)
                .setZ(z);
    }


    public Vec3f add(Vec3f other) {
        return setX(x() + other.x())
                .setY(y() + other.y())
                .setZ(z() + other.z());
    }

    public Vec3f sub(Vec3f other) {
        return setX(x() - other.x())
                .setY(y() - other.y())
                .setZ(z() - other.z());
    }

    public Vec3f mulDirection(Mat4f mat) {
        float x = x(), y = y(), z = z();
        return setX(fma(mat.m00(), x, fma(mat.m10(), y, mat.m20() * z)))
                .setY(fma(mat.m01(), x, fma(mat.m11(), y, mat.m21() * z)))
                .setZ(fma(mat.m02(), x, fma(mat.m12(), y, mat.m22() * z)));
    }

    public Vec3f normalize() {
        //https://en.wikipedia.org/wiki/Fast_inverse_square_root
        float factor = (float) (1f / sqrt(x() * x() + y() * y() + z() * z()));
        return setX(x() * factor)
                .setY(y() * factor)
                .setZ(z() * factor);
    }

    public Vec3f mul(float factor) {
        return setX(x() * factor)
                .setY(y() * factor)
                .setZ(z() * factor);
    }

    public float fma(float a, float b, float c) {
        return a * b + c;
    }

    @Override
    public String toString() {
        return String.format("(%s) | %f %f %f |", getClass().getSimpleName(), x(), y(), z());
    }

    public static void main(String[] args) {
        System.out.println(new Vec3f(10f, 20f, 30f).normalize());
    }
}
