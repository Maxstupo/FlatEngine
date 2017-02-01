package com.github.maxstupo.flatengine.map.layer;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public interface ITileRenderer {

    /**
     * Renders a tile at the given position.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param layer
     *            the map layer this tile on.
     * @param camera
     *            the camera.
     * @param pos
     *            the pixel position of this tile.
     * @param gid
     *            the global id of the sprite to render.
     * @param i
     *            the x index of the tile grid of the given map layer.
     * @param j
     *            the y index of the tile grid of the given map layer.
     * @param isFringe
     *            true if the sprite should be rendered at actual size, and not resized to the tile size.
     */
    void renderTile(Graphics2D g, MapLayer layer, Camera camera, Vector2i pos, int gid, int i, int j, boolean isFringe);

}
