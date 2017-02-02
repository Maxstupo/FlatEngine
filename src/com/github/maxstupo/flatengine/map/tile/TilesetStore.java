package com.github.maxstupo.flatengine.map.tile;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.Util;

/**
 * This class allows for storage of multiple tilesets, and allows for caching of tiles by global id.
 * 
 * @author Maxstupo
 */
public class TilesetStore {

    private int currentFirstGid;

    private final Map<Integer, Tileset> tilesets = new HashMap<>();

    private Sprite[] tiles;

    /**
     * Adds a given tileset to this store.
     * 
     * @param tileset
     *            the tileset to add.
     * @param useTilesetGlobalIds
     *            if true {@link Tileset#getFirstGid()} is used instead of the index within this store.
     * @return this object for chaining.
     */
    public TilesetStore addTileset(Tileset tileset, boolean useTilesetGlobalIds) {
        if (useTilesetGlobalIds) {
            tilesets.put(tileset.getFirstGid(), tileset);

        } else {
            tilesets.put(currentFirstGid, tileset);

        }

        currentFirstGid += tileset.getTotalTiles();
        return this;
    }

    /**
     * Converts all stored tilesets into a single array of tiles allowing for fast access to each tile.
     * <p>
     * Call this method after adding all tilesets.
     * 
     * @return this object for chaining.
     */
    public TilesetStore recacheTiles() {
        int totalTiles = 0;
        for (Entry<Integer, Tileset> entry : tilesets.entrySet())
            totalTiles = Math.max(totalTiles, entry.getKey() + entry.getValue().getTotalTiles());

        tiles = new Sprite[totalTiles];
        for (Entry<Integer, Tileset> entry : tilesets.entrySet()) {

            Tileset tileset = entry.getValue();

            for (int gid = entry.getKey(); gid < entry.getKey() + tileset.getTotalTiles(); gid++) {

                if (tiles[gid] != null) {
                    System.err.println("Warn: " + gid + " not null!"); // TODO: Use logger.
                } else {
                    tiles[gid] = tileset.getTileByGid(gid);
                    // System.out.println("Tile[" + gid + "] = Tileset(\"" + tileset.getName() + "\"," + tileset.getTotalTiles() + ")"); // TODO: Use
                    // logger.
                }
            }
        }
        return this;
    }

    /**
     * Returns a sprite referenced by a global id. If {@link #recacheTiles()} hasn't been called yet it will be.
     * <p>
     * If the given id is out of bounds null will be returned.
     * 
     * @param gid
     *            the global id of the tile.
     * @return the sprite representing the tile, or null if the given id is out of bounds.
     */
    public Sprite getTileByGlobalId(int gid) {
        if (tiles == null)
            recacheTiles();

        if (!Util.isValid(tiles, gid))
            return null;

        return tiles[gid];
    }

}
