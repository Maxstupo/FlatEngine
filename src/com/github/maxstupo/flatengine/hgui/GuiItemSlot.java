package com.github.maxstupo.flatengine.hgui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.hgui.AbstractAlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.item.IItemStack;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This GUI node represents a item slot, it can contain a stack of items defined by the interface {@link IItemStack}.
 * 
 * @author Maxstupo
 * @param <T>
 *            the item stack type.
 */
public class GuiItemSlot<T extends IItemStack> extends GuiImage {

    private int oldAmount;
    /** The item stack within this item slot. */
    protected T contents;
    private boolean isTextAmountDirty = false;

    private final GuiText textAmount;

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
     *            the contents stored within this item slot, can be null.
     */
    public GuiItemSlot(AbstractScreen screen, float localX, float localY, int size, T contents) {
        super(screen, localX, localY, size, size);
        this.contents = contents;

        this.textAmount = new GuiText(screen, Alignment.BOTTOM_RIGHT);
        add(textAmount);
    }

    public void renderItemName(Graphics2D g, Vector2i pos) {

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
        return shouldHandleInput && !isMouseOver();
    }

    /**
     * Updates the text node with the amount within the {@link IItemStack}.
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
        this.contents = contents;
        setTextAmountDirty();
        return this;
    }

}
