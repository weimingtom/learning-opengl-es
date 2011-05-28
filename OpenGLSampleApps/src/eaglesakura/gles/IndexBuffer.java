package eaglesakura.gles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import eaglesakura.android.mqo.MQOIndexBuffer;

/**
 * インデックスバッファを作成する。
 */
public class IndexBuffer extends GLObject {
    private int indices = 0;
    private int indices_length = 0;

    public IndexBuffer(GLManager glManager, MQOIndexBuffer ib) {
        setGLManager(glManager);
        indices = getGLManager().genVBO();

        bind();
        {
            short[] buf = ib.getIndices();
            ShortBuffer sb = ByteBuffer.allocateDirect(buf.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
            sb.put(buf);
            sb.position(0);
            indices_length = buf.length;

            GL11 gl = getGLManager().getGL();
            gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, sb.capacity() * 2, sb, GL11.GL_STATIC_DRAW);
        }
        unbind();
    }

    @Override
    public void dispose() {
        if (indices != 0) {
            getGLManager().delVBO(indices);
            indices = 0;
        }
    }

    public void draw() {
        GL11 gl = getGLManager().getGL();
        //   bind( );
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indices);
        gl.glDrawElements(GL10.GL_TRIANGLES, indices_length, GL10.GL_UNSIGNED_SHORT, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void bind() {
        GL11 gl = getGLManager().getGL();
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indices);
    }

    @Override
    public void unbind() {
        GL11 gl = getGLManager().getGL();
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
