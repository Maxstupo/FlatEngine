package com.github.maxstupo.flatengine.hgui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This GUI node is a progress bar that allows for both vertical and horizontal display and allows a value between zero and a given max value.
 * 
 * @author Maxstupo
 */
public class GuiProgressBar extends GuiContainer {

    private float maxValue = 100;
    private float value = 0;

    /** The color of the progress bar. */
    protected Color progressColor = Color.DARK_GRAY;
    /** The color of the outline around the progress bar. */
    protected Color progressOutlineColor = Color.BLACK;
    /** The stroke for the outline around the progress bar. */
    protected Stroke progressOutlineStroke = new BasicStroke(1);

    private int spacing = 2;

    private boolean isVertical = false;

    /**
     * Create a new {@link GuiProgressBar} object.
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
    public GuiProgressBar(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {
        return shouldHandleInput && !isMouseOver();
    }

    @Override
    protected void render(Graphics2D g) {
        super.render(g);

        Vector2i gpos = getGlobalPosition();

        int progressWidth = getProgressWidth();

        if (isVertical()) {

            if (getProgressColor() != null) {
                g.setColor(getProgressColor());
                g.fillRect(gpos.x + getSpacing(), gpos.y + (getHeight() - progressWidth) - getSpacing(), getWidth() - getSpacing() * 2, progressWidth);
            }

            if (getProgressOutlineColor() != null) {
                Stroke defaultStroke = g.getStroke();
                g.setStroke(getProgressOutlineStroke());
                {
                    g.setColor(getProgressOutlineColor());
                    g.drawRect(gpos.x + getSpacing(), gpos.y + (getHeight() - progressWidth) - getSpacing(), getWidth() - getSpacing() * 2, progressWidth);
                }
                g.setStroke(defaultStroke);
            }

        } else {

            if (getProgressColor() != null) {
                g.setColor(getProgressColor());
                g.fillRect(gpos.x + getSpacing(), gpos.y + getSpacing(), progressWidth, getHeight() - getSpacing() * 2);
            }

            if (getProgressOutlineColor() != null) {
                Stroke defaultStroke = g.getStroke();
                g.setStroke(getProgressOutlineStroke() != null ? getProgressOutlineStroke() : defaultStroke);
                {
                    g.setColor(getProgressOutlineColor());
                    g.drawRect(gpos.x + getSpacing(), gpos.y + getSpacing(), progressWidth, getHeight() - getSpacing() * 2);
                }
                g.setStroke(defaultStroke);
            }
        }
    }

    /**
     * Returns the scaled width/height of the progress bar depending on if {@link #isVertical()} is enabled.
     * 
     * @return the scaled width/height of the progress bar depending on if {@link #isVertical()} is enabled.
     */
    protected int getProgressWidth() {
        return (int) UtilMath.scaleF(getValue(), getMaxValue(), isVertical() ? getHeight() - getSpacing() * 2 : getWidth() - getSpacing() * 2);
    }

    /**
     * Returns the progress bar outline stroke.
     * 
     * @return the progress bar outline stroke.
     */
    public Stroke getProgressOutlineStroke() {
        return progressOutlineStroke;
    }

    /**
     * Sets the progress bar outline stroke.
     * 
     * @param progressOutlineStroke
     *            the progress bar outline stroke.
     * @return this object for chaining.
     */
    public GuiProgressBar setProgressOutlineStroke(Stroke progressOutlineStroke) {
        this.progressOutlineStroke = progressOutlineStroke;
        return this;
    }

    /**
     * Returns the outline color of the progress bar.
     * 
     * @return the outline color of the progress bar.
     */
    public Color getProgressOutlineColor() {
        return progressOutlineColor;
    }

    /**
     * Sets the outline color of the progress bar, set to null to disable.
     * 
     * @param progressOutlineColor
     *            the outline color of the progress bar.
     * @return this object for chaining.
     */
    public GuiProgressBar setProgressOutlineColor(Color progressOutlineColor) {
        this.progressOutlineColor = progressOutlineColor;
        return this;
    }

    /**
     * Returns the color of the progress bar.
     * 
     * @return the color of the progress bar.
     */
    public Color getProgressColor() {
        return progressColor;
    }

    /**
     * Sets the color of the progress bar, set to null to disable.
     * 
     * @param progressColor
     *            the progress bar color.
     * @return this object for chaining.
     */
    public GuiProgressBar setProgressColor(Color progressColor) {
        this.progressColor = progressColor;
        return this;
    }

    /**
     * Returns true if this progress bar is displayed vertically.
     * 
     * @return true if this progress bar is displayed vertically.
     */
    public boolean isVertical() {
        return isVertical;
    }

    /**
     * Sets if this progress bar is displayed vertically.
     * 
     * @param isVertical
     *            true for a vertically displayed progress bar.
     * @return this object for chaining.
     */
    public GuiProgressBar setVertical(boolean isVertical) {

        if (isVertical && !isVertical()) {// switching to.
            setSize(getHeight(), getWidth());

        } else if (!isVertical && isVertical()) {// switching from.
            setSize(getHeight(), getWidth());

        }
        this.isVertical = isVertical;
        return this;
    }

    /**
     * Returns the max value {@link #getValue()} can return.
     * 
     * @return the max value {@link #getValue()} can return.
     */
    public float getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the max value {@link #getValue()} can return.
     * 
     * @param maxValue
     *            the max value that {@link #getValue()} can return.
     * @return this object for chaining.
     */
    public GuiProgressBar setMaxValue(float maxValue) {
        this.maxValue = UtilMath.clampF(maxValue, 0, Float.MAX_VALUE);
        this.value = UtilMath.clampF(getValue(), 0, maxValue);
        return this;
    }

    /**
     * Returns the value of this progress bar.
     * 
     * @return the value of this progress bar.
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets the value of the progress bar.
     * 
     * @param value
     *            the value of the progress bar.
     * @return this object for chaining.
     */
    public GuiProgressBar setValue(float value) {
        this.value = UtilMath.clampF(value, 0, getMaxValue());
        return this;
    }

    /**
     * Returns the spacing between the progress bar and the outer container.
     * 
     * @return the spacing between the progress bar and the outer container.
     */
    public int getSpacing() {
        return spacing;
    }

    /**
     * Sets the spacing between the progress bar and the outer container.
     * 
     * @param spacing
     *            the spacing between the progress bar and the outer container.
     * @return this object for chaining.
     */
    public GuiProgressBar setSpacing(int spacing) {
        this.spacing = UtilMath.clampI(spacing, 0, (isVertical() ? getWidth() : getHeight()) / 2 - 2);
        return this;
    }

}
