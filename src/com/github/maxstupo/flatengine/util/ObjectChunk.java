package com.github.maxstupo.flatengine.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxstupo
 *
 */
public class ObjectChunk<T extends AbstractSpatialObject<T>> {

    private final ObjectSpatialPartitioner<T> partitioner;
    private final int x;
    private final int y;

    private final List<T> objects = new ArrayList<>();

    public ObjectChunk(ObjectSpatialPartitioner<T> partitioner, int cx, int cy) {
        this.partitioner = partitioner;
        this.x = cx;
        this.y = cy;
    }

    protected void add(T t) {
        objects.add(t);
    }

    protected boolean remove(T t) {
        return objects.remove(t);
    }

    public ObjectSpatialPartitioner<T> getPartitioner() {
        return partitioner;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<T> getObjects() {
        return Collections.unmodifiableList(objects);
    }

}
