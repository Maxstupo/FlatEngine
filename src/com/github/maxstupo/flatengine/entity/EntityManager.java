package com.github.maxstupo.flatengine.entity;

import com.github.maxstupo.flatengine.util.ObjectSpatialPartitioner;

/**
 * This class manages {@link AbstractEntity entities} using an {@link ObjectSpatialPartitioner} to keep performance up.
 * 
 * @author Maxstupo
 */
public class EntityManager extends ObjectSpatialPartitioner<AbstractEntity> {

    /**
     * Create a new {@link EntityManager} object.
     * 
     * @param gridWidth
     *            the width of the grid.
     * @param gridHeight
     *            the height of the grid.
     * @param chunkSize
     *            the size of each chunk on the grid.
     */
    public EntityManager(int gridWidth, int gridHeight, float chunkSize) {
        super(gridWidth, gridHeight, chunkSize);

    }

}
