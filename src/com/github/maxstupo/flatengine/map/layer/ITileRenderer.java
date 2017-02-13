package com.github.maxstupo.flatengine.map.layer;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This interface allows {@link TileLayer}s to render each tile.
 * 
 * @author Maxstupo
 *
 */
public interface ITileRenderer {

    /**
     * Render a tile at the given position.
     * 
     * @param g
     *            the graphics context to render to.
     * @param layer
     *            the map layer this tile is on.
     * @param camera
     *            the camera.
     * @param pos
     *            the pixel position of this tile.
     * @param gid
     *            the global id of tile to render.
     * @param i
     *            the x index of the tile grid of the given map layer.
     * @param j
     *            the y index of the tile grid of the given map layer.
     */
    void renderTile(Graphics2D g, TileLayer layer, Camera camera, Vector2i pos, int gid, int i, int j);

}
