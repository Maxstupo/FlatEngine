package com.github.maxstupo.flatengine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public class GuiText extends AlignableGuiNode {

    private String text;

    protected Color color = Color.WHITE;
    private Font font = null;

    private boolean isTextDirty = false;

    public GuiText(AbstractScreen screen, Alignment alignment) {
        this(screen, alignment, "");
    }

    public GuiText(AbstractScreen screen, Alignment alignment, String text) {
        this(screen, Vector2i.ZERO, text);
        setAlignment(alignment);
    }

    public GuiText(AbstractScreen screen, Vector2i localPosition) {
        this(screen, localPosition, "");
    }

    public GuiText(AbstractScreen gamestate, Vector2i localPosition, String text) {
        super(gamestate, localPosition, null);
        setText(text);
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {
        return shouldHandleInput;
    }

    @Override
    public void render(Graphics2D g) {
        if (isTextDirty)
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

    protected void calculateBounds(Graphics2D g) {
        if (font == null)
            font = g.getFont();

        Dimension bounds = UtilGraphics.getStringBounds(g, text, font);
        getSize().set(bounds.width, bounds.height);

        updateAlignment();

        isTextDirty = false;
    }

    @Override
    protected void onDispose() {

    }

    public GuiText setText(String text) {
        this.text = text;
        setTextDirty();
        return this;
    }

    public GuiText setColor(Color color) {
        this.color = color;
        return this;
    }

    public GuiText setFont(Font font) {
        this.font = font;
        setTextDirty();
        return this;
    }

    public GuiText setTextDirty() {
        isTextDirty = true;
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
        return String.format("GuiText [text=%s, color=%s, font=%s, isTextDirty=%s, screen=%s, localPosition=%s, size=%s, isEnabled=%s, isDebug=%s]", text, color, font, isTextDirty, screen, localPosition, size, isEnabled(), isDebug());
    }

}