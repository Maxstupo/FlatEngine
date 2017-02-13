package com.github.maxstupo.flatengine.map.tile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.maxstupo.flatengine.util.Util;

/**
 * This class allows for storage of multiple tilesets, and allows for caching of tiles by global id.
 * 
 * @author Maxstupo
 */
public class TilesetStore {

    private int currentFirstGid;

    private final Map<Integer, Tileset> tilesets = new HashMap<>();

    private Tile[] tiles;

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

        tiles = new Tile[totalTiles];
        for (Entry<Integer, Tileset> entry : tilesets.entrySet()) {

            Tileset tileset = entry.getValue();

            for (int gid = entry.getKey(); gid < entry.getKey() + tileset.getTotalTiles(); gid++) {
                tiles[gid] = tileset.getTileByGid(gid);
            }
        }
        return this;
    }

    /**
     * Returns the tileset that has the given first global id.
     * 
     * @param firstgid
     *            the first global id of the tileset.
     * @return the tileset that has the given first global id.
     */
    public Tileset getTilesetByFirstGid(int firstgid) {
        return tilesets.get(firstgid);
    }

    /**
     * Returns the first tileset that has a name that matches the given name.
     * 
     * @param name
     *            the name of the tileset.
     * @return the first tileset that has a name that matches the given name.
     */
    public Tileset getTilesetByName(String name) {
        for (Tileset tileset : tilesets.values()) {
            if (tileset.getName().equals(name))
                return tileset;
        }
        return null;
    }

    /**
     * Returns an unmodifiable collection of all tilesets within this store.
     * 
     * @return an unmodifiable collection of all tilesets within this store.
     */
    public Collection<Tileset> getTilesets() {
        return Collections.unmodifiableCollection(tilesets.values());
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
    public Tile getTileByGlobalId(int gid) {
        if (tiles == null)
            recacheTiles();

        if (!Util.isValid(tiles, gid))
            return null;

        return tiles[gid];
    }

    @Override
    public String toString() {
        return String.format("%s [currentFirstGid=%s, tilesets=%s]", getClass().getSimpleName(), currentFirstGid, tilesets);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentFirstGid;
        result = prime * result + Arrays.hashCode(tiles);
        result = prime * result + ((tilesets == null) ? 0 : tilesets.hashCode());
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
        TilesetStore other = (TilesetStore) obj;
        if (currentFirstGid != other.currentFirstGid)
            return false;
        if (!Arrays.equals(tiles, other.tiles))
            return false;
        if (tilesets == null) {
            if (other.tilesets != null)
                return false;
        } else if (!tilesets.equals(other.tilesets))
            return false;
        return true;
    }

}
