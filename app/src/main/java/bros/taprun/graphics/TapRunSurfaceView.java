package bros.taprun.graphics;

import android.app.Activity;
import android.opengl.GLSurfaceView;

import bros.taprun.graphics.mesh.MeshRenderer;

public class TapRunSurfaceView extends GLSurfaceView {

    private GLSurfaceView.Renderer renderer;

    public TapRunSurfaceView(Activity activity) {
        super(activity);

        setEGLContextClientVersion(2);

        renderer = new MeshRenderer();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

}
