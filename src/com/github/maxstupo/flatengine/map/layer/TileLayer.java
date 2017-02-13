package com.github.maxstupo.flatengine.map.layer;

import java.awt.Graphics2D;
import java.util.Arrays;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This class represents a single layer of tiles arranged in a grid.
 * 
 * @author Maxstupo
 */
public class TileLayer extends AbstractMapLayer {

    /** A grid of global ids used to represent this tile map layer. */
    protected final int[][] tiles;

    /** The tile renderer used to render each tile of this layer. */
    protected ITileRenderer tileRenderer = new TileRenderer();

    /**
     * Create a new {@link TileLayer} object.
     * 
     * @param map
     *            the map that owns this layer.
     * @param id
     *            the id of this layer.
     * @param alpha
     *            the transparency of this layer, between 0.0 - 1.0
     * @param isVisible
     *            true to render this layer.
     * @param properties
     *            the properties of this layer.
     */
    public TileLayer(TiledMap map, String id, float alpha, boolean isVisible, MapProperties properties) {
        super(map, id, alpha, isVisible, properties);
        this.tiles = new int[map.getWidth()][map.getHeight()];
    }

    @Override
    public void render(Graphics2D g, Camera camera) {
        int[][] points = camera.getGridPoints(map.getWidth(), map.getHeight());

        for (int x = points[0][0]; x < points[0][1]; x++) {
            for (int y = points[1][0]; y < points[1][1]; y++) {

                Vector2i pos = camera.getRenderLocation(x, y);
                if (camera.isOutOfBounds(pos))
                    continue;

                tileRenderer.renderTile(g, this, camera, pos, tiles[x][y], x, y);
            }
        }
    }

    /**
     * Sets the tile at the given x,y position.
     * 
     * @param x
     *            the x tile position.
     * @param y
     *            the y tile position.
     * @param gid
     *            the global tile id.
     */
    public void setTileAt(int x, int y, int gid) {
        if (!Util.isValid(tiles, x, y))
            return;
        tiles[x][y] = gid;
    }

    /**
     * Sets the tile renderer for this map layer, if null is given {@link TileRenderer} is used.
     * 
     * @param tileRenderer
     *            the tile renderer.
     */
    public void setTileRenderer(ITileRenderer tileRenderer) {
        this.tileRenderer = (tileRenderer != null) ? tileRenderer : new TileRenderer();
    }

    /**
     * Returns the grid of global ids used to represent this tile map layer.
     * 
     * @return the grid of global ids used to represent this tile map layer.
     */
    public int[][] getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        return String.format("%s [tileRenderer=%s, map=%s, id=%s, isVisible=%s, properties=%s]", getClass().getSimpleName(), tileRenderer, map, id, isVisible, properties);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.deepHashCode(tiles);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TileLayer other = (TileLayer) obj;
        if (!Arrays.deepEquals(tiles, other.tiles))
            return false;
        return true;
    }

}
