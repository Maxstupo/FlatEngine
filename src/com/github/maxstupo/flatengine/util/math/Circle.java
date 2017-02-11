package com.github.maxstupo.flatengine.util.math;

/**
 * This class represents a 2D circle shape.
 * 
 * @author Maxstupo
 */
public class Circle extends AbstractBasicShape {

    /** The radius of this circle. */
    protected float radius;

    /**
     * Create a new {@link Circle} shape object, with all zero values.
     */
    public Circle() {
        this(0, 0, 0);
    }

    /**
     * Create a new {@link Circle} shape object.
     * 
     * @param cx
     *            the center point of the circle along the x axis.
     * @param cy
     *            the center point of the circle along the y axis.
     * @param radius
     *            the radius of the circle.
     */
    public Circle(float cx, float cy, float radius) {
        super(cx, cy);
        this.radius = radius;
    }

    /**
     * Returns true if the given rectangle is within this circle.
     * 
     * @param r
     *            the rectangle.
     * @return true if the given rectangle is within this circle.
     */
    public boolean contains(Rectangle r) {
        return contains(r.x, r.y) && contains(r.x + r.width, r.y) && contains(r.x + r.width, r.y + r.height) && contains(r.x, r.y + r.height);
    }

    /**
     * Returns true if the given rectangle is within this circle.
     * 
     * @param x
     *            x position of the rectangle, top-left origin.
     * @param y
     *            y position of the rectangle, top-left origin.
     * @param w
     *            the width of the rectangle.
     * @param h
     *            the height of the rectangle.
     * @return true if the given rectangle is within this circle.
     */
    public boolean contains(float x, float y, float w, float h) {
        return contains(x, y) && contains(x + w, y) && contains(x + w, y + h) && contains(x, y + h);
    }

    /**
     * Returns true if the given circle is within this circle.
     * 
     * @param c
     *            the circle.
     * @return true if the given circle is within this circle.
     */
    public boolean contains(Circle c) {
        return contains(c.x, c.y, c.radius);
    }

    /**
     * Returns true if the given circle is within this circle.
     * 
     * @param cx
     *            the center point of the circle along the x axis.
     * @param cy
     *            the center point of the circle along the y axis.
     * @param radius
     *            the radius of the circle.
     * @return true if the given circle is within this circle.
     */
    public boolean contains(float cx, float cy, float radius) {
        if (radius <= 0)
            return false;
        return Math.pow(this.x - cx, 2f) + Math.pow(this.y - cy, 2f) < Math.pow(Math.abs(this.radius - radius), 2);
    }

    /**
     * Returns true if the given point is within this circle.
     * 
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     * @return true if the given point is within this circle.
     */
    public boolean contains(float x, float y) {
        return Math.pow(this.x - x, 2f) + Math.pow(this.y - y, 2f) < radius * radius;
    }

    /**
     * Returns true if the given circle intersects with this circle.
     * 
     * @param c
     *            the circle.
     * @return true if the given circle intersects with this circle.
     */
    public boolean intersects(Circle c) {
        return Math.pow(this.x - c.x, 2f) + Math.pow(this.y - c.y, 2f) <= Math.pow(Math.abs(this.radius + c.radius), 2);
    }

    /**
     * Returns true if the given rectangle intersects with this circle.
     * 
     * @param r
     *            the rectangle.
     * @return true if the given rectangle intersects with this circle.
     */
    public boolean intersects(Rectangle r) {
        return r.intersects(this);
    }

    /**
     * Returns true if the given rectangle intersects with this circle.
     * 
     * @param x
     *            x position of the rectangle, top-left origin.
     * @param y
     *            y position of the rectangle, top-left origin.
     * @param w
     *            the width of the rectangle.
     * @param h
     *            the height of the rectangle.
     * @return true if the given rectangle intersects with this circle.
     */
    public boolean intersects(float x, float y, float w, float h) {
        if (w <= 0 || h <= 0)
            return false;

        float distX = Math.abs(x - this.x + w / 2f);
        float distY = Math.abs(y - this.y + h / 2f);

        if (distX > (w / 2f + radius) || distY > (h / 2f + radius))
            return false;

        if (distX <= (w / 2f) || distY <= (h / 2f))
            return true;

        float dx = distX - w / 2f;
        float dy = distY - h / 2f;
        return (dx * dx + dy * dy <= (radius * radius));
    }

    /**
     * Returns the diameter of this circle.
     * 
     * @return the diameter of this circle.
     */
    public float getDiameter() {
        return getRadius() * 2f;
    }

    /**
     * Sets the diameter of this circle.
     * 
     * @param diameter
     *            the diameter.
     * @return this object for chaining.
     */
    public Circle setDiameter(float diameter) {
        return setRadius(diameter / 2f);
    }

    /**
     * Returns the radius of this circle.
     * 
     * @return the radius of this circle.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Sets the radius of this circle.
     * 
     * @param radius
     *            the radius.
     * @return this object for chaining.
     */
    public Circle setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s [radius=%s, x=%s, y=%s]", getClass().getSimpleName(), radius, x, y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Float.floatToIntBits(radius);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Circle other = (Circle) obj;
        if (Float.floatToIntBits(radius) != Float.floatToIntBits(other.radius))
            return false;
        return true;
    }

}
