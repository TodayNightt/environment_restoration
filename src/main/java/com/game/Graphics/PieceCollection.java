package com.game.Graphics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.game.Graphics.Mesh.PieceMesh;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceCollection{

    private static PieceCollection instance;
    Map<String, PieceMesh> collection;

    private PieceCollection() {
        initPieces();
    }


    private static PieceCollection getInstance() {
        if (instance == null) {
            init();
        }
        return instance;
    }
    public static void init(){
        instance = new PieceCollection();
    }


    protected void initPieces() {
        this.collection = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode root;
        try {
            root = (ArrayNode) mapper.readTree(new File("src/main/resources/piece/pieces.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        root.elements().forEachRemaining(item -> {
            String name = item.get("name").asText();
            PieceMesh mesh = mapper.convertValue(item.get("meshData"), PieceMesh.class);
            this.collection.put(name, mesh);
        });

    }


    public static List<String> getPieceType() {
        return getInstance().collection.keySet().stream().toList();
    }

    public static PieceMesh getMesh(String name) {
        return getInstance().collection.get(name);
    }


}
