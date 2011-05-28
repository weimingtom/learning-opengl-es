/**
 *
 */
package eaglesakura.gles;

/**
 * OpenGL資源管理の基底クラス。
 */
public abstract class GLObject {
    private GLManager glManager = null;

    public GLObject() {

    }

    /**
     * オブジェクトが消える前にかならず解放処理を呼ぶようにする。
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    /**
     * GL管理クラスを設定する。
     * @param glManager
     */
    public void setGLManager(GLManager glManager) {
        this.glManager = glManager;
    }

    /**
     * GL管理クラスを取得する。
     * @return
     */
    public GLManager getGLManager() {
        return glManager;
    }

    /**
     * GLに関連付ける。
     */
    public abstract void bind();

    /**
     * GLから切り離す。
     */
    public abstract void unbind();

    /**
     * 管理している資源を解放する。
     */
    public abstract void dispose();
}
