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

public class OpenGLSample_3_1 extends Activity {
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
        glSurfaceView.setRenderer(new GLRenderSample7());
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
     * 今まで通り三角形を描画する。
     */
    class GLRenderSample1 implements Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
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
                        0.0f, 1.0f, 0.0f, //
                        0.0f, -1.0f, 0.0f, //
                        1.0f, -1.0f, 0.0f, //
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
            }

            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
        }
    }

    /**
     * 遠くから三角形を見つめる。
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
                //! x       y       z
                        0.0f, 1.0f, 0.0f, //
                        0.0f, -1.0f, 0.0f, //
                        1.0f, -1.0f, 0.0f, //
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

            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
        }
    }

    //! カメラ位置が反映されていることを確認
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
                GLU.gluLookAt(gl, 0, 0, -10.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
        }
    }

    //! クリップ領域を意識する
    class GLRenderSample4 implements Renderer {
        float aspect = 0.0f;
        float zPosition = -10.0f;

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
                GLU.gluPerspective(gl, 45.0f, aspect, 2.0f, 15.0f);
                GLU.gluLookAt(gl, 0, 0, zPosition, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);

                //! 標的に近づける
                zPosition += 0.01f;
            }

            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
        }
    }

    //! クリップ領域を意識する
    class GLRenderSample5 implements Renderer {
        float aspect = 0.0f;
        float zPosition = -10.0f;

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
                GLU.gluPerspective(gl, 45.0f, aspect, 2.0f, 15.0f);
                GLU.gluLookAt(gl, zPosition / 2, -zPosition, zPosition, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);

                //! 標的に近づける
                zPosition += 0.01f;
            }

            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
        }
    }

    //! 画角を意識する
    class GLRenderSample6 implements Renderer {
        float aspect = 0.0f;
        float fov = 45.0f;

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
                GLU.gluPerspective(gl, fov, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 0, -10.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);

                fov -= 0.1f;
            }

            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
        }
    }

    //! 上方向ベクトルを意識する
    class GLRenderSample7 implements Renderer {
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
                GLU.gluLookAt(gl, 0, 0, -10.0f, 0, 0, 0.0f, 1.0f, 1.0f, 0.0f);
            }

            //! 描画
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
        }
    }
}
