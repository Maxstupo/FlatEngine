package com.github.maxstupo.flatengine.util.math;

/**
 * This class represents a 2D vector using floats.
 * 
 * @author Maxstupo
 */
public class Vector2f {

    /** A vector with a value of [0,0]. */
    public static final Vector2f ZERO = new Vector2f(0f, 0f);

    /** A vector with a value of [0,-1]. */
    public static final Vector2f UP = new Vector2f(0f, -1f);

    /** A vector with a value of [0,1]. */
    public static final Vector2f DOWN = new Vector2f(0f, 1f);

    /** A vector with a value of [-1,0]. */
    public static final Vector2f LEFT = new Vector2f(-1f, 0f);

    /** A vector with a value of [1,0]. */
    public static final Vector2f RIGHT = new Vector2f(1f, 0f);

    @SuppressWarnings("javadoc")
    public float x, y;

    /**
     * Creates a new {@link Vector2f} object set to [0,0].
     */
    public Vector2f() {
        this(0, 0);
    }

    /**
     * Create a new {@link Vector2f} object.
     * 
     * @param x
     *            the x value.
     * @param y
     *            the y value.
     */
    public Vector2f(float x, float y) {
        set(x, y);
    }

    /**
     * Returns the dot product of this vector and the given vector.
     * 
     * @param n
     *            the other vector.
     * @return the dot product, or {@link Float#NaN} if the given vector is null.
     */
    public float dot(Vector2f n) {
        if (n == null)
            return Float.NaN;
        return x * n.x + y * n.y;
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
    public Vector2f lerp(Vector2f vec, float n) {
        this.x = UtilMath.lerpF(this.x, vec.x, n);
        this.y = UtilMath.lerpF(this.y, vec.y, n);
        return this;
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
     * Normalizes this vector.
     * 
     * @return this object for chaining.
     */
    public Vector2f normalizeLocal() {
        return scaleLocal(1f / length());
    }

    /**
     * Returns a new {@link Vector2f} set to this vectors values and normalized.
     * 
     * @return a new {@link Vector2f} set to this vectors values and normalized.
     */
    public Vector2f normalize() {
        return scale(1f / length());
    }

    /**
     * Sets this vector to equal the given vector. If the given vector is null this method does nothing.
     * 
     * @param n
     *            the vector.
     * @return this object for chaining.
     */
    public Vector2f set(Vector2f n) {
        if (n == null)
            return this;
        return set(n.x, n.y);
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
    public Vector2f set(float x, float y) {
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
    public Vector2f addLocal(Vector2f n) {
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
    public Vector2f subLocal(Vector2f n) {
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
    public Vector2f divLocal(Vector2f n) {
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
    public Vector2f mulLocal(Vector2f n) {
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
    public Vector2f add(Vector2f n) {
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
    public Vector2f sub(Vector2f n) {
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
    public Vector2f div(Vector2f n) {
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
    public Vector2f mul(Vector2f n) {
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
    public Vector2f addLocal(float x, float y) {
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
    public Vector2f subLocal(float x, float y) {
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
    public Vector2f divLocal(float x, float y) {
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
    public Vector2f mulLocal(float x, float y) {
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
    public Vector2f scaleLocal(float scalar) {
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
    public Vector2f add(float x, float y) {
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
    public Vector2f sub(float x, float y) {
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
    public Vector2f div(float x, float y) {
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
    public Vector2f mul(float x, float y) {
        return copy().mulLocal(x, y);
    }

    /**
     * Returns a new vector equal to this vector scaled by the given amount.
     * 
     * @param scalar
     *            the amount to scale by.
     * @return a new vector.
     */
    public Vector2f scale(float scalar) {
        return copy().scaleLocal(scalar);
    }

    /**
     * Returns the distance between this vector and the given vector.
     * 
     * @param n
     *            the other vector.
     * 
     * @return the distance between this vector and the given vector, or {@link Float#NaN} if the given vector is null.
     */
    public float distance(Vector2f n) {
        if (n == null)
            return Float.NaN;
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
    public float distance(float x, float y) {
        float px = x - this.x;
        float py = y - this.y;
        return (float) Math.sqrt(px * px + py * py);
    }

    /**
     * Sets the angle of this vector, and normalizes it.
     * 
     * @param angle
     *            the angle in radians.
     * @return this object for chaining.
     */
    public Vector2f setAngle(float angle) {
        x = (float) Math.cos(angle);
        y = (float) Math.sin(angle);
        normalizeLocal();
        return this;
    }

    /**
     * Returns a new {@link Vector2f} with the same values.
     * 
     * @return a new {@link Vector2f} with the same values.
     */
    public Vector2f copy() {
        return new Vector2f(x, y);
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
    public Vector2f zero() {
        return set(0, 0);
    }

    /**
     * Returns a new {@link Vector2i} of this {@link Vector2f} object.
     * 
     * @return a new {@link Vector2i} of this {@link Vector2f} object.
     */
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
