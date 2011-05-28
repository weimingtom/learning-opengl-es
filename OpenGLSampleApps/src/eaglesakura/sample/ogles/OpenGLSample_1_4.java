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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;

/**
 *
 */
public class OpenGLSample_1_4 extends Activity {
    /**
     * 描画対象のView
     */
    private GLSurfaceView glSurfaceView = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);

        //  必要なレンダークラスを生成する
        // ! setRenderせずに処理を継続した場合、Exceptionが投げられるため、
        // ! 必ず設定を行うこと。
        glSurfaceView.setRenderer(new GLRenderSample2());
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
     * 以後のサンプルで利用するインターフェース。<BR>
     * 確保したメモリの破棄が必要になるため、インターフェース側で定義しておく。
     */
    abstract class GLRenderSampleBase implements GLSurfaceView.Renderer {
        /**
         * ログ出力を行う。
         *
         * @param message
         */
        public void log(String message) {
            Log.i("GLES_SAMPLE", message);
        }
    };

    /**
     * テクスチャの生成を行う。
     */
    class GLRenderSample1 extends GLRenderSampleBase {
        private int screenWidth, screenHeight;

        /**
         * OpenGLから割り当てられたテクスチャ名
         */
        private int texture;

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

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_512);

            log("bitmap size : " + bitmap.getWidth() + " x " + bitmap.getHeight());

            gl10.glEnable(GL10.GL_TEXTURE_2D);
            int[] buffers = new int[1];
            gl10.glGenTextures(1, buffers, 0); // !< テクスチャ用のメモリを指定数確保
            texture = buffers[0]; // !< テクスチャ名を保存する

            // ! テクスチャ情報の設定
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, texture);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // ! 拡大・縮小時の処理を指定する。
            // ! GL10.GL_LINEAR / GL10.GL_NEARESTで指定する。
            // ! MINは縮小時、MAGは拡大時の処理に指定する。
            // ! リニアは高品位・低速、ニアレストは低品位・高速に動作する。
            // ! これを指定しない場合、正常にテクスチャが表示されない場合がある
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            // ! bitmapを破棄
            bitmap.recycle();
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
            float left = ((float) x / (float) screenWidth) * 2.0f - 1.0f, top = ((float) y / (float) screenHeight) * 2.0f - 1.0f;

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
            gl10.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! テクスチャ全体のUVを指定する。
            {
                // ! UV情報
                float uv[] = {
                // ! u v
                        0.0f, 0.0f, // !< 左上
                        0.0f, 1.0f, // !< 左下
                        1.0f, 0.0f, // !< 右上
                        1.0f, 1.0f, // !< 右下
                };

                // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
                // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
                ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer fb = bb.asFloatBuffer();
                fb.put(uv);
                fb.position(0);

                gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, fb);
            }

            // ! 等倍で表示する
            drawQuad(gl10, 0, 0, 512, 512);

            // ! 拡大縮小して表示する
            drawQuad(gl10, screenWidth / 2, screenHeight / 2, 800, 100);
        }
    }

    /**
     * テクスチャの生成を行う。
     */
    class GLRenderSample2 extends GLRenderSampleBase {
        private int screenWidth, screenHeight;

        /**
         * OpenGLから割り当てられたテクスチャ名
         */
        private int textureName;

        private int textureWidth, textureHeight;

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

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_512);

            textureWidth = bitmap.getWidth();
            textureHeight = bitmap.getHeight();
            log("bitmap size : " + bitmap.getWidth() + " x " + bitmap.getHeight());

            gl10.glEnable(GL10.GL_TEXTURE_2D);
            int[] buffers = new int[1];
            gl10.glGenTextures(1, buffers, 0); // !< テクスチャ用のメモリを指定数確保
            textureName = buffers[0]; // !< テクスチャ名を保存する

            // ! テクスチャ情報の設定
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // ! 拡大・縮小時の処理を指定する。
            // ! GL10.GL_LINEAR / GL10.GL_NEARESTで指定する。
            // ! MINは縮小時、MAGは拡大時の処理に指定する。
            // ! リニアは高品位・低速、ニアレストは低品位・高速に動作する。
            // ! これを指定しない場合、正常にテクスチャが表示されない場合がある
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            // ! bitmapを破棄
            bitmap.recycle();
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
            float left = ((float) x / (float) screenWidth) * 2.0f - 1.0f, top = ((float) y / (float) screenHeight) * 2.0f - 1.0f;

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
         * テクスチャの描画範囲を指定する。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void setTextureArea(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) textureWidth);
            float top = ((float) y / (float) textureHeight);

            float right = left + ((float) w / (float) textureWidth);
            float bottom = top + ((float) h / (float) textureHeight);

            // ! 位置情報
            float uv[] = {
            // ! u v
                    left, top, // !< 左上
                    left, bottom, // !< 左下
                    right, top, // !< 右上
                    right, bottom, // !< 右下
            };

            ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(uv);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, fb);
        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            setTextureArea(gl10, 0, 0, 256, 256);
            drawQuad(gl10, 0, 0, 256, 256);

            setTextureArea(gl10, 256, 0, 256, 256);
            drawQuad(gl10, 256, 0, 256, 256);

            setTextureArea(gl10, 0, 256, 256, 256);
            drawQuad(gl10, 0, 256, 256, 256);

            setTextureArea(gl10, 256, 256, 256, 256);
            drawQuad(gl10, 256, 256, 256, 256);
        }
    };

    /**
     * テクスチャの生成を行う。
     */
    class GLRenderSample3 extends GLRenderSampleBase {
        private int screenWidth, screenHeight;

        /**
         * OpenGLから割り当てられたテクスチャ名
         */
        private int textureName;

        private int textureWidth, textureHeight;

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

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_512);

            textureWidth = bitmap.getWidth();
            textureHeight = bitmap.getHeight();
            log("bitmap size : " + bitmap.getWidth() + " x " + bitmap.getHeight());

            gl10.glEnable(GL10.GL_TEXTURE_2D);
            int[] buffers = new int[1];
            gl10.glGenTextures(1, buffers, 0); // !< テクスチャ用のメモリを指定数確保
            textureName = buffers[0]; // !< テクスチャ名を保存する

            // ! テクスチャ情報の設定
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // ! 拡大・縮小時の処理を指定する。
            // ! GL10.GL_LINEAR / GL10.GL_NEARESTで指定する。
            // ! MINは縮小時、MAGは拡大時の処理に指定する。
            // ! リニアは高品位・低速、ニアレストは低品位・高速に動作する。
            // ! これを指定しない場合、正常にテクスチャが表示されない場合がある
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            // ! bitmapを破棄
            bitmap.recycle();
        }

        /**
         * 色を変えて描画を行う。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void drawQuad(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) screenWidth) * 2.0f - 1.0f, top = ((float) y / (float) screenHeight) * 2.0f - 1.0f;

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
         * テクスチャの描画範囲を指定する。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void setTextureArea(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) textureWidth), top = ((float) y / (float) textureHeight);

            float right = left + ((float) w / (float) textureWidth);
            float bottom = top + ((float) h / (float) textureHeight);

            // ! 位置情報
            float uv[] = {
            // ! u v
                    left, top, // !< 左上
                    left, bottom, // !< 左下
                    right, top, // !< 右上
                    right, bottom, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(uv);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, fb);
        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {

            gl10.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            /**
             * 左上
             */
            gl10.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            setTextureArea(gl10, 0, 0, 256, 256);
            drawQuad(gl10, 0, 0, 256, 256);

            /**
             * 右上
             */
            gl10.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
            setTextureArea(gl10, 256, 0, 256, 256);
            drawQuad(gl10, 256, 0, 256, 256);

            /**
             * 左下
             */
            gl10.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
            setTextureArea(gl10, 0, 256, 256, 256);
            drawQuad(gl10, 0, 256, 256, 256);

            /**
             * 右下
             */
            gl10.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            setTextureArea(gl10, 256, 256, 256, 256);
            drawQuad(gl10, 256, 256, 256, 256);
        }
    };

    /**
     * 文字を描画する
     */
    class GLRenderSample4 extends GLRenderSampleBase {
        private int screenWidth, screenHeight;

        /**
         * OpenGLから割り当てられたテクスチャ名
         */
        private int textureName;

        private int textureWidth, textureHeight;

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

            /**
             * 空のビットマップを作成し、そこへ文字を書き込むことでGL上に描画を行う。
             */
            Bitmap bitmap = Bitmap.createBitmap(256, 256, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Style.FILL);
            canvas.drawColor(0);
            canvas.drawText("文字描画サンプル", 0, 15, paint);

            textureWidth = bitmap.getWidth();
            textureHeight = bitmap.getHeight();
            log("bitmap size : " + bitmap.getWidth() + " x " + bitmap.getHeight());

            gl10.glEnable(GL10.GL_TEXTURE_2D);
            int[] buffers = new int[1];
            gl10.glGenTextures(1, buffers, 0); // !< テクスチャ用のメモリを指定数確保
            textureName = buffers[0]; // !< テクスチャ名を保存する

            // ! テクスチャ情報の設定
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            // ! bitmapを破棄
            bitmap.recycle();

        }

        /**
         * 四角形描画を行う。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void drawQuad(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) screenWidth) * 2.0f - 1.0f, top = ((float) y / (float) screenHeight) * 2.0f - 1.0f;

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
         * テクスチャの描画範囲を指定する。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void setTextureArea(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) textureWidth), top = ((float) y / (float) textureHeight);

            float right = left + ((float) w / (float) textureWidth);
            float bottom = top + ((float) h / (float) textureHeight);

            // ! 位置情報
            float uv[] = {
            // ! u v
                    left, top, // !< 左上
                    left, bottom, // !< 左下
                    right, top, // !< 右上
                    right, bottom, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(uv);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, fb);
        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {

            gl10.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            /**
             * おまけ この３行でアルファが有効になる gl10.glEnable( GL10.GL_BLEND );
             * gl10.glEnable( GL10.GL_ALPHA ); gl10.glBlendFunc(
             * GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
             */

            setTextureArea(gl10, 0, 0, 256, 256);
            gl10.glColor4f(1.0f, 0, 0, 1);
            drawQuad(gl10, 0, 0, 256, 256);

        }
    };

    /**
     * 複数枚のテクスチャを利用する
     */
    class GLRenderSample5 extends GLRenderSampleBase {
        private int screenWidth, screenHeight;

        /**
         * テクスチャID配列
         */
        private int[] textures = null;

        /**
         * テクスチャ幅配列
         */
        private int[] widths = null;

        /**
         * テクスチャ高さ配列
         */
        private int[] heights = null;

        private int textureWidth, textureHeight;

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

            // ! 利用するテクスチャ数
            final int[] resources = { R.drawable.image_512, R.drawable.image_128 };

            textures = new int[resources.length];
            widths = new int[resources.length];
            heights = new int[resources.length];

            // ! テクスチャ生成
            gl10.glGenTextures(resources.length, textures, 0);
            gl10.glEnable(GL10.GL_TEXTURE_2D);

            for (int i = 0; i < resources.length; ++i) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resources[i]);

                widths[i] = bitmap.getWidth();
                heights[i] = bitmap.getHeight();
                log("bitmap size : " + bitmap.getWidth() + " x " + bitmap.getHeight());

                // ! テクスチャ情報の設定
                gl10.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

                // ! bitmapを破棄
                bitmap.recycle();

            }
        }

        private void setTexture(GL10 gl10, int index) {
            if (index < 0 || index >= textures.length) {
                gl10.glDisable(GL10.GL_TEXTURE_2D);
            } else {
                gl10.glEnable(GL10.GL_TEXTURE_2D);
                gl10.glBindTexture(GL10.GL_TEXTURE_2D, textures[index]);
                textureWidth = widths[index];
                textureHeight = heights[index];
            }
        }

        /**
         * 四角形描画を行う。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void drawQuad(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) screenWidth) * 2.0f - 1.0f, top = ((float) y / (float) screenHeight) * 2.0f - 1.0f;

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
         * テクスチャの描画範囲を指定する。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void setTextureArea(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) textureWidth), top = ((float) y / (float) textureHeight);

            float right = left + ((float) w / (float) textureWidth);
            float bottom = top + ((float) h / (float) textureHeight);

            // ! 位置情報
            float uv[] = {
            // ! u v
                    left, top, // !< 左上
                    left, bottom, // !< 左下
                    right, top, // !< 右上
                    right, bottom, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(uv);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, fb);
        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {

            gl10.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            setTexture(gl10, 0);
            setTextureArea(gl10, 0, 0, 256, 256);
            gl10.glColor4f(1.0f, 0, 0, 1);
            drawQuad(gl10, 0, 0, 256, 256);

            setTexture(gl10, -1);
            gl10.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
            drawQuad(gl10, 100, 200, 300, 400);
        }
    };

    /**
     * テクスチャの生成を行う。
     */
    class GLRenderSample7 extends GLRenderSampleBase {
        private int screenWidth, screenHeight;

        /**
         * OpenGLから割り当てられたテクスチャ名
         */
        private int textureName;

        private int textureWidth, textureHeight;

        /**
         * サーフェイス作成時の処理。
         *
         * @param gl10
         * @param eglconfig
         */
        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglconfig) {
        }

        int[] textures = null, widths = null, heights = null;

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

            final int[] resources = { R.drawable.image_128, R.drawable.image_512 };

            //! IDや幅・高さの格納先を確保する。
            textures = new int[resources.length];
            heights = new int[resources.length];
            widths = new int[resources.length];

            gl10.glEnable(GL10.GL_TEXTURE_2D);
            gl10.glGenTextures(resources.length, textures, 0);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_512);

            textureWidth = bitmap.getWidth();
            textureHeight = bitmap.getHeight();
            log("bitmap size : " + bitmap.getWidth() + " x " + bitmap.getHeight());

            gl10.glEnable(GL10.GL_TEXTURE_2D);
            int[] buffers = new int[1];
            gl10.glGenTextures(1, buffers, 0); // !< テクスチャ用のメモリを指定数確保
            textureName = buffers[0]; // !< テクスチャ名を保存する

            // ! テクスチャ情報の設定
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            // ! bitmapを破棄
            bitmap.recycle();

        }

        /**
         * 色を変えて描画を行う。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void drawQuad(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) screenWidth) * 2.0f - 1.0f, top = ((float) y / (float) screenHeight) * 2.0f - 1.0f;

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
         * テクスチャの描画範囲を指定する。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void setTextureArea(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) textureWidth), top = ((float) y / (float) textureHeight);

            float right = left + ((float) w / (float) textureWidth);
            float bottom = top + ((float) h / (float) textureHeight);

            // ! 位置情報
            float uv[] = {
            // ! u v
                    left, top, // !< 左上
                    left, bottom, // !< 左下
                    right, top, // !< 右上
                    right, bottom, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(uv);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, fb);
        }

        /**
         * 毎フレーム描画処理。
         *
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {

            gl10.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            /**
             * 左上
             */
            gl10.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            setTextureArea(gl10, 0, 0, 256, 256);
            drawQuad(gl10, 0, 0, 256, 256);

            /**
             * 右上
             */
            gl10.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
            setTextureArea(gl10, 256, 0, 256, 256);
            drawQuad(gl10, 256, 0, 256, 256);

            /**
             * 左下
             */
            gl10.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
            setTextureArea(gl10, 0, 256, 256, 256);
            drawQuad(gl10, 0, 256, 256, 256);

            /**
             * 右下
             */
            gl10.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            setTextureArea(gl10, 256, 256, 256, 256);
            drawQuad(gl10, 256, 256, 256, 256);
        }
    };

}
