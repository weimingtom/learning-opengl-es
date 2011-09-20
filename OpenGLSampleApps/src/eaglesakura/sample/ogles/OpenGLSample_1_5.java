package eaglesakura.sample.ogles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

/**
 * 5章 画像をブレンドする
 * @author SAKURA
 *
 */
public class OpenGLSample_1_5 extends Activity {
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
     * 複数枚のテクスチャを利用する
     */
    class GLRenderSample1 extends GLRenderSampleBase {
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
            final int[] resources = { R.drawable.image_512 };

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
                //                GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bitmap, 0 );

                {
                    int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
                    byte[] texture = new byte[bitmap.getWidth() * bitmap.getHeight() * 4];

                    bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
                    for (int k = 0; k < pixels.length; ++k) {
                        texture[k * 4 + 0] = (byte) Color.red(pixels[k]);
                        texture[k * 4 + 1] = (byte) Color.green(pixels[k]);
                        texture[k * 4 + 2] = (byte) Color.blue(pixels[k]);
                        texture[k * 4 + 3] = (byte) Color.alpha(pixels[k]);
                    }
                    ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length * 4).order(ByteOrder.nativeOrder());
                    buffer.put(texture);
                    buffer.position(0);

                    gl10.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
                            buffer);
                }

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

            // ! アルファブレンドを有効にする
            {
                gl10.glEnable(GL10.GL_BLEND);
                gl10.glEnable(GL10.GL_ALPHA);

                gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            }
            setTexture(gl10, 0);
            setTextureArea(gl10, 0, 0, 512, 512);
            gl10.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            drawQuad(gl10, 0, 0, 512, 512);

            /*
            {
            //! 描画される色が手計算しやすいよう、単純なポリゴンを描画する。
                gl10.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
                gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

                gl10.glBlendFunc(GL10.GL_ONE, GL10.GL_ZERO);
                setTexture(gl10, -1);
                gl10.glColor4f(0.0f, 0.0f, 0.75f, 1.0f);
                drawQuad(gl10, 128, 0, 480 - 128 * 2, 500);

                //! アルファ演算適用
                gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

                //! アルファ加算
                gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

                //! 乗算
                gl10.glBlendFunc(GL10.GL_DST_COLOR, GL10.GL_ZERO);

                gl10.glColor4f(1.0f, 1.0f, 0.5f, 0.5f);
                drawQuad(gl10, 128, screenHeight - 500, 480 - 128 * 2, 500);
            }
            */
        }
    }
}
