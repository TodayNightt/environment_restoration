package Graphics;

import imgui.*;
import imgui.flag.ImGuiBackendFlags;
import imgui.type.ImInt;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengles.GLES31.*;

public class ImGuiGL31ES {

    // Used to store tmp renderer data
    private final ImVec2 displaySize = new ImVec2();
    private final ImVec2 framebufferScale = new ImVec2();
    private final ImVec2 displayPos = new ImVec2();
    private final ImVec4 clipRect = new ImVec4();
    private final float[] orthoProjMatrix = new float[4 * 4];
    // Variables used to backup GL state before and after the rendering of Dear ImGui
    private final int[] lastActiveTexture = new int[1];
    private final int[] lastProgram = new int[1];
    private final int[] lastTexture = new int[1];
    private final int[] lastArrayBuffer = new int[1];
    private final int[] lastVertexArrayObject = new int[1];
    private final int[] lastViewport = new int[4];
    private final int[] lastScissorBox = new int[4];
    private final int[] lastBlendSrcRgb = new int[1];
    private final int[] lastBlendDstRgb = new int[1];
    private final int[] lastBlendSrcAlpha = new int[1];
    private final int[] lastBlendDstAlpha = new int[1];
    private final int[] lastBlendEquationRgb = new int[1];
    private final int[] lastBlendEquationAlpha = new int[1];
    private ShaderProgram shaderP;
    private int gFontTexture = 0;
    private int gAttribLocationTex = 0;
    private int gAttribLocationProjMtx = 0;
    private int gAttribLocationVtxPos = 0;
    private int gAttribLocationVtxUV = 0;
    private int gAttribLocationVtxColor = 0;
    private int gVboHandle = 0;
    private int gElementsHandle = 0;
    private int gVertexArrayObjectHandle = 0;
    private boolean lastEnableBlend = false;
    private boolean lastEnableCullFace = false;
    private boolean lastEnableDepthTest = false;
    private boolean lastEnableStencilTest = false;
    private boolean lastEnableScissorTest = false;

    public void init() throws Exception {
        setupBackendCapabilitiesFlags();
        createDeviceObjects();
//        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
//            initPlatformInterface();
//        }
    }

    private void setupBackendCapabilitiesFlags(){
        final ImGuiIO io = ImGui.getIO();

        io.setBackendRendererName("imgui_java_impl_opengles3");

        //glVesion >= 320
        io.setBackendFlags(ImGuiBackendFlags.RendererHasVtxOffset);

        io.setBackendFlags(ImGuiBackendFlags.RendererHasViewports);


    }

    private void createDeviceObjects() throws Exception {
        //GL state
        final int[] lastTexture = new int[1];
        final int[] lastArrayBuffer = new int[1];
        final int[] lastVertexArray = new int[1];
        glGetIntegerv(GL_TEXTURE_BINDING_2D,lastTexture);
        glGetIntegerv(GL_ARRAY_BUFFER_BINDING,lastArrayBuffer);
        glGetIntegerv(GL_VERTEX_ARRAY_BINDING,lastVertexArray);

        createShader();

        int gShaderHandle = shaderP.getProgramId();

        gAttribLocationTex = glGetUniformLocation(gShaderHandle,"Texture");
        gAttribLocationTex = glGetUniformLocation(gShaderHandle, "Texture");
        gAttribLocationProjMtx = glGetUniformLocation(gShaderHandle, "ProjMtx");
        gAttribLocationVtxPos = glGetAttribLocation(gShaderHandle, "Position");
        gAttribLocationVtxUV = glGetAttribLocation(gShaderHandle, "UV");
        gAttribLocationVtxColor = glGetAttribLocation(gShaderHandle, "Color");


        gVboHandle = glGenBuffers();
        gElementsHandle = glGenBuffers();

        updateFontsTexture();

        glBindTexture(GL_TEXTURE_2D,lastTexture[0]);
        glBindBuffer(GL_ARRAY_BUFFER,lastArrayBuffer[0]);
        glBindVertexArray(lastVertexArray[0]);

    }

    private void createShader() throws Exception {
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(new ShaderProgram.ShaderData(getVertexShaderGlsl300es(), GL_VERTEX_SHADER));
        shaderDataList.add(new ShaderProgram.ShaderData(getFragmentShaderGlsl300es(), GL_FRAGMENT_SHADER));
        shaderP = new ShaderProgram(shaderDataList);
    }

    /**
     * Call this method in the end of your application cycle to dispose resources used by {@link }.
     */
    public void dispose() {
        glDeleteBuffers(gVboHandle);
        glDeleteBuffers(gElementsHandle);
        shaderP.cleanup();
        glDeleteTextures(gFontTexture);
        shutdownPlatformInterface();
    }

    private void shutdownPlatformInterface() {
        ImGui.destroyPlatformWindows();
    }

    public void updateFontsTexture() {
        glDeleteTextures(gFontTexture);

        final ImFontAtlas fontAtlas = ImGui.getIO().getFonts();
        final ImInt width = new ImInt();
        final ImInt height = new ImInt();
        final ByteBuffer buffer = fontAtlas.getTexDataAsRGBA32(width, height);

        gFontTexture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, gFontTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        fontAtlas.setTexID(gFontTexture);
    }

//    private void initPlatformInterface() {
//        ImGui.getPlatformIO().setRendererRenderWindow(new ImPlatformFuncViewport() {
//            @Override
//            public void accept(final ImGuiViewport vp) {
//                if (!vp.hasFlags(ImGuiViewportFlags.NoRendererClear)) {
//                    glClearColor(0, 0, 0, 0);
//                    glClear(GL_COLOR_BUFFER_BIT);
//                }
//                renderDrawData(vp.getDrawData());
//            }
//        });
//    }

    public void renderDrawData(final ImDrawData drawData) {
        if (drawData.getCmdListsCount() <= 0) {
            return;
        }

        // Will project scissor/clipping rectangles into framebuffer space
        drawData.getDisplaySize(displaySize);           // (0,0) unless using multi-viewports
        drawData.getDisplayPos(displayPos);
        drawData.getFramebufferScale(framebufferScale); // (1,1) unless using retina display which are often (2,2)

        final float clipOffX = displayPos.x;
        final float clipOffY = displayPos.y;
        final float clipScaleX = framebufferScale.x;
        final float clipScaleY = framebufferScale.y;

        // Avoid rendering when minimized, scale coordinates for retina displays (screen coordinates != framebuffer coordinates)
        final int fbWidth = (int) (displaySize.x * framebufferScale.x);
        final int fbHeight = (int) (displaySize.y * framebufferScale.y);

        if (fbWidth <= 0 || fbHeight <= 0) {
            return;
        }

        backupGlState();
        bind(fbWidth, fbHeight);

        // Render command lists
        for (int cmdListIdx = 0; cmdListIdx < drawData.getCmdListsCount(); cmdListIdx++) {
            // Upload vertex/index buffers
            glBufferData(GL_ARRAY_BUFFER, drawData.getCmdListVtxBufferData(cmdListIdx), GL_STREAM_DRAW);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, drawData.getCmdListIdxBufferData(cmdListIdx), GL_STREAM_DRAW);

            for (int cmdBufferIdx = 0; cmdBufferIdx < drawData.getCmdListCmdBufferSize(cmdListIdx); cmdBufferIdx++) {
                drawData.getCmdListCmdBufferClipRect(cmdListIdx, cmdBufferIdx, clipRect);

                final float clipMinX = (clipRect.x - clipOffX) * clipScaleX;
                final float clipMinY = (clipRect.y - clipOffY) * clipScaleY;
                final float clipMaxX = (clipRect.z - clipOffX) * clipScaleX;
                final float clipMaxY = (clipRect.w - clipOffY) * clipScaleY;

                if (clipMaxX <= clipMinX || clipMaxY <= clipMinY) {
                    continue;
                }

                // Apply scissor/clipping rectangle (Y is inverted in OpenGL)
                glScissor((int) clipMinX, (int) (fbHeight - clipMaxY), (int) (clipMaxX - clipMinX), (int) (clipMaxY - clipMinY));

                // Bind texture, Draw
                final int textureId = drawData.getCmdListCmdBufferTextureId(cmdListIdx, cmdBufferIdx);
                final int elemCount = drawData.getCmdListCmdBufferElemCount(cmdListIdx, cmdBufferIdx);
                final int idxBufferOffset = drawData.getCmdListCmdBufferIdxOffset(cmdListIdx, cmdBufferIdx);
                final int vtxBufferOffset = drawData.getCmdListCmdBufferVtxOffset(cmdListIdx, cmdBufferIdx);
                final int indices = idxBufferOffset * ImDrawData.SIZEOF_IM_DRAW_IDX;

                glBindTexture(GL_TEXTURE_2D, textureId);

                glDrawElements(GL_TRIANGLES, elemCount, GL_UNSIGNED_SHORT, indices);
            }
        }

        unbind();
        restoreModifiedGlState();
    }
    private void restoreModifiedGlState() {
        glUseProgram(lastProgram[0]);
        glBindTexture(GL_TEXTURE_2D, lastTexture[0]);
        glActiveTexture(lastActiveTexture[0]);
        glBindVertexArray(lastVertexArrayObject[0]);
        glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer[0]);
        glBlendEquationSeparate(lastBlendEquationRgb[0], lastBlendEquationAlpha[0]);
        glBlendFuncSeparate(lastBlendSrcRgb[0], lastBlendDstRgb[0], lastBlendSrcAlpha[0], lastBlendDstAlpha[0]);
        // @formatter:off CHECKSTYLE:OFF
        if (lastEnableBlend) glEnable(GL_BLEND); else glDisable(GL_BLEND);
        if (lastEnableCullFace) glEnable(GL_CULL_FACE); else glDisable(GL_CULL_FACE);
        if (lastEnableDepthTest) glEnable(GL_DEPTH_TEST); else glDisable(GL_DEPTH_TEST);
        if (lastEnableStencilTest) glEnable(GL_STENCIL_TEST); else glDisable(GL_STENCIL_TEST);
        if (lastEnableScissorTest) glEnable(GL_SCISSOR_TEST); else glDisable(GL_SCISSOR_TEST);
        // @formatter:on CHECKSTYLE:ON
        glViewport(lastViewport[0], lastViewport[1], lastViewport[2], lastViewport[3]);
        glScissor(lastScissorBox[0], lastScissorBox[1], lastScissorBox[2], lastScissorBox[3]);
    }


    private void backupGlState() {
        glGetIntegerv(GL_ACTIVE_TEXTURE, lastActiveTexture);
        glActiveTexture(GL_TEXTURE0);
        glGetIntegerv(GL_CURRENT_PROGRAM, lastProgram);
        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture);
        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer);
        glGetIntegerv(GL_VERTEX_ARRAY_BINDING, lastVertexArrayObject);
        glGetIntegerv(GL_VIEWPORT, lastViewport);
        glGetIntegerv(GL_SCISSOR_BOX, lastScissorBox);
        glGetIntegerv(GL_BLEND_SRC_RGB, lastBlendSrcRgb);
        glGetIntegerv(GL_BLEND_DST_RGB, lastBlendDstRgb);
        glGetIntegerv(GL_BLEND_SRC_ALPHA, lastBlendSrcAlpha);
        glGetIntegerv(GL_BLEND_DST_ALPHA, lastBlendDstAlpha);
        glGetIntegerv(GL_BLEND_EQUATION_RGB, lastBlendEquationRgb);
        glGetIntegerv(GL_BLEND_EQUATION_ALPHA, lastBlendEquationAlpha);
        lastEnableBlend = glIsEnabled(GL_BLEND);
        lastEnableCullFace = glIsEnabled(GL_CULL_FACE);
        lastEnableDepthTest = glIsEnabled(GL_DEPTH_TEST);
        lastEnableStencilTest = glIsEnabled(GL_STENCIL_TEST);
        lastEnableScissorTest = glIsEnabled(GL_SCISSOR_TEST);
    }

    // Setup desired GL state
    private void bind(final int fbWidth, final int fbHeight) {
        // Recreate the VAO every time (this is to easily allow multiple GL contexts to be rendered to. VAO are not shared among GL contexts)
        // The renderer would actually work without any VAO bound, but then our VertexAttrib calls would overwrite the default one currently bound.
        gVertexArrayObjectHandle = glGenVertexArrays();

        // Setup render state: alpha-blending enabled, no face culling, no depth testing, scissor enabled, polygon fill
        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_STENCIL_TEST);
        glEnable(GL_SCISSOR_TEST);

        // Setup viewport, orthographic projection matrix
        // Our visible imgui space lies from draw_data->DisplayPos (top left) to draw_data->DisplayPos+data_data->DisplaySize (bottom right).
        // DisplayPos is (0,0) for single viewport apps.
        glViewport(0, 0, fbWidth, fbHeight);
        final float left = displayPos.x;
        final float right = displayPos.x + displaySize.x;
        final float top = displayPos.y;
        final float bottom = displayPos.y + displaySize.y;

        // Orthographic matrix projection
        orthoProjMatrix[0] = 2.0f / (right - left);
        orthoProjMatrix[5] = 2.0f / (top - bottom);
        orthoProjMatrix[10] = -1.0f;
        orthoProjMatrix[12] = (right + left) / (left - right);
        orthoProjMatrix[13] = (top + bottom) / (bottom - top);
        orthoProjMatrix[15] = 1.0f;

        // Bind shader
        shaderP.bind();
        glUniform1i(gAttribLocationTex, 0);
        glUniformMatrix4fv(gAttribLocationProjMtx, false, orthoProjMatrix);

        glBindVertexArray(gVertexArrayObjectHandle);

        // Bind vertex/index buffers and setup attributes for ImDrawVert
        glBindBuffer(GL_ARRAY_BUFFER, gVboHandle);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, gElementsHandle);
        glEnableVertexAttribArray(gAttribLocationVtxPos);
        glEnableVertexAttribArray(gAttribLocationVtxUV);
        glEnableVertexAttribArray(gAttribLocationVtxColor);
        glVertexAttribPointer(gAttribLocationVtxPos, 2, GL_FLOAT, false, ImDrawData.SIZEOF_IM_DRAW_VERT, 0);
        glVertexAttribPointer(gAttribLocationVtxUV, 2, GL_FLOAT, false, ImDrawData.SIZEOF_IM_DRAW_VERT, 8);
        glVertexAttribPointer(gAttribLocationVtxColor, 4, GL_UNSIGNED_BYTE, true, ImDrawData.SIZEOF_IM_DRAW_VERT, 16);
    }

    private void unbind() {
        // Destroy the temporary VAO
        glDeleteVertexArrays(gVertexArrayObjectHandle);
    }

    private String getVertexShaderGlsl300es() {
        return "#version 310 es\n"
                + "precision highp float;\n"
                + "layout (location = 0) in vec2 Position;\n"
                + "layout (location = 1) in vec2 UV;\n"
                + "layout (location = 2) in vec4 Color;\n"
                + "uniform mat4 ProjMtx;\n"
                + "out vec2 Frag_UV;\n"
                + "out vec4 Frag_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Frag_UV = UV;\n"
                + "    Frag_Color = Color;\n"
                + "    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n"
                + "}\n";
    }

    private String getFragmentShaderGlsl300es() {
        return "#version 310 es\n"
                + "precision mediump float;\n"
                + "uniform sampler2D Texture;\n"
                + "in vec2 Frag_UV;\n"
                + "in vec4 Frag_Color;\n"
                + "layout (location = 0) out vec4 Out_Color;\n"
                + "void main()\n"
                + "{\n"
                + "    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n"
                + "}\n";
    }

}
