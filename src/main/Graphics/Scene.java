package Graphics;

import GUI.UiInstance;
import GameLogic.Piece;
import GameLogic.PieceCollection;
import Terrain.TerrainMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL30.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL30.GL_VERTEX_SHADER;

public class Scene {
//    private Textures textureList;
    private TerrainMap terrain;
    private List<Piece> pieces;
    private HashMap<String,ShaderProgram> shaderProgramList;
    private HashMap<String,UniformsMap> uniformsMapList;
    private UiInstance uiInstance;

    public Scene() throws Exception {
        init();
    }
    private void init() throws Exception {
//        this.textureList = new Textures();
        this.shaderProgramList = new HashMap<>();
        this.uniformsMapList = new HashMap<>();
        this.pieces = new ArrayList<>();
//        initializeTexture();
        initializePiece();
        initializeTerrainGen();
    }
//    private void initializeTexture() throws IOException {
//        TextureGenerator.GenerateAtlas();
//        textureList.createTexture("block_atlas", "src/main/resources/textures/block_atlas_texture.png", true);
//    }
//
//    public Textures getTextureList(){
//        return  textureList;
//    }

    public TerrainMap getTerrain(){
        return terrain;
    }

    private void initializeTerrainGen() throws Exception {

        //Initialize shaderProgram
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/terrain.vert", GL_VERTEX_SHADER));
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/terrain.frag", GL_FRAGMENT_SHADER));
        ShaderProgram shaderProgram = new ShaderProgram(shaderDataList);

        //Initialize uniformMap
        UniformsMap uniformsMap = new UniformsMap(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix");
        uniformsMap.createUniform("modelMatrix");
//        uniformsMap.createUniform("tex");
//        uniformsMap.createUniform("textureRow");



        this.shaderProgramList.put("terrain",shaderProgram);
        this.uniformsMapList.put("terrain",uniformsMap);
        this.terrain = new TerrainMap("block_atlas");
    }

    private void initializePiece() throws Exception {
        PieceCollection piecesCollection = new PieceCollection();

        //Initialize shaderProgram
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/piece.vert", GL_VERTEX_SHADER));
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/piece.frag", GL_FRAGMENT_SHADER));
        ShaderProgram shaderProgram = new ShaderProgram(shaderDataList);

        //Initialize uniformMap
        UniformsMap uniformsMap = new UniformsMap(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("size");

        this.shaderProgramList.put("piece",shaderProgram);
        this.uniformsMapList.put("piece",uniformsMap);
        Piece piece = new Piece(piecesCollection,"straight4t1w");
        piece.translatePiece(-3.0f,0.0f,-1.0f);
        piece.rotatePiece(MatrixCalc.rotationMatrix(45.f,(byte) 2));
        this.pieces.add(piece);
        piece = new Piece(piecesCollection,"square1d");
        piece.translatePiece(3.0f,0.0f,-1.0f);
        piece.rotatePiece(MatrixCalc.rotationMatrix(45.f,(byte) 2));
        this.pieces.add(piece);
        piece = new Piece(piecesCollection,"lshape3t1d");
        piece.translatePiece(0.f,0.0f,-1.0f);
        piece.rotatePiece(MatrixCalc.rotationMatrix(45.f,(byte) 2));
        this.pieces.add(piece);
    }

    public List<Piece> getPiece(){
        return pieces;
    }

    public ShaderProgram getShaderProgram(String name){
        return shaderProgramList.get(name);
    }

    public void cleanup(){
        shaderProgramList.values().forEach(ShaderProgram::cleanup);
    }

    public UniformsMap getUniformMap(String name){
        return uniformsMapList.get(name);
    }
}
