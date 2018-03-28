package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
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
public class GuiItemContainer<T extends AbstractItemStack> extends GuiContainer implements IEventListener<GuiItemSlot<T>, T, T> {

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

    private ISlotLogic slotLogic = new SlotLogic();

    private boolean isItemSlotsDirty;
    private boolean isNameplateDirty;

    private final GuiLabel nameplate;

    /** The offset from the mouse cursor that the nameplate will be located. */
    protected final Vector2i nameplateOffset = new Vector2i(10, -20);

    /** The offset for the held item. */
    protected final Vector2i holdingOffset = new Vector2i(-1, -1);

    private GuiItemSlot<T> slotHovered;
    private GuiItemSlot<T> oldSelectedSlot;
    private GuiItemSlot<T> holdingItemSlot; // Used to render item only.
    private int hoverI, hoverJ, oldHoverI, oldHoverJ;

    private boolean isTakeOnly;
    private final List<IEventListener<GuiItemContainer<T>, T, GuiItemSlot<T>>> listeners = new ArrayList<>();

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
     * @param isTakeOnly
     *            if true item slots can only have items taken out.
     */
    public GuiItemContainer(AbstractScreen screen, float localX, float localY, int slotSize, int spacing, T[][] items, T holding, boolean isTakeOnly) {
        super(screen, localX, localY, items.length * (slotSize + spacing), items[0].length * (slotSize + spacing) + 8);
        this.slotSize = slotSize;
        this.spacing = spacing;
        this.holding = holding;
        this.isTakeOnly = isTakeOnly;
        this.contents = items;

        this.defaultSlot = new GuiItemSlot<>(null, 0, 0, 0, null, false, null, null);

        this.nameplate = new GuiLabel(screen, 0, 0, -1, -1, "");
        this.nameplate.setBackgroundColor(Color.LIGHT_GRAY);
        this.nameplate.setVisible(false);
        add(nameplate);

        this.holdingItemSlot = new GuiItemSlot<T>(screen, 0, 0, slotSize, null, false, null, null) {

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

            if (hoverJ != oldHoverJ || hoverI != oldHoverI || getMouse().hasMouseMoved())
                setNameplateDirty();

        } else if (oldSelectedSlot != null) {

            setNameplateDirty();
        }

        if (!getHolding().isEmpty()) {
            Vector2i mpos = getLocalMousePosition();

            int ox = (holdingOffset.x == -1) ? holdingItemSlot.getWidth() / 2 : holdingOffset.x;
            int oy = (holdingOffset.y == -1) ? holdingItemSlot.getHeight() / 2 : holdingOffset.y;

            holdingItemSlot.setContents(getHolding());
            holdingItemSlot.setLocalPosition(mpos.x - ox, mpos.y - oy);

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

                GuiItemSlot<T> slot = (slots[i][j] = new GuiItemSlot<T>(screen, i * (slotSize + spacing) + spacing, j * (slotSize + spacing) + spacing, slotSize, contents[i][j], isTakeOnly, slotLogic, holding) {

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

                slot.addListener(this);

                add(slot);
            }
        }

        add(nameplate); // Re-add name plate because we called removeChildren()
        add(holdingItemSlot);

        if (isAutoSize)
            setSize(contents.length * (slotSize + spacing) + spacing, contents[0].length * (slotSize + spacing) + spacing);

        isItemSlotsDirty = false;
    }

    /**
     * Fires all event listeners within this item container.
     */
    protected void fireEventListeners() {
        for (IEventListener<GuiItemContainer<T>, T, GuiItemSlot<T>> listener : listeners)
            listener.onEvent(this, getHolding(), getSlotHovered());
    }

    @Override
    public void onEvent(GuiItemSlot<T> executor, T actionItem, T action) {
        fireEventListeners();
    }

    /**
     * Adds a listener to this item container.
     * 
     * @param listener
     *            the listener to add.
     * @return this object for chaining.
     */
    public GuiItemContainer<T> addListener(IEventListener<GuiItemContainer<T>, T, GuiItemSlot<T>> listener) {
        if (listener != null)
            listeners.add(listener);
        return this;
    }

    /**
     * Returns an unmodifiable list of listeners for this item container.
     * 
     * @return an unmodifiable list of listeners for this item container.
     */
    public List<IEventListener<GuiItemContainer<T>, T, GuiItemSlot<T>>> getListeners() {
        return Collections.unmodifiableList(listeners);
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
     * Returns true if item slots can only have items taken out.
     * 
     * @return true if item slots can only have items taken out.
     */
    public boolean isTakeOnly() {
        return isTakeOnly;
    }

    /**
     * Set true to allow item slots to take out items only.
     * 
     * @param isTakeOnly
     *            true to allow item slots to take out items only.
     * @return this object for chaining.
     */
    public GuiItemContainer<T> setTakeOnly(boolean isTakeOnly) {
        if (this.isTakeOnly != isTakeOnly) {
            this.isTakeOnly = isTakeOnly;
            setItemSlotsDirty();
        }
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
        setItemSlotsDirty();
        return this;
    }

    /**
     * Returns the offset used for the item that is held.
     * <p>
     * Set x or y (or both) to -1 to use the center of the item icon.
     * 
     * @return the offset used for the item that is held.
     */
    public Vector2i getHoldingOffset() {
        return holdingOffset;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.deepHashCode(contents);
        result = prime * result + ((defaultSlot == null) ? 0 : defaultSlot.hashCode());
        result = prime * result + ((holding == null) ? 0 : holding.hashCode());
        result = prime * result + ((holdingItemSlot == null) ? 0 : holdingItemSlot.hashCode());
        result = prime * result + ((holdingOffset == null) ? 0 : holdingOffset.hashCode());
        result = prime * result + hoverI;
        result = prime * result + hoverJ;
        result = prime * result + (isAutoSize ? 1231 : 1237);
        result = prime * result + (isItemSlotsDirty ? 1231 : 1237);
        result = prime * result + (isNameplateDirty ? 1231 : 1237);
        result = prime * result + (isTakeOnly ? 1231 : 1237);
        result = prime * result + ((listeners == null) ? 0 : listeners.hashCode());
        result = prime * result + ((nameplate == null) ? 0 : nameplate.hashCode());
        result = prime * result + ((nameplateOffset == null) ? 0 : nameplateOffset.hashCode());
        result = prime * result + oldHoverI;
        result = prime * result + oldHoverJ;
        result = prime * result + ((oldSelectedSlot == null) ? 0 : oldSelectedSlot.hashCode());
        result = prime * result + ((slotHovered == null) ? 0 : slotHovered.hashCode());
        result = prime * result + slotSize;
        result = prime * result + Arrays.deepHashCode(slots);
        result = prime * result + spacing;
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
        GuiItemContainer<?> other = (GuiItemContainer<?>) obj;
        if (!Arrays.deepEquals(contents, other.contents))
            return false;
        if (defaultSlot == null) {
            if (other.defaultSlot != null)
                return false;
        } else if (!defaultSlot.equals(other.defaultSlot))
            return false;
        if (holding == null) {
            if (other.holding != null)
                return false;
        } else if (!holding.equals(other.holding))
            return false;
        if (holdingItemSlot == null) {
            if (other.holdingItemSlot != null)
                return false;
        } else if (!holdingItemSlot.equals(other.holdingItemSlot))
            return false;
        if (holdingOffset == null) {
            if (other.holdingOffset != null)
                return false;
        } else if (!holdingOffset.equals(other.holdingOffset))
            return false;
        if (hoverI != other.hoverI)
            return false;
        if (hoverJ != other.hoverJ)
            return false;
        if (isAutoSize != other.isAutoSize)
            return false;
        if (isItemSlotsDirty != other.isItemSlotsDirty)
            return false;
        if (isNameplateDirty != other.isNameplateDirty)
            return false;
        if (isTakeOnly != other.isTakeOnly)
            return false;
        if (listeners == null) {
            if (other.listeners != null)
                return false;
        } else if (!listeners.equals(other.listeners))
            return false;
        if (nameplate == null) {
            if (other.nameplate != null)
                return false;
        } else if (!nameplate.equals(other.nameplate))
            return false;
        if (nameplateOffset == null) {
            if (other.nameplateOffset != null)
                return false;
        } else if (!nameplateOffset.equals(other.nameplateOffset))
            return false;
        if (oldHoverI != other.oldHoverI)
            return false;
        if (oldHoverJ != other.oldHoverJ)
            return false;
        if (oldSelectedSlot == null) {
            if (other.oldSelectedSlot != null)
                return false;
        } else if (!oldSelectedSlot.equals(other.oldSelectedSlot))
            return false;
        if (slotHovered == null) {
            if (other.slotHovered != null)
                return false;
        } else if (!slotHovered.equals(other.slotHovered))
            return false;
        if (slotSize != other.slotSize)
            return false;
        if (!Arrays.deepEquals(slots, other.slots))
            return false;
        if (spacing != other.spacing)
            return false;
        return true;
    }

    /**
     * Returns the {@link GuiItemSlot} used for holding the item stack.
     * 
     * @return the {@link GuiItemSlot} used for holding the item stack.
     */
    public GuiItemSlot<T> getHoldingItemSlot() {
        return holdingItemSlot;
    }

}
