package com.github.maxstupo.flatengine.item;

/**
 * This interface defines a stack of items.
 * 
 * @author Maxstupo
 */
public abstract class AbstractItemStack {

    private int amount;
    private int id;

    /**
     * Create a {@link AbstractItemStack}.
     * 
     * @param stack
     *            the item stack to set equal to.
     */
    public AbstractItemStack(AbstractItemStack stack) {
        this(stack.getId(), stack.getAmount());
    }

    /**
     * Create a {@link AbstractItemStack}.
     * 
     * @param id
     *            the item id within this item stack.
     * @param amount
     *            the amount of items within this item stack.
     */
    public AbstractItemStack(int id, int amount) {
        set(id, amount);
    }

    /**
     * Sets this item stack empty.
     * 
     * @return this object for chaining.
     */
    public AbstractItemStack setEmpty() {
        this.amount = 0;
        this.id = -1;
        return this;
    }

    /**
     * Sets this item stack to the given values.
     * 
     * @param id
     *            the new item id within this stack.
     * @param amount
     *            the new amount of items within this stack.
     * @return this object for chaining.
     */
    public AbstractItemStack set(int id, int amount) {
        this.id = id;
        this.amount = amount;
        return this;
    }

    /**
     * Sets this item stack to have the same contents as the given stack.
     * 
     * @param stack
     *            the item stack to set equal to.
     * @return this object for chaining.
     */
    public AbstractItemStack set(AbstractItemStack stack) {
        return set(stack.getId(), stack.getAmount());
    }

    /**
     * Returns the sprite id for the item within this item stack.
     * 
     * @return the sprite id for the item within this item stack.
     */
    public abstract String getIconId();

    /**
     * Returns the name of the item within this item stack.
     * 
     * @return the name of the item within this item stack.
     */
    public abstract String getName();

    /**
     * Returns true if this item stack is empty. (e.g. amount &lt;= 0)
     * 
     * @return true if this item stack is empty.
     */
    public boolean isEmpty() {
        return getAmount() <= 0 || getId() <= -1;
    }

    /**
     * Returns the amount of items within this item stack.
     * 
     * @return the amount of items within this item stack.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Returns the id of the item within this item stack.
     * 
     * @return the id of the item within this item stack.
     */
    public int getId() {
        return id;
    }

}
