package com.github.maxstupo.flatengine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.util.math.Vector2f;

/**
 * This class handles objects in a grid allowing for fast retrieval. It splits the objects stored into groups (chunks) allowing for efficient
 * retrieval and updating.
 * 
 * @author Maxstupo
 * @param <T>
 *            The object type.
 */
public class ObjectSpatialPartitioner<T extends AbstractSpatialObject> {

    /** The size of each chunk */
    protected final float chunkSize;

    private final Map<String, T> registeredEntities = new HashMap<>();
    private final Chunk<T>[][] chunks;

    /** ArrayList for methods of {@link ObjectSpatialPartitioner}. */
    private final List<T> objects = new ArrayList<>();

    /**
     * Create a new {@link ObjectSpatialPartitioner} object.
     * 
     * @param gridWidth
     *            the width of the grid.
     * @param gridHeight
     *            the height of the grid.
     * @param chunkSize
     *            the size of each chunk.
     */
    @SuppressWarnings("unchecked")
    public ObjectSpatialPartitioner(int gridWidth, int gridHeight, float chunkSize) {
        this.chunkSize = chunkSize;

        int chunkColumns = (gridWidth / (int) chunkSize) + 1;
        int chunkRows = (gridHeight / (int) chunkSize) + 1;

        this.chunks = new Chunk[chunkColumns][chunkRows];

        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                chunks[x][y] = new Chunk<>();
            }
        }
    }

    /**
     * Updates all objects in a rectangle around the given object.
     * 
     * @param deltaTime
     *            the time span between the current frame and the last frame in seconds.
     * @param camera
     *            the camera.
     * @param chunkRadius
     *            the radius of the rectangle, in chunks.
     * @param updateAround
     *            the object to center the update around.
     * @return the total objects updated.
     */
    public int updateObjects(float deltaTime, Camera camera, int chunkRadius, AbstractSpatialObject updateAround) {

        int cx = (int) (updateAround.getPosition().x / chunkSize);
        int cy = (int) (updateAround.getPosition().y / chunkSize);

        final int minX = (int) Math.max(cx - chunkRadius, 0f);
        final int maxX = (int) Math.min(cx + chunkRadius + 1f, getChunkColumns());
        final int minY = (int) Math.max(cy - chunkRadius, 0f);
        final int maxY = (int) Math.min(cy + chunkRadius + 1f, getChunkRows());
        int totalObjectsUpdated = 0;

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                Chunk<T> chunk = getChunk(x, y);

                Iterator<T> it = chunk.getObjects().iterator();

                while (it.hasNext()) {
                    T t = it.next();

                    if (t.isFlaggedForRemoval()) {
                        it.remove();
                        registeredEntities.remove(t.getId());
                    } else {

                        totalObjectsUpdated++;

                        t.update(deltaTime, camera);

                        updateChunkPosition(t);
                    }

                }
                chunk.update();

            }
        }
        return totalObjectsUpdated;
    }

    /**
     * Updates what chunk the given object is in. Called by {@link #updateObjects(float, Camera, int, AbstractSpatialObject)} and by
     * {@link AbstractSpatialObject#setPosition(Vector2f)}.
     * 
     * @param obj
     *            the object to update.
     * @return true if a new chunk was set for the given object.
     */
    protected boolean updateChunkPosition(T obj) {
        int cx = (int) (obj.getPosition().x / chunkSize);
        int cy = (int) (obj.getPosition().y / chunkSize);
        int oldCx = (int) (obj.getOldPosition().x / chunkSize);
        int oldCy = (int) (obj.getOldPosition().y / chunkSize);

        if ((cx == oldCx && cy == oldCy) || !registeredEntities.containsKey(obj.getId()))
            return false;

        Chunk<T> chunk = getChunk(oldCx, oldCy);
        if (chunk == null)
            return false;

        if (!chunk.removeEntity(obj))
            return false;

        addObjectToChunk(obj);
        return true;
    }

    /**
     * Add a object.
     * 
     * @param obj
     *            the object to add.
     * @return the object given for chaining.
     * @throws IllegalArgumentException
     *             if the id of the given object is already registered.
     */
    public T add(T obj) throws IllegalArgumentException {
        if (!registeredEntities.containsKey(obj.getId())) {
            addObjectToChunk(obj);
            registeredEntities.put(obj.getId(), obj);
            return obj;

        } else {
            throw new IllegalArgumentException("Object id already registered for object: " + obj);
        }
    }

    private void addObjectToChunk(T obj) {
        float cx = obj.getPosition().x / chunkSize;
        float cy = obj.getPosition().y / chunkSize;
        Chunk<T> chunk = getChunk((int) cx, (int) cy);

        if (chunk != null)
            chunk.add(obj);

    }

    /**
     * Returns an array containing all objects within chunks of the given object and the given radius.
     * 
     * @param obj
     *            the object to center the collection from.
     * @param radiusX
     *            the radius along the x axis, in chunks.
     * @param radiusY
     *            the radius along the y axis, in chunks.
     * @return an array containing objects within the radius of the given object.
     */
    public List<T> getObjectsInArea(T obj, int radiusX, int radiusY) {
        float cx = obj.getPosition().x / chunkSize;
        float cy = obj.getPosition().y / chunkSize;

        int minX = (int) Math.max(cx - radiusX, 0f);
        int maxX = (int) Math.min(cx + radiusX + 1f, chunks.length);
        int minY = (int) Math.max(cy - radiusY, 0f);
        int maxY = (int) Math.min(cy + radiusY + 1f, chunks[0].length);

        objects.clear();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                Chunk<T> c = getChunk(x, y);
                objects.addAll(c.getObjects());

            }
        }
        return objects;
    }

    /**
     * Returns all objects visible on the given camera, centered around the given objects.
     * 
     * @param obj
     *            the objects to center around.
     * @param camera
     *            the camera to get objects within view.
     * @return all objects visible on the given camera.
     */
    public List<T> getObjectsVisible(T obj, Camera camera) {
        final int chunksWidth = (int) (camera.getViewportWidth() / chunkSize);
        final int chunksHeight = (int) (camera.getViewportHeight() / chunkSize);
        return getObjectsInArea(obj, (int) ((chunksWidth / 2f) + 1f), (int) ((chunksHeight / 2f) + 1f));
    }

    /**
     * Returns true if the given id is registered to a object within this {@link ObjectSpatialPartitioner}.
     * 
     * @param id
     *            the id to check.
     * @return true if the given id is registered to a object.
     */
    public boolean isRegistered(String id) {
        return registeredEntities.containsKey(id);
    }

    /**
     * Returns true if the object is registered within this {@link ObjectSpatialPartitioner}.
     * 
     * @param obj
     *            the object to check.
     * @return true if the given object is registered.
     */
    public boolean isRegistered(T obj) {
        return isRegistered(obj.getId());
    }

    /**
     * Removes all objects from this {@link ObjectSpatialPartitioner}.
     */
    public void clear() {
        registeredEntities.clear();
        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                chunks[x][y].clear();
            }
        }
    }

    /**
     * Returns a {@link Chunk} at the specific chunk positions.
     * 
     * @param pos
     *            the position of the chunk.
     * @return the chunk, or null if given position is invalid.
     */
    public Chunk<T> getChunk(Vector2f pos) {
        return getChunk((int) pos.x, (int) pos.y);
    }

    /**
     * Returns a {@link Chunk} at the specific chunkX, chunkY positions.
     * 
     * @param cx
     *            the x position of the chunk.
     * @param cy
     *            the y position of the chunk.
     * @return the chunk, or null if given position is invalid.
     */
    public Chunk<T> getChunk(int cx, int cy) {
        if (!Util.isValid(chunks, cx, cy))
            return null;
        return chunks[cx][cy];
    }

    /**
     * Returns the total objects within this {@link ObjectSpatialPartitioner}.
     * 
     * @return the total objects.
     */
    public int getTotalEntities() {
        return registeredEntities.size();
    }

    /**
     * Returns the average objects per chunk.
     * 
     * @return the average objects per chunk.
     */
    public float getAverageEntitiesPerChunk() {
        return (float) getTotalEntities() / getTotalChunks();
    }

    /**
     * Returns the total chunks this {@link ObjectSpatialPartitioner} has.
     * 
     * @return the total chunks.
     */
    public int getTotalChunks() {
        return chunks.length * chunks[0].length;
    }

    /**
     * Returns the size of each chunk.
     * 
     * @return the chunk size.
     */
    public float getChunkSize() {
        return chunkSize;
    }

    /**
     * Returns the number of rows this partitioner has.
     * 
     * @return the number of rows this partitioner has.
     */
    public int getChunkRows() {
        return chunks[0].length;
    }

    /**
     * Returns the number of columns this partitioner has.
     * 
     * @return the number of columns this partitioner has.
     */
    public int getChunkColumns() {
        return chunks.length;
    }
}
