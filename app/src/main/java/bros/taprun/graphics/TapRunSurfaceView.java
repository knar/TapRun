package bros.taprun.graphics;

import android.app.Activity;
import android.opengl.GLSurfaceView;

import bros.taprun.graphics.mesh.MeshRenderer;

public class TapRunSurfaceView extends GLSurfaceView {

    public TapRunSurfaceView(Activity activity, GLSurfaceView.Renderer renderer) {
        super(activity);

        setEGLContextClientVersion(2);

        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

}
