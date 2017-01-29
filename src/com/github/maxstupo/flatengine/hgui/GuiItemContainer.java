package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;

import com.github.maxstupo.flatengine.item.IItemStack;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class GuiItemContainer<T extends IItemStack> extends GuiContainer {

    private int slotSize;
    private int spacing;
    protected final T holding;

    private boolean isAutoSize = true;

    protected final GuiItemSlot<T> defaultSlot;

    private T[][] items;
    private GuiItemSlot<T>[][] slots;

    private boolean isItemSlotsDirty;
    private boolean isNameplateDirty;

    private final GuiLabel nameplate;
    protected final Vector2i nameplateOffset = new Vector2i(10, -20);

    private GuiItemSlot<T> selectedSlot;
    private GuiItemSlot<T> oldSelectedSlot;
    private int hoverI, hoverJ, oldHoverI, oldHoverJ;

    public GuiItemContainer(AbstractScreen screen, float localX, float localY, int slotSize, int spacing, T[][] items, T holding) {
        super(screen, localX, localY, items.length * (slotSize + spacing), items[0].length * (slotSize + spacing) + 8);
        this.slotSize = slotSize;
        this.spacing = spacing;
        this.holding = holding;
        this.items = items;

        this.defaultSlot = new GuiItemSlot<>(null, 0, 0, 0, null);

        this.nameplate = new GuiLabel(screen, 0, 0, -1, -1);
        this.nameplate.setVisible(false);
        this.nameplate.setBackgroundColor(Color.LIGHT_GRAY);
        add(nameplate);

        setItemSlotsDirty();
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {
        if (isItemSlotsDirty)
            rebuildItemSlots();

        if (isNameplateDirty)
            updateNameplate();

        if (!shouldHandleInput) {
            selectedSlot = null;
            nameplate.setVisible(false);
            return shouldHandleInput;
        }

        oldSelectedSlot = selectedSlot;
        selectedSlot = getSlotMouseHover();

        if (selectedSlot != null) {

            if (hoverJ != oldHoverJ || hoverI != oldHoverI || getMouse().hasMouseMoved())
                isNameplateDirty = true;

        } else if (oldSelectedSlot != null) {

            isNameplateDirty = true;
        }

        return shouldHandleInput && !isMouseOver();
    }

    protected void updateNameplate() {
        if (getSlotHovered() != null) {
            nameplate.getTextNode().setText(getItemHovered().getName());

            Vector2i mpos = getMouse().getPosition();
            Vector2i gpos = getGlobalPosition();

            nameplate.setLocalPosition(mpos.x - gpos.x + nameplateOffset.x, mpos.y - gpos.y + nameplateOffset.y);
            nameplate.setVisible(true);

        } else {
            nameplate.setVisible(false);
        }

        isNameplateDirty = false;
    }

    protected GuiItemSlot<T> getSlotMouseHover() {
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
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

        this.slots = new GuiItemSlot[items.length][items[0].length];
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {

                GuiItemSlot<T> slot = (slots[i][j] = new GuiItemSlot<T>(screen, i * (slotSize + spacing) + spacing, j * (slotSize + spacing) + spacing, slotSize, items[i][j]) {

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

        bringToFront(nameplate);

        if (isAutoSize) {
            setSize(items.length * (slotSize + spacing) + spacing, items[0].length * (slotSize + spacing) + spacing);
        }

        isItemSlotsDirty = false;
    }

    @Override
    public AbstractNode setEnabled(boolean isEnabled) {
        selectedSlot = null;
        nameplate.setVisible(false);
        return super.setEnabled(isEnabled);
    }

    public GuiItemContainer<T> setSpacing(int spacing) {
        if (this.spacing != spacing)
            isItemSlotsDirty = true;
        this.spacing = spacing;
        return this;
    }

    public GuiItemContainer<T> setSlotSize(int slotSize) {
        if (this.slotSize != slotSize)
            isItemSlotsDirty = true;
        this.slotSize = slotSize;
        return this;
    }

    public GuiItemContainer<T> setContents(T[][] items) {
        this.items = items;
        isItemSlotsDirty = true;
        return this;
    }

    public GuiItemContainer<T> setAutoSize(boolean isAutoSize) {
        if (this.isAutoSize != isAutoSize)
            isItemSlotsDirty = true;
        this.isAutoSize = isAutoSize;
        return this;
    }

    public GuiItemSlot<T> getSlotHovered() {
        return selectedSlot;
    }

    public T getItemHovered() {
        return getSlotHovered().getContents();
    }

    public T[][] getContents() {
        return items;
    }

    public GuiItemSlot<T>[][] getSlots() {
        return slots;
    }

    public int getSlotSize() {
        return slotSize;
    }

    public int getSpacing() {
        return spacing;
    }

    public boolean isAutoSize() {
        return isAutoSize;
    }

    public GuiItemSlot<T> getDefaultSlot() {
        return defaultSlot;
    }

    public GuiItemContainer<T> setItemSlotsDirty() {
        this.isItemSlotsDirty = true;
        return this;
    }
}
