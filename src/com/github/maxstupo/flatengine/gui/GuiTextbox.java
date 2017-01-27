package com.github.maxstupo.flatengine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.github.maxstupo.flatengine.input.IKeyListener;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.input.filter.IKeycodeFilter;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public class GuiTextbox extends GuiContainer implements IKeyListener {

    protected boolean isSelected = false;

    protected String text = "";

    protected int cursorCount = 0;

    protected String cursor = "_";
    protected String cursor_temp = "";
    protected IKeycodeFilter filter = null;

    protected Color foregroundColor = Color.WHITE;

    public GuiTextbox(AbstractScreen screen, Vector2i localPosition, Vector2i size) {
        super(screen, localPosition, size);
        setBackgroundColor(UtilGraphics.changeAlpha(Color.BLACK, 100));
        setOutlineColor(Color.BLACK);
        screen.getScreenManager().getEngine().getKeyboard().addListener(this);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleInput(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    public void doInputLogic() {
        if (screen.getScreenManager().getEngine().getMouse().isMouseDown(Mouse.LEFT_CLICK))
            isSelected = isMouseOver() && isEnabled();
    }

    protected void handleInput(KeyEvent e) {
        if (!isSelected)
            return;
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (text.length() > 0)
                text = text.substring(0, text.length() - 1);
        } else {
            if (filter != null) {
                if (!filter.filterKeycode(e)) {
                    return;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_WINDOWS || e.getKeyCode() == KeyEvent.VK_SHIFT || e.getKeyCode() == KeyEvent.VK_CAPS_LOCK || e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_ALT || e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_END || e.getKeyCode() == KeyEvent.VK_HOME) {
                return;
            }

            text += e.getKeyChar();

        }
    }

    @Override
    public boolean update(float delta, boolean shouldHandleInput) {
        if (shouldHandleInput)
            doInputLogic();

        if (cursorCount > 10) {
            cursorCount = 0;
            if (isSelected) {
                if (cursor_temp.equalsIgnoreCase(cursor)) {
                    cursor_temp = "";
                } else {
                    cursor_temp = cursor;
                }
            } else {
                cursor_temp = "";
            }
        } else {
            cursorCount++;
        }

        return !isEnabled() || shouldHandleInput;
    }

    @Override
    public void render(Graphics2D g) {
        Vector2i gpos = getGlobalPosition();

        g.setColor(backgroundColor);
        g.fillRect(gpos.x, gpos.y, size.x, size.y);

        g.setColor(outlineColor);
        g.drawRect(gpos.x, gpos.y, size.x, size.y);

        g.setColor(foregroundColor);
        Dimension r = UtilGraphics.getStringBounds(g, text);
        int textX = gpos.x + 2;
        int textY = gpos.y + (size.y / 2 - r.height / 2) + 1;
        UtilGraphics.drawString(g, text + cursor_temp, textX, textY);

    }

    @Override
    protected void onDispose() {
        screen.getScreenManager().getEngine().getKeyboard().removeListener(this);
    }

    @Override
    public void onResize(int width, int height) {

    }

    public void setForegroundColor(Color color) {
        foregroundColor = color;
    }

    public void select() {
        if (isEnabled())
            isSelected = true;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public IKeycodeFilter getFilter() {
        return filter;
    }

    public void setFilter(IKeycodeFilter filter) {
        this.filter = filter;
    }

    public void unselect() {
        isSelected = false;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return String.format("GuiTextbox [isSelected=%s, text=%s, cursorCount=%s, cursor=%s, cursor_temp=%s, filter=%s, outlineColor=%s, backgroundColor=%s, foregroundColor=%s, screen=%s, localPosition=%s, size=%s, isEnabled=%s, isDebug=%s]", isSelected, text, cursorCount, cursor, cursor_temp, filter, outlineColor, backgroundColor, foregroundColor, screen, localPosition, size, isEnabled(), isDebug());
    }
}
