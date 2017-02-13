package com.github.maxstupo.flatengine.map.layer;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.util.math.UtilMath;

/**
 * This class is the base for all map layers (e.g. tile/object layers).
 * 
 * @author Maxstupo
 */
public abstract class AbstractMapLayer {

    /** The map that owns this layer. */
    protected final TiledMap map;

    /** The id of this layer. */
    protected final String id;

    private float alpha;
    private AlphaComposite composite;

    /** If this layer will be rendered. */
    protected boolean isVisible;

    /** The properties for this layer. */
    protected final MapProperties properties = new MapProperties();

    /**
     * A map layer.
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
    public AbstractMapLayer(TiledMap map, String id, float alpha, boolean isVisible, MapProperties properties) {
        this.map = map;
        this.id = id;
        this.setAlpha(alpha);
        this.isVisible = isVisible;
        this.properties.add(properties);
    }

    /**
     * Render this layer.
     * 
     * @param g
     *            the graphics context to render to.
     * @param camera
     *            the camera.
     */
    public abstract void render(Graphics2D g, Camera camera);

    /**
     * Calls {@link #render(Graphics2D, Camera)} with the correct composite. If this layer is not visible or is transparent
     * {@link #render(Graphics2D, Camera)} won't be called.
     * 
     * @param g
     *            the graphics context to render to.
     * @param camera
     *            the camera.
     */
    public void doRender(Graphics2D g, Camera camera) {
        if (!isVisible() || getAlpha() <= 0f)
            return;

        Composite defaultComposite = g.getComposite();
        {
            g.setComposite(getComposite());
            render(g, camera);
        }
        g.setComposite(defaultComposite);

    }

    /**
     * Sets the transparency of this layer. The given alpha value will be clamped between 0.0 - 1.0
     * 
     * @param alpha
     *            the alpha between 0.0 - 1.0
     */
    public void setAlpha(float alpha) {
        this.alpha = UtilMath.clampF(alpha, 0, 1);
        this.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.alpha);
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
     * Returns the {@link AlphaComposite} used to apply transparency to this layer.
     * 
     * @return the {@link AlphaComposite} used to apply transparency to this layer.
     */
    public AlphaComposite getComposite() {
        return composite;
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

    /**
     * Returns the transparency of this layer.
     * 
     * @return the transparency of this layer, a value between 0.0 and 1.0
     */
    public float getAlpha() {
        return alpha;
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
     * Returns the properties of this layer.
     * 
     * @return the properties of this layer.
     */
    public MapProperties getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return String.format("%s [map=%s, id=%s, alpha=%s, composite=%s, isVisible=%s, properties=%s]", getClass().getSimpleName(), map, id, alpha, composite, isVisible, properties);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(alpha);
        result = prime * result + ((composite == null) ? 0 : composite.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (isVisible ? 1231 : 1237);
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
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
        AbstractMapLayer other = (AbstractMapLayer) obj;
        if (Float.floatToIntBits(alpha) != Float.floatToIntBits(other.alpha))
            return false;
        if (composite == null) {
            if (other.composite != null)
                return false;
        } else if (!composite.equals(other.composite))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (isVisible != other.isVisible)
            return false;
        if (map == null) {
            if (other.map != null)
                return false;
        } else if (!map.equals(other.map))
            return false;
        if (properties == null) {
            if (other.properties != null)
                return false;
        } else if (!properties.equals(other.properties))
            return false;
        return true;
    }
}
