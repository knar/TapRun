package utils;

public class Utils {
    public static float lerp(float a, float b, float delta) {
        return (a * (1 - delta)) + (b * delta);
    }

    public static float[] lerp4(float[] a, float[] b, float delta) {
        float[] c = new float[4];

        for (int i = 0; i < 4; i++) {
            c[i] = lerp(a[i], b[i], delta);
        }

        return c;
    }

    public static int floatsToColor(float[] color) {
        int result = 0;
        for (int i = 3; i < 7; i++) { // index 3, 0, 1, 2
            result <<= 8;
            result += (short)Math.floor(color[i % 4] * 255);
        }
        return result;
    }

    public static float[] changeAlpha(float[] color, float alpha) {
        return new float[] {color[0], color[1], color[2], alpha};
    }

    public static int randInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min)) + min;
    }

    public static int bytesToInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result <<= 8;
            result += (int) bytes[i] & 0xFF;
        }
        return result;
    }

    public static float[] intColorToFloats(int r, int g, int b, int a) {
        return new float[] {r / 255f, g / 255f, b / 255f, a / 255f};
    }
}
