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
     * Create a empty {@link AbstractItemStack}.
     */
    public AbstractItemStack() {
        set(0, 0);
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
     * Adds the given item stack to this item stack.
     * 
     * @param stack
     *            the stack to add.
     * @return the overflow amount if the given item stack can't all fit within this stack.
     */
    public int add(AbstractItemStack stack) {

        if (isEmpty()) { // Add items to an empty item stack.
            set(stack);
            return 0;

        }

        if (getId() != stack.getId()) // Given item stack doesn't contain the same item.
            return stack.getAmount();

        if ((getAmount() + stack.getAmount()) <= getMaxAmount()) { // Given stack will fit within this stack without overflowing.
            setAmount(getAmount() + stack.getAmount());
            return 0;

        } else { // Given stack will overflow, fill this stack and return the amount overflown.
            int leftOvers = stack.getAmount() - (getMaxAmount() - getAmount());
            setAmount(getMaxAmount());
            return leftOvers;

        }

    }

    /**
     * Decreases the item stack by the given amount.
     * 
     * @param amount
     *            the amount to remove from this item stack.
     * @return true if the item stack is empty.
     */
    public boolean decrease(int amount) {
        setAmount(getAmount() - amount);
        return isEmpty();
    }

    /**
     * Increases the item stack by the given amount.
     * 
     * @param amount
     *            the amount to add to this item stack.
     * @return true if the given amount was above the {@link #getMaxAmount() max amount}, if true the amount within this stack will be equal to the
     *         {@link #getMaxAmount() max amount}.
     */
    public boolean increase(int amount) {
        int combinedAmount = getAmount() + amount;

        if (combinedAmount > getMaxAmount()) {
            setAmount(getMaxAmount());
            return true;

        } else {
            setAmount(combinedAmount);
            return false;

        }
    }

    /**
     * Returns the maximum amount of items this item stack can contain.
     * 
     * @return the maximum amount of items this item stack can contain.
     */
    public abstract int getMaxAmount();

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
        if (stack == null)
            return setEmpty();
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
     * Returns a copy of this item stack.
     * 
     * @return a copy of this item stack.
     */
    public abstract AbstractItemStack copy();

    /**
     * Returns true if this item stack is empty. (e.g. amount &lt;= 0)
     * 
     * @return true if this item stack is empty.
     */
    public boolean isEmpty() {
        return getAmount() <= 0 || getId() <= -1;
    }

    /**
     * Sets the amount of items within this item stack.
     * 
     * @param amount
     *            the amount of items.
     * @return this object for chaining.
     */
    public AbstractItemStack setAmount(int amount) {
        this.amount = amount;
        return this;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + amount;
        result = prime * result + id;
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
        AbstractItemStack other = (AbstractItemStack) obj;
        if (amount != other.amount)
            return false;
        if (id != other.id)
            return false;
        return true;
    }

}
