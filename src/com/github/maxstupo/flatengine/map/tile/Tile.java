package com.github.maxstupo.flatengine.map.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.object.MapObject;

/**
 * This class represents a tile from a tileset, it contains both the sprite and properties of the tile.
 * 
 * @author Maxstupo
 */
public class Tile {

    private final Sprite sprite;
    private final MapProperties properties = new MapProperties();
    private final List<MapObject> collisionObjects = new ArrayList<>();

    /**
     * Create a new {@link Tile} object.
     * 
     * @param sprite
     *            the sprite that represents this tile.
     */
    public Tile(Sprite sprite) {
        this(sprite, null);
    }

    /**
     * Create a new {@link Tile} object.
     * 
     * @param sprite
     *            the sprite that represents this tile.
     * @param properties
     *            the properties of this tile.
     */
    public Tile(Sprite sprite, MapProperties properties) {
        this.sprite = sprite;
        this.properties.add(properties);
    }

    /**
     * Create a new {@link Tile} object.
     * 
     * @param sprite
     *            the sprite that represents this tile.
     * @param properties
     *            the properties of this tile.
     * @param collisionObjects
     *            objects representing the hitbox of this tile.
     */
    public Tile(Sprite sprite, MapProperties properties, List<MapObject> collisionObjects) {
        this.sprite = sprite;
        if (properties != null)
            this.properties.add(properties);
        if (collisionObjects != null)
            this.collisionObjects.addAll(collisionObjects);
    }

    /**
     * Returns the sprite that represents this tile.
     * 
     * @return the sprite that represents this tile.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Returns the properties of this tile.
     * 
     * @return the properties of this tile.
     */
    public MapProperties getProperties() {
        return properties;
    }

    /**
     * Returns an unmodifiable list of collision objects.
     * 
     * @return an unmodifiable list of collision objects.
     */
    public List<MapObject> getCollisionObjects() {
        return Collections.unmodifiableList(collisionObjects);
    }

    @Override
    public String toString() {
        return String.format("%s [sprite=%s, properties=%s]", getClass().getSimpleName(), sprite, properties);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
        result = prime * result + ((sprite == null) ? 0 : sprite.hashCode());
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
        Tile other = (Tile) obj;
        if (properties == null) {
            if (other.properties != null)
                return false;
        } else if (!properties.equals(other.properties))
            return false;
        if (sprite == null) {
            if (other.sprite != null)
                return false;
        } else if (!sprite.equals(other.sprite))
            return false;
        return true;
    }
}
