package bros.taprun.graphics.mesh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Mesh {

    public static final int COORDS_PER_VERTEX = 3;

    private static final float squareCoords[] = {
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
    };
    private static final short squareDrawOrder[] = { 0, 1, 2, 0, 2, 3 };

    private static final float rightTriangleCoords[] = {
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };
    private static final short rightTriangleDrawOrder[] = { 0, 1, 2 };

    private static final float tileCoords[] = {
            0.5f, 0f, 0f,
            -0.5f, 1f / (float) Math.sqrt(3), 0f,
            -0.5f, -1f / (float) Math.sqrt(3), 0f
    };
    private static final short tileDrawOrder[] = { 0, 1, 2 };

    private static final int FLOAT_SIZE = 4;

    public static final Mesh SQUARE_TYPE = new Mesh(Mesh.squareCoords, Mesh.squareDrawOrder);
    public static final Mesh RIGHT_TRIANGLE_TYPE = new Mesh(Mesh.rightTriangleCoords, Mesh.rightTriangleDrawOrder);
    public static final Mesh TRIANGLE_TYPE = new PolygonMesh(3);
    public static final Mesh TILE_TYPE = new Mesh(Mesh.tileCoords, Mesh.tileDrawOrder);
    public static final Mesh CIRCLE_TYPE = new PolygonMesh(12);

    private float[] coords;
    private short[] drawOrder;

    private int vertexCount;
    private int vertexStride;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    public Mesh() { }

    public Mesh(float[] coords, short[] drawOrder) {
        init(coords, drawOrder);
    }

    public void init(float[] coords, short[] drawOrder) {
        this.coords = coords;
        this.drawOrder = drawOrder;

        vertexCount = coords.length / COORDS_PER_VERTEX;
        vertexStride = COORDS_PER_VERTEX * FLOAT_SIZE;

        createBuffers();
    }

    private void createBuffers() {
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getVertexStride() {
        return vertexStride;
    }

    public FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public ShortBuffer getDrawListBuffer() {
        return drawListBuffer;
    }

    public int getDrawOrderLength() {
        return drawOrder.length;
    }

}
