package com.github.maxstupo.flatengine.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class allows key-to-value entries to be stored and retrieved.
 * 
 * @author Maxstupo
 */
public class Store implements Iterable<Entry<String, Object>> {

    private final Map<String, Object> entries = new HashMap<>();

    /**
     * Create a new empty {@link Store} object.
     */
    public Store() {
    }

    /**
     * Create a new {@link Store} object containing the items from the given {@link Store}.
     * 
     * @param store
     *            the store to copy items from.
     */
    public Store(Store store) {
        add(store);
    }

    /**
     * Removes all items from this store.
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Adds all items from the given store to this store.
     * 
     * @param store
     *            the store to copy items from. If null method does nothing.
     */
    public void add(Store store) {
        if (store != null)
            entries.putAll(store.entries);
    }

    /**
     * Adds a item based on the given string values.
     * 
     * @param id
     *            the id of the value.
     * @param type
     *            the data type of the value. (string, float, int, boolean, long), if the type is unknown the value will be parsed as a string.
     * @param value
     *            the string representation of the value.
     * @return true if the value was added, false if the given id has already been used.
     */
    public boolean parse(String id, String type, String value) {
        switch (type.toLowerCase()) {
            case "string":
                return add(id, value);
            case "float":
                return add(id, Float.parseFloat(value));
            case "int":
                return add(id, Integer.parseInt(value));
            case "bool":
                return add(id, Boolean.parseBoolean(value));
            default:
                return add(id, value);
        }
    }

    /**
     * Add the given boolean value to this store.
     * 
     * @param id
     *            the id of the value.
     * @param value
     *            the value.
     * @return true if the value was added, false if the given id has already been used.
     */
    public boolean add(String id, boolean value) {
        return addObject(id, value);
    }

    /**
     * Add the given store to this store.
     * 
     * @param id
     *            the id of the given store.
     * @param value
     *            the store.
     * @return true if the store was added, false if the given id has already been used.
     */
    public boolean add(String id, Store value) {
        return addObject(id, value);
    }

    /**
     * Add the given float value to this store.
     * 
     * @param id
     *            the id of the value.
     * @param value
     *            the value.
     * @return true if the value was added, false if the given id has already been used.
     */
    public boolean add(String id, float value) {
        return addObject(id, value);
    }

    /**
     * Add the given integer value to this store.
     * 
     * @param id
     *            the id of the value.
     * @param value
     *            the value.
     * @return true if the value was added, false if the given id has already been used.
     */
    public boolean add(String id, int value) {
        return addObject(id, value);
    }

    /**
     * Add the given long value to this store.
     * 
     * @param id
     *            the id of the value.
     * @param value
     *            the value.
     * @return true if the value was added, false if the given id has already been used.
     */
    public boolean add(String id, long value) {
        return addObject(id, value);
    }

    /**
     * Add the given string to this store.
     * 
     * @param id
     *            the id of the value.
     * @param value
     *            the value.
     * @return true if the value was added, false if the given id has already been used.
     */
    public boolean add(String id, String value) {
        return addObject(id, value);
    }

    /**
     * Add the given object to this store.
     * 
     * @param id
     *            the id of the object.
     * @param value
     *            the value.
     * @return true if the value was added, false if the given id has already been used, or the id is null.
     */
    public boolean addObject(String id, Object value) {
        if (id == null || entries.containsKey(id))
            return false;
        entries.put(id, value);
        return true;
    }

    /**
     * Returns the given value referenced by the given id.
     * 
     * @param id
     *            the id of the value.
     * @param type
     *            the value type.
     * @return the given value referenced by the given id, or null if the given id doesn't reference anything.
     */
    public <T> T get(String id, Class<T> type) {
        return get(id, type, null);
    }

    /**
     * Returns the given value referenced by the given id.
     * 
     * @param id
     *            the id of the value.
     * @param type
     *            the value type.
     * @param defaultValue
     *            the value returned if the id doesn't reference anything.
     * @return the given value referenced by the given id, or the defaultValue if the given id doesn't reference anything.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String id, Class<T> type, T defaultValue) {
        Object obj = entries.get(id);
        return (obj != null) ? ((T) obj) : defaultValue;
    }

    @Override
    public Iterator<Entry<String, Object>> iterator() {
        return entries.entrySet().iterator();
    }

    /**
     * Returns true if the given id exists within this store.
     * 
     * @param id
     *            the id to check.
     * @return true if the given id exists within this store.
     */
    public boolean containsId(String id) {
        return entries.containsKey(id);
    }

    /**
     * Removes an item from this store by using the given id.
     * 
     * @param id
     *            the id of the item to remove.
     * @return true if the item was removed, false if no item exists with the given id.
     */
    public boolean remove(String id) {
        return entries.remove(id) != null;
    }

    @Override
    public String toString() {
        return String.format("%s [entries=%s]", getClass().getSimpleName(), entries);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entries == null) ? 0 : entries.hashCode());
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
        Store other = (Store) obj;
        if (entries == null) {
            if (other.entries != null)
                return false;
        } else if (!entries.equals(other.entries))
            return false;
        return true;
    }
}
