package com.github.maxstupo.flatengine.map.layer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.map.object.MapObject;
import com.github.maxstupo.flatengine.util.UtilGraphics;

/**
 * This map layer class contains map objects, each object can have a shape. Rendering {@link ObjectLayer}s will render all objects using
 * {@link UtilGraphics#drawShape(Graphics2D, Camera, com.github.maxstupo.flatengine.util.math.AbstractBasicShape, java.awt.Color)
 * UtilGraphics.drawShape()}.
 * 
 * @author Maxstupo
 */
public class ObjectLayer extends AbstractMapLayer {

    private final List<MapObject> objects = new ArrayList<>();

    /**
     * Create a new {@link ObjectLayer} object.
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
    public ObjectLayer(TiledMap map, String id, float alpha, boolean isVisible, MapProperties properties) {
        super(map, id, alpha, isVisible, properties);
    }

    @Override
    public void render(Graphics2D g, Camera camera) {
        for (MapObject obj : objects)
            UtilGraphics.drawShape(g, camera, obj.getShape(), obj.getShapeColor());
    }

    /**
     * Returns all map objects with the given type.
     * 
     * @param type
     *            the type.
     * @return a new list of all map objects with the given type.
     */
    public List<MapObject> getAllOfType(String type) {
        List<MapObject> list = new ArrayList<>();

        for (MapObject obj : objects) {
            if (obj.getType().equals(type))
                list.add(obj);
        }
        return list;
    }

    /**
     * Returns the first map object with the given name.
     * 
     * @param name
     *            the name of the map object.
     * @return the first map object with the given name.
     */
    public MapObject getByName(String name) {
        for (MapObject obj : objects) {
            if (obj.getName().equals(name))
                return obj;
        }
        return null;
    }

    /**
     * Returns the first map object with the given id.
     * 
     * @param id
     *            the id of the map object.
     * @return the first map object with the given id.
     */
    public MapObject getById(int id) {
        for (MapObject obj : objects) {
            if (obj.getId() == id)
                return obj;
        }
        return null;
    }

    /**
     * Adds a map object to this layer. If null is given it will be ignored.
     * 
     * @param obj
     *            the map object to add.
     */
    public void addObject(MapObject obj) {
        if (obj != null)
            objects.add(obj);
    }

    /**
     * Returns an unmodifiable list of map objects.
     * 
     * @return an unmodifiable list of map objects.
     */
    public List<MapObject> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    @Override
    public String toString() {
        return String.format("%s [map=%s, id=%s, isVisible=%s, properties=%s]", getClass().getSimpleName(), map, id, isVisible, properties);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((objects == null) ? 0 : objects.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ObjectLayer other = (ObjectLayer) obj;
        if (objects == null) {
            if (other.objects != null)
                return false;
        } else if (!objects.equals(other.objects))
            return false;
        return true;
    }
}
