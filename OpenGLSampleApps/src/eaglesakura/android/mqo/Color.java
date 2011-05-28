package eaglesakura.android.mqo;

/**
 * MQOの色情報を扱う。
 * @author eaglesakura
 *
 */
public class Color {
    public float r = 1.0f;
    public float g = 1.0f;
    public float b = 1.0f;
    public float a = 1.0f;

    public Color() {

    }

    public void set(int argb) {
        int ia = (argb >> 24) & 0xff;
        int ir = (argb >> 16) & 0xff;
        int ig = (argb >> 8) & 0xff;
        int ib = (argb) & 0xff;

        r = ((float) ir) / 255.0f;
        g = ((float) ig) / 255.0f;
        b = ((float) ib) / 255.0f;
        a = ((float) ia) / 255.0f;
    }

    @Override
    public boolean equals(Object o) {
        Color c = (Color) o;
        return c.r == r && c.g == g && c.b == b && c.a == a;
    }
}
