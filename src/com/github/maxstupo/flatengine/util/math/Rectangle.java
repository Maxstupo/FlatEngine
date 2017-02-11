package com.github.maxstupo.flatengine.util.math;

/**
 * This class represents a 2D rectangle shape.
 * 
 * @author Maxstupo
 */
public class Rectangle extends AbstractBasicShape {

    /** The width of this shape. */
    protected float width;

    /** The height of this shape. */
    protected float height;

    /**
     * Create a new {@link Rectangle} shape object, with all zero values.
     */
    public Rectangle() {
        this(0, 0, 0, 0);
    }

    /**
     * Create a new {@link Rectangle} shape object.
     * 
     * @param x
     *            x position of the rectangle, top-left origin.
     * @param y
     *            y position of the rectangle, top-left origin.
     * @param width
     *            the width of the rectangle.
     * @param height
     *            the height of the rectangle.
     */
    public Rectangle(float x, float y, float width, float height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    /**
     * Returns true if this rectangle contains the given rectangle.
     * 
     * @param r
     *            the rectangle.
     * @return true if this rectangle contains the given rectangle.
     */
    public boolean contains(Rectangle r) {
        return contains(r.x, r.y, r.width, r.height);
    }

    /**
     * Returns true if this rectangle contains the given circle.
     * 
     * @param c
     *            the circle.
     * @return true if this rectangle contains the given circle.
     */
    public boolean contains(Circle c) {
        return contains(c.x, c.y, c.radius);
    }

    /**
     * Returns true if this rectangle intersects the given circle.
     * 
     * @param c
     *            the circle.
     * @return true if this rectangle intersects the given circle.
     */
    public boolean intersects(Circle c) {
        return intersects(c.x, c.y, c.radius);
    }

    /**
     * Returns true if this rectangle intersects the given rectangle.
     * 
     * @param r
     *            the rectangle.
     * @return true if this rectangle intersects the given rectangle.
     */
    public boolean intersects(Rectangle r) {
        return intersects(r.x, r.y, r.width, r.height);
    }

    /**
     * Returns true if this rectangle contains the given circle.
     * 
     * @param cx
     *            the center point of the circle along the x axis.
     * @param cy
     *            the center point of the circle along the y axis.
     * @param radius
     *            the radius of the circle.
     * @return true if this rectangle contains the given circle.
     */
    public boolean contains(float cx, float cy, float radius) {
        return contains(cx - radius, cy - radius, radius * 2f, radius * 2f);
    }

    /**
     * Returns true if the given point is within this rectangle.
     * 
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     * @return true if the given point is within this rectangle.
     */
    public boolean contains(float x, float y) {
        return x >= this.x && y >= this.y && x <= this.x + this.width && y <= this.y + this.height;
    }

    /**
     * Returns true if this rectangle contains the given rectangle.
     * 
     * @param x
     *            x position of the rectangle, top-left origin.
     * @param y
     *            y position of the rectangle, top-left origin.
     * @param w
     *            the width of the rectangle.
     * @param h
     *            the height of the rectangle.
     * @return true if this rectangle contains the given rectangle.
     */
    public boolean contains(float x, float y, float w, float h) {
        if (w <= 0 || h <= 0)
            return false;
        return contains(x, y) && contains(x + w, y) && contains(x + w, y + h) && contains(x, y + h);
    }

    /**
     * Returns true if the given rectangle intersects this rectangle.
     * 
     * @param x
     *            x position of the rectangle, top-left origin.
     * @param y
     *            y position of the rectangle, top-left origin.
     * @param w
     *            the width of the rectangle.
     * @param h
     *            the height of the rectangle.
     * @return true if the given rectangle intersects this rectangle.
     */
    public boolean intersects(float x, float y, float w, float h) {
        if (w <= 0 || h <= 0)
            return false;
        return (this.x < x + w) && (this.x + this.width > x) && (this.y < y + h) && (this.y + this.height > y);
    }

    /**
     * Returns true if the given circle intersects this rectangle.
     * 
     * @param cx
     *            the center point of the circle along the x axis.
     * @param cy
     *            the center point of the circle along the y axis.
     * @param radius
     *            the radius of the circle.
     * @return true if the given circle intersects this circle.
     */
    public boolean intersects(float cx, float cy, float radius) {
        if (radius <= 0)
            return false;

        float distX = Math.abs(cx - this.x - width / 2f);
        float distY = Math.abs(cy - this.y - height / 2f);

        if (distX > (this.width / 2f + radius) || distY > (this.height / 2f + radius))
            return false;

        if (distX <= (this.width / 2f) || distY <= (this.height / 2f))
            return true;

        float dx = distX - this.width / 2f;
        float dy = distY - this.height / 2f;
        return (dx * dx + dy * dy <= (radius * radius));
    }

    @Override
    public String toString() {
        return String.format("%s [x=%s, y=%s, width=%s, height=%s]", getClass().getSimpleName(), x, y, width, height);
    }

    /**
     * Sets the width and height of this shape.
     * 
     * @param width
     *            the width of this shape.
     * @param height
     *            the height of this shape.
     * @return this object for chaining.
     */
    public Rectangle setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Returns the width of this shape.
     * 
     * @return the width of this shape.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Sets the width of this shape.
     * 
     * @param width
     *            the width of this shape.
     * @return this object for chaining.
     */
    public Rectangle setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * Returns the height of this shape.
     * 
     * @return the height of this shape.
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the height of this shape.
     * 
     * @param height
     *            the height of this shape.
     * @return this object for chaining.
     */
    public Rectangle setHeight(float height) {
        this.height = height;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Float.floatToIntBits(height);
        result = prime * result + Float.floatToIntBits(width);
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
        Rectangle other = (Rectangle) obj;
        if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
            return false;
        if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width))
            return false;
        return true;
    }

}
