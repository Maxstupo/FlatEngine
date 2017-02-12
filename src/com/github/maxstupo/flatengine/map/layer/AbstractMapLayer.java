package com.github.maxstupo.flatengine.map.layer;

import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.util.math.UtilMath;

/**
 * @author Maxstupo
 *
 */
public abstract class AbstractMapLayer {

    /** The map that owns this layer. */
    protected final TiledMap map;

    /** The id of this layer. */
    protected final String id;

    /** The transparency of this layer. */
    protected final float alpha;

    /** If this layer will be rendered. */
    protected boolean isVisible;

    protected final MapProperties properties;

    public AbstractMapLayer(TiledMap map, String id, float alpha, boolean isVisible, MapProperties properties) {
        this.map = map;
        this.id = id;
        this.alpha = UtilMath.clampF(alpha, 0, 1);
        this.isVisible = (alpha <= 0) ? false : isVisible;
        this.properties = properties;
    }

    public MapProperties getProperties() {
        return properties;
    }

    /**
     * Returns true if this layer will be rendered.
     * 
     * @return true if this layer will be rendered.
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Sets if this layer will be rendered.
     * 
     * @param isVisible
     *            true to render this layer.
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Returns the transparency of this layer.
     * 
     * @return the transparency of this layer, a value between 0.0 and 1.0
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Returns the map that owns this layer.
     * 
     * @return the map that owns this layer.
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Returns the id of this layer.
     * 
     * @return the id of this layer.
     */
    public String getId() {
        return id;
    }

}
