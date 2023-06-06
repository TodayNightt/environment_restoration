package GUI;

import Graphics.ShaderProgram;
import Graphics.UniformsMap;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL3;
import imgui.ImFontAtlas;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.type.ImInt;
import org.joml.Vector2f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class UiRenderer {
    private GL3 gl;
    private final ShaderProgram shaderP;
    private UiMesh uiMesh;
    private Vector2f scale;
    private String textureName;
    private UniformsMap uniformsMap;

    public UiRenderer(GL3 gl, GLWindow window) throws Exception {
        this.gl = gl;
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/gui.vert",GL3.GL_VERTEX_SHADER));
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/gui.frag",GL3.GL_FRAGMENT_SHADER));
        shaderP =  new ShaderProgram(gl,shaderDataList);
        createUniforms();
        createUIResources(window);
    }

    private void createUIResources(GLWindow window){
        ImGui.createContext();

        ImGuiIO imGuiIO = ImGui.getIO();
        imGuiIO.setIniFilename(null);
        imGuiIO.setDisplaySize(window.getWidth(),window.getHeight());

        ImFontAtlas fontAtlas = ImGui.getIO().getFonts();
        ImInt width = new ImInt();
        ImInt height = new ImInt();
        ByteBuffer buf = fontAtlas.getTexDataAsRGBA32(width,height);

    }

    private void createUniforms() {
        uniformsMap = new UniformsMap(shaderP.getProgramId(), gl);
    }


}
