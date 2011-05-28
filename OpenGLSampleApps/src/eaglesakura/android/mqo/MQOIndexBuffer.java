package eaglesakura.android.mqo;

/**
 * MQOファイル用のインデックスバッファを構築する。<BR>
 * 内部のインデックスバッファはGL用に最低限の再構築を施している。
 * @author eaglesakura
 *
 */
public class MQOIndexBuffer {
    private short[] indices = null;
    private int current = 0;
    private int material = 0;

    /**
     *
     * @param triangles
     */
    public MQOIndexBuffer(int triangles) {
        indices = new short[triangles * 3];
    }

    /**
     * 三角形を追加する。
     *
     * @param v0
     * @param v1
     * @param v2
     */
    public void add(int v0, int v1, int v2) {
        indices[current] = (short) v0;
        ++current;
        indices[current] = (short) v1;
        ++current;
        indices[current] = (short) v2;
        ++current;
    }

    /**
     * インデックスバッファを取得する。
     *
     * @return
     */
    public short[] getIndices() {
        return indices;
    }

    /**
     * マテリアル番号取得。
     * @return
     */
    public int getMaterial() {
        return material;
    }

    /**
     * マテリアル番号設定。
     * @param material
     */
    public void setMaterial(int material) {
        this.material = material;
    }
}
