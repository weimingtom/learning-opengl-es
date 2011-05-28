package eaglesakura.gles;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import eaglesakura.android.mqo.MQOMesh;

/**
 * OpenGL用１オブジェクトを管理する。
 */
public class Mesh {
    /**
     * 描画用のマテリアル。
     */
    private Material material = null;

    /**
     * 頂点バッファ。
     */
    private VertexBuffer vertexBuffer = null;

    /**
     * インデックスバッファ。
     */
    private IndexBuffer indexBuffer = null;

    /**
     * メッシュ名。
     */
    private String name = "";

    /**
     *
     */
    public Mesh(GLManager gl, Figure parent, MQOMesh mesh) {
        vertexBuffer = new VertexBuffer(gl, mesh.getVertices());
        indexBuffer = new IndexBuffer(gl, mesh.getIndices());
        material = parent.getMaterial(mesh.getIndices().getMaterial());
        name = mesh.getName();
    }

    public VertexBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public Material getMaterial() {
        return material;
    }

    public IndexBuffer getIndexBuffer() {
        return indexBuffer;
    }

    public void draw() {
        GL11 gl = getVertexBuffer().getGLManager().getGL();
        //! 名前からコマンドを取り出す
        {
            //! カリングを行う
            if (name.indexOf("[CULL]") >= 0) {
                gl.glEnable(GL10.GL_CULL_FACE);
                gl.glCullFace(GL10.GL_BACK);
            } else {
                gl.glDisable(GL10.GL_CULL_FACE);
            }

            //! アルファテスト
            if (name.indexOf("[ALPHA]") >= 0) {
                gl.glEnable(GL10.GL_ALPHA_TEST);
                gl.glAlphaFunc(GL10.GL_EQUAL, 1.0f);
            } else {
                gl.glDisable(GL10.GL_ALPHA_TEST);
            }
        }

        material.bind();
        vertexBuffer.bind();
        {
            indexBuffer.draw();
        }
        vertexBuffer.unbind();
        material.unbind();
    }

    public void drawEdge( )
    {
        GL11 gl = getVertexBuffer().getGLManager().getGL();
        //! 名前からコマンドを取り出す
        {
            if (name.indexOf("[EDGE]") >= 0) {
                //! カリングを反転する
                gl.glEnable(GL10.GL_CULL_FACE);
                gl.glCullFace(GL10.GL_FRONT);
            } else {
                //! エッジ対象でないメッシュは描画しない
                return;
            }
        }

        vertexBuffer.bind();
        {
            indexBuffer.draw();
        }
        vertexBuffer.unbind();
    }
}
