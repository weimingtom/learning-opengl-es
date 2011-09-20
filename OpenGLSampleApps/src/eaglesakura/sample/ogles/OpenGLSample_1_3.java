/**
 *
 */
package eaglesakura.sample.ogles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * 3章 簡単な図形を描画する
 * @author SAKURA
 *
 */
public class OpenGLSample_1_3 extends Activity {
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
     * 描画用コールバック。<BR>
     * 描画はUIスレッドとは別のスレッドで動作する。
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
            gl10.glViewport(0, 0, w, h);
        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 位置情報
            float positions[] = {
            // ! x y zの順で定義する
                    0.0f, 1.0f, 0.0f, // !< 左上
                    0.0f, 0.0f, 0.0f, // !< 左下
                    1.0f, 1.0f, 0.0f, // !< 右上
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
            gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
        }
    }

    /**
     * 三角形（下向き）を描画する。
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
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 位置情報
            float positions[] = {
            // ! x y z
                    0.0f, 0.0f, 0.0f, // !< 左下
                    1.0f, 1.0f, 0.0f, // !< 右上
                    1.0f, 0.0f, 0.0f, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
        }
    }

    /**
     * 三角形（上向き・下向き）を同時に描画する（１）。
     */
    class GLRenderSample3 implements GLSurfaceView.Renderer {

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
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 位置情報
            float positions[] = {
            // ! x y z
                    // ! 下向き
                    0.0f, 1.0f, 0.0f, // !< 左上
                    0.0f, 0.0f, 0.0f, // !< 左下
                    1.0f, 1.0f, 0.0f, // !< 右上

                    // ! 上向き
                    0.0f, 0.0f, 0.0f, // !< 左下
                    1.0f, 1.0f, 0.0f, // !< 右上
                    1.0f, 0.0f, 0.0f, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3 * 2);
        }
    }

    /**
     * 三角形（上向き・下向き）を同時に描画する（２）。
     */
    class GLRenderSample4 implements GLSurfaceView.Renderer {

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
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 位置情報
            float positions[] = {
            // ! x y z
                    0.0f, 1.0f, 0.0f, // !< 左上
                    0.0f, 0.0f, 0.0f, // !< 左下
                    1.0f, 1.0f, 0.0f, // !< 右上
                    1.0f, 0.0f, 0.0f, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    /**
     * 指定した2DのXYWHに四角形を描画する。
     */
    class GLRenderSample5 implements GLSurfaceView.Renderer {
        private int screenWidth, screenHeight;

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
            screenWidth = w;
            screenHeight = h;
        }

        /**
         * 指定した2D座標に四角形を描画する。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void drawQuad(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) screenWidth) * 2.0f - 1.0f;
            float top = ((float) y / (float) screenHeight) * 2.0f - 1.0f;

            float right = left + ((float) w / (float) screenWidth) * 2.0f;
            float bottom = top + ((float) h / (float) screenHeight) * 2.0f;

            // ! 上下を反転させる
            top = -top;
            bottom = -bottom;

            // ! 位置情報
            float positions[] = {
            // ! x y z
                    left, top, 0.0f, // !< 左上
                    left, bottom, 0.0f, // !< 左下
                    right, top, 0.0f, // !< 右上
                    right, bottom, 0.0f, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            drawQuad(gl10, 100, 200, 300, 400);
        }
    };

    /**
     * 指定した2DのXYWHに指定した色の四角形を描画する。
     */
    class GLRenderSample6 implements GLSurfaceView.Renderer {
        private int screenWidth, screenHeight;

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
            screenWidth = w;
            screenHeight = h;
        }

        /**
         * 指定した2D座標に四角形を描画する。
         *
         * @param gl10
         * @param x
         *            ウィンドウX座標
         * @param y
         *            ウィンドウY座標
         * @param w
         *            四角形幅（ピクセル）
         * @param h
         *            四角形高さ（ピクセル）
         */
        private void drawQuad(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) screenWidth) * 2.0f - 1.0f;
            float top = ((float) y / (float) screenHeight) * 2.0f - 1.0f;

            float right = left + ((float) w / (float) screenWidth) * 2.0f;
            float bottom = top + ((float) h / (float) screenHeight) * 2.0f;

            // ! 上下を反転させる
            top = -top;
            bottom = -bottom;

            // ! 位置情報
            float positions[] = {
            // ! x y z
                    left, top, 0.0f, // !< 左上
                    left, bottom, 0.0f, // !< 左下
                    right, top, 0.0f, // !< 右上
                    right, bottom, 0.0f, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 赤い四角形を描画する
            gl10.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            drawQuad(gl10, 0, 0, 128, 128);

            // ! 赤い四角形を描画する
            drawQuad(gl10, 100, 200, 300, 400);

            // ! 青い四角形を描画する
            gl10.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
            drawQuad(gl10, screenWidth / 2, screenHeight / 2, 256, 128);
        }
    }

}
