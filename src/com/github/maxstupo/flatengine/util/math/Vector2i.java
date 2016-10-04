package com.github.maxstupo.flatengine.util.math;

/**
 *
 * @author Maxstupo
 */
public class Vector2i {

    public static final Vector2i ZERO = new Vector2i(0, 0);

    public static final Vector2i UP = new Vector2i(0, -1);
    public static final Vector2i DOWN = new Vector2i(0, 1);
    public static final Vector2i LEFT = new Vector2i(-1, 0);
    public static final Vector2i RIGHT = new Vector2i(1, 0);

    public int x, y;

    public Vector2i() {
        this(0, 0);
    }

    public Vector2i(int x, int y) {
        set(x, y);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2i set(Vector2i n) {
        if (n == null)
            return this;
        return set(n.x, n.y);
    }

    public Vector2i lerp(Vector2i vec, int n) {
        this.x = UtilMath.lerpI(this.x, vec.x, n);
        this.y = UtilMath.lerpI(this.y, vec.y, n);
        return this;
    }

    public Vector2i set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2i addLocal(Vector2i n) {
        if (n == null)
            return this;
        return addLocal(n.x, n.y);
    }

    public Vector2i subLocal(Vector2i n) {
        if (n == null)
            return this;
        return subLocal(n.x, n.y);
    }

    public Vector2i divLocal(Vector2i n) {
        if (n == null)
            return this;
        return divLocal(n.x, n.y);
    }

    public Vector2i mulLocal(Vector2i n) {
        if (n == null)
            return this;
        return mulLocal(n.x, n.y);
    }

    public Vector2i add(Vector2i n) {
        if (n == null)
            return this;
        return add(n.x, n.y);
    }

    public Vector2i sub(Vector2i n) {
        if (n == null)
            return this;
        return sub(n.x, n.y);
    }

    public Vector2i div(Vector2i n) {
        if (n == null)
            return this;
        return div(n.x, n.y);
    }

    public Vector2i mul(Vector2i n) {
        if (n == null)
            return this;
        return mul(n.x, n.y);
    }

    public Vector2i addLocal(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2i subLocal(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2i divLocal(int x, int y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Vector2i mulLocal(int x, int y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vector2i scaleLocal(int scalar) {
        return mulLocal(scalar, scalar);
    }

    public Vector2i add(int x, int y) {
        return copy().addLocal(x, y);
    }

    public Vector2i sub(int x, int y) {
        return copy().subLocal(x, y);
    }

    public Vector2i div(int x, int y) {
        return copy().divLocal(x, y);
    }

    public Vector2i mul(int x, int y) {
        return copy().mulLocal(x, y);
    }

    public Vector2i scale(int scalar) {
        return copy().scaleLocal(scalar);
    }

    public float distance(Vector2i n) {
        if (n == null)
            return -1;
        return distance(n.x, n.y);
    }

    public float distance(int x, int y) {
        float px = x - this.x;
        float py = y - this.y;
        return (float) Math.sqrt(px * px + py * py);
    }

    public Vector2i setAngle(int angle) {
        x = (int) Math.cos(angle);
        y = (int) Math.sin(angle);
        return this;
    }

    public Vector2i copy() {
        return new Vector2i(x, y);
    }

    @Override
    public String toString() {
        return "Vector2i [x=" + x + ", y=" + y + "]";
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void zero() {
        set(0, 0);
    }

    public Vector2f toVector2f() {
        return new Vector2f(x, y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
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
        Vector2i other = (Vector2i) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

}
