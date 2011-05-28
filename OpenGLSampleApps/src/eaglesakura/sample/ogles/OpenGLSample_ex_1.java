package eaglesakura.sample.ogles;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import eaglesakura.sample.ogles.bmp.PaletteBitmap;

public class OpenGLSample_ex_1 extends OpenGLSample_ex_2 {
    GLSurfaceView.Renderer render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        render = new GLRenderSample1();
    }

    /**
     * GLの初期化を行う。
     */
    @Override
    public void initializeGL() {
        super.initializeGL();

        render.onSurfaceChanged(gl10, getWindowWidth(), getWindowHeight());
    }

    @Override
    public void repaint() {
        render.onDrawFrame(gl10);
        swapBuffers();
    }

    Bitmap[] bitmaps = null;

    public class GLRenderSample1 implements GLSurfaceView.Renderer {
        FloatBuffer posBuffer = null, uvBuffer = null;

        int textureID, textureWidth, textureHeight;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        /**
         * 指定した名称の256色Bitmapを読み込む。
         *
         * @param fileName
         */
        public void readTexture(GL10 gl10, String fileName) {
            InputStream is = null;
            try {
                is = getAssets().open(fileName);

                // ! 256色Bitmapとして読み込む
                PaletteBitmap bmp = PaletteBitmap.createInstance(is);

                int[] buffers = new int[1];
                gl10.glGenTextures(1, buffers, 0); // !< テクスチャ用のメモリを指定数確保
                textureID = buffers[0]; // !< テクスチャ名を保存する

                // ! テクスチャ情報の設定
                gl10.glEnable(GL10.GL_TEXTURE_2D);
                gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureID);

                // ! テクスチャ情報転送
                gl10.glCompressedTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_PALETTE8_RGBA8_OES, bmp.getWidth(), bmp.getHeight(), 0,
                        bmp.getGLTextureBufferSize(), bmp.toGLTextureBuffer());

                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

                textureHeight = bmp.getHeight();
                textureWidth = bmp.getWidth();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {

                }
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // ! 事前に転送済みにしておく

            {
                float positions[] = {
                // ! x y z
                        -0.5f, 0.5f, 0.0f, // !< 左上
                        -0.5f, -0.5f, 0.0f, // !< 左下
                        0.5f, 0.5f, 0.0f, // !< 右上
                        0.5f, -0.5f, 0.0f, // !< 右下
                };

                // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
                // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
                ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
                bb.order(ByteOrder.nativeOrder());
                posBuffer = bb.asFloatBuffer();
                posBuffer.put(positions);
                posBuffer.position(0);

                gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, posBuffer);
            }

            // ! テクスチャ読み込み
            readTexture(gl10, "sample_ex_1.bmp");

            // ! UV設定
            {
                float uv[] = { 0, 0, 0, 1, 1, 0, 1, 1, };

                // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
                // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
                ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
                bb.order(ByteOrder.nativeOrder());
                uvBuffer = bb.asFloatBuffer();
                uvBuffer.put(uv);
                uvBuffer.position(0);

                gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuffer);
            }
        }

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

        /**
         * テクスチャの描画エリアを指定する。
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        @SuppressWarnings("all")
        private void setTextureArea(GL10 gl10, int x, int y, int w, int h) {
            float tw = (float) w / (float) textureWidth, th = (float) h / (float) textureHeight, tx = (float) x / (float) textureWidth, ty = (float) y
                    / (float) textureHeight;

            gl10.glMatrixMode(GL10.GL_TEXTURE);
            gl10.glLoadIdentity();

            gl10.glTranslatef(tx, ty, 0.0f);
            gl10.glScalef(tw, th, 1.0f);

            gl10.glMatrixMode(GL10.GL_MODELVIEW);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            if (bitmaps != null)
                bitmaps[0] = bitmaps[0];
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! アルファブレンドを有効にする
            {
                gl10.glEnable(GL10.GL_BLEND);
                gl10.glEnable(GL10.GL_ALPHA);

                gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            }
            // setTextureArea( gl10, 0, 0, 128, 64 );
            drawQuad(gl10, 0, 0, 256, 256);
        }
    };

}
