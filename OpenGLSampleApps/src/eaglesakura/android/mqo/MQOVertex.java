/**
 *
 */
package eaglesakura.android.mqo;

/**
 * MQOの頂点情報を管理する。<BR>
 * 一時オブジェクトであり、GL用には別途コンバートされる。
 */
public class MQOVertex {
    /**
     * 頂点位置情報。
     */
    public Vector3 position = new Vector3();

    /**
     * UV情報
     */
    public Vector2 uv = null;

    /**
     * 色情報
     */
    public Color color = null;

    @Override
    public boolean equals(Object o) {
        MQOVertex v = (MQOVertex) o;
        return v.position.equals(position) && v.uv.equals(uv) && v.color.equals(color);
    }
}
