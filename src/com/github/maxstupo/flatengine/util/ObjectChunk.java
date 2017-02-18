package com.github.maxstupo.flatengine.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class stores a portion of objects from a {@link ObjectSpatialPartitioner}
 * 
 * @author Maxstupo
 * @param <T>
 *            the object type.
 *
 */
public class ObjectChunk<T extends AbstractSpatialObject<T>> {

    private final ObjectSpatialPartitioner<T> partitioner;
    private final int x;
    private final int y;

    private final List<T> objects = new ArrayList<>();

    /**
     * Create a new {@link ObjectChunk} object.
     * 
     * @param partitioner
     *            the partitioner that owns this chunk.
     * @param cx
     *            the x chunk position.
     * @param cy
     *            the y chunk position.
     */
    public ObjectChunk(ObjectSpatialPartitioner<T> partitioner, int cx, int cy) {
        this.partitioner = partitioner;
        this.x = cx;
        this.y = cy;
    }

    /**
     * Adds the given object to this chunk.
     * 
     * @param t
     *            the object to add.
     */
    protected void add(T t) {
        objects.add(t);
    }

    /**
     * Removes the given object from this chunk.
     * 
     * @param t
     *            the object to remove.
     * @return true if the object was removed.
     */
    protected boolean remove(T t) {
        return objects.remove(t);
    }

    /**
     * Returns the partitioner that owns this chunk.
     * 
     * @return the partitioner that owns this chunk.
     */
    public ObjectSpatialPartitioner<T> getPartitioner() {
        return partitioner;
    }

    /**
     * Returns the x chunk position.
     * 
     * @return the x chunk position.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y chunk position.
     * 
     * @return the y chunk position.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a unmodifiable list of objects stored within this chunk.
     * 
     * @return a unmodifiable list of objects stored within this chunk.
     */
    public List<T> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((objects == null) ? 0 : objects.hashCode());
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
        ObjectChunk<?> other = (ObjectChunk<?>) obj;
        if (objects == null) {
            if (other.objects != null)
                return false;
        } else if (!objects.equals(other.objects))
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

}
