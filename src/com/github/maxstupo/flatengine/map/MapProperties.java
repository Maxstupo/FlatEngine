package com.github.maxstupo.flatengine.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class allows storing multiple keys to values.
 * 
 * @author Maxstupo
 */
public class MapProperties implements Iterable<Entry<String, Object>> {

    private final Map<String, Object> properties = new HashMap<>();

    /**
     * Add the given integer value to this property list.
     * 
     * @param id
     *            the id of the value.
     * @param value
     *            the value.
     */
    public void add(String id, int value) {
        properties.put(id, value);
    }

    /**
     * Add the given float value to this property list.
     * 
     * @param id
     *            the id of the value.
     * @param value
     *            the value.
     */
    public void add(String id, float value) {
        properties.put(id, value);
    }

    /**
     * Add the given string value to this property list.
     * 
     * @param id
     *            the id of the value.
     * @param value
     *            the value.
     */
    public void add(String id, String value) {
        properties.put(id, value);
    }

    /**
     * Add the given boolean value to this property list.
     * 
     * @param id
     *            the id of the value.
     * @param value
     *            the value.
     */
    public void add(String id, boolean value) {
        properties.put(id, value);
    }

    /**
     * Adds a property based on the given string value.
     * 
     * @param id
     *            the id of the property.
     * @param type
     *            the data type of the value. (string, float, int, bool), if the type is unknown the value will be parsed as a string.
     * @param value
     *            the string representation of the value.
     */
    public void parseAdd(String id, String type, String value) {
        switch (type.toLowerCase()) {
            case "string":
                add(id, value);
                break;
            case "float":
                add(id, Float.parseFloat(value));
                break;
            case "int":
                add(id, Integer.parseInt(value));
                break;
            case "bool":
                add(id, Boolean.parseBoolean(value));
                break;
            default:
                add(id, value);
                break;
        }
    }

    /**
     * Returns the value representing the given id, or null if the id is unregistered.
     * 
     * @param id
     *            the id of the value to return.
     * @return the value representing the given id, or the null if the id is unregistered.
     */
    public String getString(String id) {
        return getString(id, null);
    }

    /**
     * Returns the value representing the given id, or the default value if the id is unregistered.
     * 
     * @param id
     *            the id of the value to return.
     * @param defaultValue
     *            the value to return if the id is unregistered.
     * @return the value representing the given id, or the default value if the id is unregistered.
     */
    public String getString(String id, String defaultValue) {
        if (!properties.containsKey(id))
            return defaultValue;
        return (String) properties.get(id);
    }

    /**
     * Returns the value representing the given id, or 0 if the id is unregistered.
     * 
     * @param id
     *            the id of the value to return.
     * @return the value representing the given id, or the 0 if the id is unregistered.
     */
    public int getInt(String id) {
        return getInt(id, 0);
    }

    /**
     * Returns the value representing the given id, or the default value if the id is unregistered.
     * 
     * @param id
     *            the id of the value to return.
     * @param defaultValue
     *            the value to return if the id is unregistered.
     * @return the value representing the given id, or the default value if the id is unregistered.
     */
    public int getInt(String id, int defaultValue) {
        if (!properties.containsKey(id))
            return defaultValue;
        return (int) properties.get(id);
    }

    /**
     * Returns the value representing the given id, or false if the id is unregistered.
     * 
     * @param id
     *            the id of the value to return.
     * @return the value representing the given id, or the false if the id is unregistered.
     */
    public boolean getBoolean(String id) {
        return getBoolean(id, false);
    }

    /**
     * Returns the value representing the given id, or the default value if the id is unregistered.
     * 
     * @param id
     *            the id of the value to return.
     * @param defaultValue
     *            the value to return if the id is unregistered.
     * @return the value representing the given id, or the default value if the id is unregistered.
     */
    public boolean getBoolean(String id, boolean defaultValue) {
        if (!properties.containsKey(id))
            return defaultValue;
        return (boolean) properties.get(id);
    }

    /**
     * Returns the value representing the given id, or 0 if the id is unregistered.
     * 
     * @param id
     *            the id of the value to return.
     * @return the value representing the given id, or the 0 if the id is unregistered.
     */
    public float getFloat(String id) {
        return getFloat(id, 0);
    }

    /**
     * Returns the value representing the given id, or the default value if the id is unregistered.
     * 
     * @param id
     *            the id of the value to return.
     * @param defaultValue
     *            the value to return if the id is unregistered.
     * @return the value representing the given id, or the default value if the id is unregistered.
     */
    public float getFloat(String id, float defaultValue) {
        if (!properties.containsKey(id))
            return defaultValue;
        return (float) properties.get(id);
    }

    @Override
    public Iterator<Entry<String, Object>> iterator() {
        return properties.entrySet().iterator();
    }

    /**
     * Removes the given property registered with the given id.
     * 
     * @param id
     *            the id of the property to remove.
     */
    public void remove(String id) {
        properties.remove(id);
    }

}
