package com.game.common.GameLogic;

import com.game.common.Utils.Mat4f;
import com.game.common.Utils.Vec3f;
import com.game.lwjgl.Graphics.LwjglPieceCollection;
import com.game.lwjgl.Graphics.Mesh.LwjglPieceMesh;

public class Piece {
    private final LwjglPieceMesh mesh;
    private final Mat4f modelMatrix;
    private final Vec3f position;

    public Piece(String pieceType, float x, float y, float z) {
        this.position = new Vec3f(x, y, z);
        this.mesh = LwjglPieceCollection.getInstance().getMesh(pieceType);
//        this.modelMatrix = MatrixCalc.createModelMatrix(
//                new Matrix4f().identity(),
//                new Matrix4f().identity(),
//                new Matrix4f().identity(),
//                new Matrix4f().identity().translation(position),
//                new Matrix4f().scale(1.0f)
//        );
//        rotatePiece(MatrixCalc.rotationMatrix(new Random().nextFloat(360),(byte) new Random().nextInt(1,3)));
        this.modelMatrix = null;
    }

    public void rotatePiece(Mat4f rotation) {
        this.modelMatrix.mul(rotation);
    }

    //Getter
    public Vec3f getPosition() {
        return position;
    }

    public LwjglPieceMesh getMesh() {
        return mesh;
    }

    public Mat4f getModelMatrix() {
        return modelMatrix;
    }
}
