package com.github.maxstupo.flatengine.hgui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.hgui.AbstractAlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.item.AbstractItemStack;
import com.github.maxstupo.flatengine.item.ISlotLogic;
import com.github.maxstupo.flatengine.screen.AbstractScreen;

/**
 * This GUI node represents a item slot, it can contain a stack of items defined by the interface {@link AbstractItemStack}.
 * 
 * @author Maxstupo
 * @param <T>
 *            the item stack type it must derive from {@link AbstractItemStack}.
 */
public class GuiItemSlot<T extends AbstractItemStack> extends GuiImage {

    private int oldAmount;

    /** The item stack within this item slot. */
    protected T contents;

    private boolean isTextAmountDirty = false;

    /** The text node containing the amount this item stack has. */
    protected final GuiText textAmount;

    /** The logic for this item slot. */
    protected ISlotLogic logic;

    /** The item stack that is held. This is used to share an item stack between multiple item slots. */
    protected final T holding;

    /** True if this item slot can remove items only. */
    protected boolean isTakeOnly;

    private final List<IEventListener<GuiItemSlot<T>, T, T>> listeners = new ArrayList<>();

    /**
     * Create a new {@link GuiItemSlot} object.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param localX
     *            the local x position.
     * @param localY
     *            the local y position.
     * @param size
     *            the size of this square item slot.
     * @param contents
     *            the contents stored within this item slot, can't be null.
     * @param isTakeOnly
     *            true if this item slot can remove items only.
     * @param logic
     *            the slot logic for this item slot.
     * @param holding
     *            the item stack that is held. This is used to share an item stack between multiple item slots.
     */
    public GuiItemSlot(AbstractScreen screen, float localX, float localY, int size, T contents, boolean isTakeOnly, ISlotLogic logic, T holding) {
        super(screen, localX, localY, size, size);
        this.contents = contents;
        this.isTakeOnly = isTakeOnly;
        this.holding = holding;
        this.logic = logic;

        this.textAmount = new GuiText(screen, Alignment.BOTTOM_RIGHT);
        add(textAmount);
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {
        super.update(delta, shouldHandleInput);

        if (isTextAmountDirty)
            updateTextAmount();

        if (getContents() != null) {
            if (oldAmount != getContents().getAmount())
                setTextAmountDirty();
        }

        if (getHolding() != null && getLogic() != null && isMouseOver()) {
            if (getLogic().doSlotLogic(this, getContents(), getHolding(), isTakeOnly(), getMouse())) {
                fireEventListeners();
            }
        }

        return shouldHandleInput && !isMouseOver();
    }

    /**
     * Fires all event listeners for this item slot.
     */
    protected void fireEventListeners() {
        for (IEventListener<GuiItemSlot<T>, T, T> listener : listeners)
            listener.onEvent(this, getContents(), getHolding());
    }

    /**
     * Updates the text node with the amount within the {@link AbstractItemStack}.
     */
    protected void updateTextAmount() {
        if (getContents() != null) {

            if (!getContents().isEmpty() && getContents().getAmount() > 1) {
                getTextAmount().setText(getContents().getAmount() + "");
            } else {
                getTextAmount().setText("");
            }

            oldAmount = getContents().getAmount();
        }

        isTextAmountDirty = false;
    }

    /**
     * Adds a listener to this item slot.
     * 
     * @param listener
     *            the listener to add.
     * @return this object for chaining.
     */
    public GuiItemSlot<T> addListener(IEventListener<GuiItemSlot<T>, T, T> listener) {
        if (listener != null)
            listeners.add(listener);
        return this;
    }

    /**
     * Returns an unmodifiable list of listeners for this item slot.
     * 
     * @return an unmodifiable list of listeners for this item slot.
     */
    public List<IEventListener<GuiItemSlot<T>, T, T>> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * Requests that the text node be updated from the item stack this slot holds.
     * 
     * @return this object for chaining.
     */
    public GuiItemSlot<T> setTextAmountDirty() {
        this.isTextAmountDirty = true;
        return this;
    }

    @Override
    public Sprite getIcon() {
        if (contents == null)
            return icon;
        if (contents.isEmpty())
            return null;
        return screen.getScreenManager().getEngine().getAssetManager().getSprite(contents.getIconId());
    }

    /**
     * Returns the text node for the amount of items.
     * 
     * @return the text node for the amount of items.
     */
    public GuiText getTextAmount() {
        return textAmount;
    }

    /**
     * Returns the contents of this item slot.
     * 
     * @return the contents of this item slot.
     */
    public T getContents() {
        return contents;
    }

    /**
     * Sets the contents of this item slot.
     * 
     * @param contents
     *            the contents for this item slot.
     * @return this object for chaining.
     */
    public GuiItemSlot<T> setContents(T contents) {
        if (!contents.equals(this.contents)) {
            this.contents = contents;
            setTextAmountDirty();
        }
        return this;
    }

    /**
     * Returns the slot logic for this item slot.
     * 
     * @return the slot logic for this item slot.
     */
    public ISlotLogic getLogic() {
        return logic;
    }

    /**
     * Sets the slot logic for this item slot.
     * 
     * @param logic
     *            the logic.
     * @return this object for chaining.
     */
    public GuiItemSlot<T> setLogic(ISlotLogic logic) {
        this.logic = logic;
        return this;
    }

    /**
     * Returns the item stack that is held.
     * 
     * @return the item stack that is held.
     */
    public T getHolding() {
        return holding;
    }

    /**
     * Returns true if this item slot can remove items only.
     * 
     * @return true if this item slot can remove items only.
     */
    public boolean isTakeOnly() {
        return isTakeOnly;
    }

    /**
     * Sets if this item slot can only remove items.
     * 
     * @param isTakeOnly
     *            true to prevent items being added.
     * @return this object for chaining.
     */
    public GuiItemSlot<T> setTakeOnly(boolean isTakeOnly) {
        this.isTakeOnly = isTakeOnly;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((contents == null) ? 0 : contents.hashCode());
        result = prime * result + ((holding == null) ? 0 : holding.hashCode());
        result = prime * result + (isTakeOnly ? 1231 : 1237);
        result = prime * result + (isTextAmountDirty ? 1231 : 1237);
        result = prime * result + ((listeners == null) ? 0 : listeners.hashCode());
        result = prime * result + oldAmount;
        result = prime * result + ((textAmount == null) ? 0 : textAmount.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GuiItemSlot<?> other = (GuiItemSlot<?>) obj;
        if (contents == null) {
            if (other.contents != null)
                return false;
        } else if (!contents.equals(other.contents))
            return false;
        if (holding == null) {
            if (other.holding != null)
                return false;
        } else if (!holding.equals(other.holding))
            return false;
        if (isTakeOnly != other.isTakeOnly)
            return false;
        if (isTextAmountDirty != other.isTextAmountDirty)
            return false;
        if (listeners == null) {
            if (other.listeners != null)
                return false;
        } else if (!listeners.equals(other.listeners))
            return false;
        if (oldAmount != other.oldAmount)
            return false;
        if (textAmount == null) {
            if (other.textAmount != null)
                return false;
        } else if (!textAmount.equals(other.textAmount))
            return false;
        return true;
    }

}
