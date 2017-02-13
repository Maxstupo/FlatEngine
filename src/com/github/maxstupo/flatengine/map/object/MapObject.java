package com.github.maxstupo.flatengine.map.object;

import java.awt.Color;

import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.layer.ObjectLayer;
import com.github.maxstupo.flatengine.util.math.AbstractBasicShape;
import com.github.maxstupo.flatengine.util.math.Circle;
import com.github.maxstupo.flatengine.util.math.Rectangle;

/**
 * This class represents a map object it can have a shape and properties. Uses for this include collision areas, event trigger areas, npc spawn areas,
 * etc.
 * 
 * @author Maxstupo
 */
public class MapObject {

    private final ObjectLayer layer;

    private final int id;

    private final String name;
    private final String type;

    private Color shapeColor;
    private final AbstractBasicShape shape;

    private final MapProperties properties = new MapProperties();

    /**
     * Create a new {@link MapObject} object.
     * 
     * @param layer
     *            the layer that owns this map object.
     * @param id
     *            the id of this object.
     * @param name
     *            the name of this object.
     * @param type
     *            the type or category of this object.
     * @param shape
     *            the shape of this object (e.g. {@link Rectangle}, {@link Circle})
     */
    public MapObject(ObjectLayer layer, int id, String name, String type, AbstractBasicShape shape) {
        this.layer = layer;
        this.id = id;
        this.name = name;
        this.type = type;
        this.shape = shape;
    }

    /**
     * Sets the color the {@link ObjectLayer} will render the shape of this object with.
     * 
     * @param shapeColor
     *            the color of the shape.
     */
    public void setShapeColor(Color shapeColor) {
        this.shapeColor = shapeColor;
    }

    /**
     * Returns the id of this object.
     * 
     * @return the id of this object.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the object layer that owns this map object.
     * 
     * @return the object layer that owns this map object.
     */
    public ObjectLayer getLayer() {
        return layer;
    }

    /**
     * Returns the type or category of this object.
     * 
     * @return the type or category of this object.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the name of this object.
     * 
     * @return the name of this object.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the shape of this map object.
     * 
     * @return the shape of this map object.
     */
    public AbstractBasicShape getShape() {
        return shape;
    }

    /**
     * Returns the properties of this map object.
     * 
     * @return the properties of this map object.
     */
    public MapProperties getProperties() {
        return properties;
    }

    /**
     * Returns the color the shape will be rendered in by the {@link ObjectLayer} that owns this map object.
     * 
     * @return the color the shape will be rendered in by the {@link ObjectLayer} that owns this map object.
     */
    public Color getShapeColor() {
        return shapeColor;
    }

    @Override
    public String toString() {
        return String.format("%s [layer=%s, id=%s, name=%s, type=%s, shapeColor=%s, shape=%s, properties=%s]", getClass().getSimpleName(), layer, id, name, type, shapeColor, shape, properties);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((layer == null) ? 0 : layer.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
        result = prime * result + ((shape == null) ? 0 : shape.hashCode());
        result = prime * result + ((shapeColor == null) ? 0 : shapeColor.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        MapObject other = (MapObject) obj;
        if (id != other.id)
            return false;
        if (layer == null) {
            if (other.layer != null)
                return false;
        } else if (!layer.equals(other.layer))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (properties == null) {
            if (other.properties != null)
                return false;
        } else if (!properties.equals(other.properties))
            return false;
        if (shape == null) {
            if (other.shape != null)
                return false;
        } else if (!shape.equals(other.shape))
            return false;
        if (shapeColor == null) {
            if (other.shapeColor != null)
                return false;
        } else if (!shapeColor.equals(other.shapeColor))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
