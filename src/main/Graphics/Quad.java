package Graphics;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import static Graphics.Chunk.Cube;

//https://github.com/TanTanDev/first_voxel_engine/blob/main/src/voxel_tools/mesh_builder.rs
public enum Quad {
    TOP, BOTTOM, FRONT, BACK, LEFT, RIGHT;

    private static final float HALF_SIZE = 0.5f;

    public static float[] getMeshFace(Quad faces, Vector3f position) {
        switch (faces) {
            case TOP:
                return new float[] {
                        position.x - HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f,
                        position.x + HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 1.0f, 1.0f,
                        position.x - HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 0.0f, 0.0f,
                        position.x + HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f
                };

            case BOTTOM:
                return new float[] {
                        position.x - HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f,
                        position.x + HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 1.0f, 1.0f,
                        position.x - HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 0.0f, 0.0f,
                        position.x + HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f
                };

            case BACK:
                return new float[] {
                        position.x - HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f,
                        position.x + HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 1.0f, 1.0f,
                        position.x - HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 0.0f,
                        position.x + HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 1.0f, 0.0f
                };

            case FRONT:
                return new float[] {
                        position.x - HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 0.0f, 1.0f,
                        position.x + HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 1.0f,
                        position.x - HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 0.0f, 0.0f,
                        position.x + HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f
                };

            case LEFT:
                return new float[] {
                        position.x - HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f,
                        position.x - HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 1.0f,
                        position.x - HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 0.0f,
                        position.x - HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f

                };

            case RIGHT:
                return new float[] {

                        position.x + HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f,
                        position.x + HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 1.0f,
                        position.x + HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 0.0f,
                        position.x + HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f
                };

            default:
                return null;
        }
    }

    public static void processQuad(FloatBuffer vertexDataBuffer, Cube self, Cube left, Cube right, Cube bottom,
            Cube back, Cube front) {
        Vector3f position = self.position();
        if (self.is_Solid() && self != null) {
            if (!left.is_Solid()) {
                vertexDataBuffer.put(getMeshFace(LEFT, position));
            }
            if (!right.is_Solid()) {
                vertexDataBuffer.put(getMeshFace(RIGHT, position));
            }
            if (!bottom.is_Solid()) {
                vertexDataBuffer.put(getMeshFace(BOTTOM, position));
            }
            if (!back.is_Solid()) {
                vertexDataBuffer.put(getMeshFace(BACK, position));
            }
            if (!front.is_Solid()) {
                vertexDataBuffer.put(getMeshFace(FRONT, position));
            }

        } else {
            if (left.is_Solid()) {
                vertexDataBuffer.put(getMeshFace(LEFT, position));
            }
            if (bottom.is_Solid()) {
                vertexDataBuffer.put(getMeshFace(BOTTOM, position));
            }
            if (back.is_Solid()) {
                vertexDataBuffer.put(getMeshFace(BACK, position));
            }

        }
    }

}
