package com.github.maxstupo.flatengine.util;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.util.math.Vector2f;

/**
 * This class provides a base for all objects used within the {@link ObjectSpatialPartitioner}.
 * 
 * @author Maxstupo
 */
public abstract class AbstractSpatialObject {

    private final String id;

    private final ObjectSpatialPartitioner<AbstractSpatialObject> partitioner;

    private final Vector2f position = new Vector2f();

    /**
     * A spatial object.
     * 
     * @param partitioner
     *            the partitioner that owns this object.
     * @param id
     *            the id of this object.
     */
    public AbstractSpatialObject(ObjectSpatialPartitioner<AbstractSpatialObject> partitioner, String id) {
        this.id = id;
        this.partitioner = partitioner;
    }

    /**
     * Called when {@link ObjectSpatialPartitioner#updateObjects(float, Camera, int, AbstractSpatialObject)} is called and this object is in a chunk
     * that is updated.
     * 
     * @param deltaTime
     *            the time span between the current frame and the last frame in seconds.
     * @param camera
     *            the camera.
     */
    public abstract void update(float deltaTime, Camera camera);

    /**
     * Return true to remove this object upon the next update of the partitioner.
     * 
     * @return true to remove this object upon the next update of the partitioner.
     */
    public abstract boolean isFlaggedForRemoval();

    /**
     * Returns the old position of this object. The old position is used to move the object between chunks.
     * 
     * @return the old position of this object.
     */
    public abstract Vector2f getOldPosition();

    /**
     * Sets the position, and updates the chunk position within the {@link ObjectSpatialPartitioner} that owns this object.
     * 
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     * 
     * @return this object for chaining.
     */
    public AbstractSpatialObject setPosition(float x, float y) {
        this.position.set(position);
        this.partitioner.updateChunkPosition(this);
        return this;
    }

    /**
     * Convenience method for {@link #setPosition(Vector2f)}.
     * 
     * @param position
     *            the position.
     * @return this object for chaining.
     */
    public AbstractSpatialObject setPosition(Vector2f position) {
        return setPosition(position.x, position.y);
    }

    /**
     * Returns the object position.
     * <p>
     * DO NOT use this vector to set the position, instead use {@link #setPosition(Vector2f)} or {@link #setPosition(float, float)}.
     * 
     * @return the object position.
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * Returns the id of this object.
     * 
     * @return the id of this object.
     */
    public String getId() {
        return id;
    }
}
