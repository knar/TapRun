package bros.taprun.game;

import bros.taprun.graphics.mesh.Mesh;
import bros.taprun.graphics.mesh.MeshType;

public class Tile {

    private Mesh mesh;

    private static float[] color = new float[] {0.9f, .9f, 0.9f, 1f};

    public Tile(int tileType, int x, int y) {
        if (tileType == 0) {
            mesh = null;
        }
        else if (tileType == 1) {
            mesh = new Mesh(MeshType.SQUARE_TYPE, new float[] { x + 0.5f, y + 0.5f}, 0f, new float[] { 1f, 1f }, color);
        }
        else {
            float rotation = 90 * (tileType - 2);
            mesh = new Mesh(MeshType.RIGHT_TRIANGLE_TYPE, new float[] { x + 0.5f, y + 0.5f}, rotation, new float[] { 1f, 1f }, color);
        }
    }

    public Mesh getMesh() {
        return mesh;
    }
}
