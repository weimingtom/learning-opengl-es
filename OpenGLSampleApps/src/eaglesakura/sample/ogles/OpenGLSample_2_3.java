package eaglesakura.sample.ogles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;

public class OpenGLSample_2_3 extends OpenGLSample_ex_2 {
    GLSurfaceView.Renderer render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        render = new GLRenderSample3();
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
        int posBufferObject = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            // ! 事前に転送済みにしておく
            float positions[] = {
            //! x      y     z
                    -0.5f, 0.5f, 0.0f, // !< 左上
                    -0.5f, -0.5f, 0.0f, // !< 左下
                    0.5f, 0.5f, 0.0f, // !< 右上
                    0.5f, -0.5f, 0.0f, // !< 右下
            };

            // ! OpenGLはビッグエンディアンではなく、CPUごとのネイティブエンディアンで数値を伝える必要がある。
            // ! そのためJavaヒープを直接的には扱えず、java.nio配下のクラスへ一度値を格納する必要がある。
            ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer posBuffer = bb.asFloatBuffer();
            posBuffer.put(positions);
            posBuffer.position(0);

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            //! 頂点オブジェクト作成
            {
                GL11 gl11 = (GL11) gl10;
                int[] buffers = new int[1];
                gl11.glGenBuffers(1, buffers, 0);
                posBufferObject = buffers[0];
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, posBufferObject);
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, posBuffer.capacity() * 4, posBuffer, GL11.GL_STATIC_DRAW);
                gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);

                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
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

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            drawQuad(gl10, 0, 0, 256, 256);
        }
    }

    public class GLRenderSample2 implements GLSurfaceView.Renderer {
        int posBufferObject = 0;
        int uvBufferObject = 0;
        int textureID = 0;
        int textureHeight = 0;
        int textureWidth = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            // ! 事前に転送済みにしておく
            {
                float positions[] = {
                // ! x y z
                        -0.5f, 0.5f, 0.0f, // !< 左上
                        -0.5f, -0.5f, 0.0f, // !< 左下
                        0.5f, 0.5f, 0.0f, // !< 右上
                        0.5f, -0.5f, 0.0f, // !< 右下
                };

                ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer posBuffer = bb.asFloatBuffer();
                posBuffer.put(positions);
                posBuffer.position(0);

                gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                //! 頂点オブジェクト作成
                {
                    GL11 gl11 = (GL11) gl10;
                    int[] buffers = new int[1];
                    gl11.glGenBuffers(1, buffers, 0);
                    posBufferObject = buffers[0];
                    gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, posBufferObject);
                    gl11.glBufferData(GL11.GL_ARRAY_BUFFER, posBuffer.capacity() * 4, posBuffer, GL11.GL_STATIC_DRAW);
                    gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);

                    gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
                }
            }

            //! UVを転送する

            //! UV設定
            {
                float uv[] = {
                //! u, v
                        0.0f, 0.0f, //
                        0.0f, 1.0f, //
                        1.0f, 0.0f, //
                        1.0f, 1.0f, //
                };

                ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer uvBuffer = bb.asFloatBuffer();
                uvBuffer.put(uv);
                uvBuffer.position(0);

                gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                //! 頂点オブジェクト作成
                {
                    GL11 gl11 = (GL11) gl10;
                    int[] buffers = new int[1];
                    gl11.glGenBuffers(1, buffers, 0);
                    uvBufferObject = buffers[0];
                    gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, uvBufferObject);
                    gl11.glBufferData(GL11.GL_ARRAY_BUFFER, uvBuffer.capacity() * 4, uvBuffer, GL11.GL_STATIC_DRAW);

                    gl11.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
                    //! バッファを無効化する
                    gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
                }
            }
            //! texture
            {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_512);

                gl10.glEnable(GL10.GL_TEXTURE_2D);
                int[] buffer = new int[1];
                gl10.glGenTextures(1, buffer, 0);
                textureID = buffer[0];
                textureWidth = bitmap.getWidth();
                textureHeight = bitmap.getHeight();

                gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

                // ! bitmapを破棄
                bitmap.recycle();
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
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
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
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            setTextureArea(gl10, 0, 0, 256, 256);
            drawQuad(gl10, 0, 0, 256, 256);
        }
    };

    public class GLRenderSample3 implements GLSurfaceView.Renderer {
        int vertexBufferObject = 0;

        int textureID = 0;
        int textureHeight = 0;
        int textureWidth = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            // ! 事前に転送済みにしておく
            {
                float vertices[] = {
                // !  x       y    z     u       v
                        -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, // !< 左上
                        -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, // !< 左下
                        0.5f, 0.5f, 0.0f, 1.0f, 0.0f, // !< 右上
                        0.5f, -0.5f, 0.0f, 1.0f, 1.0f, // !< 右下
                };

                ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer vbo = bb.asFloatBuffer();
                vbo.put(vertices);
                vbo.position(0);

                gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);

                GL11 gl11 = (GL11) gl10;
                //! 頂点オブジェクト作成
                {
                    int[] buffers = new int[1];
                    gl11.glGenBuffers(1, buffers, 0);
                    vertexBufferObject = buffers[0];
                    gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertexBufferObject);
                    gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vbo.capacity() * 4, vbo, GL11.GL_STATIC_DRAW);
                }
                {
                    gl11.glVertexPointer(3, GL10.GL_FLOAT, 4 * 5, 0);
                    gl11.glTexCoordPointer(2, GL10.GL_FLOAT, 4 * 5, 4 * 3);
                }
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
            }
            //! texture
            {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_512);

                gl10.glEnable(GL10.GL_TEXTURE_2D);
                int[] buffer = new int[1];
                gl10.glGenTextures(1, buffer, 0);
                textureID = buffer[0];
                textureWidth = bitmap.getWidth();
                textureHeight = bitmap.getHeight();

                gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

                // ! bitmapを破棄
                bitmap.recycle();
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
         *
         * @param gl10
         * @param x
         * @param y
         * @param w
         * @param h
         */
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
            gl10.glClearColor(0, 1, 1, 1);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

            setTextureArea(gl10, 0, 0, 256, 256);
            drawQuad(gl10, 0, 0, 256, 256);
        }
    };
}
