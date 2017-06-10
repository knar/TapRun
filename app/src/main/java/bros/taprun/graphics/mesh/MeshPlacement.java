package bros.taprun.graphics.mesh;

import android.opengl.Matrix;

public class MeshPlacement {

    private Mesh meshType;

    // Transform
    private float[] position;
    private float rotation;
    private float[] scale;

    //Appearance
    private float[] color;

    public MeshPlacement(Mesh meshType, float[] position, float rotation, float[] scale, float[] color) {
        this.meshType = meshType;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.color = color;
    }

    public float[] createModelMatrix() {
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, position[0], position[1], 0);
        Matrix.scaleM(modelMatrix, 0, scale[0], scale[1], 0);
        Matrix.rotateM(modelMatrix, 0, rotation, 0f, 0f, 1.0f);
        return modelMatrix;
    }

    // Setters and Getters

    public Mesh getMeshType() {
        return meshType;
    }

    public void setMeshType(Mesh meshType) {
        this.meshType = meshType;
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float[] scale) {
        this.scale = scale;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

}
