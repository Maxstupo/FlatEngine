package com.github.maxstupo.flatengine.map.objects;

import com.github.maxstupo.flatengine.map.MapProperties;

/**
 * @author Maxstupo
 *
 */
public class MapObject {

    private String name;
    private final int id;
    private String type;
    private float x;
    private float y;
    private float width;
    private float height;

    private final MapProperties properties;

    public MapObject(int id, String name, String type, float x, float y, MapProperties properties) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.properties = properties;
    }

    @Override
    public String toString() {
        return String.format("MapObject [name=%s, id=%s, type=%s, x=%s, y=%s, properties=%s]", name, id, type, x, y, properties);
    }

    public MapProperties getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

}
