package com.github.maxstupo.flatengine.item;

/**
 * This interface defines a stack of items.
 * 
 * @author Maxstupo
 */
public interface IItemStack {

    /**
     * Returns the amount of items within this item stack.
     * 
     * @return the amount of items within this item stack.
     */
    int getAmount();

    /**
     * Returns the sprite id for the item within this item stack.
     * 
     * @return the sprite id for the item within this item stack.
     */
    String getIconId();

    /**
     * Returns the name of the item within this item stack.
     * 
     * @return the name of the item within this item stack.
     */
    String getName();

    /**
     * Returns true if this item stack is empty. (e.g. amount <= 0 and id == none)
     * 
     * @return true if this item stack is empty.
     */
    boolean isEmpty();

}
