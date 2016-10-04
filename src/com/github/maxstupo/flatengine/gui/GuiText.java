package com.github.maxstupo.flatengine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public class GuiText<T extends Enum<T>> extends AbstractGuiNode<T> {

    private String text;

    protected Color color = Color.WHITE;
    private Font font = null;

    private boolean isTextDirty = false;

    public GuiText(AbstractGamestate<T> gamestate, Vector2i localPosition) {
        this(gamestate, localPosition, "");
    }

    public GuiText(AbstractGamestate<T> gamestate, Vector2i localPosition, String text) {
        super(gamestate, localPosition, null);
        this.setText(text);
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {
        return !isEnabled() || shouldHandleInput;
    }

    @Override
    public void render(Graphics2D g) {
        if (!isEnabled())
            return;

        calculateBounds(g);

        Vector2i gpos = getGlobalPosition();

        Font defaultFont = g.getFont();
        Color defaultColor = g.getColor();
        {
            g.setFont(font);
            g.setColor(color);

            UtilGraphics.drawString(g, text, gpos.x, gpos.y);

            if (isDebug()) {
                g.setColor(Color.WHITE);
                g.drawRect(gpos.x, gpos.y, size.x, size.y);
            }
        }
        g.setFont(defaultFont);
        g.setColor(defaultColor);

    }

    private void calculateBounds(Graphics2D g) {
        if (!isTextDirty)
            return;
        if (font == null)
            font = g.getFont();

        Dimension bounds = UtilGraphics.getStringBounds(g, text, font);
        getSize().set(bounds.width, bounds.height);

        isTextDirty = false;
    }

    public GuiText<T> setText(String text) {
        this.text = text;
        this.isTextDirty = true;
        return this;
    }

    public GuiText<T> setColor(Color color) {
        this.color = color;
        return this;
    }

    public GuiText<T> setFont(Font font) {
        this.font = font;
        this.isTextDirty = true;
        return this;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    public Font getFont() {
        return font;
    }

    @Override
    public String toString() {
        return "GuiText [text=" + text + ", color=" + color + ", font=" + font + ", isDirty=" + isTextDirty + "]";
    }

}