package com.github.maxstupo.flatengine.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.map.layer.AbstractMapLayer;
import com.github.maxstupo.flatengine.map.layer.TileLayer;
import com.github.maxstupo.flatengine.map.tile.TilesetStore;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This class represents a tiled map, the map is made of tiled layers, each layer can be placed in the background (rendered behind all entities) or
 * foreground (rendered in front of all entities).
 * 
 * @author Maxstupo
 */
public class TiledMap {

    /** The id of this map. */
    protected final String id;

    /** The name of this map. */
    protected final String name;

    /** The width of this map in tiles. */
    protected final int width;

    /** The height of this map in tiles. */
    protected final int height;

    /** The width of each tile in pixels. */
    protected final int tileWidth;

    /** The height of each tile in pixels. */
    protected final int tileHeight;

    /** The background color of this map, rendered before all map layers. */
    protected Color backgroundColor;

    private final List<AbstractMapLayer> layers = new ArrayList<>();
    private final List<TileLayer> backgroundLayers = new ArrayList<>();
    private final List<TileLayer> foregroundLayers = new ArrayList<>();

    private final TilesetStore tilesetStore = new TilesetStore();

    private final MapProperties properties;

    /**
     * Create a new {@link TiledMap} object.
     * 
     * @param id
     *            the id of this map.
     * @param name
     *            the name of this map.
     * @param width
     *            the width of this map in tiles.
     * @param height
     *            the height of this map in tiles.
     * @param tileWidth
     *            the width of each tile in pixels.
     * @param tileHeight
     *            the height of each tile in pixels.
     * @param properties
     *            the properties of this map.
     */
    public TiledMap(String id, String name, int width, int height, int tileWidth, int tileHeight, MapProperties properties) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.properties = properties;
    }

    /**
     * Render all tile layers and entities within this map.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param camera
     *            the camera.
     */
    public void render(Graphics2D g, Camera camera) {
        if (backgroundColor != null) {
            int[][] points = camera.getGridPoints(width, height);

            // Vector2i pos1 = camera.getRenderLocation(points[0][0], points[1][0]);
            Vector2i pos2 = camera.getRenderLocation(points[0][1], points[1][1]);

            g.setColor(backgroundColor);
            g.fillRect(0, 0, pos2.x, pos2.y);
        }

        for (TileLayer layer : backgroundLayers)
            layer.render(g, camera);

        // TODO: Render entities and fringe tiles.

        for (TileLayer layer : foregroundLayers)
            layer.render(g, camera);
    }

    /**
     * Calculates what {@link TileLayer}s should be rendered in the background and foreground, and caches them.
     * <p>
     * All tile layers starting with 'foreground' or 'over' will be rendered in the foreground (in front of all entities). <br>
     * All tile layers starting with 'background' or 'ground' will be rendered in the background (behind all entities).<br>
     * 
     * <p>
     * Note: Should be called after adding new {@link TileLayer}s.
     */
    public void calculateRenderableLayers() {
        foregroundLayers.clear();
        backgroundLayers.clear();

        for (AbstractMapLayer layer : layers) {

            if (!(layer instanceof TileLayer))
                continue;
            String id = layer.getId().toLowerCase();

            if (id.startsWith("over") || id.startsWith("foreground")) {
                foregroundLayers.add((TileLayer) layer);

            } else if (id.startsWith("ground") || id.startsWith("background")) {
                backgroundLayers.add((TileLayer) layer);

            } else if (id.startsWith("fringe")) {
                System.err.println("Fringe layer hasn't been implemented yet! " + layer);

            } else {
                System.out.println("Unknown layer: " + layer);

            }
        }
    }

    /**
     * Set the background color of this tiled map, set to null to disable.
     * 
     * @param backgroundColor
     *            the background color.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Adds a map layer to this map. If null is given this method does nothing.
     * <p>
     * After adding a {@link TileLayer} object, calling {@link #calculateRenderableLayers()} is needed.
     * 
     * @param layer
     *            the layer.
     * @return this object for chaining.
     */
    public TiledMap addLayer(AbstractMapLayer layer) {
        if (layer != null)
            layers.add(layer);
        return this;
    }

    /**
     * Returns the id of this tiled map.
     * 
     * @return the id of this tiled map.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of this tiled map.
     * 
     * @return the name of this tiled map.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the width of this map in tiles.
     * 
     * @return the width of this map in tiles.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of this map in tiles.
     * 
     * @return the height of this map in tiles.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the store containing all tiles used by this map.
     * 
     * @return the store containing all tiles used by this map.
     */
    public TilesetStore getTilesetStore() {
        return tilesetStore;
    }

    /**
     * Returns the properties of this map.
     * 
     * @return the properties of this map.
     */
    public MapProperties getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return String.format("TiledMap [id=%s, name=%s, width=%s, height=%s, backgroundColor=%s]", id, name, width, height, backgroundColor);
    }

    /**
     * Returns the first layer that matches the given id, will be cast to the given type.
     * 
     * @param id
     *            the id of the layer.
     * @param clazz
     *            the type of the layer.
     * @return the first layer that matches the given id.
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractMapLayer> T getLayer(String id, Class<T> clazz) {
        for (AbstractMapLayer layer : layers) {
            if (layer.getId().equals(id))
                return (T) layer;
        }
        return null;
    }

    /**
     * Returns all layers within this map.
     * 
     * @return all layers within this map.
     */
    public List<AbstractMapLayer> getLayers() {
        return Collections.unmodifiableList(layers);
    }

    /**
     * Returns the height of each tile in pixels.
     * 
     * @return the height of each tile in pixels.
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * Returns the width of each tile in pixels.
     * 
     * @return the width of each tile in pixels.
     */
    public int getTileWidth() {
        return tileWidth;
    }
}
