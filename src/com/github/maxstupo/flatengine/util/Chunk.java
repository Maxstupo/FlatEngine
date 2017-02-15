package com.github.maxstupo.flatengine.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains objects for a single chunk.
 * 
 * @author Maxstupo
 * @param <T>
 *            The object type.
 * @see ObjectSpatialPartitioner
 */
public class Chunk<T extends AbstractSpatialObject> {

    private final List<T> objects = new ArrayList<>();
    private final List<T> objectsToAdd = new ArrayList<>();

    /**
     * Adds all queued objects for this chunk into this chunk.
     */
    public void update() {
        objects.addAll(objectsToAdd);
        objectsToAdd.clear();
    }

    /**
     * Removes an object from this chunk.
     * 
     * @param t
     *            the object to remove.
     * @return true if object was removed.
     */
    public boolean removeEntity(T t) {
        if (objects.remove(t))
            return true;
        if (objectsToAdd.remove(t))
            return true;
        return false;
    }

    /**
     * Adds the given object into this chunk's queue. It will be added on the next call to {@link #update()}.
     * 
     * @param t
     *            the object to add.
     */
    public void add(T t) {
        objectsToAdd.add(t);
    }

    /**
     * Returns the object list.
     * 
     * @return the object list.
     */
    public List<T> getObjects() {
        return objects;
    }

    /**
     * Returns the number of objects within this chunk.
     * 
     * @return the number of objects within this chunk.
     */
    public int getTotalObjects() {
        return objects.size();
    }

    /**
     * Clears the list within this chunk.
     */
    public void clear() {
        objects.clear();
        objectsToAdd.clear();
    }

    /**
     * Returns the number of objects waiting to be added into the chunk.
     * 
     * @return the number of objects waiting to be added into the chunk.
     */
    public int getQueuedObjects() {
        return objectsToAdd.size();
    }

}
