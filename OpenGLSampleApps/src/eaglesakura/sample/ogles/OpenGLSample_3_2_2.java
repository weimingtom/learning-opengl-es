package eaglesakura.sample.ogles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;

/**
 * 9章 インデックスバッファの存在
 * @author SAKURA
 *
 */
public class OpenGLSample_3_2_2 extends Activity {
    /**
     * 描画対象のView
     */
    private GLSurfaceView glSurfaceView = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLConfigChooser(true);

        // ! setRenderせずに処理を継続した場合、Exceptionが投げられるため、
        // ! 必ず設定を行うこと。
        glSurfaceView.setRenderer(new GLRenderSample6());
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
     * TriangleStripのみで立方体を再現する
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
            gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 5.0f, 5.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            //! 頂点転送
            {
                final float one = 1.0f;

                final float[] vertices = new float[] {
                //!    x,  y,  z
                        one, one, one, //!<  0  左上手前
                        one, one, -one, //!<  1  左上奥
                        -one, one, one, //!<  2  右上手前
                        -one, one, -one, //!<  3  右上奥
                        one, -one, one, //!<  4  左下手前
                        one, -one, -one, //!<  5  左下奥
                        -one, -one, one, //!<  6  右下手前
                        -one, -one, -one, //!<  7  右下奥
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
            }

            {
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glRotatef(1.0f, 0, 1, 0);
            }

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] { 0, 1, 2, 3, 6, 7, 4, 5, 0, 1, };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                bb.position(0);
                //! IndexBufferの描画にはglDrawElementsを利用する
                gl.glColor4f(0, 1, 0, 1);
                gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, bb.capacity(), GL10.GL_UNSIGNED_BYTE, bb);
            }
            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] { 1, 5, 3, 7, };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                bb.position(0);
                //! IndexBufferの描画にはglDrawElementsを利用する
                gl.glColor4f(0, 0, 1, 1);
                gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, bb.capacity(), GL10.GL_UNSIGNED_BYTE, bb);
            }
            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] { 0, 2, 4, 6, };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                bb.position(0);
                //! IndexBufferの描画にはglDrawElementsを利用する
                gl.glColor4f(1, 0, 0, 1);
                gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, bb.capacity(), GL10.GL_UNSIGNED_BYTE, bb);
            }
        }
    }

    /**
     * TriangleStripのみで立方体を再現する
     * １バッファのみに抑える
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
            gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 5.0f, 5.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            //! 頂点転送
            {
                final float one = 1.0f;

                final float[] vertices = new float[] {
                //!    x,  y,  z
                        one, one, one, //!<  0  左上手前
                        one, one, -one, //!<  1  左上奥
                        -one, one, one, //!<  2  右上手前
                        -one, one, -one, //!<  3  右上奥
                        one, -one, one, //!<  4  左下手前
                        one, -one, -one, //!<  5  左下奥
                        -one, -one, one, //!<  6  右下手前
                        -one, -one, -one, //!<  7  右下奥
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
            }

            {
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glRotatef(1.0f, 0, 1, 0);
            }

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] {
                //!
                        //! 1回目の描画
                        0, 1, 2, 3, 6, 7, 4, 5, 0, 1,
                        //! 2回目の描画
                        1, 5, 3, 7,
                        //! 3回目の描画
                        0, 2, 4, 6, };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);

                //! １回目の描画
                gl.glColor4f(0, 1, 0, 1);
                bb.position(0);
                gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 10, GL10.GL_UNSIGNED_BYTE, bb);

                //! ２回目の描画
                gl.glColor4f(0, 0, 1, 1);
                bb.position(10);
                gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, bb);

                //! ３回目の描画
                gl.glColor4f(1, 0, 0, 1);
                bb.position(14);
                gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, bb);
            }
        }
    }

    /**
     * TriangleStripのみで立方体を再現する
     * １回の描画で済ませる
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
            gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 5.0f, 5.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            //! 頂点転送
            {
                final float one = 1.0f;

                final float[] vertices = new float[] {
                //!    x,  y,  z
                        one, one, one, //!<  0  左上手前
                        one, one, -one, //!<  1  左上奥
                        -one, one, one, //!<  2  右上手前
                        -one, one, -one, //!<  3  右上奥
                        one, -one, one, //!<  4  左下手前
                        one, -one, -one, //!<  5  左下奥
                        -one, -one, one, //!<  6  右下手前
                        -one, -one, -one, //!<  7  右下奥
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
            }

            {
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glRotatef(1.0f, 0, 1, 0);
            }

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] {
                //!
                        0, 1, 2, //
                        2, 1, 3, //
                        2, 3, 6, //
                        6, 3, 7, //
                        6, 7, 4, //
                        4, 7, 5, //
                        4, 5, 0, //
                        0, 5, 1, //
                        1, 5, 3, //
                        3, 5, 7, //
                        0, 2, 4, //
                        4, 2, 6, //
                };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);

                //! １回目の描画
                gl.glColor4f(1, 1, 0, 1);
                bb.position(0);
                gl.glDrawElements(GL10.GL_TRIANGLES, bb.capacity(), GL10.GL_UNSIGNED_BYTE, bb);
            }
        }
    }

    /**
     * VBOで立方体を見せる。
     */
    class GLRenderSample4 implements Renderer {
        float aspect = 0.0f;
        int vertices = 0;
        int indices = 0;
        int indicesLength = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            aspect = (float) width / (float) height;
            gl.glViewport(0, 0, width, height);

            //! バッファを生成
            GL11 gl11 = (GL11) gl;
            {
                int[] buffer = new int[2];
                gl11.glGenBuffers(2, buffer, 0);

                vertices = buffer[0];
                indices = buffer[1];
            }

            //! 頂点バッファを作成する。
            //! 頂点転送
            {
                final float one = 1.0f;

                final float[] vertices = new float[] {
                //!    x,  y,  z
                        one, one, one, //!<  0  左上手前
                        one, one, -one, //!<  1  左上奥
                        -one, one, one, //!<  2  右上手前
                        -one, one, -one, //!<  3  右上奥
                        one, -one, one, //!<  4  左下手前
                        one, -one, -one, //!<  5  左下奥
                        -one, -one, one, //!<  6  右下手前
                        -one, -one, -one, //!<  7  右下奥
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, this.vertices);
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, fb.capacity() * 4, fb, GL11.GL_STATIC_DRAW);

                //! 頂点バッファの関連付け
                gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
            }

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] {
                //
                        0, 1, 2, //
                        2, 1, 3, //
                        2, 3, 6, //
                        6, 3, 7, //
                        6, 7, 4, //
                        4, 7, 5, //
                        4, 5, 0, //
                        0, 5, 1, //
                        1, 5, 3, //
                        3, 5, 7, //
                        0, 2, 4, //
                        4, 2, 6, //
                };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                indicesLength = bb.capacity();
                bb.position(0);

                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, this.indices);
                gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, bb.capacity(), bb, GL11.GL_STATIC_DRAW);
            }
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 5.0f, 5.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            {
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glRotatef(1.0f, 0, 1, 0);
            }

            GL11 gl11 = (GL11) gl;
            //! 頂点バッファの関連付け
            gl11.glDrawElements(GL10.GL_TRIANGLES, indicesLength, GL10.GL_UNSIGNED_BYTE, 0);
        }
    }

    /**
     * 作成した立体を複数描画する。
     * 手前と奥が無視されている。
     */
    class GLRenderSample5 implements Renderer {
        float aspect = 0.0f;
        int vertices = 0;
        int indices = 0;
        int indicesLength = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            aspect = (float) width / (float) height;
            gl.glViewport(0, 0, width, height);

            //! バッファを生成
            GL11 gl11 = (GL11) gl;
            {
                int[] buffer = new int[2];
                gl11.glGenBuffers(2, buffer, 0);

                vertices = buffer[0];
                indices = buffer[1];
            }

            //! 頂点バッファを作成する。
            //! 頂点転送
            {
                final float one = 1.0f;

                final float[] vertices = new float[] {
                //!    x,  y,  z
                        one, one, one, //!<  0  左上手前
                        one, one, -one, //!<  1  左上奥
                        -one, one, one, //!<  2  右上手前
                        -one, one, -one, //!<  3  右上奥
                        one, -one, one, //!<  4  左下手前
                        one, -one, -one, //!<  5  左下奥
                        -one, -one, one, //!<  6  右下手前
                        -one, -one, -one, //!<  7  右下奥
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, this.vertices);
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, fb.capacity() * 4, fb, GL11.GL_STATIC_DRAW);

                //! 頂点バッファの関連付け
                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
            }

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] { 0, 1, 2, 2, 1, 3, 2, 3, 6, 6, 3, 7, 6, 7, 4, 4, 7, 5, 4, 5, 0, 0, 5, 1,

                1, 5, 3, 3, 5, 7,

                0, 2, 4, 4, 2, 6, };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                indicesLength = bb.capacity();

                bb.position(0);

                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, this.indices);
                gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, bb.capacity(), bb, GL11.GL_STATIC_DRAW);
            }
        }

        class Object3D {
            /**
             * X位置
             */
            public float posX = 0.0f;

            /**
             * Y位置
             */
            public float posY = 0.0f;

            /**
             * Z位置
             */
            public float posZ = 0.0f;

            /**
             * 回転（３６０度系）
             */
            public float rotateY = 0.0f;

            /**
             * 拡大率
             */
            public float scale = 1.0f;

            /**
             * 赤成分
             */
            public float colR = 1.0f;
            /**
             * 緑成分
             */
            public float colG = 1.0f;

            /**
             * 青成分
             */
            public float colB = 1.0f;

            /**
             * α成分
             */
            public float colA = 1.0f;

            public void drawBox(GL10 gl) {
                //! 行列をデフォルト状態へ戻す。
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();

                //! 指定行列を生成
                gl.glTranslatef(posX, posY, posZ);
                gl.glRotatef(rotateY, 0, 1, 0);
                gl.glScalef(scale, scale, scale);

                //! いろ指定
                gl.glColor4f(colR, colG, colB, colA);

                GL11 gl11 = (GL11) gl;
                //! 頂点バッファの関連付け
                gl11.glDrawElements(GL10.GL_TRIANGLES, indicesLength, GL10.GL_UNSIGNED_BYTE, 0);
            }
        }

        Object3D box0 = new Object3D();
        Object3D box1 = new Object3D();

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 5.0f, 5.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            gl.glMatrixMode(GL10.GL_MODELVIEW);

            {
                box0.posX = -0.5f;
                box0.rotateY += 1.0f;
                box0.colR = 1.0f;
                box0.colG = 0.0f;
                box0.colB = 1.0f;

                box0.drawBox(gl);
            }
            {
                box1.posX = 0.5f;
                box1.rotateY -= 0.5f;
                box1.colR = 0.0f;
                box1.colG = 0.0f;
                box1.colB = 1.0f;

                box1.drawBox(gl);
            }
        }
    }

    /**
     * 作成した立体を複数描画する。
     * 手前と奥を正確に行う。
     */
    class GLRenderSample6 implements Renderer {
        float aspect = 0.0f;
        int vertices = 0;
        int indices = 0;
        int indicesLength = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            aspect = (float) width / (float) height;
            gl.glViewport(0, 0, width, height);

            //! 深度バッファを有効にする
            gl.glEnable(GL10.GL_DEPTH_TEST);

            //! バッファを生成
            GL11 gl11 = (GL11) gl;
            {
                int[] buffer = new int[2];
                gl11.glGenBuffers(2, buffer, 0);

                vertices = buffer[0];
                indices = buffer[1];
            }

            //! 頂点バッファを作成する。
            //! 頂点転送
            {
                final float one = 1.0f;

                final float[] vertices = new float[] {
                //!    x,  y,  z
                        one, one, one, //!<  0  左上手前
                        one, one, -one, //!<  1  左上奥
                        -one, one, one, //!<  2  右上手前
                        -one, one, -one, //!<  3  右上奥
                        one, -one, one, //!<  4  左下手前
                        one, -one, -one, //!<  5  左下奥
                        -one, -one, one, //!<  6  右下手前
                        -one, -one, -one, //!<  7  右下奥
                };

                FloatBuffer fb = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                fb.put(vertices);
                fb.position(0);

                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, this.vertices);
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, fb.capacity() * 4, fb, GL11.GL_STATIC_DRAW);

                //! 頂点バッファの関連付け
                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
            }

            //! インデックスバッファ生成
            {
                final byte[] indices = new byte[] {
                //!
                        0, 1, 2, //
                        2, 1, 3, //
                        2, 3, 6, //
                        6, 3, 7, //
                        6, 7, 4, //
                        4, 7, 5, //
                        4, 5, 0, //
                        0, 5, 1, //
                        1, 5, 3, //
                        3, 5, 7, //
                        0, 2, 4, //
                        4, 2, 6, //
                };
                ByteBuffer bb = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
                bb.put(indices);
                indicesLength = bb.capacity();

                bb.position(0);

                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, this.indices);
                gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, bb.capacity(), bb, GL11.GL_STATIC_DRAW);
            }
        }

        class Object3D {
            /**
             * X位置
             */
            public float posX = 0.0f;

            /**
             * Y位置
             */
            public float posY = 0.0f;

            /**
             * Z位置
             */
            public float posZ = 0.0f;

            /**
             * 回転（３６０度系）
             */
            public float rotateY = 0.0f;

            /**
             * 拡大率
             */
            public float scale = 1.0f;

            /**
             * 赤成分
             */
            public float colR = 1.0f;
            /**
             * 緑成分
             */
            public float colG = 1.0f;

            /**
             * 青成分
             */
            public float colB = 1.0f;

            /**
             * α成分
             */
            public float colA = 1.0f;

            public void drawBox(GL10 gl) {
                //! 行列をデフォルト状態へ戻す。
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();

                //! 指定行列を生成
                gl.glTranslatef(posX, posY, posZ);
                gl.glRotatef(rotateY, 0, 1, 0);
                gl.glScalef(scale, scale, scale);

                //! いろ指定
                gl.glColor4f(colR, colG, colB, colA);

                GL11 gl11 = (GL11) gl;
                //! 頂点バッファの関連付け
                gl11.glDrawElements(GL10.GL_TRIANGLES, indicesLength, GL10.GL_UNSIGNED_BYTE, 0);
            }
        }

        Object3D box0 = new Object3D();
        Object3D box1 = new Object3D();

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            //! カメラ転送
            {
                //! 呼び出さなかった場合の動作を考える
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();

                //! 転送順番に注意する
                GLU.gluPerspective(gl, 45.0f, aspect, 0.01f, 100.0f);
                GLU.gluLookAt(gl, 0, 5.0f, 5.0f, 0, 0, 0.0f, 0.0f, 1.0f, 0.0f);
            }

            gl.glMatrixMode(GL10.GL_MODELVIEW);

            {
                box0.posX = -0.5f;
                box0.rotateY += 1.0f;
                box0.colR = 1.0f;
                box0.colG = 0.0f;
                box0.colB = 1.0f;

                box0.drawBox(gl);
            }
            {
                box1.posX = 0.5f;
                box1.rotateY -= 0.5f;
                box1.colR = 0.0f;
                box1.colG = 0.0f;
                box1.colB = 1.0f;

                box1.drawBox(gl);
            }
        }
    }
}
