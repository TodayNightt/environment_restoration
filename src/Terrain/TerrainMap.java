package Terrain;

public class TerrainMap {
    protected final int MAX_CHUNK = 2;

    private Chunk[][] chunks;

    public TerrainMap() {
        chunks = new Chunk[MAX_CHUNK][MAX_CHUNK];
    }
}
