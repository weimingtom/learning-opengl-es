package eaglesakura.sample.ogles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 6章 行列のできるOpenGL
 * @author SAKURA
 *
 */
public class OpenGLSample_2_1 extends OpenGLSample_ex_2 {
    GLSurfaceView.Renderer render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        render = new GLRenderSample6();
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

    public class GLRenderSample1 implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 位置情報
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
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            {
                //! 行列を変更する
                gl10.glMatrixMode(GL10.GL_MODELVIEW);
                gl10.glTranslatef(0.1f, 0, 0);
            }

            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    public class GLRenderSample2 implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 位置情報
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
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            {
                //! 行列を変更する
                gl10.glMatrixMode(GL10.GL_MODELVIEW);
                gl10.glLoadIdentity();
                gl10.glScalef(0.5f, 2.0f, 1.0f);
            }

            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    public class GLRenderSample3 implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 位置情報
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
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            {
                //! 行列を変更する
                gl10.glMatrixMode(GL10.GL_MODELVIEW);
                gl10.glLoadIdentity();
                gl10.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
            }

            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    public class GLRenderSample4A implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 位置情報
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
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            {
                //! 行列を変更する
                gl10.glMatrixMode(GL10.GL_MODELVIEW);
                gl10.glLoadIdentity();
                gl10.glRotatef(135.0f, 0.0f, 0.0f, 1.0f);
                gl10.glTranslatef(0.5f, 0.0f, 0.0f);
            }

            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    public class GLRenderSample4B implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // ! 位置情報
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
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

            {
                //! 行列を変更する
                gl10.glMatrixMode(GL10.GL_MODELVIEW);
                gl10.glLoadIdentity();
                gl10.glTranslatef(0.5f, 0.0f, 0.0f);
                gl10.glRotatef(135.0f, 0.0f, 0.0f, 1.0f);
            }
            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    public class GLRenderSample5 implements GLSurfaceView.Renderer {
        FloatBuffer posBuffer = null;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //! 事前に転送済みにしておく
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

        /**
         * 四角形を描画する。
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        public void drawQuad(GL10 gl10, int x, int y, int w, int h) {
            //! 描画位置を行列で操作する
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
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            gl10.glColor4f(1, 0, 0, 1);
            drawQuad(gl10, 0, 0, getWindowWidth() / 2, getWindowHeight() / 2);

            gl10.glColor4f(0, 1, 0, 1);
            drawQuad(gl10, getWindowWidth() / 2, getWindowHeight() / 2, getWindowWidth() / 2, getWindowHeight() / 2);
        }
    }

    public class GLRenderSample6 implements GLSurfaceView.Renderer {
        FloatBuffer posBuffer = null, uvBuffer = null;

        int textureID, textureWidth, textureHeight;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //! 事前に転送済みにしておく

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

            //! テクスチャ読み込み
            {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_512);

                textureWidth = bitmap.getWidth();
                textureHeight = bitmap.getHeight();
                gl10.glEnable(GL10.GL_TEXTURE_2D);
                int[] buffers = new int[1];
                gl10.glGenTextures(1, buffers, 0); // !< テクスチャ用のメモリを指定数確保
                textureID = buffers[0]; // !< テクスチャ名を保存する

                // ! テクスチャ情報の設定
                gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

                // ! bitmapを破棄
                bitmap.recycle();

            }

            //! UV設定
            {
                float uv[] = {
                //! u, v
                        0, 0, //
                        0, 1, //
                        1, 0, //
                        1, 1, //
                };

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
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        public void drawQuad(GL10 gl10, int x, int y, int w, int h) {
            //! 描画位置を行列で操作する
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
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        private void setTextureArea(GL10 gl10, int x, int y, int w, int h) {
            float tw = (float) w / (float) textureWidth;
            float th = (float) h / (float) textureHeight;
            float tx = (float) x / (float) textureWidth;
            float ty = (float) y / (float) textureHeight;

            gl10.glMatrixMode(GL10.GL_TEXTURE);
            gl10.glLoadIdentity();

            gl10.glTranslatef(tx, ty, 0.0f);
            gl10.glScalef(tw, th, 1.0f);

            gl10.glMatrixMode(GL10.GL_MODELVIEW);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            gl10.glColor4f(1, 0, 0, 1);
            setTextureArea(gl10, 0, 0, 128, 128);
            drawQuad(gl10, 0, 0, getWindowWidth() / 2, getWindowHeight() / 2);

            gl10.glColor4f(0, 1, 0, 1);
            setTextureArea(gl10, 128, 128, 256, 256);
            drawQuad(gl10, getWindowWidth() / 2, getWindowHeight() / 2, getWindowWidth() / 2, getWindowHeight() / 2);
        }
    }

    public class GLRenderSample7 implements GLSurfaceView.Renderer {
        FloatBuffer posBuffer = null, uvBuffer = null;

        int textureID, textureWidth, textureHeight;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //! 事前に転送済みにしておく

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

            //! テクスチャ読み込み
            {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_512);

                textureWidth = bitmap.getWidth();
                textureHeight = bitmap.getHeight();
                log("bitmap size : " + bitmap.getWidth() + " x " + bitmap.getHeight());

                gl10.glEnable(GL10.GL_TEXTURE_2D);
                int[] buffers = new int[1];
                gl10.glGenTextures(1, buffers, 0); // !< テクスチャ用のメモリを指定数確保
                textureID = buffers[0]; // !< テクスチャ名を保存する

                // ! テクスチャ情報の設定
                gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

                // ! bitmapを破棄
                bitmap.recycle();

            }

            //! UV設定
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
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        public void drawQuadM(GL10 gl10, int x, int y, int w, int h) {
            //! 描画位置を行列で操作する
            float sizeX = (float) w / (float) getWindowWidth() * 2, sizeY = (float) h / (float) getWindowHeight() * 2, sx = (float) x
                    / (float) getWindowWidth() * 2, sy = (float) y / (float) getWindowHeight() * 2;
            gl10.glLoadIdentity();
            gl10.glTranslatef(-1.0f + sizeX / 2.0f + sx, 1.0f - sizeY / 2.0f - sy, 0.0f);
            gl10.glScalef(sizeX, sizeY, 1.0f);
            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }

        /**
         * テクスチャの描画エリアを指定する。
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
        @SuppressWarnings("all")
        private void setTextureAreaM(GL10 gl10, int x, int y, int w, int h) {
            float tw = (float) w / (float) textureWidth, th = (float) h / (float) textureHeight, tx = (float) x / (float) textureWidth, ty = (float) y
                    / (float) textureHeight;

            gl10.glMatrixMode(GL10.GL_TEXTURE);
            gl10.glLoadIdentity();

            gl10.glTranslatef(tx, ty, 0.0f);
            gl10.glScalef(tw, th, 1.0f);

            gl10.glMatrixMode(GL10.GL_MODELVIEW);
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
        private void drawQuadR(GL10 gl10, int x, int y, int w, int h) {
            float left = ((float) x / (float) getWindowWidth()) * 2.0f - 1.0f, top = ((float) y / (float) getWindowHeight()) * 2.0f - 1.0f;

            float right = left + ((float) w / (float) getWindowWidth()) * 2.0f;
            float bottom = top + ((float) h / (float) getWindowHeight()) * 2.0f;

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

            posBuffer.put(positions);
            posBuffer.position(0);

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
        private void setTextureAreaR(GL10 gl10, int x, int y, int w, int h) {
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

            uvBuffer.put(uv);
            uvBuffer.position(0);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            gl10.glColor4f(1, 1, 1, 1);

            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; ++i) {

                /*
                setTextureAreaM( gl10,  ( int )( Math.random() * 256 ),
                                        ( int )( Math.random() * 256 ),
                                        ( int )( Math.random() * 256 ),
                                        ( int )( Math.random() * 256 )
                                        );
                drawQuadM( gl10,( int )( Math.random() * 256 ),
                                ( int )( Math.random() * 256 ),
                                ( int )( Math.random() * 256 ),
                                ( int )( Math.random() * 256 )
                                );
                setTextureAreaM( gl10, 128, 128, 256, 256 );
                drawQuadM( gl10, 256, 256, 480, 480 );
                                                                */

                setTextureAreaR(gl10, (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
                drawQuadR(gl10, (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));

            }
            long end = System.currentTimeMillis();

            log("Time : " + (end - start) + " ms");
            Toast.makeText(OpenGLSample_2_1.this, "Time : " + (end - start) + " ms", Toast.LENGTH_LONG).show();
        }
    };
}
