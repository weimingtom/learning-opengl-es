package eaglesakura.android.mqo;

public class Vector2 {
    public float x = 0.0f;
    public float y = 0.0f;

    @Override
    public boolean equals(Object o) {
        Vector2 v = (Vector2) o;
        return v.x == x && v.y == y;
    }
}
