package bros.taprun.game;

import android.opengl.GLSurfaceView;

import bros.taprun.graphics.mesh.MeshRenderer;
import bros.taprun.graphics.mesh.MeshType;
import utils.Utils;

public class Grid {
    private final short width = 13;
    private final short height = 26;

    private Tile[] tiles = new Tile[width * height];

    private short bottomRow;

    public Grid() {

        int minWidth = 2;
        int maxWidth = 6;

        int left = width / 2;
        int right = left + minWidth;
        int lastLeft = left;
        int lastRight = right;

        for (int r = 0; r < height; r++) {

            for (int c = 0; c < left - 1; c++)
                tiles[r * width + c] = new Tile(0, c, r);

            if (left > lastLeft) { // left got moved to the right
                tiles[r * width + left - 1] = new Tile(3, left - 1, r);
                tiles[r * width + left] = new Tile(1, left, r);
            } else if (left < lastLeft) { // left got moved to the left
                tiles[r * width + left - 1] = new Tile(0, left - 1, r);
                tiles[r * width + left] = new Tile(4, left, r);
            } else {
                tiles[r * width + left - 1] = new Tile(0, left - 1, r);
                tiles[r * width + left] = new Tile(1, left, r);
            }

            for (int c = left + 1; c < right; c++)
                tiles[r * width + c] = new Tile(1, c, r);

            if (right > lastRight) { // right got moved to the right
                tiles[r * width + right] = new Tile(5, right, r);
                tiles[r * width + right + 1] = new Tile(0, right + 1, r);
            }
            else if (right < lastRight) { // right got moved to the left
                tiles[r * width + right] = new Tile(1, right, r);
                tiles[r * width + right + 1] = new Tile(2, right + 1, r);
            }
            else {
                tiles[r * width + right] = new Tile(1, right, r);
                tiles[r * width + right + 1] = new Tile(0, right + 1, r);
            }

            for (int c = right + 2; c < width; c++)
                tiles[r * width + c] = new Tile(0, c, r);

            lastLeft = left;
            lastRight = right;



            int rand = Utils.randInt(0, 5);

            if (rand == 1 && right < width - 2) { // move left
                left++;
                right++;
            }
            else if (rand == 2 && left > 1) { // move right
                left--;
                right--;
            }
            else if (rand == 3 && right - left > minWidth) { // shrink
                left++;
                right--;
            }
            else if (rand == 4 && right - left < maxWidth && left > 1 && right < width - 2) { // grow
                left--;
                right++;
            }

        }

        bottomRow = 0;
    }

    public void addMeshes(MeshRenderer renderer) {
        for (Tile tile : tiles) {
            if (tile.getMesh() != null)
                renderer.addMesh(tile.getMesh());
        }
    }

    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }



}
