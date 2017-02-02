package com.github.maxstupo.flatengine.map.layer;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This class is a basic implementation of {@link ITileRenderer}.
 * 
 * @author Maxstupo
 */
public class TileRenderer implements ITileRenderer {

    @Override
    public void renderTile(Graphics2D g, TileLayer layer, Camera camera, Vector2i pos, int gid, int i, int j, boolean isFringe) {
        Sprite spr = layer.getMap().getTilesetStore().getTileByGlobalId(gid);

        if (spr != null) {
            if (isFringe) {
                spr.draw(g, pos.x, pos.y);
            } else {
                spr.draw(g, pos.x, pos.y, camera.getTileSize(), camera.getTileSize());
            }
        }
    }

}
