package com.github.maxstupo.flatengine.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public class GuiSelectionList<T extends Enum<T>, L> extends GuiList<T, L> {

    protected int selected = -1;
    protected Color selectedColor = Color.WHITE;

    public GuiSelectionList(AbstractGamestate<T> gamestate, Vector2i localPosition, Vector2i size) {
        super(gamestate, localPosition, size);
        addListener(new IEventListener<GuiList<T, L>, Integer, Integer>() {

            @Override
            public void onEvent(GuiList<T, L> executor, Integer actionItem, Integer action) {
                selected = actionItem.intValue();
            }
        });
    }

    @Override
    protected void renderItem(Graphics2D g, Vector2i pos, int index, L item) {
        super.renderItem(g, pos, index, item);

        if (index == selected)
            renderSelected(g, pos);
    }

    protected void renderSelected(Graphics2D g, Vector2i pos) {
        g.setColor(selectedColor);
        g.drawRect(pos.x, pos.y, getEntryWidth() - 1, getEntryHeight());
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public GuiSelectionList<T, L> unselect() {
        selected = -1;
        return this;
    }

    public GuiSelectionList<T, L> setSelection(int i) {
        selected = UtilMath.clampI(i, 0, getTotalEntries() - 1);
        return this;
    }

    public boolean hasSelected() {
        return selected != -1;
    }

    public int getSelected() {
        return selected;
    }
}
