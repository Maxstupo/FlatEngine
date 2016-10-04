package com.github.maxstupo.flatengine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public class GuiList<T extends Enum<T>, L> extends AbstractGuiNode<T> {

    protected Color backgroundColor = UtilGraphics.changeAlpha(Color.BLACK, 127);
    protected Color foregroundColor = Color.BLACK;
    protected Color outlineColor = Color.BLACK;
    protected Color hoverColor = Color.LIGHT_GRAY;
    protected Color barColor = Color.DARK_GRAY;
    protected Color barBackgroundColor = Color.LIGHT_GRAY;
    private Font font;

    private final List<L> entires = new ArrayList<>();
    protected final List<IEventListener<GuiList<T, L>, Integer, Integer>> listeners = new ArrayList<>();

    private final Vector2i scrollBarPosition = new Vector2i();
    private final Vector2i renderEntryPosition = new Vector2i();

    private int scroll;
    protected int spacing = 0;
    private int indexHover = -1;

    private final Rectangle entryBounds = new Rectangle(0, 0, -1, -1);
    private boolean entryHeightDirty = true;

    // protected boolean scrollBarSelected = false;
    protected boolean scrollBarEnabled = true;
    protected int scrollBarWidth = 7;
    protected int scrollBarHeight = 11;

    public GuiList(AbstractGamestate<T> gamestate, Vector2i localPosition, Vector2i size) {
        super(gamestate, localPosition, size);
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {
        if (!isEnabled())
            return shouldHandleInput;

        indexHover = -1;
        if (shouldHandleInput) {
            Vector2i gpos = getGlobalPosition();

            doInputLogic();
            int end = Math.min(getEntriesPerPage() + scroll, entires.size());
            for (int i = scroll; i < end; i++) {
                Vector2i entryPos = getEntryRenderPosition(i, gpos);
                entryBounds.setLocation(entryPos.x, entryPos.y);

                if (getMouse().getBounds().intersects(entryBounds)) {
                    indexHover = i;

                    for (int j = 1; j <= Mouse.TOTAL_MOUSE_BUTTONS_MONITORED; j++) {
                        if (getMouse().isMouseDown(j))
                            fireClickEvent(i, j);
                    }
                }
            }
        }
        return shouldHandleInput && !isMouseOver();
    }

    protected void doInputLogic() {
        if (gamestate.getGamestateManager().getEngine().getMouse().didMouseWheelMove() && isMouseOver()) {
            if (gamestate.getGamestateManager().getEngine().getMouse().getWheelRotation() > 0) {
                scrollDown();
            } else {
                scrollUp();
            }
        }
    }

    private void fireClickEvent(int i, int mouseCode) {
        for (IEventListener<GuiList<T, L>, Integer, Integer> listener : listeners)
            listener.onEvent(this, i, mouseCode);
    }

    protected Vector2i getScrollBarLocation(Vector2i gpos) {
        int barX = gpos.x + size.x - scrollBarWidth;
        int barY = gpos.y + (int) UtilMath.scaleF(scroll, entires.size() - getEntriesPerPage(), size.y - scrollBarHeight - 1);
        return scrollBarPosition.set(barX, barY + 1);
    }

    private int calcEntryHeight(Graphics2D g) {
        int totalHeight = Integer.MIN_VALUE;
        for (L l : entires) {
            Dimension r = UtilGraphics.getStringBounds(g, l.toString());
            totalHeight = Math.max(totalHeight, r.height);
        }
        return totalHeight;
    }

    protected Vector2i getEntryRenderPosition(int index, Vector2i gpos) {
        int x = gpos.x + 2;
        int y = gpos.y + (entryBounds.height + spacing) * (index - scroll) + 1;
        return renderEntryPosition.set(x, y);
    }

    protected int getEntriesPerPage() {
        return size.y / (entryBounds.height + spacing);
    }

    @Override
    public void render(Graphics2D g) {
        Vector2i gpos = getGlobalPosition();

        renderBackground(g, gpos);
        renderItems(g, gpos);
        renderScrollBar(g, gpos);
    }

    protected void renderScrollBar(Graphics2D g, Vector2i gpos) {
        if (!scrollBarEnabled || entires.size() <= getEntriesPerPage())
            return;

        Vector2i pos = getScrollBarLocation(gpos);

        g.setColor(barBackgroundColor);
        g.fillRect(pos.x, gpos.y + 1, scrollBarWidth, size.y - 1);

        g.setColor(barColor);
        g.fillRect(pos.x, pos.y, scrollBarWidth, scrollBarHeight);
    }

    protected void renderItems(Graphics2D g, Vector2i gpos) {
        Font oldFont = g.getFont();
        g.setFont(font);

        entryBounds.width = size.x - (scrollBarEnabled && (entires.size() > getEntriesPerPage()) ? scrollBarWidth : 0) - 3;
        if (entryHeightDirty) {
            entryBounds.height = calcEntryHeight(g);
            entryHeightDirty = false;
        }

        g.setClip(getBounds());

        int end = Math.min(getEntriesPerPage() + scroll, entires.size());
        for (int i = scroll; i < end; i++) {
            Vector2i entryPos = getEntryRenderPosition(i, gpos);

            if (i == indexHover && isEnabled())
                renderHover(g, entryPos, entryBounds);

            renderItem(g, entryPos, i, entires.get(i));
        }

        g.setClip(null);
        g.setFont(oldFont);
    }

    protected void renderHover(Graphics2D g, Vector2i pos, Rectangle size) {
        g.setColor(hoverColor);
        g.fillRect(pos.x, pos.y, size.width, size.height);
    }

    protected void renderItem(Graphics2D g, Vector2i pos, int index, L item) {
        g.setColor(getItemColor(index, item));
        String text = (item instanceof IListItem) ? ((IListItem) item).getListItemText() : item.toString();
        UtilGraphics.drawString(g, text, pos.x + 5, pos.y);
    }

    protected Color getItemColor(int index, L item) {
        return foregroundColor;
    }

    protected void renderBackground(Graphics2D g, Vector2i gpos) {
        g.setColor(backgroundColor);
        g.fillRect(gpos.x, gpos.y, size.x, size.y);

        g.setColor(outlineColor);
        g.drawRect(gpos.x, gpos.y, size.x, size.y);
    }

    public GuiList<T, L> addEntry(L entry) {
        entires.add(entry);
        this.entryHeightDirty = true;
        return this;
    }

    public boolean canScrollUp() {
        return scroll > 0;
    }

    public boolean canScrollDown() {
        return (entires.size() - getEntriesPerPage()) - scroll > 0;
    }

    public void scrollUp() {
        if (canScrollUp())
            scroll--;
    }

    public void scrollDown() {
        if (canScrollDown())
            scroll++;
    }

    public GuiList<T, L> addListener(IEventListener<GuiList<T, L>, Integer, Integer> listener) {
        listeners.add(listener);
        return this;
    }

    public void clear() {
        entires.clear();
    }

    public List<IEventListener<GuiList<T, L>, Integer, Integer>> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    public L getEntry(int i) {
        if (i < 0 || i >= entires.size())
            return null;
        return entires.get(i);
    }

    public GuiList<T, L> setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public GuiList<T, L> setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        return this;
    }

    public GuiList<T, L> setFont(Font font) {
        this.font = font;
        this.entryHeightDirty = true;
        return this;
    }

    public GuiList<T, L> setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public GuiList<T, L> setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        return this;
    }

    public GuiList<T, L> setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
        return this;
    }

    public GuiList<T, L> setBarColor(Color barColor) {
        this.barColor = barColor;
        return this;
    }

    public Color getBarBackgroundColor() {
        return barBackgroundColor;
    }

    public GuiList<T, L> setBarBackgroundColor(Color barBackgroundColor) {
        this.barBackgroundColor = barBackgroundColor;
        return this;
    }

    public GuiList<T, L> setScrollBarEnabled(boolean scrollBarEnabled) {
        this.scrollBarEnabled = scrollBarEnabled;
        return this;
    }

    public GuiList<T, L> setScrollBarWidth(int scrollBarWidth) {
        this.scrollBarWidth = scrollBarWidth;
        return this;
    }

    public GuiList<T, L> setScrollBarHeight(int scrollBarHeight) {
        this.scrollBarHeight = scrollBarHeight;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public int getEntryWidth() {
        return entryBounds.width;
    }

    public int getEntryHeight() {
        return entryBounds.height;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public int getTotalEntries() {
        return entires.size();
    }

    public int getSpacing() {
        return spacing;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getBarColor() {
        return barColor;
    }

    public boolean isScrollBarEnabled() {
        return scrollBarEnabled;
    }

    public int getScrollBarWidth() {
        return scrollBarWidth;
    }

    public int getScrollBarHeight() {
        return scrollBarHeight;
    }

}
