package com.github.maxstupo.flatengine.util;

import com.github.maxstupo.flatengine.util.math.Vector2f;

/**
 * @author Maxstupo
 *
 */
public abstract class AbstractSpatialObject<T extends AbstractSpatialObject<T>> {

    private final ObjectSpatialPartitioner<T> partitioner;
    private final Vector2f position = new Vector2f();
    private final Vector2f oldPosition = new Vector2f();
    private final String id;

    public AbstractSpatialObject(ObjectSpatialPartitioner<T> partitioner, String id) {
        this.partitioner = partitioner;
        this.id = id;
    }

    public AbstractSpatialObject<T> setPosition(float x, float y) {
        oldPosition.set(position);
        position.set(x, y);

        partitioner.updateObject((T) this);
        return this;
    }

    public AbstractSpatialObject<T> setX(float x) {
        return setPosition(x, getY());
    }

    public AbstractSpatialObject<T> setY(float y) {
        return setPosition(getX(), y);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getOldX() {
        return oldPosition.x;
    }

    public float getOldY() {
        return oldPosition.y;
    }

    public ObjectSpatialPartitioner<T> getPartitioner() {
        return partitioner;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s [partitioner=%s, position=%s, oldPosition=%s, id=%s]", getClass().getSimpleName(), partitioner, position, oldPosition, id);
    }
}
