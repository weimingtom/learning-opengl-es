package eaglesakura.sample.ogles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;

public class OpenGLSample_3_2 extends Activity {
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
        glSurfaceView.setRenderer(new GLRenderSample4());
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
     * indexバッファを作成する
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

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] { 0, 1, 2, };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                bb.position(0);

                //! IndexBufferの描画にはglDrawElementsを利用する
                gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 3, GL10.GL_UNSIGNED_BYTE, bb);
            }
        }
    }

    /**
     * indexバッファを作成する（２）
     * ２枚目以降の対応
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

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] { 0, 1, 2, 3 };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                bb.position(0);
                //! IndexBufferの描画にはglDrawElementsを利用する
                gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, bb);
            }
        }
    }

    /**
     * indexバッファを作成する（TRIANGLES）
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
                //! x       y       z
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

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] {
                //
                        0, 1, 2, // ポリゴン1
                        1, 2, 3, // ポリゴン2
                };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                bb.position(0);
                //! IndexBufferの描画にはglDrawElementsを利用する
                gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, bb);
            }
        }
    }

    /**
     * indexバッファを作成する（３）
     * 頂点の順番を入れ替える
     */
    class GLRenderSample4 implements Renderer {
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
                        0.0f, 1.0f, 0.0f, // !< 左上
                        1.0f, 1.0f, 0.0f, // !< 右上
                        0.0f, 0.0f, 0.0f, // !< 左下
                        1.0f, 0.0f, 0.0f, // !< 右下
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
            }

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] { 0, 2, 1, 3, };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                bb.position(0);
                //! IndexBufferの描画にはglDrawElementsを利用する
                gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, bb);
            }
        }
    }
}
