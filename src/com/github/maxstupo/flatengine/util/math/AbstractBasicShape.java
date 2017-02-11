package com.github.maxstupo.flatengine.util.math;

/**
 * This class represents all basic shapes within FlatEngine.
 * 
 * @author Maxstupo
 */
public abstract class AbstractBasicShape {

    /** The x position of this shape. */
    protected float x;

    /** The y position of this shape. */
    protected float y;

    /**
     * A {@link AbstractBasicShape} with [0,0] position.
     */
    public AbstractBasicShape() {
        this(0, 0);
    }

    /**
     * AQ {@link AbstractBasicShape}.
     * 
     * @param x
     *            the x position of this shape.
     * @param y
     *            the y position of this shape.
     */
    public AbstractBasicShape(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns true if the given shape intersects this shape.
     * 
     * @param shape
     *            the shape to check.
     * @return true if the given shape intersects this shape.
     */
    public boolean intersects(AbstractBasicShape shape) {
        return AbstractBasicShape.intersects(this, shape);
    }

    /**
     * Returns true if this shape contains the given shape.
     * 
     * @param shape
     *            the shape.
     * @return true if this shape contains the given shape.
     */
    public boolean contains(AbstractBasicShape shape) {
        return AbstractBasicShape.contains(this, shape);
    }

    @Override
    public String toString() {
        return String.format("%s [x=%s, y=%s]", getClass().getSimpleName(), x, y);
    }

    /**
     * Sets the position of this shape.
     * 
     * @param x
     *            the x position of this shape.
     * @param y
     *            the y position of this shape.
     * @return this object for chaining.
     */
    public AbstractBasicShape setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Returns the x position of this shape.
     * 
     * @return the x position of this shape.
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x position of this shape.
     * 
     * @param x
     *            the x position.
     * @return this object for chaining.
     */
    public AbstractBasicShape setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Returns the y position of this shape.
     * 
     * @return the y position of this shape.
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the y position of this shape.
     * 
     * @param y
     *            the y position.
     * @return this object for chaining.
     */
    public AbstractBasicShape setY(float y) {
        this.y = y;
        return this;
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
        AbstractBasicShape other = (AbstractBasicShape) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
            return false;
        return true;
    }

    /**
     * Returns true if the given shape contains the shapeToTest.
     * 
     * @param shape
     *            the shape that might contain another.
     * @param shapeToTest
     *            the shape.
     * @return true if the given shape contains the shapeToTest.
     */
    public static boolean contains(AbstractBasicShape shape, AbstractBasicShape shapeToTest) {
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

    /**
     * Returns true if the given shapeToTest intersects the given shape.
     * 
     * @param shape
     *            the shape.
     * @param shapeToTest
     *            the shape that might be intersecting.
     * @return true if the given shape contains the shapeToTest.
     */
    public static boolean intersects(AbstractBasicShape shape, AbstractBasicShape shapeToTest) {
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
