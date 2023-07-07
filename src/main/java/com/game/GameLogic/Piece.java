package com.game.GameLogic;

import com.game.Graphics.MatrixCalc;
import com.game.Graphics.Mesh.PieceMesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Random;

public class Piece {
    private final PieceMesh mesh;
    private final Matrix4f modelMatrix;
    private final Vector3f position;

    public Piece(String pieceType,float x, float y ,float z) {
        long first = System.currentTimeMillis();
        this.position = new Vector3f(x,y ,z);
        this.mesh = PieceCollection.getInstance().getMesh(pieceType);
        this.modelMatrix = MatrixCalc.createModelMatrix(
                new Matrix4f().identity(),
                new Matrix4f().identity(),
                new Matrix4f().identity(),
                new Matrix4f().identity().translation(position),
                new Matrix4f().scale(1.0f)
        );
        rotatePiece(MatrixCalc.rotationMatrix((float) Math.random(),(byte)new Random().nextInt(1,3)));
        System.out.println("Piece" + (System.currentTimeMillis() - first));
    }

    public Vector3f getPosition() {
        return position;
    }

    public void translatePiece(float x, float y, float z) {
        this.modelMatrix.translation(x, y, z);
    }

    public void rotatePiece(Matrix4f rotation) {
        this.modelMatrix.mul(rotation);
    }

    public PieceMesh getMesh() {
        return mesh;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
}
