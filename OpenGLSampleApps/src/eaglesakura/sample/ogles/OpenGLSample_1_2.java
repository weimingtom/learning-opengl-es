package eaglesakura.sample.ogles;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class OpenGLSample_1_2 extends Activity {

    /**
     * 描画対象のView
     */
    private GLSurfaceView glSurfaceView = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);
        // ! setRenderせずに処理を継続した場合、Exceptionが投げられるため、
        // ! 必ず設定を行うこと。
        glSurfaceView.setRenderer(new GLRenderSample1());
        setContentView(glSurfaceView);
    }

    /**
     * Activity休止時の処理を行う。 この処理を行わない場合、正常に描画用スレッドが<BR>
     * 停止されない等の不具合が発生する。
     */
    @Override
    protected void onPause() {
        super.onPause();

        glSurfaceView.onPause();
    }

    /**
     * Activity復帰時の処理を行う。 この処理を行わない場合、描画用スレッドが再開されないため<BR>
     * 画面更新が出来なくなる。
     */
    @Override
    protected void onResume() {
        super.onResume();

        glSurfaceView.onResume();
    }

    /**
     * 描画用コールバック。
     */
    class GLRenderSample1 implements GLSurfaceView.Renderer {

        /**
         * サーフェイス作成時の処理。
         *
         * @param gl10
         * @param eglconfig
         */
        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglconfig) {
        }

        /**
         * サーフェイスが変更になった。
         *
         * @param gl10
         * @param w
         *            画面横の長さ（ピクセル）
         * @param h
         *            画面縦の長さ（ピクセル）
         */
        @Override
        public void onSurfaceChanged(GL10 gl10, int w, int h) {

        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {
        }
    }

    /**
     * 描画用コールバック。<BR>
     * 描画はUIスレッドとは別のスレッドで動作する。
     */
    class GLRenderSample2 implements GLSurfaceView.Renderer {

        /**
         * サーフェイス作成時の処理。
         *
         * @param gl10
         * @param eglconfig
         */
        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglconfig) {

        }

        /**
         * サーフェイスが変更になった。
         *
         * @param gl10
         * @param w
         *            画面横の長さ（ピクセル）
         * @param h
         *            画面縦の長さ（ピクセル）
         */
        @Override
        public void onSurfaceChanged(GL10 gl10, int w, int h) {
            gl10.glViewport(0, 0, w, h);
        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        }
    }
}
