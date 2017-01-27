package com.github.maxstupo.flatengine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.item.IItemStack;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class GuiItemSlot<T extends IItemStack> extends GuiContainer {

    private T contents;

    private int borderSize = 5;
    private Font font = new Font("Segoe UI", Font.PLAIN, 15);

    public GuiItemSlot(AbstractScreen screen, Vector2i localPosition, int slotSize, T contents) {
        super(screen, localPosition, new Vector2i(slotSize, slotSize));
        this.contents = contents;

        setBackgroundColor(Color.DARK_GRAY);
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        Vector2i gpos = getGlobalPosition();
        renderItem(g, gpos);
    }

    protected void renderItemName(Graphics2D g, Vector2i mpos, int namePlateBorderSize, Color plateColor, Color textColor) {
        if (getContents().isEmpty())
            return;

        String name = getContents().getName();

        Dimension r = UtilGraphics.getStringBounds(g, name);

        int plateWidth = r.width + namePlateBorderSize * 2;
        int plateHeight = r.height + namePlateBorderSize * 2;
        int x = mpos.x + 15;
        int y = mpos.y + 15;

        g.setColor(plateColor);
        g.fill3DRect(x, y, plateWidth, plateHeight, true);

        g.setColor(textColor);
        UtilGraphics.drawString(g, name, x + 1 + (plateWidth / 2 - r.width / 2), y + (plateHeight / 2 - r.height / 2));
    }

    protected void renderItem(Graphics2D g, Vector2i gpos) {
        if (contents == null || contents.isEmpty())
            return;

        AssetManager am = screen.getScreenManager().getEngine().getAssetManager();

        Sprite spr = am.getSprite(contents.getIconId());

        int slotSize = size.x;
        int itemSize = slotSize - borderSize;

        if (spr != null) {
            spr.draw(g, gpos.x + slotSize / 2 - itemSize / 2, gpos.y + slotSize / 2 - itemSize / 2, itemSize, itemSize);

        } else {
            g.setColor(Color.PINK);
            g.fillRect(gpos.x + slotSize / 2 - itemSize / 2, gpos.y + slotSize / 2 - itemSize / 2, itemSize, itemSize);
        }

        if (contents.getAmount() > 1) {
            g.setColor(Color.WHITE);
            g.setFont(font);

            String amt = contents.getAmount() + "";
            Dimension r = UtilGraphics.getStringBounds(g, amt);
            UtilGraphics.drawString(g, amt, gpos.x + (size.x - r.width - 2), gpos.y);
        }
    }

    public T getContents() {
        return contents;
    }

    public GuiItemSlot<T> setContents(T contents) {
        this.contents = contents;
        return this;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public GuiItemSlot<T> setBorderSize(int borderSize) {
        this.borderSize = borderSize;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public GuiItemSlot<T> setFont(Font font) {
        this.font = font;
        return this;
    }

    public GuiItemSlot<T> useSettingsFrom(GuiItemSlot<?> slot) {
        setFont(slot.getFont());
        setBackgroundColor(slot.getBackgroundColor());
        setBorderSize(slot.getBorderSize());
        setOutlineColor(slot.getOutlineColor());
        return this;
    }
}
