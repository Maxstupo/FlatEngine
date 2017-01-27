package com.github.maxstupo.flatengine.util.math;

/**
 * This class represents a 2D vector using integers.
 * 
 * @author Maxstupo
 */
public class Vector2i {

    /** A vector with a value of [0,0]. */
    public static final Vector2i ZERO = new Vector2i(0, 0);

    /** A vector with a value of [0,-1]. */
    public static final Vector2i UP = new Vector2i(0, -1);

    /** A vector with a value of [0,1]. */
    public static final Vector2i DOWN = new Vector2i(0, 1);

    /** A vector with a value of [-1,0]. */
    public static final Vector2i LEFT = new Vector2i(-1, 0);

    /** A vector with a value of [1,0]. */
    public static final Vector2i RIGHT = new Vector2i(1, 0);

    @SuppressWarnings("javadoc")
    public int x, y;

    /**
     * Creates a new {@link Vector2i} object set to [0,0].
     */
    public Vector2i() {
        this(0, 0);
    }

    /**
     * Create a new {@link Vector2i} object.
     * 
     * @param x
     *            the x value.
     * @param y
     *            the y value.
     */
    public Vector2i(int x, int y) {
        set(x, y);
    }

    /**
     * Returns the length of this vector.
     * 
     * @return the length of this vector.
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Sets this vector to equal the given vector. If the given vector is null this method does nothing.
     * 
     * @param n
     *            the vector.
     * @return this object for chaining.
     */
    public Vector2i set(Vector2i n) {
        if (n == null)
            return this;
        return set(n.x, n.y);
    }

    /**
     * Lerps this vector between the given vector and this vector by the given amount of n.
     * 
     * @param vec
     *            the second vector to lerp.
     * @param n
     *            the amount to lerp.
     * @return this object for chaining.
     */
    public Vector2i lerp(Vector2i vec, int n) {
        this.x = UtilMath.lerpI(this.x, vec.x, n);
        this.y = UtilMath.lerpI(this.y, vec.y, n);
        return this;
    }

    /**
     * Sets this vectors values.
     * 
     * @param x
     *            the x value.
     * @param y
     *            the y value.
     * @return this object for chaining.
     */
    public Vector2i set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Adds the given vector to this vector. If the given vector is null this method does nothing.
     * 
     * @param n
     *            the vector.
     * @return this object for chaining.
     */
    public Vector2i addLocal(Vector2i n) {
        if (n == null)
            return this;
        return addLocal(n.x, n.y);
    }

    /**
     * Subtracts the given vector from this vector. If the given vector is null this method does nothing.
     * 
     * @param n
     *            the vector.
     * @return this object for chaining.
     */
    public Vector2i subLocal(Vector2i n) {
        if (n == null)
            return this;
        return subLocal(n.x, n.y);
    }

    /**
     * Divides this vector by the given vector. If the given vector is null this method does nothing.
     * 
     * @param n
     *            the vector.
     * @return this object for chaining.
     */
    public Vector2i divLocal(Vector2i n) {
        if (n == null)
            return this;
        return divLocal(n.x, n.y);
    }

    /**
     * Multiplies this vector by the given vector. If the given vector is null this method does nothing.
     * 
     * @param n
     *            the vector.
     * @return this object for chaining.
     */
    public Vector2i mulLocal(Vector2i n) {
        if (n == null)
            return this;
        return mulLocal(n.x, n.y);
    }

    /**
     * Returns a new vector with the sum of this vector and the given vector. If the given vector is null this method returns a new vector of this
     * vector.
     * 
     * @param n
     *            the vector.
     * @return a new vector.
     */
    public Vector2i add(Vector2i n) {
        if (n == null)
            return copy();
        return add(n.x, n.y);
    }

    /**
     * Returns a new vector of this vector subtracted by the given vector. If the given vector is null this method returns a new vector of this
     * vector.
     * 
     * @param n
     *            the vector.
     * @return a new vector.
     */
    public Vector2i sub(Vector2i n) {
        if (n == null)
            return copy();
        return sub(n.x, n.y);
    }

    /**
     * Returns a new vector of this vector divided by the given vector. If the given vector is null this method returns a new vector of this vector.
     * 
     * @param n
     *            the vector.
     * @return a new vector.
     */
    public Vector2i div(Vector2i n) {
        if (n == null)
            return copy();
        return div(n.x, n.y);
    }

    /**
     * Returns a new vector of this vector multiplied by the given vector. If the given vector is null this method returns a new vector of this
     * vector.
     * 
     * @param n
     *            the vector.
     * @return a new vector.
     */
    public Vector2i mul(Vector2i n) {
        if (n == null)
            return copy();
        return mul(n.x, n.y);
    }

    /**
     * Adds the given [x,y] to this vector.
     * 
     * @param x
     *            the x value to add.
     * @param y
     *            the y value to add.
     * @return this vector for chaining.
     */
    public Vector2i addLocal(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Subtracts the given [x,y] from this vector.
     * 
     * @param x
     *            the x value to subtract.
     * @param y
     *            the y value to subtract.
     * @return this vector for chaining.
     */
    public Vector2i subLocal(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    /**
     * Divides this vector using the given [x,y].
     * 
     * @param x
     *            the x value.
     * @param y
     *            the y value.
     * @return this vector for chaining.
     */
    public Vector2i divLocal(int x, int y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    /**
     * Multiplies the given [x,y] values with this vector.
     * 
     * @param x
     *            the x value.
     * @param y
     *            the y value.
     * @return this vector for chaining.
     */
    public Vector2i mulLocal(int x, int y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    /**
     * Scales this vector.
     * 
     * @param scalar
     *            the amount to scale by.
     * @return this object for chaining.
     */
    public Vector2i scaleLocal(int scalar) {
        return mulLocal(scalar, scalar);
    }

    /**
     * Returns a new vector with the sum of this vector and the given [x,y] values.
     * 
     * @param x
     *            x value.
     * @param y
     *            y value.
     * @return a new vector.
     */
    public Vector2i add(int x, int y) {
        return copy().addLocal(x, y);
    }

    /**
     * Returns a new vector equal to this vector subtracted by the given [x,y] values.
     * 
     * @param x
     *            x value.
     * @param y
     *            y value.
     * @return a new vector.
     */
    public Vector2i sub(int x, int y) {
        return copy().subLocal(x, y);
    }

    /**
     * Returns a new vector equal to this vector divided by the given [x,y] values.
     * 
     * @param x
     *            x value.
     * @param y
     *            y value.
     * @return a new vector.
     */
    public Vector2i div(int x, int y) {
        return copy().divLocal(x, y);
    }

    /**
     * Returns a new vector equal to this vector multiplied by the given [x,y] values.
     * 
     * @param x
     *            x value.
     * @param y
     *            y value.
     * @return a new vector.
     */
    public Vector2i mul(int x, int y) {
        return copy().mulLocal(x, y);
    }

    /**
     * Returns a new vector equal to this vector scaled by the given amount.
     * 
     * @param scalar
     *            the amount to scale by.
     * @return a new vector.
     */
    public Vector2i scale(int scalar) {
        return copy().scaleLocal(scalar);
    }

    /**
     * Returns the distance between this vector and the given vector.
     * 
     * @param n
     *            the other vector.
     * 
     * @return the distance between this vector and the given vector, or -1 if the given vector is null.
     */
    public float distance(Vector2i n) {
        if (n == null)
            return -1;
        return distance(n.x, n.y);
    }

    /**
     * Returns the distance between this vector and the given x,y values.
     * 
     * @param x
     *            x axis.
     * @param y
     *            y axis.
     * @return the distance between this vector and the given x,y values.
     */
    public float distance(int x, int y) {
        float px = x - this.x;
        float py = y - this.y;
        return (float) Math.sqrt(px * px + py * py);
    }

    /**
     * Sets the angle of this vector.
     * 
     * @param angle
     *            the angle in radians.
     * @return this object for chaining.
     */
    public Vector2i setAngle(int angle) {
        x = (int) Math.cos(angle);
        y = (int) Math.sin(angle);
        return this;
    }

    /**
     * Returns a new {@link Vector2i} with the same values.
     * 
     * @return a new {@link Vector2i} with the same values.
     */
    public Vector2i copy() {
        return new Vector2i(x, y);
    }

    @Override
    public String toString() {
        return String.format("%s [x=%s, y=%s]", getClass().getSimpleName(), x, y);
    }

    /**
     * Sets this vector to [0,0].
     * 
     * @return this object for chaining.
     */
    public Vector2i zero() {
        set(0, 0);
        return this;
    }

    /**
     * Returns a new {@link Vector2f} of this {@link Vector2i} object.
     * 
     * @return a new {@link Vector2f} of this {@link Vector2i} object.
     */
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
