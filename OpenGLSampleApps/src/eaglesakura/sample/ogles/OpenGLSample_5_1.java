package eaglesakura.sample.ogles;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import eaglesakura.android.mqo.MQOFormatImporter;
import eaglesakura.gles.Figure;
import eaglesakura.gles.GLManager;
import eaglesakura.gles.Object3D;

public class OpenGLSample_5_1 extends Activity {
    /**
     * 描画対象のView
     */
    private GLSurfaceView glSurfaceView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLConfigChooser(true);
        //! 深度バッファを要求する
        // ! setRenderせずに処理を継続した場合、Exceptionが投げられるため、
        // ! 必ず設定を行うこと。
        glSurfaceView.setRenderer(new MQRender());
        setContentView(glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    public void log(String msg) {
        Log.i("MQO", msg);
    }

    public class MQRender implements Renderer {
        private Object3D itokawa = new Object3D();
        private Object3D hayabusa = new Object3D();
        private Object3D minerva = new Object3D();

        private GLManager glManager = null;
        private float aspect = 0.0f;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
            gl.glEnable(GL10.GL_DEPTH_TEST);
            aspect = (float) width / (float) height;
            //! MQO読み込み
            try {
                glManager = new GLManager((GL11) gl, width, height);
                glManager.setContext(OpenGLSample_5_1.this);

                //! はやぶさ本体
                {
                    InputStream is = getAssets().open("hayabusa.mqo");

                    MQOFormatImporter imp = new MQOFormatImporter(is);

                    hayabusa.setGlManager(glManager);
                    hayabusa.setFigure(new Figure(glManager, imp.getConvertObject()));

                    is.close();
                }

                //! イトカワ本体
                {
                    InputStream is = getAssets().open("itokawa.mqo");

                    MQOFormatImporter imp = new MQOFormatImporter(is);

                    itokawa.setGlManager(glManager);
                    itokawa.setFigure(new Figure(glManager, imp.getConvertObject()));

                    is.close();
                }

                //! ミネルヴァ
                {
                    InputStream is = getAssets().open("minerva.mqo");

                    MQOFormatImporter imp = new MQOFormatImporter(is);

                    minerva.setGlManager(glManager);
                    minerva.setFigure(new Figure(glManager, imp.getConvertObject()));

                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int frame = 0;

        @Override
        public void onDrawFrame(GL10 gl) {
            if (++frame > 150)
                return;
            gl.glClearColor(0, 0.0f, 0.5f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 1.0f, 1000.0f);
                GLU.gluLookAt(gl, 250, 250, -500, 0, 0, 0, 0, 1, 0);
            }

            gl.glMatrixMode(GL10.GL_MODELVIEW);

            //! イトカワ描画
            {
                itokawa.draw();
            }

            //! はやぶさ描画
            {
                hayabusa.getPosition().y = 70.0f;
                hayabusa.getScale().set(5, 5, 5);
                hayabusa.getRotate().y += 1.0f;
                hayabusa.draw();
            }

            //! ミネルヴァ描画
            {
                minerva.getScale().set(0.5f, 0.5f, 0.5f);
                minerva.getRotate().y -= 2.0f;
                minerva.getPosition().set(0.0f, 50.0f, -150.0f);
                minerva.draw();
            }

        }
    }
}
