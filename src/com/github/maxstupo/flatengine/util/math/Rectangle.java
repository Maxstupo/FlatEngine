package com.github.maxstupo.flatengine.util.math;

/**
 * @author Maxstupo
 *
 */
public class Rectangle extends BasicShape {

    protected float width, height;

    public Rectangle(float x, float y, float width, float height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    public boolean contains(Rectangle r) {
        return contains(r.x, r.y, r.width, r.height);
    }

    public boolean contains(Circle c) {
        return contains(c.x, c.y, c.radius);
    }

    public boolean intersects(Circle c) {
        return intersects(c.x, c.y, c.radius);
    }

    public boolean intersects(Rectangle r) {
        return intersects(r.x, r.y, r.width, r.height);
    }

    public boolean contains(float x, float y, float radius) {
        return contains(x - radius, y - radius, radius * 2f, radius * 2f);
    }

    public boolean contains(float x, float y) {
        return x >= this.x && y >= this.y && x <= this.x + this.width && y <= this.y + this.height;
    }

    public boolean contains(float x, float y, float w, float h) {
        if (w <= 0 || h <= 0)
            return false;
        return contains(x, y) && contains(x + w, y) && contains(x + w, y + h) && contains(x, y + h);
    }

    public boolean intersects(float x, float y, float w, float h) {
        if (w <= 0 || h <= 0)
            return false;
        return (this.x < x + w) && (this.x + this.width > x) && (this.y < y + h) && (this.y + this.height > y);
    }

    public boolean intersects(float x, float y, float radius) {
        if (radius <= 0)
            return false;

        float distX = Math.abs(x - this.x - width / 2f);
        float distY = Math.abs(y - this.y - height / 2f);

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
        return String.format("Rectangle [x=%s, y=%s, width=%s, height=%s]", x, y, width, height);
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
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
