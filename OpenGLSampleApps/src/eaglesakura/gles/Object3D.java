package eaglesakura.gles;

import javax.microedition.khronos.opengles.GL10;

import eaglesakura.android.mqo.Vector3;

public class Object3D {
    private Figure figure = null;
    private Vector3 position = new Vector3();
    private Vector3 rotate = new Vector3();
    private Vector3 scale = new Vector3(1, 1, 1);
    private GLManager glManager = null;

    public Object3D() {

    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getRotate() {
        return rotate;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setGlManager(GLManager glManager) {
        this.glManager = glManager;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public void draw() {
        GL10 gl = glManager.getGL();
        gl.glLoadIdentity();

        gl.glTranslatef( position.x, position.y, position.z );
        //! XYZの順に回転させる。適用は逆順のためZYX
        {
            gl.glRotatef( rotate.z, 0, 0, 1 );
            gl.glRotatef( rotate.y, 0, 1, 0 );
            gl.glRotatef( rotate.x, 1, 0, 0 );
        }
        gl.glScalef( scale.x, scale.y, scale.z );
        figure.draw();
    }
    public void drawEdge() {
        GL10 gl = glManager.getGL();
        gl.glLoadIdentity();

        gl.glTranslatef( position.x, position.y, position.z );
        //! XYZの順に回転させる。適用は逆順のためZYX
        {
            gl.glRotatef( rotate.z, 0, 0, 1 );
            gl.glRotatef( rotate.y, 0, 1, 0 );
            gl.glRotatef( rotate.x, 1, 0, 0 );
        }
        //! セルエッジ描画のため、本体よりも少し大きなスケーリングを適用する
        gl.glScalef( scale.x * 1.003f, scale.y * 1.003f, scale.z * 1.003f );
        figure.drawEdge();
    }
}
