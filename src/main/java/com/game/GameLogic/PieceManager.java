package com.game.GameLogic;

import java.util.ArrayList;
import java.util.List;

public class PieceManager {
    private static PieceManager pieceManager;
    List<Piece> pieceList;
    private int count = 0;

    private PieceManager() {
        this.pieceList = new ArrayList<>();
    }

    public static PieceManager getInstance() {
        if (pieceManager == null) {
            pieceManager = new PieceManager();
        }
        return pieceManager;
    }

    public static void addPiece(String pieceType,float x,float y , float z) {
        getInstance().pieceList.add(new Piece(pieceType,x,y,z));
    }

    public List<Piece> getPieceList() {
        return pieceList;
    }
}
