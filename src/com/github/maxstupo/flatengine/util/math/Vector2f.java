package com.github.maxstupo.flatengine.util.math;

/**
 *
 * @author Maxstupo
 */
public class Vector2f {

    public static final Vector2f ZERO = new Vector2f(0f, 0f);

    public static final Vector2f UP = new Vector2f(0f, -1f);
    public static final Vector2f DOWN = new Vector2f(0f, 1f);
    public static final Vector2f LEFT = new Vector2f(-1f, 0f);
    public static final Vector2f RIGHT = new Vector2f(1f, 0f);

    public float x, y;

    public Vector2f() {
        this(0, 0);
    }

    public Vector2f(float x, float y) {
        set(x, y);
    }

    public float dot(Vector2f n) {
        if (n == null)
            return Float.NaN;
        return x * n.x + y * n.y;
    }

    public Vector2f lerp(Vector2f vec, float n) {
        this.x = UtilMath.lerpF(this.x, vec.x, n);
        this.y = UtilMath.lerpF(this.y, vec.y, n);
        return this;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2f normalizeLocal() {
        return scaleLocal(1f / length());
    }

    public Vector2f normalize() {
        return scale(1f / length());
    }

    public Vector2f set(Vector2f n) {
        if (n == null)
            return this;
        return set(n.x, n.y);
    }

    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f addLocal(Vector2f n) {
        if (n == null)
            return this;
        return addLocal(n.x, n.y);
    }

    public Vector2f subLocal(Vector2f n) {
        if (n == null)
            return this;
        return subLocal(n.x, n.y);
    }

    public Vector2f divLocal(Vector2f n) {
        if (n == null)
            return this;
        return divLocal(n.x, n.y);
    }

    public Vector2f mulLocal(Vector2f n) {
        if (n == null)
            return this;
        return mulLocal(n.x, n.y);
    }

    public Vector2f add(Vector2f n) {
        if (n == null)
            return this;
        return add(n.x, n.y);
    }

    public Vector2f sub(Vector2f n) {
        if (n == null)
            return this;
        return sub(n.x, n.y);
    }

    public Vector2f div(Vector2f n) {
        if (n == null)
            return this;
        return div(n.x, n.y);
    }

    public Vector2f mul(Vector2f n) {
        if (n == null)
            return this;
        return mul(n.x, n.y);
    }

    public Vector2f addLocal(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2f subLocal(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2f divLocal(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Vector2f mulLocal(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vector2f scaleLocal(float scalar) {
        return mulLocal(scalar, scalar);
    }

    public Vector2f add(float x, float y) {
        return copy().addLocal(x, y);
    }

    public Vector2f sub(float x, float y) {
        return copy().subLocal(x, y);
    }

    public Vector2f div(float x, float y) {
        return copy().divLocal(x, y);
    }

    public Vector2f mul(float x, float y) {
        return copy().mulLocal(x, y);
    }

    public Vector2f scale(float scalar) {
        return copy().scaleLocal(scalar);
    }

    public float distance(Vector2f n) {
        if (n == null)
            return Float.NaN;
        return distance(n.x, n.y);
    }

    public float distance(float x, float y) {
        float px = x - this.x;
        float py = y - this.y;
        return (float) Math.sqrt(px * px + py * py);
    }

    public Vector2f setAngle(float angle) {
        x = (float) Math.cos(angle);
        y = (float) Math.sin(angle);
        normalizeLocal();
        return this;
    }

    public Vector2f copy() {
        return new Vector2f(x, y);
    }

    @Override
    public String toString() {
        return "Vector2f [x=" + x + ", y=" + y + "]";
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void zero() {
        set(0, 0);
    }

    public Vector2i toVector2i() {
        return new Vector2i((int) x, (int) y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector2f other = (Vector2f) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
            return false;
        return true;
    }
}
