package com.github.maxstupo.flatengine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.maxstupo.flatengine.map.Camera;

/**
 * This class allows for objects to be partitioned into a grid (chunks) which can increase performance. Rather than iterating over all objects, the
 * partitioner will allow iterating over select chunks. Using methods such as {@link #getObjectsInRectangle(int, int, int, int)},
 * {@link #getObjectsInArea(int, int, int)}, etc..
 * 
 * @author Maxstupo
 * @param <T>
 *            the object type.
 */
public class ObjectSpatialPartitioner<T extends AbstractSpatialObject<T>> {

    private final int gridWidth;
    private final int gridHeight;
    private final int chunkSize;

    private final ObjectChunk<T>[][] chunks;

    private final Map<String, T> registeredObjects = new HashMap<>();
    private final List<T> objects = new ArrayList<>();

    /**
     * Create a new {@link ObjectSpatialPartitioner} object.
     * 
     * @param gridWidth
     *            the number of units in width.
     * @param gridHeight
     *            the number of units in height.
     * @param chunkSize
     *            the number of units that make a chunk.
     */
    @SuppressWarnings("unchecked")
    public ObjectSpatialPartitioner(int gridWidth, int gridHeight, int chunkSize) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.chunkSize = chunkSize;
        this.chunks = new ObjectChunk[getChunkColumns()][getChunkRows()];

        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks[0].length; j++) {
                this.chunks[i][j] = new ObjectChunk<>(this, i, j);
            }
        }
    }

    /**
     * Adds the given object.
     * 
     * @param t
     *            the object.
     * @return false if the object {@link AbstractSpatialObject#getId() id} has already been registered.
     */
    public boolean add(T t) {
        if (hasRegistered(t))
            return false;
        registeredObjects.put(t.getId(), t);
        addObjectToChunkGrid(t);
        return true;
    }

    /**
     * Removes the given object.
     * 
     * @param t
     *            the object to remove.
     * @return false if the object {@link AbstractSpatialObject#getId() id} isn't registered.
     */
    public boolean remove(T t) {
        if (!hasRegistered(t))
            return false;

        int cx = getChunkXPositionOf(t);
        int cy = getChunkYPositionOf(t);
        ObjectChunk<T> chunk = getChunk(cx, cy);

        if (chunk != null) {
            if (chunk.remove(t)) {
                registeredObjects.remove(t.getId());
                return true;
            }

        }
        return false;
    }

    /**
     * Updates what chunk the object is stored in based on the position of the object. If the object isn't registered this method does nothing.
     * 
     * @param t
     *            the object.
     */
    protected void updateObject(T t) {
        int cx = getChunkXPositionOf(t);
        int cy = getChunkYPositionOf(t);
        int oldcx = getChunkXPositionOf(t);
        int oldcy = getChunkYPositionOf(t);

        if ((cx == oldcx && cy == oldcy) || !hasRegistered(t)) // Object hasn't moved chunks, or isn't registered.
            return;

        ObjectChunk<T> oldChunk = getChunk(oldcx, oldcy);
        if (oldChunk != null) {
            oldChunk.remove(t);
            addObjectToChunkGrid(t);

        }
    }

    private ObjectChunk<T> addObjectToChunkGrid(T t) {
        int cx = getChunkXPositionOf(t);
        int cy = getChunkYPositionOf(t);

        ObjectChunk<T> chunk = getChunk(cx, cy);
        if (chunk != null)
            chunk.add(t);
        return chunk;
    }

    /**
     * Returns all objects from all chunks within the defined 'radius' of the square with the center being the given object.
     * 
     * @param t
     *            the object used as a center point.
     * @param radius
     *            the 'radius' of the square measured in chunks.
     * @return a list of objects from all chunks within the defined 'radius' of the square with the center being the given object.
     */
    public List<T> getObjectsInArea(T t, int radius) {
        return getObjectsInArea((int) t.getX(), (int) t.getY(), radius);
    }

    /**
     * Returns all objects from all chunks within the defined 'radius' of the square.
     * 
     * @param cx
     *            the center point of the square on the x axis, measured in chunks.
     * @param cy
     *            the center point of the square on the y axis, measured in chunks.
     * @param radius
     *            the 'radius' of the square measured in chunks.
     * @return a list of objects from all chunks within the defined 'radius' of the square.
     */
    public List<T> getObjectsInArea(int cx, int cy, int radius) {
        return getObjectsInRectangle(cx - radius, cy - radius, radius * 2, radius * 2);
    }

    /**
     * Returns all objects from all chunks within the defined rectangle.
     * 
     * @param cx
     *            the upper left corner of the rectangle on the x axis, measured in chunks.
     * @param cy
     *            the upper left corner of the rectangle on the y axis, measured in chunks.
     * @param cw
     *            the width of the rectangle, measured in chunks.
     * @param ch
     *            the height of the rectangle, measured in chunks.
     * @return a list of objects from all chunks within the defined rectangle.
     */
    public List<T> getObjectsInRectangle(int cx, int cy, int cw, int ch) {
        objects.clear();

        int minX = Math.max(cx, 0);
        int maxX = Math.min(cx + cw, getChunkColumns());
        int minY = Math.max(cy, 0);
        int maxY = Math.min(cy + ch, getChunkRows());

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                ObjectChunk<T> chunk = getChunk(x, y);
                objects.addAll(chunk.getObjects());
            }
        }
        return objects;
    }

    /**
     * Returns all objects visible to the given camera.
     * 
     * @param camera
     *            the camera.
     * @return a list of objects visible to the camera.
     */
    public List<T> getObjectsVisible(Camera camera) {
        int[][] points = camera.getGridPoints(gridWidth, gridHeight);

        int cw = Math.abs(points[0][1] - points[0][0]) / chunkSize + 1;
        int ch = Math.abs(points[1][1] - points[1][0]) / chunkSize + 1;
        int cx = points[0][0];
        int cy = points[1][0];

        return getObjectsInRectangle(cx, cy, cw, ch);
    }

    /**
     * Returns the x chunk position of the given object.
     * 
     * @param t
     *            the object.
     * @return the x chunk position of the given object.
     */
    public int getChunkXPositionOf(T t) {
        return (int) (t.getX() / chunkSize);
    }

    /**
     * Returns the y chunk position of the given object.
     * 
     * @param t
     *            the object.
     * @return the y chunk position of the given object.
     */
    public int getChunkYPositionOf(T t) {
        return (int) (t.getY() / chunkSize);
    }

    /**
     * Returns the old x chunk position of the given object.
     * 
     * @param t
     *            the object.
     * @return the old x chunk position of the given object.
     */
    public int getOldChunkXPositionOf(T t) {
        return (int) (t.getOldX() / chunkSize);
    }

    /**
     * Returns the old y chunk position of the given object.
     * 
     * @param t
     *            the object.
     * @return the old y chunk position of the given object.
     */
    public int getOldChunkYPositionOf(T t) {
        return (int) (t.getOldY() / chunkSize);
    }

    /**
     * Returns the chunk at the given chunk x and y.
     * 
     * @param cx
     *            the x chunk position.
     * @param cy
     *            the y chunk position.
     * @return the chunk or null if the given position is invalid.
     */
    public ObjectChunk<T> getChunk(int cx, int cy) {
        if (!Util.isValid(chunks, cx, cy))
            return null;
        return chunks[cx][cy];
    }

    /**
     * Returns true if the given object is registered.
     * 
     * @param t
     *            the object to check.
     * @return true if the given object is registered.
     */
    public boolean hasRegistered(T t) {
        return hasRegistered(t.getId());
    }

    /**
     * Returns true if the given id is registered.
     * 
     * @param id
     *            the id to check.
     * @return true if the given id is registered.
     */
    public boolean hasRegistered(String id) {
        return registeredObjects.containsKey(id);
    }

    /**
     * Returns the number of chunk rows.
     * 
     * @return the number of chunk rows.
     */
    public int getChunkRows() {
        return gridHeight / chunkSize;
    }

    /**
     * Returns the number of chunk columns.
     * 
     * @return the number of chunk columns.
     */
    public int getChunkColumns() {
        return gridWidth / chunkSize;
    }

    @Override
    public String toString() {
        return String.format("%s [gridWidth=%s, gridHeight=%s, chunkSize=%s, registeredObjects=%s]", getClass().getSimpleName(), gridWidth, gridHeight, chunkSize, registeredObjects);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + chunkSize;
        result = prime * result + Arrays.deepHashCode(chunks);
        result = prime * result + gridHeight;
        result = prime * result + gridWidth;
        result = prime * result + ((registeredObjects == null) ? 0 : registeredObjects.hashCode());
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
        ObjectSpatialPartitioner<?> other = (ObjectSpatialPartitioner<?>) obj;
        if (chunkSize != other.chunkSize)
            return false;
        if (!Arrays.deepEquals(chunks, other.chunks))
            return false;
        if (gridHeight != other.gridHeight)
            return false;
        if (gridWidth != other.gridWidth)
            return false;
        if (registeredObjects == null) {
            if (other.registeredObjects != null)
                return false;
        } else if (!registeredObjects.equals(other.registeredObjects))
            return false;
        return true;
    }
}
