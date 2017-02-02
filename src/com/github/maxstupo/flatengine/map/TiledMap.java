package com.github.maxstupo.flatengine.map;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.map.layer.TileLayer;
import com.github.maxstupo.flatengine.map.tile.TilesetStore;

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

    private final List<TileLayer> backgroundLayers = new ArrayList<>();
    private final List<TileLayer> foregroundLayers = new ArrayList<>();

    private final TilesetStore tilesetStore = new TilesetStore();

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
     */
    public TiledMap(String id, String name, int width, int height) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
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
        for (TileLayer layer : backgroundLayers)
            layer.render(g, camera);

        // TODO: Render entities and fringe tiles.

        for (TileLayer layer : foregroundLayers)
            layer.render(g, camera);
    }

    /**
     * Adds a background tile layer to this map. If null is given this method does nothing.
     * 
     * @param layer
     *            the layer.
     * @return this object for chaining.
     */
    public TiledMap addBackgroundLayer(TileLayer layer) {
        if (layer != null)
            backgroundLayers.add(layer);
        return this;
    }

    /**
     * Adds a foreground tile layer to this map. If null is given this method does nothing.
     * 
     * @param layer
     *            the layer.
     * @return this object for chaining.
     */
    public TiledMap addForegroundLayer(TileLayer layer) {
        if (layer != null)
            foregroundLayers.add(layer);
        return this;
    }

    /**
     * Removes the given tile layer.
     * 
     * @param layer
     *            the layer.
     * @return this object for chaining.
     */
    public TiledMap removeBackgroundLayer(TileLayer layer) {
        backgroundLayers.remove(layer);
        return this;
    }

    /**
     * Removes the given tile layer.
     * 
     * @param layer
     *            the layer.
     * @return this object for chaining.
     */
    public TiledMap removeForegroundLayer(TileLayer layer) {
        foregroundLayers.remove(layer);
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
}
