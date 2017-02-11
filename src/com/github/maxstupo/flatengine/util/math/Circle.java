package com.github.maxstupo.flatengine.util.math;

/**
 * @author Maxstupo
 *
 */
public class Circle extends BasicShape {

    protected float radius;

    public Circle(float cx, float cy, float radius) {
        super(cx, cy);
        this.radius = radius;
    }

    public boolean contains(Rectangle r) {
        return contains(r.x, r.y) && contains(r.x + r.width, r.y) && contains(r.x + r.width, r.y + r.height) && contains(r.x, r.y + r.height);
    }

    public boolean contains(float x, float y, float w, float h) {
        return contains(x, y) && contains(x + w, y) && contains(x + w, y + h) && contains(x, y + h);
    }

    public boolean contains(Circle c) {
        return contains(c.x, c.y, c.radius);
    }

    public boolean contains(float x, float y, float radius) {
        if (radius <= 0)
            return false;
        return Math.pow(this.x - x, 2f) + Math.pow(this.y - y, 2f) < Math.pow(Math.abs(this.radius - radius), 2);
    }

    public boolean contains(float x, float y) {
        return Math.pow(this.x - x, 2f) + Math.pow(this.y - y, 2f) < radius * radius;
    }

    public boolean intersects(Circle c) {
        return Math.pow(this.x - c.x, 2f) + Math.pow(this.y - c.y, 2f) <= Math.pow(Math.abs(this.radius + c.radius), 2);
    }

    public boolean intersects(Rectangle r) {
        return r.intersects(this);
    }

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

    public float getDiameter() {
        return getRadius() * 2f;
    }

    public void setDiameter(float diameter) {
        setRadius(diameter / 2f);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return String.format("Circle [radius=%s, x=%s, y=%s]", radius, x, y);
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
