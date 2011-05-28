/**
 *
 */
package eaglesakura.gles;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 *
 */
public class GLManager {
    private GL11 gl = null;
    private int windowWidth = 0;
    private int windowHeight = 0;
    private Context context = null;

    public GLManager(GL11 gl, int w, int h) {
        this.gl = gl;
        windowWidth = w;
        windowHeight = h;
    }

    public GL11 getGL() {
        return gl;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * アセットからテクスチャを生成する。
     * @param assetFileName
     * @return
     * @throws IOException
     */
    public Texture createTextureFromAsset(String assetFileName) throws IOException {
        InputStream is = context.getAssets().open(assetFileName);
        Bitmap bmp = BitmapFactory.decodeStream(is);
        Texture result = Texture.createInstance(this, bmp);
        bmp.recycle();
        is.close();

        return result;
    }

    /**
     * ウィンドウの幅を取得する。
     * @return
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * ウィンドウの高さを取得する。
     * @return
     */
    public int getWindowHeight() {
        return windowHeight;
    }

    /**
     * VBOのバッファをひとつ作成する。
     * @return
     */
    public int genVBO() {
        int[] buf = new int[1];
        gl.glGenBuffers(1, buf, 0);
        return buf[0];
    }

    /**
     * VBOのバッファをひとつ削除する。
     * @param vbo
     */
    public void delVBO(int vbo) {
        gl.glDeleteBuffers(1, new int[] { vbo }, 0);
    }

    /**
     * テクスチャバッファをひとつ作成する。
     * @return
     */
    public int genTex() {
        int[] buf = new int[1];
        gl.glGenTextures(1, buf, 0);
        return buf[0];
    }

    /**
     * テクスチャバッファを削除する。
     * @param tex
     */
    public void delTex(int tex) {
        gl.glDeleteTextures(0, new int[] { tex }, 0);
    }

    /**
     * 背景をクリアする。
     * @param r
     * @param g
     * @param b
     */
    public void clear(float r, float g, float b) {
        gl.glClearColor(r, g, b, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }
}
