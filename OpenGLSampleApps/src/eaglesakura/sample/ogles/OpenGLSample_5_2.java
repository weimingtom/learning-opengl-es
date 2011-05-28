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
import eaglesakura.gles.Mesh;
import eaglesakura.gles.Object3D;

public class OpenGLSample_5_2 extends Activity {
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
        private Object3D girl = new Object3D();
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
                glManager.setContext(OpenGLSample_5_2.this);

                //! 人物本体
                {
                    InputStream is = getAssets().open("girl.mqo");

                    MQOFormatImporter imp = new MQOFormatImporter(is);

                    Figure figure = new Figure(glManager, imp.getConvertObject());

                    for (int i = 0; i < figure.getMeshCount(); ++i) {
                        Mesh mesh = figure.getMesh(i);
                        mesh.getVertexBuffer().setColorEnable(false);
                    }

                    girl.setGlManager(glManager);
                    girl.setFigure(figure);

                    is.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClearColor(1, 1, 1, 1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            //! カメラ設定
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 30.0f, 500.0f);
                GLU.gluLookAt(gl, 0, 120.0f, 150.0f, 0, 120.0f, 0, 0, 1, 0);
            }

            gl.glMatrixMode(GL10.GL_MODELVIEW);

            {
                girl.getRotate().y += 1;

                //! 本体を描画する
                gl.glColor4f(1, 1, 1, 1); //!<    テクスチャの色をそのまま反映させるため白を指定
                girl.draw(); //!<    本体を普通に描画

                //! セルエッジを描画する
                gl.glColor4f(0, 0, 0, 1); //!<    セルエッジの陰の色を指定
                girl.drawEdge(); //!<    エッジを描画
            }
        }

    };
}
