package com.github.maxstupo.flatengine.map.layer;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This class represents a single layer of tiles arranged in a grid.
 * 
 * @author Maxstupo
 */
public class TileLayer extends AbstractMapLayer {

    /** A grid of global ids used to represent this tile map layer. */
    protected final int[][] tiles;

    /** The {@link AlphaComposite} used to apply transparency to this layer. */
    protected final AlphaComposite composite;

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
     *            a value between 0.0 and 1.0, representing the transparency of this layer.
     * @param isVisible
     * 
     */
    public TileLayer(TiledMap map, String id, float alpha, boolean isVisible, MapProperties properties) {
        super(map, id, alpha, isVisible, properties);

        this.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, UtilMath.clampF(alpha, 0, 1));
        this.tiles = new int[map.getWidth()][map.getHeight()];
    }

    /**
     * Render this tile layer.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param camera
     *            the camera.
     */
    public void render(Graphics2D g, Camera camera) {
        if (alpha <= 0 || !isVisible)
            return;

        Composite defaultComposite = g.getComposite();
        {
            g.setComposite(composite);

            int[][] points = camera.getGridPoints(map.getWidth(), map.getHeight());
            // System.out.println(points[0][1] - points[0][0]);
            for (int x = points[0][0]; x < points[0][1]; x++) {
                for (int y = points[1][0]; y < points[1][1]; y++) {

                    Vector2i pos = camera.getRenderLocation(x, y);
                    if (camera.isOutOfBounds(pos))
                        continue;

                    tileRenderer.renderTile(g, this, camera, pos, tiles[x][y], x, y, false);
                }
            }
        }
        g.setComposite(defaultComposite);
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
     * Returns the {@link AlphaComposite} used to apply transparency to this layer.
     * 
     * @return the {@link AlphaComposite} used to apply transparency to this layer.
     */
    public AlphaComposite getComposite() {
        return composite;
    }

    /**
     * Returns the grid of global ids used to represent this tile map layer.
     * 
     * @return the grid of global ids used to represent this tile map layer.
     */
    public int[][] getTiles() {
        return tiles;
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

    // XXX: Debug method
    @SuppressWarnings("javadoc")
    public void fillRandom(int... gid) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++)
                tiles[i][j] = gid[Rand.INSTANCE.nextIntRange(0, gid.length - 1)];
        }
    }

    // XXX: Debug method
    @SuppressWarnings("javadoc")
    public void fill(int gid) {
        fillRandom(gid, gid);
    }

    @Override
    public String toString() {
        return String.format("TileLayer [map=%s, id=%s, alpha=%s, isVisible=%s, tileRenderer=%s]", map, id, alpha, isVisible, tileRenderer.getClass().getSimpleName());
    }

}
