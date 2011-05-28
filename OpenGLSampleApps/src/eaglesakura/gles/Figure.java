/**
 *
 */
package eaglesakura.gles;

import java.util.ArrayList;
import java.util.List;

import eaglesakura.android.mqo.MQOFigure;

/**
 *
 */
public class Figure {
    private List<Material> materials = new ArrayList<Material>();
    private List<Mesh> meshs = new ArrayList<Mesh>();

    public Figure(GLManager gl, MQOFigure obj) {
        //! マテリアル生成
        {
            for (int i = 0; i < obj.getMaterialCount(); ++i) {
                materials.add(new Material(gl, obj.getMaterial(i)));
            }
        }

        //! メッシュ生成
        {
            for (int i = 0; i < obj.getMeshCount(); ++i) {
                meshs.add(new Mesh(gl, this, obj.getMesh(i)));
            }
        }
    }

    /**
     * 指定番号のマテリアルを取得する。
     * @param index
     * @return
     */
    public Material getMaterial(int index) {
        return materials.get(index);
    }

    /**
     * メッシュ数を取得する。
     * @return
     */
    public int getMeshCount( )
    {
        return  meshs.size();
    }

    /**
     * メッシュを取得する。
     * @param index
     * @return
     */
    public Mesh getMesh( int index )
    {
        return  meshs.get( index );
    }

    /**
     * 描画を行う。
     */
    public void draw() {
        for (Mesh mesh : meshs) {
            mesh.draw();
        }
    }

    /**
     * 輪郭線の描画を行う。
     */
    public void drawEdge( )
    {
        for (Mesh mesh : meshs) {
            mesh.drawEdge();
        }
    }
}
