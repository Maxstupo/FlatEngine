package com.github.maxstupo.flatengine.map.tile;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.UtilGraphics;

/**
 * This class represents a tileset, it will split a given tileset image into tiles that can be obtained via {@link #getTileByGid(int)} or
 * {@link #getTileByLocalId(int)}.
 * 
 * @author Maxstupo
 */
public class Tileset {

    private final int firstGid;
    private final String name;

    private final int tileWidth;
    private final int tileHeight;

    private final int tileSpacing;
    private final int tileMargin;

    private final Tile[] tiles;

    private final MapProperties properties = new MapProperties();

    /**
     * Create a new {@link Tileset} object.
     * 
     * @param firstGid
     *            the first global id of the first tile within this tileset.
     * @param name
     *            the name of this tileset.
     * @param tileWidth
     *            the width in pixels for each tile.
     * @param tileHeight
     *            the height in pixels for each tile.
     * @param tileSpacing
     *            the spacing of each tile in pixels.
     * @param tileMargin
     *            the margin of the tileset in pixels.
     * @param tilesetImage
     *            the tileset image.
     * @param tileProperties
     *            properties for each tile, set to null to ignore. Note: The array length can be less than the total tiles within the tileset.
     */
    public Tileset(int firstGid, String name, int tileWidth, int tileHeight, int tileSpacing, int tileMargin, BufferedImage tilesetImage, Map<Integer, MapProperties> tileProperties) {
        this.firstGid = firstGid;
        this.name = name;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileSpacing = tileSpacing;
        this.tileMargin = tileMargin;

        BufferedImage[] tileImages = UtilGraphics.getTileImages(tilesetImage, tileWidth, tileHeight, tileSpacing, tileMargin);
        this.tiles = new Tile[tileImages.length];

        for (int i = 0; i < tiles.length; i++) {
            Sprite sprite = new Sprite(tileImages[i], name + "_" + firstGid + "_" + i);

            MapProperties properties = (tileProperties != null) ? tileProperties.get(i) : null;

            this.tiles[i] = new Tile(sprite, properties);
        }

    }

    /**
     * Returns the tile of the given global id.
     * 
     * @param gid
     *            the global id of the tile.
     * @return the tile or null if the given global id is out of bounds.
     */
    public Tile getTileByGid(int gid) {
        if (!Util.isValid(tiles, gid - firstGid))
            return null;
        return tiles[gid - firstGid];
    }

    /**
     * Returns the tile of the given local id.
     * 
     * @param id
     *            the local id between 0 and ({@link #getTotalTiles()} - 1).
     * @return the tile or null if the given local id is out of bounds.
     */
    public Tile getTileByLocalId(int id) {
        if (!Util.isValid(tiles, id))
            return null;
        return tiles[id];
    }

    /**
     * Returns the first global id of the first tile in this tileset.
     * 
     * @return the first global id of the first tile in this tileset.
     */
    public int getFirstGid() {
        return firstGid;
    }

    /**
     * Returns the name of this tileset.
     * 
     * @return the name of this tileset.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the width in pixels for each tile.
     * 
     * @return the width in pixels for each tile.
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * Returns the height in pixels for each tile.
     * 
     * @return the height in pixels for each tile.
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * Returns the tile spacing in pixels.
     * 
     * @return the tile spacing in pixels.
     */
    public int getTileSpacing() {
        return tileSpacing;
    }

    /**
     * Returns the tile margin in pixels.
     * 
     * @return the tile margin in pixels.
     */
    public int getTileMargin() {
        return tileMargin;
    }

    /**
     * Returns the properties of this tileset.
     * 
     * @return the properties of this tileset.
     */
    public MapProperties getProperties() {
        return properties;
    }

    /**
     * Returns the total number of tiles this tileset contains.
     * 
     * @return the total number of tiles this tileset contains.
     */
    public int getTotalTiles() {
        return tiles.length;
    }

    /**
     * Returns the upper global id of this tileset.
     * 
     * @return the upper global id of this tileset.
     */
    public int getUpperGid() {
        return getFirstGid() + getTotalTiles();
    }

    @Override
    public String toString() {
        return String.format("%s [firstGid=%s, name=%s, tileWidth=%s, tileHeight=%s, tileSpacing=%s, tileMargin=%s, tiles=%s, properties=%s]", getClass().getSimpleName(), firstGid, name, tileWidth, tileHeight, tileSpacing, tileMargin, Arrays.toString(tiles), properties);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + firstGid;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
        result = prime * result + tileHeight;
        result = prime * result + tileMargin;
        result = prime * result + tileSpacing;
        result = prime * result + tileWidth;
        result = prime * result + Arrays.hashCode(tiles);
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
        Tileset other = (Tileset) obj;
        if (firstGid != other.firstGid)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (properties == null) {
            if (other.properties != null)
                return false;
        } else if (!properties.equals(other.properties))
            return false;
        if (tileHeight != other.tileHeight)
            return false;
        if (tileMargin != other.tileMargin)
            return false;
        if (tileSpacing != other.tileSpacing)
            return false;
        if (tileWidth != other.tileWidth)
            return false;
        if (!Arrays.equals(tiles, other.tiles))
            return false;
        return true;
    }

}
