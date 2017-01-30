package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;

import com.github.maxstupo.flatengine.item.AbstractItemStack;
import com.github.maxstupo.flatengine.item.ISlotLogic;
import com.github.maxstupo.flatengine.item.SlotLogic;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This GUI node represents an item container that displays all the contained items in a grid of {@link GuiItemSlot item slots}.
 * 
 * @author Maxstupo
 * @param <T>
 *            the item stack type stored within this item container, the type must derive from {@link AbstractItemStack}.
 *
 */
public class GuiItemContainer<T extends AbstractItemStack> extends GuiContainer {

    private int slotSize;
    private int spacing;
    /** The item stack object that is currently held within this container. */
    protected final T holding;

    private boolean isAutoSize = true;

    /**
     * The default item slot, used as a template when rebuilding the containing item slots. If changes are made to this item slot call
     * {@link #setItemSlotsDirty()}, to rebuild item slots.
     */
    protected final GuiItemSlot<T> defaultSlot;

    private T[][] contents;
    private GuiItemSlot<T>[][] slots;

    /** The item slot logic used for all item slots within this item container. */
    protected ISlotLogic slotLogic = new SlotLogic();

    private boolean isItemSlotsDirty;
    private boolean isNameplateDirty;

    private final GuiLabel nameplate;

    /** The offset from the mouse cursor that the nameplate will be located. */
    protected final Vector2i nameplateOffset = new Vector2i(10, -20);
    private final Vector2i holdingClickOrigin = new Vector2i();

    private GuiItemSlot<T> slotHovered;
    private GuiItemSlot<T> oldSelectedSlot;
    private GuiItemSlot<T> holdingItemSlot; // Used to render item only.
    private int hoverI, hoverJ, oldHoverI, oldHoverJ;

    /**
     * Create a new {@link GuiItemContainer} object.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param localX
     *            the local x position.
     * @param localY
     *            the local y position.
     * @param slotSize
     *            the size of each item slot.
     * @param spacing
     *            the spacing between each item slot.
     * @param items
     *            the items within the item slots.
     * @param holding
     *            the item stack object that is currently held within this container.
     */
    public GuiItemContainer(AbstractScreen screen, float localX, float localY, int slotSize, int spacing, T[][] items, T holding) {
        super(screen, localX, localY, items.length * (slotSize + spacing), items[0].length * (slotSize + spacing) + 8);
        this.slotSize = slotSize;
        this.spacing = spacing;
        this.holding = holding;
        this.contents = items;

        this.defaultSlot = new GuiItemSlot<>(null, 0, 0, 0, null);

        this.nameplate = new GuiLabel(screen, 0, 0, -1, -1, "");
        this.nameplate.setBackgroundColor(Color.LIGHT_GRAY);
        this.nameplate.setVisible(false);
        add(nameplate);

        this.holdingItemSlot = new GuiItemSlot<T>(screen, 5, 5, slotSize, null) {

            @Override
            protected boolean update(float delta, boolean shouldHandleInput) {
                super.update(delta, shouldHandleInput);
                return shouldHandleInput;
            }
        };
        this.holdingItemSlot.setBackgroundColor(null);
        this.holdingItemSlot.setOutlineColor(null);
        this.holdingItemSlot.setIconSpacing(1);
        this.holdingItemSlot.setVisible(false);
        add(holdingItemSlot);

        setItemSlotsDirty();
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {
        if (isItemSlotsDirty)
            rebuildItemSlots();

        if (isNameplateDirty)
            updateNameplate();

        if (!shouldHandleInput) {
            slotHovered = null;
            nameplate.setVisible(false);
            return shouldHandleInput;
        }

        oldSelectedSlot = slotHovered;
        slotHovered = getSlotMouseHover();

        if (slotHovered != null) {

            if (slotLogic != null) {
                if (slotLogic.doSlotLogic(getSlotHovered(), getItemHovered(), getHolding(), false, getMouse())) {
                    setNameplateDirty();

                    if (!getHolding().isEmpty()) {
                        Vector2i mpos = getSlotHovered().getLocalMousePosition();

                        holdingClickOrigin.set(mpos.x, mpos.y);
                        holdingItemSlot.setContents(getHolding());
                    }

                    onGridChange(getSlotHovered(), false);
                }
            }

            if (hoverJ != oldHoverJ || hoverI != oldHoverI || getMouse().hasMouseMoved())
                setNameplateDirty();

        } else if (oldSelectedSlot != null) {

            setNameplateDirty();
        }

        if (!getHolding().isEmpty()) {
            Vector2i mpos = getLocalMousePosition();

            holdingItemSlot.setLocalPosition(mpos.x - holdingClickOrigin.x, mpos.y - holdingClickOrigin.y);
            holdingItemSlot.setVisible(true);
        } else {
            holdingItemSlot.setVisible(false);
        }

        return shouldHandleInput && !isMouseOver();
    }

    /**
     * Called when the item slot grid changes.
     * 
     * @param slot
     *            the item slot that changes
     * @param takeOnly
     *            true if the item slot logic was run on take only mode.
     */
    protected void onGridChange(GuiItemSlot<T> slot, boolean takeOnly) {

    }

    /**
     * Updates the nameplate text and repositions it, if no slot is hovered the nameplate will be hidden.
     */
    protected void updateNameplate() {
        if (getSlotHovered() != null && !getItemHovered().isEmpty() && getHolding().isEmpty()) {
            nameplate.getTextNode().setText(getItemHovered().getName());

            Vector2i mpos = getLocalMousePosition();

            nameplate.setLocalPosition(mpos.x + nameplateOffset.x, mpos.y + nameplateOffset.y);
            nameplate.setVisible(true);

        } else {
            nameplate.setVisible(false);
        }

        isNameplateDirty = false;
    }

    /**
     * Returns the item slot the mouse cursor is hovering over, or null if cursor isn't over any item slot.
     * 
     * 
     * @return the item slot the mouse cursor is hovering over, or null if cursor isn't over any item slot.
     */
    protected GuiItemSlot<T> getSlotMouseHover() {
        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[0].length; j++) {
                if (!slots[i][j].isEnabled())
                    continue;

                if (slots[i][j].isMouseOver()) {
                    oldHoverI = hoverI;
                    oldHoverJ = hoverJ;
                    hoverI = i;
                    hoverJ = j;
                    return slots[i][j];
                }
            }
        }
        return null;
    }

    /**
     * Rebuilds all item slot nodes within this container.
     */
    @SuppressWarnings("unchecked")
    protected void rebuildItemSlots() {
        removeChildren();

        this.slots = new GuiItemSlot[contents.length][contents[0].length];
        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[0].length; j++) {

                GuiItemSlot<T> slot = (slots[i][j] = new GuiItemSlot<T>(screen, i * (slotSize + spacing) + spacing, j * (slotSize + spacing) + spacing, slotSize, contents[i][j]) {

                    @Override
                    protected boolean update(float delta, boolean shouldHandleInput) {
                        super.update(delta, shouldHandleInput);
                        return shouldHandleInput;
                    }
                });

                slot.getTextAmount().setTextFont(defaultSlot.getTextAmount().getTextFont());
                slot.getTextAmount().setTextColor(defaultSlot.getTextAmount().getTextColor());
                slot.getTextAmount().setAlignment(defaultSlot.getTextAmount().getAlignment());
                slot.setBackgroundColor(defaultSlot.getBackgroundColor());
                slot.setOutlineColor(defaultSlot.getOutlineColor());
                slot.setOutlineStroke(defaultSlot.getOutlineStroke());
                slot.setIconSpacing(defaultSlot.getIconSpacing());
                slot.setAspectRatioKept(defaultSlot.isAspectRatioKept());
                slot.setIconResized(defaultSlot.isIconResized());
                slot.setIcon(defaultSlot.getIcon());

                add(slot);
            }
        }

        add(nameplate); // Re-add name plate because we called removeChildren()
        add(holdingItemSlot);

        if (isAutoSize)
            setSize(contents.length * (slotSize + spacing) + spacing, contents[0].length * (slotSize + spacing) + spacing);

        isItemSlotsDirty = false;
    }

    @Override
    public AbstractNode setEnabled(boolean isEnabled) {
        slotHovered = null;
        nameplate.setVisible(false);
        return super.setEnabled(isEnabled);
    }

    /**
     * Sets the spacing between each item slot.
     * 
     * @param spacing
     *            the spacing between each item slot.
     * @return this object for chaining.
     */
    public GuiItemContainer<T> setSpacing(int spacing) {
        if (this.spacing != spacing)
            isItemSlotsDirty = true;
        this.spacing = spacing;
        return this;
    }

    /**
     * Sets the size of all item slots.
     * 
     * @param slotSize
     *            the size of all item slots.
     * @return this object for chaining.
     */
    public GuiItemContainer<T> setSlotSize(int slotSize) {
        if (this.slotSize != slotSize)
            isItemSlotsDirty = true;
        this.slotSize = slotSize;
        return this;
    }

    /**
     * Sets the contents of this item container.
     * 
     * @param items
     *            the items, the 2D array can be of any length.
     * @return this object for chaining.
     */
    public GuiItemContainer<T> setContents(T[][] items) {
        this.contents = items;
        isItemSlotsDirty = true;
        return this;
    }

    /**
     * Sets if the item container will resize based on the item slots within.
     * 
     * @param isAutoSize
     *            true to automatically resize the item container based on the item slots.
     * @return this object for chaining.
     */
    public GuiItemContainer<T> setAutoSize(boolean isAutoSize) {
        if (this.isAutoSize != isAutoSize)
            isItemSlotsDirty = true;
        this.isAutoSize = isAutoSize;
        return this;
    }

    /**
     * Returns the item slot the mouse cursor is currently hovering over.
     * 
     * @return the item slot the mouse cursor is currently hovering over.
     */
    public GuiItemSlot<T> getSlotHovered() {
        return slotHovered;
    }

    /**
     * Requests that the name plate be updated with the correct position, item name and visibility.
     * 
     * @return this object for chaining.
     */
    public GuiItemContainer<T> setNameplateDirty() {
        this.isNameplateDirty = true;
        return this;
    }

    /**
     * Returns the item within the item slot the mouse cursor is currently hovering over.
     * 
     * @return the item within the item slot the mouse cursor is currently hovering over.
     */
    public T getItemHovered() {
        return getSlotHovered().getContents();
    }

    /**
     * Returns the contents of this item container.
     * 
     * @return the contents of this item container.
     */
    public T[][] getContents() {
        return contents;
    }

    /**
     * Returns the item slots this item container contains.
     * 
     * @return the item slots this item container contains.
     */
    public GuiItemSlot<T>[][] getSlots() {
        return slots;
    }

    /**
     * Returns the slot size of all item slots within this container.
     * 
     * @return the slot size of all item slots within this container.
     */
    public int getSlotSize() {
        return slotSize;
    }

    /**
     * Returns the spacing between each item slot.
     * 
     * @return the spacing between each item slot.
     */
    public int getSpacing() {
        return spacing;
    }

    /**
     * Returns true if the container will auto size to fit all item slots.
     * 
     * @return true if the container will auto size to fit all item slots.
     */
    public boolean isAutoSize() {
        return isAutoSize;
    }

    /**
     * Returns the default item slot, used as a template when rebuilding the containing item slots. If changes are made to this item slot call
     * setItemSlotsDirty(), to rebuild item slots.
     * 
     * @return the default item slot, used as a template when rebuilding the containing item slots.
     */
    public GuiItemSlot<T> getDefaultSlot() {
        return defaultSlot;
    }

    /**
     * Returns the item stack this item container is currently holding.
     * 
     * @return the item stack this item container is currently holding.
     */
    public T getHolding() {
        return holding;
    }

    /**
     * Returns the label node that represents the name plate for item names.
     * 
     * @return the label node that represents the name plate for item names.
     */
    public GuiLabel getNameplate() {
        return nameplate;
    }

    /**
     * Returns the offset from the mouse cursor that the nameplate will be located.
     * 
     * @return the offset from the mouse cursor that the nameplate will be located.
     */
    public Vector2i getNameplateOffset() {
        return nameplateOffset;
    }

    /**
     * Requests that the item slots be rebuilt.
     * 
     * @return this object for chaining.
     */
    public GuiItemContainer<T> setItemSlotsDirty() {
        this.isItemSlotsDirty = true;
        return this;
    }

    /**
     * Returns the item slot logic for this item container.
     * 
     * @return the item slot logic for this item container.
     */
    public ISlotLogic getSlotLogic() {
        return slotLogic;
    }

    /**
     * Sets the slot logic used for this item container.
     * 
     * @param slotLogic
     *            the logic of each item slot.
     * @return this object for chaining.
     */
    public GuiItemContainer<T> setSlotLogic(ISlotLogic slotLogic) {
        this.slotLogic = slotLogic;
        return this;
    }
}