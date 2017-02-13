package com.github.maxstupo.flatengine.map.tile;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.map.MapProperties;

/**
 * This class represents a tile from a tileset, it contains both the sprite and properties of the tile.
 * 
 * @author Maxstupo
 */
public class Tile {

    private final Sprite sprite;
    private final MapProperties properties = new MapProperties();

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
