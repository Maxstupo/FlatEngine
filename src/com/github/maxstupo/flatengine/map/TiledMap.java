package com.github.maxstupo.flatengine.map;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.map.layer.MapLayer;

/**
 * @author Maxstupo
 *
 */
public class TiledMap {

    private final String id;
    private final String name;
    private final int width;
    private final int height;

    private final List<MapLayer> backgroundLayers = new ArrayList<>();
    private final List<MapLayer> foregroundLayers = new ArrayList<>();

    private final TilesetStore tilesetStore = new TilesetStore();

    public TiledMap(String id, String name, int width, int height) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public void render(Graphics2D g, Camera camera) {
        for (MapLayer layer : backgroundLayers)
            layer.render(g, camera);

        // TODO: Render entities and fringe tiles.

        for (MapLayer layer : foregroundLayers)
            layer.render(g, camera);
    }

    public void clearLayers() {
        backgroundLayers.clear();
        foregroundLayers.clear();
    }

    public void addBackgroundLayer(MapLayer layer) {
        if (layer != null)
            backgroundLayers.add(layer);
    }

    public void addForegroundLayer(MapLayer layer) {
        if (layer != null)
            foregroundLayers.add(layer);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TilesetStore getTilesetStore() {
        return tilesetStore;
    }
}
