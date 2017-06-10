package bros.taprun.game;

import bros.taprun.graphics.mesh.MeshPlacement;
import bros.taprun.graphics.mesh.Mesh;

public class Tile {

    private MeshPlacement mesh;

    private static float[] color = new float[] {0.9f, .9f, 0.9f, 1f};

    public Tile(int column, int row) {

        float yOffset = 1f / (float) Math.sqrt(3);
        float rotation = ((column + row) % 2) * 180;

        mesh = new MeshPlacement(Mesh.TILE_TYPE, new float[] {column + 0.5f, row * yOffset},
                    rotation, new float[] { 1f, 1f }, color);

    }

    public MeshPlacement getMesh() {
        return mesh;
    }
}
