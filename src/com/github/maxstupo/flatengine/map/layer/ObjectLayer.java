package com.github.maxstupo.flatengine.map.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.map.MapProperties;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.map.objects.MapObject;

/**
 * @author Maxstupo
 *
 */
public class ObjectLayer extends AbstractMapLayer {

    private final List<MapObject> objects = new ArrayList<>();

    public ObjectLayer(TiledMap map, String id, float alpha, boolean isVisible, MapProperties properties) {
        super(map, id, alpha, isVisible, properties);
    }

    public List<MapObject> getAllOfType(String type) {
        List<MapObject> list = new ArrayList<>();

        for (MapObject obj : objects) {
            if (obj.getType().equals(type))
                list.add(obj);
        }
        return list;
    }

    public MapObject getByName(String name) {
        for (MapObject obj : objects) {
            if (obj.getName().equals(name))
                return obj;
        }
        return null;
    }

    public MapObject getById(int id) {
        for (MapObject obj : objects) {
            if (obj.getId() == id)
                return obj;
        }
        return null;
    }

    public void addObject(MapObject obj) {
        objects.add(obj);
    }

    public List<MapObject> getObjects() {
        return Collections.unmodifiableList(objects);
    }

}
