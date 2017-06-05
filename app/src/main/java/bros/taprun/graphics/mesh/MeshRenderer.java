package bros.taprun.graphics.mesh;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import bros.taprun.game.Grid;

public class MeshRenderer implements GLSurfaceView.Renderer {

    // The Map of Mesh Lists
    private Map<MeshType, ArrayList<Mesh>> meshes;

    // The Camera transform
    private float[] viewMatrix = new float[16];

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

    private Grid grid;

    //Methods

    public MeshRenderer() {

        meshes = new HashMap<>();

        grid = new Grid();
        grid.addMeshes(this);

        Matrix.setIdentityM(viewMatrix, 0);
    }

    public void addMesh(Mesh mesh) {
        if (!meshes.containsKey(mesh.getMeshType())) {
            meshes.put(mesh.getMeshType(), new ArrayList<Mesh>());
        }
        meshes.get(mesh.getMeshType()).add(mesh);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.15f, 0.15f, 0.15f, 1.0f);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        initShader();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.translateM(viewMatrix, 0, -1f, -1f, 0f);
        Matrix.scaleM(viewMatrix, 0, 1, ratio, 1);
        Matrix.scaleM(viewMatrix, 0,  2f / grid.getWidth(), 2f / grid.getWidth(), 1);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glUseProgram(program);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        for (MeshType meshType : meshes.keySet()) {

            positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
            GLES20.glEnableVertexAttribArray(positionHandle);
            GLES20.glVertexAttribPointer(positionHandle, MeshType.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, meshType.getVertexStride(), meshType.getVertexBuffer());

            mvpHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
            colorHandle = GLES20.glGetUniformLocation(program, "vColor");

            float[] mvp;

            for (Mesh mesh : meshes.get(meshType)) {

                mvp = mesh.createModelMatrix();
                Matrix.multiplyMM(mvp, 0, viewMatrix, 0, mvp, 0);

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

}
