package bros.taprun.graphics.mesh;

/**
 * Created by Jonah on 6/5/2017.
 */

public class PolygonMesh extends Mesh {

    public PolygonMesh(int nSides) {

        float radius = 0.5f;
        float interiorAngle = 2 * (float) Math.PI / nSides;

        float[] coords = new float[3 * nSides];
        short[] drawOrder = new short[3 * (nSides - 2)];

        for (int i = 0; i < nSides; i++) {
            coords[i * 3 + 0] = (float) Math.cos(i * interiorAngle) * radius;
            coords[i * 3 + 1] = (float) Math.sin(i * interiorAngle) * radius;
            coords[i * 3 + 2] = 0f;
        }

        for (int i = 0; i < nSides - 2; i++) {
            drawOrder[i * 3 + 0] = 0;
            drawOrder[i * 3 + 1] = (short) (i + 1);
            drawOrder[i * 3 + 2] = (short) (i + 2);
        }

        init(coords, drawOrder);

    }

}
