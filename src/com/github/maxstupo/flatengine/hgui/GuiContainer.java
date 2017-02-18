package com.github.maxstupo.flatengine.hgui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This GUI node represents a basic container allowing for a background and outline color.
 * 
 * @author Maxstupo
 */
public class GuiContainer extends AbstractNode {

    /** The background color of this node. */
    protected Color backgroundColor = new Color(127, 127, 127);
    /** The outline color of this node. */
    protected Color outlineColor = Color.BLACK;

    /** The stroke of the outline. */
    protected Stroke outlineStroke = new BasicStroke(1);

    /**
     * Create a new {@link GuiContainer} object.
     * 
     * 
     * @param screen
     *            the screen that owns this node.
     * @param localX
     *            the local x position.
     * @param localY
     *            the local y position.
     * @param width
     *            the width of this node.
     * @param height
     *            the height of this node.
     */
    public GuiContainer(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {
        return shouldHandleInput;
    }

    @Override
    protected void render(Graphics2D g) {
        Vector2i gpos = getGlobalPosition();
        if (getBackgroundColor() != null) {
            g.setColor(getBackgroundColor());
            g.fillRect(gpos.x, gpos.y, getWidth(), getHeight());
        }

        if (getOutlineColor() != null) {
            Stroke defaultStroke = g.getStroke();
            {
                if (getOutlineStroke() != null)
                    g.setStroke(getOutlineStroke());

                g.setColor(getOutlineColor());
                g.drawRect(gpos.x, gpos.y, getWidth() - 1, getHeight() - 1);
            }
            g.setStroke(defaultStroke);
        }
    }

    /**
     * Returns the outline color of this node.
     * 
     * @return the outline color of this node.
     */
    public Color getOutlineColor() {
        return outlineColor;
    }

    /**
     * Sets the outline color of this node, setting to null will disable the outline.
     * 
     * @param outlineColor
     *            the outline color.
     * @return this object for chaining.
     */
    public GuiContainer setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        return this;
    }

    /**
     * Returns the background color of this node.
     * 
     * @return the background color of this node.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color of this node, setting to null will disable the background.
     * 
     * @param backgroundColor
     *            the background color.
     * @return this object for chaining.
     */
    public GuiContainer setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Returns the stroke used for the outline.
     * 
     * @return the stroke used for the outline.
     */
    public Stroke getOutlineStroke() {
        return outlineStroke;
    }

    /**
     * Sets the stroke used for the outline.
     * 
     * @param outlineStroke
     *            the stroke used for the outline.
     * @return this object for chaining.
     */
    public GuiContainer setOutlineStroke(Stroke outlineStroke) {
        this.outlineStroke = outlineStroke;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s [backgroundColor=%s, outlineColor=%s, isVisible=%s, getLocalPositionX()=%s, getLocalPositionY()=%s, getWidth()=%s, getHeight()=%s, isEnabled()=%s]", getClass().getSimpleName(), backgroundColor, outlineColor, isVisible, getLocalPositionX(), getLocalPositionY(), getWidth(), getHeight(), isEnabled());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((backgroundColor == null) ? 0 : backgroundColor.hashCode());
        result = prime * result + ((outlineColor == null) ? 0 : outlineColor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GuiContainer other = (GuiContainer) obj;
        if (backgroundColor == null) {
            if (other.backgroundColor != null)
                return false;
        } else if (!backgroundColor.equals(other.backgroundColor))
            return false;
        if (outlineColor == null) {
            if (other.outlineColor != null)
                return false;
        } else if (!outlineColor.equals(other.outlineColor))
            return false;
        return true;
    }
}
