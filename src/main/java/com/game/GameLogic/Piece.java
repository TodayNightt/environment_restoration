package com.game.GameLogic;

import com.game.Utils.MatrixCalc;
import com.game.Graphics.PieceCollection;
import com.game.Graphics.Mesh.PieceMesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Piece {
    private final PieceMesh mesh;
    private final Matrix4f modelMatrix;
    private final Vector3f position;

    public Piece(String pieceType, float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        this.mesh = PieceCollection.getMesh(pieceType);
        this.modelMatrix = MatrixCalc.createModelMatrix(
                new Matrix4f().identity(),
                new Matrix4f().identity(),
                new Matrix4f().identity(),
                new Matrix4f().identity().translation(position),
                new Matrix4f().scale(1.0f)
        );
    }

    public void rotatePiece(Matrix4f rotation) {
        this.modelMatrix.mul(rotation);
    }

    //Getter
    public Vector3f getPosition() {
        return position;
    }

    public PieceMesh getMesh() {
        return mesh;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
}
