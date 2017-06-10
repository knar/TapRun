package bros.taprun.graphics.mesh;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MeshRenderer implements GLSurfaceView.Renderer {

    private Context context;

    // The thing we update
    private Runnable game;

    private float[] clearColor;

    // The Map of MeshPlacement Lists
    private Map<Mesh, ArrayList<MeshPlacement>> meshes;

    // The Camera transform
    private float[] screenMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float viewWidth = 1f;

    // Shader (Should be separate class?)
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int program;

    private int positionHandle;
    private int mvpHandle;
    private int colorHandle;

    //Methods

    public MeshRenderer(Context ctx, Runnable runner) {
        context = ctx;
        game = runner;

        meshes = new HashMap<>();

        Matrix.setIdentityM(screenMatrix, 0);
    }

    public void addMesh(MeshPlacement mesh) {
        if (!meshes.containsKey(mesh.getMeshType())) {
            meshes.put(mesh.getMeshType(), new ArrayList<MeshPlacement>());
        }
        meshes.get(mesh.getMeshType()).add(mesh);
    }

    public void removeMesh(MeshPlacement mesh) {
        meshes.get(mesh.getMeshType()).remove(mesh);
    }

    public void setViewWidth(float width) {
        viewWidth = width;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glEnable( GLES20.GL_DEPTH_TEST );

        initShader();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        float margin = 0.2f;

        Matrix.setIdentityM(screenMatrix, 0);
        Matrix.translateM(screenMatrix, 0, -1f + margin, -1f, 0f);
        Matrix.scaleM(screenMatrix, 0, 1, ratio, 1);
        Matrix.scaleM(screenMatrix, 0,  2f * (1 - margin) / viewWidth, 2f * (1 - margin) / viewWidth, 1);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        game.run();

        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);

        GLES20.glUseProgram(program);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        for (Mesh meshType : meshes.keySet()) {

            positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
            GLES20.glEnableVertexAttribArray(positionHandle);
            GLES20.glVertexAttribPointer(positionHandle, Mesh.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, meshType.getVertexStride(), meshType.getVertexBuffer());

            mvpHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
            colorHandle = GLES20.glGetUniformLocation(program, "vColor");

            float[] mvp;

            for (MeshPlacement mesh : meshes.get(meshType)) {

                mvp = mesh.createModelMatrix();
                Matrix.multiplyMM(mvp, 0, viewMatrix, 0, mvp, 0);
                Matrix.multiplyMM(mvp, 0, screenMatrix, 0, mvp, 0);

                GLES20.glUniform4fv(colorHandle, 1, mesh.getColor(), 0);
                GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvp, 0);

                GLES20.glDrawElements(GLES20.GL_TRIANGLES, meshType.getDrawOrderLength(), GLES20.GL_UNSIGNED_SHORT, meshType.getDrawListBuffer());

            }

            GLES20.glDisableVertexAttribArray(positionHandle);
        }

    }

    private void initShader() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void setClearColor(float[] color) {
        clearColor = color;
    }

    public void updateViewMatrix(float distance) {
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.translateM(viewMatrix, 0, 0, -distance, 0);
    }

}
