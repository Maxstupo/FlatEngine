package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
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
        this(screen, 0, 0, text);
        setAlignment(alignment);
    }

    public GuiText(AbstractScreen screen, float localX, float localY) {
        this(screen, localX, localY, "");
    }

    public GuiText(AbstractScreen screen, float localX, float localY, String text) {
        super(screen, localX, localY, 0, 0);
        setText(text);
    }

    @Override
    public void render(Graphics2D g) {
        if (isTextDirty)
            calculateBounds(g);

        Vector2i gpos = getGlobalPosition();

        Font defaultFont = g.getFont();
        Color defaultColor = g.getColor();
        {
            g.setFont(getFont());
            g.setColor(getColor());

            UtilGraphics.drawString(g, getText(), gpos.x, gpos.y);
        }
        g.setFont(defaultFont);
        g.setColor(defaultColor);

    }

    protected void calculateBounds(Graphics2D g) {
        if (getFont() == null)
            setFont(g.getFont());

        Dimension bounds = UtilGraphics.getStringBounds(g, getText(), getFont());
        setSize(bounds.width, bounds.height);

        // setAlignmentDirty();

        isTextDirty = false;
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
        return String.format("%s [text=%s, color=%s, font=%s, isTextDirty=%s, isVisible=%s, getAlignment()=%s, getLocalPositionX()=%s, getLocalPositionY()=%s, getWidth()=%s, getHeight()=%s, isEnabled()=%s]", getClass().getSimpleName(), text, color, font, isTextDirty, isVisible, getAlignment(), getLocalPositionX(), getLocalPositionY(), getWidth(), getHeight(), isEnabled());
    }

}
