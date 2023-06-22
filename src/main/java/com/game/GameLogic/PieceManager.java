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

    public static void addPiece(String pieceType) {
        if (!getInstance().pieceList.isEmpty()) {
            if (getInstance().pieceList.get(getInstance().pieceList.size() - 1).getPosition().equals(0.0f, 0.0f, 0.0f)) {
                getInstance().pieceList.get(getInstance().pieceList.size() - 1).translatePiece(3.0f, getInstance().count, 0.f);
                getInstance().count += 3;
            }
        }
        getInstance().pieceList.add(new Piece(pieceType));
    }

    public List<Piece> getPieceList() {
        return pieceList;
    }
}
