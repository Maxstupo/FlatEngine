package com.github.maxstupo.flatengine.util.math;

/**
 * @author Maxstupo
 *
 */
public class BasicShape {

    protected float x, y;

    public BasicShape(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean intersects(BasicShape shape) {
        return BasicShape.intersects(this, shape);
    }

    public boolean contains(BasicShape shape) {
        return BasicShape.contains(this, shape);
    }

    @Override
    public String toString() {
        return String.format("BaseShape [x=%s, y=%s]", x, y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
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
        BasicShape other = (BasicShape) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
            return false;
        return true;
    }

    public static boolean contains(BasicShape shape, BasicShape shapeToTest) {
        if (shape instanceof Rectangle) {

            Rectangle rectangle = (Rectangle) shape;

            if (shapeToTest instanceof Circle) { // Check if rectangle contains circle
                return rectangle.contains((Circle) shapeToTest);

            } else if (shapeToTest instanceof Rectangle) { // Check if rectangle contains rectangle
                return rectangle.contains((Rectangle) shapeToTest);

            }

        } else if (shape instanceof Circle) {

            Circle circle = (Circle) shape;

            if (shapeToTest instanceof Circle) { // Check if circle contains circle
                return circle.contains((Circle) shapeToTest);

            } else if (shapeToTest instanceof Rectangle) { // Check if circle contains rectangle
                return circle.contains((Rectangle) shapeToTest);

            }

        }

        return false;
    }

    public static boolean intersects(BasicShape shape, BasicShape shapeToTest) {
        if (shape instanceof Rectangle) {

            Rectangle rectangle = (Rectangle) shape;

            if (shapeToTest instanceof Circle) { // Check if rectangle intersects circle
                return rectangle.intersects((Circle) shapeToTest);

            } else if (shapeToTest instanceof Rectangle) { // Check if rectangle intersects rectangle
                return rectangle.intersects((Rectangle) shapeToTest);

            }

        } else if (shape instanceof Circle) {

            Circle circle = (Circle) shape;

            if (shapeToTest instanceof Circle) { // Check if circle intersects circle
                return circle.intersects((Circle) shapeToTest);

            } else if (shapeToTest instanceof Rectangle) { // Check if circle intersects rectangle
                return circle.intersects((Rectangle) shapeToTest);

            }

        }

        return false;
    }

}
