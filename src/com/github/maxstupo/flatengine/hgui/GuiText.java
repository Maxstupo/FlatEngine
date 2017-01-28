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

    private String text;

    /** The color of the text. */
    protected Color color = Color.WHITE;
    private Font font = null;

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
    public void render(Graphics2D g) {

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

    @Override
    protected void renderFirst(Graphics2D g) {

        if (getFont() == null)
            setFont(g.getFont());

        Dimension bounds = UtilGraphics.getStringBounds(g, getText(), getFont());
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
        this.text = text;
        setGraphicsCalculationsDirty();
        return this;
    }

    /**
     * Sets the color of the text.
     * 
     * @param color
     *            the color of the text.
     * @return this object for chaining.
     */
    public GuiText setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the font of the text.
     * 
     * @param font
     *            the font of the text.
     * @return this object for chaining.
     */
    public GuiText setFont(Font font) {
        this.font = font;
        setGraphicsCalculationsDirty();
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
    public Color getColor() {
        return color;
    }

    /**
     * Returns the font of the text.
     * 
     * @return the font of the text.
     */
    public Font getFont() {
        return font;
    }

    @Override
    public String toString() {
        return String.format("%s [text=%s, color=%s, font=%s, isVisible=%s, getAlignment()=%s, getLocalPositionX()=%s, getLocalPositionY()=%s, getWidth()=%s, getHeight()=%s, isEnabled()=%s]", getClass().getSimpleName(), text, color, font, isVisible, getAlignment(), getLocalPositionX(), getLocalPositionY(), getWidth(), getHeight(), isEnabled());
    }

}
