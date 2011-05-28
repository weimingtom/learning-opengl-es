/**
 *
 */
package eaglesakura.android.mqo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MQOVertexBuffer {
    private List<MQOVertex> vertices = new ArrayList<MQOVertex>();

    public MQOVertexBuffer() {

    }

    /**
     * 頂点情報を取得する。
     * @param index
     * @return
     */
    public MQOVertex getVertex(int index) {
        return vertices.get(index);
    }

    /**
     * 頂点数を取得する。
     * @return
     */
    public int getVertexCount() {
        return vertices.size();
    }

    /**
     * 頂点を追加し、そのインデックスを返す。
     * @param v
     * @return
     */
    public int addVertex(MQOVertex v) {
        vertices.add(v);
        return vertices.size() - 1;
    }

    /**
     * 頂点を追加する。
     * @param vIndex
     * @param color
     * @param uv
     * @return
     */
    public int addVertex(int vIndex, Color color, Vector2 uv) {
        MQOVertex v = getVertex(vIndex);
        if (v.color == null) {
            v.color = new Color();
            v.color.r = color.r;
            v.color.g = color.g;
            v.color.b = color.b;
            v.color.a = color.a;

            v.uv = new Vector2();
            v.uv.x = uv.x;
            v.uv.y = uv.y;

            return vIndex;
        } else {
            //! 一時オブジェクトを生成
            MQOVertex n = new MQOVertex();
            n.position = v.position;
            n.color = new Color();
            n.color.r = color.r;
            n.color.g = color.g;
            n.color.b = color.b;
            n.color.a = color.a;

            n.uv = new Vector2();
            n.uv.x = uv.x;
            n.uv.y = uv.y;

            if (v.equals(n)) {
                return vIndex;
            } else {
                return addVertex(n);
            }
        }
    }
}
