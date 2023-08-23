package com.game.common;

import java.util.List;

public abstract class PieceCollection {

    public abstract List<String> getPieceType();

    protected abstract void initPieces();
}
