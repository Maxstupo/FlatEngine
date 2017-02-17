package com.github.maxstupo.flatengine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class ObjectSpatialPartitioner<T extends AbstractSpatialObject<T>> {

    private final int gridWidth;
    private final int gridHeight;
    private final int chunkSize;

    private final ObjectChunk<T>[][] chunks;

    private final Map<String, T> registeredObjects = new HashMap<>();
    private final List<T> objects = new ArrayList<>();

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

    public boolean add(T t) {
        if (hasRegistered(t))
            return false;
        registeredObjects.put(t.getId(), t);
        addObjectToChunkGrid(t);
        return true;
    }

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

    public List<T> getObjectsInArea(T t, int radius) {
        return getObjectsInArea((int) t.getX(), (int) t.getY(), radius);
    }

    public List<T> getObjectsInArea(int cx, int cy, int radius) {
        return getObjectsInRectangle(cx - radius, cy - radius, radius * 2, radius * 2);
    }

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

    public List<T> getObjectsVisible(Camera camera) {
        int[][] points = camera.getGridPoints(gridWidth, gridHeight);

        int cw = Math.abs(points[0][1] - points[0][0]) / chunkSize + 1;
        int ch = Math.abs(points[1][1] - points[1][0]) / chunkSize + 1;
        int cx = points[0][0];
        int cy = points[1][0];

        return getObjectsInRectangle(cx, cy, cw, ch);
    }

    public int getChunkXPositionOf(T t) {
        return (int) (t.getX() / chunkSize);
    }

    public int getChunkYPositionOf(T t) {
        return (int) (t.getY() / chunkSize);
    }

    public int getOldChunkXPositionOf(T t) {
        return (int) (t.getOldX() / chunkSize);
    }

    public int getOldChunkYPositionOf(T t) {
        return (int) (t.getOldY() / chunkSize);
    }

    public ObjectChunk<T> getChunk(Vector2i pos) {
        return getChunk(pos.x, pos.y);
    }

    public ObjectChunk<T> getChunk(int cx, int cy) {
        if (!Util.isValid(chunks, cx, cy))
            return null;
        return chunks[cx][cy];
    }

    public boolean hasRegistered(T t) {
        return hasRegistered(t.getId());
    }

    public boolean hasRegistered(String id) {
        return registeredObjects.containsKey(id);
    }

    public int getChunkRows() {
        return gridHeight / chunkSize;
    }

    public int getChunkColumns() {
        return gridWidth / chunkSize;
    }
}
