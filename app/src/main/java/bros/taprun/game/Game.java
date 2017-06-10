package bros.taprun.game;

import android.app.Activity;
import android.util.Log;
import android.view.SurfaceView;

import bros.taprun.graphics.TapRunSurfaceView;
import bros.taprun.graphics.mesh.Mesh;
import bros.taprun.graphics.mesh.MeshPlacement;
import bros.taprun.graphics.mesh.MeshRenderer;
import utils.Utils;

public class Game implements Runnable{

    // Colors
    private float[] colorBackground = Utils.intColorToFloats(106, 179, 68, 255);
    private float[] colorPlayer = {0f, 0.5f, 1f, 1f};

    // Game Loop
    private final int msPerUpdate = 100;
    private long previousUpdate = System.currentTimeMillis();
    private long lag = 0;

    // Progress
    private float speed = 0.04f;
    private float distance = 0;

    private MeshPlacement playerMesh;

    // The grid
    private int width = 4;
    private int height = width * 4;

    private Tile[] tiles = new Tile[width * height];

    // generation state
    private int topRow = 0;
    private int pathX = width / 2;
    private int pathDir = 0;
    private int pathPartRem = 10;

    // The renderer
    private SurfaceView surfaceView;
    private MeshRenderer renderer;

    public Game(Activity context) {
        renderer = new MeshRenderer(context, this);
        surfaceView = new TapRunSurfaceView(context, renderer);

        context.setContentView(surfaceView);

        for (int i = 0; i < height; i++) {
            generateNextRow();
        }

        renderer.setViewWidth(width);
        renderer.setClearColor(colorBackground);

        playerMesh = new MeshPlacement(
                Mesh.CIRCLE_TYPE,
                new float[]{width / 2, 2f / (float) Math.sqrt(3), 0f}, 0f,
                new float[]{0.5f, 0.5f},
                colorPlayer
        );

        renderer.addMesh(playerMesh);
    }

    // Called by the renderer, happens right before each render
    public void run() {
        update();
        if (true)
        return;

        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - previousUpdate;
        previousUpdate = currentTime;
        lag += elapsed;

        processInput();

        while (lag >= msPerUpdate)
        {
            update();
            lag -= msPerUpdate;
        }
    }

    private void processInput() {
        //TODO: process input
    }

    private void update() {
        distance += speed;

        if (distance > (float) (topRow - height + 1) / Math.sqrt(3)) {
            generateNextRow();
        }

        renderer.updateViewMatrix(distance);
        playerMesh.setPosition(new float[] {width / 2 + 0.5f, distance + 2f / (float) Math.sqrt(3), 1f});
    }

    private void generateNextRow() {
        for (int c = 0; c < width; c++) {
            removeTile(c, topRow);
        }

        if (pathPartRem == 0) {
            if (pathDir != 0) {
                pathDir = 0;
                if (pathX == 0) {
                    pathPartRem = Utils.randInt(1, 3) * 2 + (pathX + topRow) % 2 + 1;
                } else if (pathX == width - 1) {
                    pathPartRem = Utils.randInt(2, 4) * 2 + (pathX + topRow) % 2;
                } else {
                    pathPartRem = Utils.randInt(3, 6);
                }
            } else {
                if ((pathX + topRow) % 2 == 1) { // right
                    pathDir = 1;
                    pathPartRem = Utils.randInt(1, width - pathX - 1);
                } else { // left
                    pathDir = -1;
                    pathPartRem = Utils.randInt(1, pathX);
                }
            }
        }
        if (pathDir == 0) {
            addTile(pathX, topRow);
        } else if (pathDir == -1) {
            addTile(pathX, topRow);
            addTile(pathX - 1, topRow);
            pathX--;
        } else {
            addTile(pathX, topRow);
            addTile(pathX + 1, topRow);
            pathX++;
        }
        pathPartRem--;

        topRow++;
    }

    private void addTile(int x, int y) {
        Tile tile = new Tile(x, y);
        tiles[x + (y % height) * width] = tile;
        renderer.addMesh(tile.getMesh());
    }

    private void removeTile(int x, int y) {
        int index = x + (y % height) * width;
        if (tiles[index] != null) {
            renderer.removeMesh(tiles[index].getMesh());
            tiles[index] = null;
        }
    }

}
