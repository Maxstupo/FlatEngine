package com.github.maxstupo.flatengine.util;

import com.github.maxstupo.flatengine.util.math.Vector2f;

/**
 * The base class that all objects must derive from to be added to a {@link ObjectSpatialPartitioner}. The class provides a position and an id.
 * 
 * @author Maxstupo
 * @param <T>
 *            the object type.
 *
 */
public abstract class AbstractSpatialObject<T extends AbstractSpatialObject<T>> {

    private final ObjectSpatialPartitioner<T> partitioner;
    private final Vector2f position = new Vector2f();
    private final Vector2f oldPosition = new Vector2f();
    private final String id;

    /**
     * 
     * @param partitioner
     *            the partitioner that owns this object.
     * @param id
     *            the id of this object.
     */
    public AbstractSpatialObject(ObjectSpatialPartitioner<T> partitioner, String id) {
        this.partitioner = partitioner;
        this.id = id;
    }

    /**
     * Sets the position of this object, and updates this object within the partitioner.
     * 
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     * @return this object for chaining.
     */
    @SuppressWarnings("unchecked")
    public AbstractSpatialObject<T> setPosition(float x, float y) {
        oldPosition.set(position);
        position.set(x, y);

        partitioner.updateObject((T) this);
        return this;
    }

    /**
     * Sets the x position of this object, and updates this object within the partitioner.
     * 
     * @param x
     *            the x position.
     * @return this object for chaining.
     */
    public AbstractSpatialObject<T> setX(float x) {
        return setPosition(x, getY());
    }

    /**
     * Sets the y position of this object, and updates this object within the partitioner.
     * 
     * @param y
     *            the y position.
     * @return this object for chaining.
     */
    public AbstractSpatialObject<T> setY(float y) {
        return setPosition(getX(), y);
    }

    /**
     * Returns the x position of this object.
     * 
     * @return the x position of this object.
     */
    public float getX() {
        return position.x;
    }

    /**
     * Returns the y position of this object.
     * 
     * @return the y position of this object.
     */
    public float getY() {
        return position.y;
    }

    /**
     * Returns the previous x position before {@link #setPosition(float, float)} was called.
     * 
     * @return the previous x position before {@link #setPosition(float, float)} was called.
     */
    public float getOldX() {
        return oldPosition.x;
    }

    /**
     * Returns the previous y position before {@link #setPosition(float, float)} was called.
     * 
     * @return the previous y position before {@link #setPosition(float, float)} was called.
     */
    public float getOldY() {
        return oldPosition.y;
    }

    /**
     * Returns the partitioner that owns this object.
     * 
     * @return the partitioner that owns this object.
     */
    public ObjectSpatialPartitioner<T> getPartitioner() {
        return partitioner;
    }

    /**
     * Returns the id of this object.
     * 
     * @return the id of this object.
     */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s [partitioner=%s, position=%s, oldPosition=%s, id=%s]", getClass().getSimpleName(), partitioner, position, oldPosition, id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((oldPosition == null) ? 0 : oldPosition.hashCode());
        result = prime * result + ((partitioner == null) ? 0 : partitioner.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
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
        AbstractSpatialObject<?> other = (AbstractSpatialObject<?>) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (oldPosition == null) {
            if (other.oldPosition != null)
                return false;
        } else if (!oldPosition.equals(other.oldPosition))
            return false;
        if (partitioner == null) {
            if (other.partitioner != null)
                return false;
        } else if (!partitioner.equals(other.partitioner))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }
}
