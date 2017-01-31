package com.github.maxstupo.flatengine.map.layer;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.map.tile.Tile;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class MapLayer {

    protected final String id;
    protected final float alpha;

    protected final TiledMap map;

    protected Tile[][] tiles;

    protected final AlphaComposite composite;

    public MapLayer(TiledMap map, String id, float alpha) {
        this.map = map;
        this.id = id;
        this.alpha = alpha;
        this.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    public void render(Graphics2D g, Camera camera) {
        if (alpha <= 0)
            return;

        Composite defaultComposite = g.getComposite();
        {
            g.setComposite(composite);

            int[][] points = camera.getGridPoints(map.getWidth(), map.getHeight());

            for (int x = points[0][0]; x < points[0][1]; x++) {
                for (int y = points[1][0]; y < points[1][1]; y++) {

                    Vector2i pos = camera.getRenderLocation(x, y);
                    if (camera.isOutOfBounds(pos))
                        continue;

                    tiles[x][y].render(g);
                }
            }
        }
        g.setComposite(defaultComposite);
    }

    public AlphaComposite getComposite() {
        return composite;
    }

    public String getId() {
        return id;
    }

    public float getAlpha() {
        return alpha;
    }

    public TiledMap getMap() {
        return map;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

}
