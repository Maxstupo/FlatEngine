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
public class GuiText extends AbstractGuiNode {

    public static enum TextAlignment {
        TOP_LEFT,
        TOP_MIDDLE,
        TOP_RIGHT,

        MIDDLE_LEFT,
        CENTER,
        MIDDLE_RIGHT,

        BOTTOM_LEFT,
        BOTTOM_MIDDLE,
        BOTTOM_RIGHT
    }

    private String text;

    protected Color color = Color.WHITE;
    private Font font = null;
    private TextAlignment alignment = TextAlignment.CENTER;

    private boolean isTextDirty = false;

    public GuiText(AbstractScreen screen, Vector2i localPosition) {
        this(screen, localPosition, "");
    }

    public GuiText(AbstractScreen gamestate, Vector2i localPosition, String text) {
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

        // Calculate local pos

        int x = 0;
        int y = 0;
        Vector2i parentSize = (getParent() != null) ? getParent().getSize() : Vector2i.ZERO;
        Vector2i size = getSize();

        switch (alignment) {
            case TOP_LEFT:
                break;
            case TOP_MIDDLE:
                x = parentSize.x / 2 - size.x / 2;
                break;
            case TOP_RIGHT:
                x = parentSize.x - size.x;
                break;
            case MIDDLE_LEFT:
                y = parentSize.y / 2 - size.y / 2;
                break;
            case CENTER:
                x = parentSize.x / 2 - size.x / 2;
                y = parentSize.y / 2 - size.y / 2;
                break;
            case MIDDLE_RIGHT:
                x = parentSize.x - size.x;
                y = parentSize.y / 2 - size.y / 2;
                break;
            case BOTTOM_LEFT:
                y = parentSize.y - size.y;
                break;
            case BOTTOM_MIDDLE:
                x = parentSize.x / 2 - size.x / 2;
                y = parentSize.y - size.y;
                break;
            case BOTTOM_RIGHT:
                x = parentSize.x - size.x;
                y = parentSize.y - size.y;
                break;
            default:
                break;
        }
        setLocalPosition(x, y);

        isTextDirty = false;
    }

    @Override
    protected void onDispose() {

    }

    public GuiText setText(String text) {
        this.text = text;
        this.isTextDirty = true;
        return this;
    }

    public GuiText setColor(Color color) {
        this.color = color;
        return this;
    }

    public GuiText setFont(Font font) {
        this.font = font;
        this.isTextDirty = true;
        return this;
    }

    public TextAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
        this.isTextDirty = true;
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