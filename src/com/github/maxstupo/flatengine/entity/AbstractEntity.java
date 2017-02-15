package com.github.maxstupo.flatengine.entity;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.util.AbstractSpatialObject;
import com.github.maxstupo.flatengine.util.ObjectSpatialPartitioner;
import com.github.maxstupo.flatengine.util.math.Vector2f;

/**
 * @author Maxstupo
 *
 */
public class AbstractEntity extends AbstractSpatialObject {

    private final Vector2f oldPosition = new Vector2f();
    private final Vector2f velocity = new Vector2f();

    private boolean isDead;

    public AbstractEntity(ObjectSpatialPartitioner<AbstractSpatialObject> partitioner, String id, Vector2f position) {
        super(partitioner, id);
        setPosition(position);
    }

    @Override
    public void update(float deltaTime, Camera camera) {
        oldPosition.set(getPosition());
    }

    public AbstractEntity setDead() {
        isDead = true;
        return this;
    }

    public boolean isDead() {
        return isDead;
    }

    @Override
    public boolean isFlaggedForRemoval() {
        return isDead();
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    @Override
    public Vector2f getOldPosition() {
        return oldPosition;
    }

}
