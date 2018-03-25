package com.github.maxstupo.flatengine.animation;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores {@link ImageSet}s based on their ids, allowing for easy retrieval via {@link #get(String)} or {@link #get(String, ImageSet)}.
 * 
 * @author Maxstupo
 */
public class ImageSetStore {

    private final Map<String, ImageSet> imageSets = new HashMap<>();

    /**
     * Creates an empty {@link ImageSetStore} object.
     * 
     */
    public ImageSetStore() {
    }

    /**
     * Creates a {@link ImageSetStore} object filled with the given {@link ImageSet}s.
     * 
     * @param imageSets
     *            the image sets to fill this store with.
     */
    public ImageSetStore(ImageSet... imageSets) {
        for (ImageSet imageSet : imageSets)
            add(imageSet);
    }

    /**
     * Adds the given {@link ImageSet} into this store.
     * 
     * @param imageSet
     *            the image set to store.
     * @throws IllegalArgumentException
     *             if the store already contains an {@link ImageSet} with the same id.
     */
    public void add(ImageSet imageSet) throws IllegalArgumentException {
        if (imageSets.containsKey(imageSet.getId()))
            throw new IllegalArgumentException("ImageSet id '" + imageSet.getId() + "' already registered!");
        imageSets.put(imageSet.getId(), imageSet);
    }

    /**
     * Returns a {@link ImageSet} that has the given id, or the given defaultValue if the id isn't registered.
     * 
     * @param id
     *            the id of the {@link ImageSet}.
     * @param defaultValue
     *            the value to return if the id isn't registered.
     * @return a {@link ImageSet} that has the given id, or the given defaultValue if the id isn't registered.
     */
    public ImageSet get(String id, ImageSet defaultValue) {
        ImageSet value = imageSets.get(id);
        return (value != null) ? value : defaultValue;
    }

    /**
     * Returns a {@link ImageSet} that has the given id, or null if the id isn't registered.
     * 
     * @param id
     *            the id of the {@link ImageSet}.
     * @return a {@link ImageSet} that has the given id, or null if the id isn't registered.
     */
    public ImageSet get(String id) {
        return get(id, null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imageSets == null) ? 0 : imageSets.hashCode());
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
        ImageSetStore other = (ImageSetStore) obj;
        if (imageSets == null) {
            if (other.imageSets != null)
                return false;
        } else if (!imageSets.equals(other.imageSets))
            return false;
        return true;
    }

}