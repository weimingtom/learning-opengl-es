/**
 *
 */
package eaglesakura.gles;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * テクスチャオブジェクトを管理する。
 */
public class Texture extends GLObject {
    private int texture = 0;
    private int width = 0;
    private int height = 0;

    private Texture() {

    }

    /**
     * 管理資源を解放する。
     */
    @Override
    public void dispose() {
        if (texture != 0) {
            getGLManager().delTex(texture);
            texture = 0;
        }
    }

    /**
     * テクスチャの幅を取得する。
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * テクスチャの高さを取得する。
     * @return
     */
    public int getHeight() {
        return height;
    }

    @Override
    public void bind() {
        GL10 gl = getGLManager().getGL();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
    }

    @Override
    public void unbind() {
        GL10 gl = getGLManager().getGL();
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    /**
     * テクスチャを生成する。
     * @param bmp
     * @return
     */
    public static Texture createInstance(GLManager glManager, Bitmap bmp) {
        Texture result = new Texture();
        result.setGLManager(glManager);

        GL11 gl = glManager.getGL();
        result.texture = glManager.genTex();
        result.width = bmp.getWidth();
        result.height = bmp.getHeight();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, result.texture);

        //! texture転送
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

        return result;
    }
}
