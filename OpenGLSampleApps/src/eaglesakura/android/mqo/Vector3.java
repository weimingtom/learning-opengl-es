package eaglesakura.android.mqo;

public class Vector3 {
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    public Vector3() {

    }

    /**
     * 値を指定して初期化する。
     * @param x
     * @param y
     * @param z
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * コピーを行う。
     * @param origin
     */
    public Vector3(Vector3 origin) {
        set(origin);
    }

    /**
     * 値のコピーを行う。
     * @param origin
     */
    public void set(Vector3 origin) {
        x = origin.x;
        y = origin.y;
        z = origin.z;
    }

    /**
     * 値のコピーを行う。
     * @param origin
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 内積を取得する。
     * @param v
     * @return
     */
    public float dot(Vector3 v) {
        return (x * v.x) + (y * v.y) + (z * v.z);
    }

    /**
     * 内積を取得する。
     * @param _x
     * @param _y
     * @param _z
     * @return
     */
    public float dot(float _x, float _y, float _z) {
        return (x * _x) + (y * _y) + (z * _z);
    }

    /**
     * 外積を取得する。
     * @param v
     * @param result
     * @return
     */
    public Vector3 cross(Vector3 v, Vector3 result) {
        result.set((y * v.z) - (z * v.y), (z * v.x) - (x * v.z), (x * v.y) - (y * v.x));
        return result;
    }

    /**
     * 外積を求め、このインスタンスに格納する。
     * @param _x
     * @param _y
     * @param _z
     */
    public void cross(float _x, float _y, float _z) {
        set((y * _z) - (z * _y), (z * _x) - (x * _z), (x * _y) - (y * _x));
    }

    /**
     * v0 - v1を計算し、このインスタンスに格納する。
     * @param v0
     * @param v1
     */
    public void sub(Vector3 v0, Vector3 v1) {
        x = v0.x - v1.x;
        y = v0.y - v1.y;
        z = v0.z - v1.z;
    }

    /**
     * 長さを取得する。
     * @return
     */
    public float length() {
        return (float) Math.sqrt((double) ((x * x) + (y * y) + (z * z)));
    }

    /**
     * ベクトルの長さを正規化する。
     */
    public void normalize() {
        final float len = length();
        x /= len;
        y /= len;
        z /= len;
    }

    @Override
    public boolean equals(Object o) {
        Vector3 v = (Vector3) o;
        return v.x == x && v.y == y && v.z == z;
    }
}
