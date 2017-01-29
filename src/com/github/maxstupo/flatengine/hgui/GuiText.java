package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This GUI node represents a label containing text.
 * 
 * @author Maxstupo
 */
public class GuiText extends AbstractAlignableGuiNode {

    private String text = "";

    /** The color of the text. */
    protected Color textColor = Color.WHITE;
    private Font textFont = null;

    /**
     * Create a new {@link GuiText} object, with an empty label.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param alignment
     *            the alignment of this node within the parent node.
     * 
     */
    public GuiText(AbstractScreen screen, Alignment alignment) {
        this(screen, alignment, "");
    }

    /**
     * Create a new {@link GuiText} object.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param alignment
     *            the alignment of this node within the parent node.
     * @param text
     *            the text of this label.
     */
    public GuiText(AbstractScreen screen, Alignment alignment, String text) {
        this(screen, 0, 0, text);
        setAlignment(alignment);
    }

    /**
     * Create a new {@link GuiText} object, with an empty label.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param localX
     *            the local x position.
     * @param localY
     *            the local y position.
     */
    public GuiText(AbstractScreen screen, float localX, float localY) {
        this(screen, localX, localY, "");
    }

    /**
     * Create a new {@link GuiText} object.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param localX
     *            the local x position.
     * @param localY
     *            the local y position.
     * @param text
     *            the text of this label.
     */
    public GuiText(AbstractScreen screen, float localX, float localY, String text) {
        super(screen, localX, localY, 0, 0);
        setText(text);
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {
        return shouldHandleInput;
    }

    @Override
    protected void render(Graphics2D g) {
        Vector2i gpos = getGlobalPosition();

        if (getText() != null) {
            Font defaultFont = g.getFont();
            Color defaultColor = g.getColor();
            {
                g.setFont(getTextFont());
                g.setColor(getTextColor());

                UtilGraphics.drawString(g, getText(), gpos.x, gpos.y);
            }
            g.setFont(defaultFont);
            g.setColor(defaultColor);
        }
    }

    @Override
    protected void renderFirst(Graphics2D g) {

        if (getTextFont() == null)
            setTextFont(g.getFont());

        Dimension bounds = UtilGraphics.getStringBounds(g, getText(), getTextFont());

        setSize(bounds.width, bounds.height);

        super.renderFirst(g); // Update alignable node, with the new GUI text size.
    }

    /**
     * Sets the text label of this node.
     * 
     * @param text
     *            the text.
     * @return this object for chaining.
     */
    public GuiText setText(String text) {
        if (this.text == null || !this.text.equals(text)) {
            this.text = text;
            setGraphicsCalculationsDirty();
        }
        return this;
    }

    /**
     * Sets the color of the text.
     * 
     * @param color
     *            the color of the text.
     * @return this object for chaining.
     */
    public GuiText setTextColor(Color color) {
        this.textColor = color;
        return this;
    }

    /**
     * Sets the font of the text.
     * 
     * @param font
     *            the font of the text.
     * @return this object for chaining.
     */
    public GuiText setTextFont(Font font) {
        if (this.textFont == null || !this.textFont.equals(font)) {
            this.textFont = font;
            setGraphicsCalculationsDirty();
        }
        return this;
    }

    /**
     * Returns the text.
     * 
     * @return the text.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the color of the text.
     * 
     * @return the color of the text.
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * Returns the font of the text.
     * 
     * @return the font of the text.
     */
    public Font getTextFont() {
        return textFont;
    }

    @Override
    public String toString() {
        return String.format("%s [text=%s, color=%s, font=%s, isVisible=%s, getAlignment()=%s, getLocalPositionX()=%s, getLocalPositionY()=%s, getWidth()=%s, getHeight()=%s, isEnabled()=%s]", getClass().getSimpleName(), text, textColor, textFont, isVisible, getAlignment(), getLocalPositionX(), getLocalPositionY(), getWidth(), getHeight(), isEnabled());
    }

}
