package com.github.maxstupo.flatengine.map;

import com.github.maxstupo.flatengine.util.Store;

/**
 * This class is used for storing map properties for layers,tiles,tilesets,etc..
 * 
 * @author Maxstupo
 */
public class MapProperties extends Store {

    /**
     * Create a new empty {@link MapProperties} object.
     */
    public MapProperties() {
    }

    /**
     * Create a new {@link MapProperties} object containing the items from the given {@link MapProperties}.
     * 
     * @param properties
     *            the properties to copy items from.
     */
    public MapProperties(MapProperties properties) {
        super(properties);
    }

}
