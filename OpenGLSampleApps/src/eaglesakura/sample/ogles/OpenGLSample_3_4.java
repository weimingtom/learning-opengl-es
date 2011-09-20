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
 * 10章 本格的に3Dを扱う
 * @author SAKURA
 *
 */
public class OpenGLSample_3_4 extends Activity {
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
        //! 深度バッファを要求する
        // ! setRenderせずに処理を継続した場合、Exceptionが投げられるため、
        // ! 必ず設定を行うこと。
        glSurfaceView.setRenderer(new GLRenderSample1());

        setContentView(glSurfaceView);
    }

    public int getWindowWidth() {
        return glSurfaceView.getWidth();
    }

    public int getWindowHeight() {
        return glSurfaceView.getHeight();
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
     * 作成した立体を複数描画する。
     * 手前と奥を正確に行う。
     */
    class GLRenderSample1 implements Renderer {
        float aspect = 0.0f;
        int vertices = 0;
        int indices = 0;
        int indicesLength = 0;

        int spriteVertices = 0;

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
                int[] buffer = new int[3];
                gl11.glGenBuffers(3, buffer, 0);

                vertices = buffer[0];
                indices = buffer[1];
                spriteVertices = buffer[2];
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

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
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

            //! スプライト用ポリゴンを生成
            {
                // ! 事前に転送済みにしておく
                float positions[] = {
                //! x      y
                        -0.5f, 0.5f, // !< 左上
                        -0.5f, -0.5f, // !< 左下
                        0.5f, 0.5f, // !< 右上
                        0.5f, -0.5f, // !< 右下
                };

                // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
                // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
                ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer posBuffer = bb.asFloatBuffer();
                posBuffer.put(positions);
                posBuffer.position(0);

                //! 頂点オブジェクト作成
                {
                    gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, spriteVertices);
                    gl11.glBufferData(GL11.GL_ARRAY_BUFFER, posBuffer.capacity() * 4, posBuffer, GL11.GL_STATIC_DRAW);
                }

            }

            // ! アルファブレンドを有効にする
            {
                gl.glEnable(GL10.GL_BLEND);
                gl.glEnable(GL10.GL_ALPHA);
                gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
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
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertices);
                gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
                gl11.glDrawElements(GL10.GL_TRIANGLES, indicesLength, GL10.GL_UNSIGNED_BYTE, 0);
            }
        }

        Object3D box0 = new Object3D();
        Object3D box1 = new Object3D();

        /**
         * 四角形を描画する。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        public void drawQuad(GL10 gl10, int x, int y, int w, int h) {
            // ! 描画位置を行列で操作する
            float sizeX = (float) w / (float) getWindowWidth() * 2;
            float sizeY = (float) h / (float) getWindowHeight() * 2;
            float sx = (float) x / (float) getWindowWidth() * 2;
            float sy = (float) y / (float) getWindowHeight() * 2;

            gl10.glLoadIdentity();
            gl10.glTranslatef(-1.0f + sizeX / 2.0f + sx, 1.0f - sizeY / 2.0f - sy, 0.0f);
            gl10.glScalef(sizeX, sizeY, 1.0f);
            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }

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
                box0.rotateY = 10.0f;
                box0.colR = 1.0f;
                box0.colG = 0.0f;
                box0.colB = 1.0f;

                box0.drawBox(gl);
            }
            {
                box1.posX = 0.5f;

                //! 同座標が重なるとおかしくなる。
                box1.rotateY = -10.5f;
                box1.colR = 0.0f;
                box1.colG = 0.0f;
                box1.colB = 1.0f;

                box1.drawBox(gl);
            }

            //! 2Dも描画する
            {
                //! 3Dで変更した行列をリセットしなければならない
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();

                //! DEPTH_TESTを無効にする
                gl.glDisable(GL10.GL_DEPTH_TEST);

                GL11 gl11 = (GL11) gl;
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, spriteVertices);
                gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, 0);

                gl.glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
                drawQuad(gl, 0, 0, getWindowWidth() / 2, getWindowHeight() / 2);

                gl.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);
                drawQuad(gl, 50, 50, getWindowWidth() / 2, getWindowHeight() / 2);

                //! DEPTH_TESTを有効に治す
                gl.glEnable(GL10.GL_DEPTH_TEST);
            }
        }
    }

}
