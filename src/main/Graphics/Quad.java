package Graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

import static Graphics.Chunk.Cube;

//https://github.com/TanTanDev/first_voxel_engine/blob/main/src/voxel_tools/mesh_builder.rs
public enum Quad {
    TOP, BOTTOM, FRONT, BACK, LEFT, RIGHT;

    private static final float HALF_SIZE = 0.5f;


public static float[] getMeshFace(Quad faces, Vector3f position, Vector2f uvOffset) {
        return switch (faces) {
            case TOP -> new float[]{
                    //Vertex                                                                //UV        //UV offsets
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
            case BOTTOM -> new float[]{
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
            case BACK -> new float[]{
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
            case FRONT -> new float[]{
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
            case LEFT -> new float[]{
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,

            };
            case RIGHT -> new float[]{
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
        };
    }

    public static void processQuad(FloatBuffer vertexDataBuffer, Cube self, Cube left, Cube right, Cube top,
            Cube bottom, Cube back, Cube front) {
        Vector3f position = self.position();
        if (self.isSolid()) {
            if (!left.isSolid()) {
                vertexDataBuffer.put(getMeshFace(LEFT, position, self.uvOffset()));
            }
            if (!right.isSolid()) {
                vertexDataBuffer.put(getMeshFace(RIGHT, position, self.uvOffset()));
            }
            if (!top.isSolid()) {
                vertexDataBuffer.put(getMeshFace(TOP, position, self.uvOffset()));
            }
            if (!bottom.isSolid()) {
                vertexDataBuffer.put(getMeshFace(BOTTOM, position, self.uvOffset()));
            }
            if (!back.isSolid()) {
                vertexDataBuffer.put(getMeshFace(BACK, position, self.uvOffset()));
            }
            if (!front.isSolid()) {
                vertexDataBuffer.put(getMeshFace(FRONT, position, self.uvOffset()));
            }
        } else {
            if (left.isSolid()) {
                vertexDataBuffer.put(getMeshFace(LEFT, position,left.uvOffset()));
            }
            if (bottom.isSolid()) {
                vertexDataBuffer.put(getMeshFace(BOTTOM, position,bottom.uvOffset()));
            }
            if (back.isSolid()) {
                vertexDataBuffer.put(getMeshFace(BACK, position, back.uvOffset()));
            }

        }
    }
}
