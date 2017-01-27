package com.github.maxstupo.flatengine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.item.IItemStack;
import com.github.maxstupo.flatengine.item.ISlotLogic;
import com.github.maxstupo.flatengine.item.SlotLogic;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class GuiItemContainer<T extends IItemStack> extends GuiContainer {

    protected final int slotSize;
    protected final int spacing;
    protected final T holding;

    private T[][] items;
    private GuiItemSlot<T>[][] slots;

    protected final GuiItemSlot<T> defaultSlot;
    private GuiItemSlot<T> selectedSlot;

    private boolean autoSize = true;
    protected ISlotLogic slotLogic = new SlotLogic();
    protected int namePlateBorderSize = 2;

    public GuiItemContainer(AbstractScreen screen, Vector2i localPosition, int slotSize, int spacing, T[][] items, T holding) {
        super(screen, localPosition, new Vector2i(items.length * (slotSize + spacing), items[0].length * (slotSize + spacing) + 8));
        this.slotSize = slotSize;
        this.spacing = spacing;
        this.holding = holding;
        this.defaultSlot = new GuiItemSlot<>(screen, Vector2i.ZERO, 0, null);
        this.items = items;
    }

    @Override
    public void renderPost(Graphics2D g) {
        super.renderPost(g);

        Vector2i mpos = getMouse().getPosition();
        renderHolding(g, mpos);

        if (selectedSlot != null)
            selectedSlot.renderItemName(g, mpos, namePlateBorderSize, Color.LIGHT_GRAY, Color.BLACK);
    }

    protected void renderHolding(Graphics2D g, Vector2i mpos) {
        if (holding.isEmpty())
            return;

        int x = mpos.x - slotSize / 2;
        int y = mpos.y - slotSize / 2;

        String iconId = holding.getIconId();
        Sprite spr = screen.getScreenManager().getEngine().getAssetManager().getSprite(iconId);
        if (spr != null) {
            spr.draw(g, x, y, slotSize, slotSize);
        } else {
            g.setColor(Color.PINK);
            g.fillRect(x, y, slotSize, slotSize);

        }
        if (holding.getAmount() > 1) {
            String amt = "" + holding.getAmount();
            g.setFont(defaultSlot.getFont());
            g.setColor(Color.WHITE);
            Dimension r = UtilGraphics.getStringBounds(g, amt);

            x += (slotSize - r.width - 2) + defaultSlot.getBorderSize() / 2;
            UtilGraphics.drawString(g, amt, x, y);
        }
    }

    @Override
    public boolean update(float delta, boolean shouldHandleInput) {

        selectedSlot = null;

        if (!shouldHandleInput)
            return shouldHandleInput;

        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                if (!slots[i][j].isEnabled())
                    continue;

                if (slots[i][j].isMouseOver())
                    selectedSlot = slots[i][j];

                if (slotLogic.doSlotLogic(slots[i][j], items[i][j], holding, false, getKeyboard()))
                    onGridChange(slots[i][j], false);
            }
        }
        return shouldHandleInput && !isMouseOver();
    }

    protected void onGridChange(GuiItemSlot<T> slot, boolean takeOnly) {

    }

    /**
     * Builds the required {@link GuiItemSlot}s for the current contents of this item container.
     * 
     * @see #setContents(IItemStack[][])
     * 
     */
    protected void buildSlots() {
        removeChildren();

        this.slots = new GuiItemSlot[items.length][items[0].length];
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                slots[i][j] = new GuiItemSlot<>(screen, new Vector2i(i * (slotSize + spacing) + spacing, j * (slotSize + spacing) + spacing), slotSize, items[i][j]);
                slots[i][j].useSettingsFrom(defaultSlot);
                addChild(slots[i][j]);
            }
        }

        if (autoSize) {
            getSize().set(items.length * (slotSize + spacing) + spacing, items[0].length * (slotSize + spacing) + spacing);
        }

    }

    @Override
    protected void onAdded() {
        super.onAdded();
        buildSlots();
    }

    public GuiItemContainer<T> setContents(T[][] items) {
        if (this.items.length != items.length || this.items[0].length != items[0].length) {
            this.items = items;
            buildSlots();
        } else {
            this.items = items;
        }
        return this;
    }

    public ISlotLogic getSlotLogic() {
        return slotLogic;
    }

    public GuiItemContainer<T> setSlotLogic(ISlotLogic slotLogic) {
        this.slotLogic = slotLogic;
        return this;
    }

    public int getNamePlateBorderSize() {
        return namePlateBorderSize;
    }

    public GuiItemContainer<T> setNamePlateBorderSize(int namePlateBorderSize) {
        this.namePlateBorderSize = namePlateBorderSize;
        return this;
    }

    public GuiItemSlot<T> getSelectedSlot() {
        return selectedSlot;
    }

    public GuiItemSlot<T> getDefaultSlot() {
        return defaultSlot;
    }

    public T[][] getContents() {
        return items;
    }

    public boolean isAutoSize() {
        return autoSize;
    }

    public GuiItemContainer<T> setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
        buildSlots();
        return this;
    }

    public GuiItemSlot<T>[][] getSlots() {
        return slots;
    }

    public T getHolding() {
        return holding;
    }

    public int getSlotSize() {
        return slotSize;
    }

    public int getSpacing() {
        return spacing;
    }
}
