package com.github.maxstupo.flatengine.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public class GuiButton<T extends Enum<T>> extends AbstractGuiNode<T> {

    private final GuiText<T> text;

    protected Color backgroundColor = Color.GRAY;
    protected Color foregroundColor = Color.WHITE;
    protected Color selectionColor = Color.RED;

    protected boolean boxLess;

    private final Stroke outlineStroke = new BasicStroke(2);

    private boolean autoSizeWidth = false;
    private boolean autoSizeHeight = false;

    private boolean isMouseOver;

    protected final List<IEventListener<GuiButton<T>, String, Integer>> listeners = new ArrayList<>();

    public GuiButton(AbstractGamestate<T> gamestate, String text, Vector2i localPosition) {
        this(gamestate, text, localPosition, new Vector2i(-1, -1));
    }

    public GuiButton(AbstractGamestate<T> gamestate, String text, Vector2i localPosition, Vector2i size) {
        super(gamestate, localPosition, size);
        this.text = new GuiText<>(gamestate, null, text);
        this.addChild(this.text);

        this.text.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        this.autoSizeWidth = (getSize().x <= 0);
        this.autoSizeHeight = (getSize().y <= 0);
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {

        if (autoSizeWidth)
            size.x = text.getSize().x;
        if (autoSizeHeight)
            size.y = text.getSize().y;

        int x = size.x / 2 - (text.getSize().x - 3) / 2;
        int y = size.y / 2 - text.getSize().y / 2;
        text.setLocalPosition(x, y);

        if (!isEnabled())
            return shouldHandleInput;

        if (shouldHandleInput) {
            isMouseOver = isMouseOver();
            doInputLogic();
        } else {
            isMouseOver = false;
        }
        return shouldHandleInput && !isMouseOver;
    }

    public void doInputLogic() {
        for (int i = 1; i <= Mouse.TOTAL_MOUSE_BUTTONS_MONITORED; i++) {
            if (isMouseClicked(i))
                fireActionEvent(i);
        }
    }

    protected void fireActionEvent(int mouseButton) {
        for (IEventListener<GuiButton<T>, String, Integer> listener : listeners)
            listener.onEvent(this, text.getText(), mouseButton);
    }

    @Override
    public void render(Graphics2D g) {

        Vector2i gpos = getGlobalPosition();

        if (!boxLess) {
            g.setColor(getBackgroundColor());
            g.fill3DRect(gpos.x, gpos.y, size.x, size.y, isEnabled);

            if (isMouseOver) {
                g.setColor(getSelectionColor());

                Stroke defaultStroke = g.getStroke();
                {
                    g.setStroke(outlineStroke);
                    g.drawRect(gpos.x + 1, gpos.y + 1, size.x - 2, size.y - 2);
                }
                g.setStroke(defaultStroke);
            }
            text.setColor(getForegroundColor());
        } else {
            text.setColor(isMouseOver ? getSelectionColor() : getForegroundColor());
        }

        if (isDebug) {
            g.setColor(Color.WHITE);
            g.drawRect(gpos.x, gpos.y, size.x, size.y);
        }
    }

    public GuiButton<T> setText(String text) {
        this.text.setText(text);
        return this;
    }

    public GuiButton<T> setFont(Font font) {
        this.text.setFont(font);
        return this;
    }

    public GuiButton<T> setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public GuiButton<T> setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        return this;
    }

    public GuiButton<T> setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
        return this;
    }

    public GuiButton<T> setBoxLess(boolean boxLess) {
        this.boxLess = boxLess;
        return this;
    }

    public GuiButton<T> setAutoSizeWidth(boolean autoSizeWidth) {
        this.autoSizeWidth = autoSizeWidth;
        return this;
    }

    public GuiButton<T> setAutoSizeHeight(boolean autoSizeHeight) {
        this.autoSizeHeight = autoSizeHeight;
        return this;
    }

    public GuiButton<T> addListener(IEventListener<GuiButton<T>, String, Integer> listener) {
        if (listener != null)
            listeners.add(listener);
        return this;
    }

    public Font getFont() {
        return text.getFont();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public boolean isBoxLess() {
        return boxLess;
    }

    public boolean isAutoSizeWidth() {
        return autoSizeWidth;
    }

    public boolean isAutoSizeHeight() {
        return autoSizeHeight;
    }

    @Override
    public String toString() {
        return "GuiButton [text=" + text + ", backgroundColor=" + backgroundColor + ", foregroundColor=" + foregroundColor + ", selectionColor=" + selectionColor + ", boxLess=" + boxLess + ", autoSizeWidth=" + autoSizeWidth + ", autoSizeHeight=" + autoSizeHeight + "]";
    }

}
