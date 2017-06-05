package bros.taprun.graphics.mesh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshType {

    public static final int COORDS_PER_VERTEX = 3;

    private static final float squareCoords[] = {
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
    };
    private static final short squareDrawOrder[] = { 0, 1, 2, 0, 2, 3 };

    private static final float kiteCoords[] = {
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.1f, 0.1f, 0.0f
    };
    private static final short kiteDrawOrder[] = { 0, 1, 2, 0, 2, 3 };



    private static final float rightTriangleCoords[] = {
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };
    private static final short rightTriangleDrawOrder[] = { 0, 1, 2 };

    private static final int FLOAT_SIZE = 4;

    public static final MeshType SQUARE_TYPE = new MeshType(MeshType.squareCoords, MeshType.squareDrawOrder);
    public static final MeshType KITE_TYPE = new MeshType(MeshType.kiteCoords, MeshType.kiteDrawOrder);
    public static final MeshType RIGHT_TRIANGLE_TYPE = new MeshType(MeshType.rightTriangleCoords, MeshType.rightTriangleDrawOrder);

    private float[] coords;
    private short[] drawOrder;

    private int vertexCount;
    private int vertexStride;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    public MeshType(float[] coords, short[] drawOrder) {
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
