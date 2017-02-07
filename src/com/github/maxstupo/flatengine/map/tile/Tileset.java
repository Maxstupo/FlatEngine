package com.github.maxstupo.flatengine.map.tile;

import java.awt.image.BufferedImage;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.Util;

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

    private final Sprite[] tiles;

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
     * @param tileMargin
     * @param tileset
     *            the tileset image.
     */
    public Tileset(int firstGid, String name, int tileWidth, int tileHeight, int tileSpacing, int tileMargin, BufferedImage tileset) {
        this.firstGid = firstGid;
        this.name = name;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tiles = new Sprite[(tileset.getWidth() / tileWidth) * (tileset.getHeight() / tileHeight)];

        int index = 0;
        for (int j = 0; j < tileset.getHeight() / tileHeight; j++) {
            for (int i = 0; i < tileset.getWidth() / tileWidth; i++) {

                BufferedImage tileImage = tileset.getSubimage(i * (tileWidth + tileSpacing), j * (tileHeight + tileSpacing), tileWidth, tileHeight);

                tiles[index++] = new Sprite(tileImage, name + "_" + (firstGid + index));
            }
        }
    }

    /**
     * Returns the tile of the given global id.
     * 
     * @param gid
     *            the global id of the tile.
     * @return the tile or null if the given global id is out of bounds.
     */
    public Sprite getTileByGid(int gid) {
        if (!Util.isValid(tiles, gid - this.firstGid))
            return null;

        return tiles[gid - this.firstGid];
    }

    /**
     * Returns the tile of the given local id.
     * 
     * @param id
     *            the local id between 0 and ({@link #getTotalTiles()} - 1).
     * @return the tile or null if the given local id is out of bounds.
     */
    public Sprite getTileByLocalId(int id) {
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
        return String.format("%s [gid=%s, name=%s, tileWidth=%s, tileHeight=%s]", getClass().getSimpleName(), firstGid, name, tileWidth, tileHeight);
    }

}
