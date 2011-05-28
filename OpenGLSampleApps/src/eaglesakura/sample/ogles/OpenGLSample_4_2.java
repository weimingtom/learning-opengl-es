package eaglesakura.sample.ogles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;

public class OpenGLSample_4_2 extends Activity {
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
     * カリング処理
     */
    class GLRenderSample1 implements Renderer {
        float aspect = 0.0f;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            aspect = (float) width / (float) height;
            gl.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            //! 頂点転送
            {
                final float[] vertices = new float[] {
                //! x       y       z
                        0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
            }

            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 0, 10.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            //! ポリゴンのカリングを指定する
            {
                gl.glEnable(GL10.GL_CULL_FACE); //!<    カリングをON
                gl.glCullFace(GL10.GL_BACK); //!<    裏側ポリゴンを描画しない
            }

            //! ポリゴンを回転させる
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glRotatef(1, 0, 1, 0);
            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
        }
    }

    /**
     * ４頂点を利用する場合
     * 空気を読んでくれる
     */
    class GLRenderSample2 implements Renderer {
        float aspect = 0.0f;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            aspect = (float) width / (float) height;
            gl.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            //! 頂点転送
            {
                final float[] vertices = new float[] {
                // ! x y z
                        0.0f, 1.0f, 0.0f, // !< 左上
                        0.0f, 0.0f, 0.0f, // !< 左下
                        1.0f, 1.0f, 0.0f, // !< 右上
                        1.0f, 0.0f, 0.0f, // !< 右下
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
            }

            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 0, 10.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            //! ポリゴンのカリングを指定する
            {
                gl.glEnable(GL10.GL_CULL_FACE); //!<    カリングをON
                gl.glCullFace(GL10.GL_BACK); //!<    裏側ポリゴンを描画しない
            }

            //! ポリゴンを回転させる
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glRotatef(1, 0, 1, 0);

            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    /**
     * ポリゴンの裏と表を反転する。
     */
    class GLRenderSample3 implements Renderer {
        float aspect = 0.0f;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            aspect = (float) width / (float) height;
            gl.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            //! 頂点転送
            {
                final float[] vertices = new float[] {
                // ! x y z
                        0.0f, 1.0f, 0.0f, // !< 左上
                        0.0f, 0.0f, 0.0f, // !< 左下
                        1.0f, 1.0f, 0.0f, // !< 右上
                        1.0f, 0.0f, 0.0f, // !< 右下
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
            }

            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 0, 10.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            //! ポリゴンのカリングを指定する
            {
                gl.glEnable(GL10.GL_CULL_FACE); //!<    カリングをON
                gl.glCullFace(GL10.GL_BACK); //!<    裏側ポリゴンを描画しない
                //    gl.glCullFace( GL10.GL_FRONT );       //!<    表側ポリゴンを描画しない

                //! 反時計回りを表にする
                gl.glFrontFace(GL10.GL_CCW);

                //! 時計回りを表にする
                gl.glFrontFace(GL10.GL_CW);
            }

            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }
    }
}
