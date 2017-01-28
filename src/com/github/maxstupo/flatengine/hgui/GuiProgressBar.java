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

    protected float maxValue = 100;
    protected float value = 0;

    protected Color progressColor = Color.DARK_GRAY;
    protected Color progressOutlineColor = Color.BLACK;
    protected Stroke progressOutlineStroke = new BasicStroke(1);

    protected int spacing = 2;

    protected boolean isVertical = false;

    public GuiProgressBar(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
    }

    @Override
    protected void render(Graphics2D g) {
        super.render(g);

        Vector2i gpos = getGlobalPosition();

        int progressWidth = getProgressWidth();

        if (isVertical()) {

            if (getProgressColor() != null) {
                g.setColor(getProgressColor());
                g.fillRect(gpos.x + spacing, gpos.y + (getHeight() - progressWidth) - spacing, getWidth() - spacing * 2, progressWidth);
            }

            if (getProgressOutlineColor() != null) {
                Stroke defaultStroke = g.getStroke();
                g.setStroke(getProgressOutlineStroke());
                {
                    g.setColor(getProgressOutlineColor());
                    g.drawRect(gpos.x + spacing, gpos.y + (getHeight() - progressWidth) - spacing, getWidth() - spacing * 2, progressWidth);
                }
                g.setStroke(defaultStroke);
            }

        } else {

            if (getProgressColor() != null) {
                g.setColor(getProgressColor());
                g.fillRect(gpos.x + spacing, gpos.y + spacing, progressWidth, getHeight() - spacing * 2);
            }

            if (getProgressOutlineColor() != null) {
                Stroke defaultStroke = g.getStroke();
                g.setStroke(getProgressOutlineStroke());
                {
                    g.setColor(getProgressOutlineColor());
                    g.drawRect(gpos.x + spacing, gpos.y + spacing, progressWidth, getHeight() - spacing * 2);
                }
                g.setStroke(defaultStroke);
            }
        }
    }

    protected int getProgressWidth() {
        return (int) UtilMath.scaleF(value, maxValue, isVertical ? getHeight() - spacing * 2 : getWidth() - spacing * 2);
    }

    public Stroke getProgressOutlineStroke() {
        return progressOutlineStroke;
    }

    public GuiProgressBar setProgressOutlineStroke(Stroke progressOutlineStroke) {
        this.progressOutlineStroke = progressOutlineStroke;
        return this;
    }

    public Color getProgressOutlineColor() {
        return progressOutlineColor;
    }

    public GuiProgressBar setProgressOutlineColor(Color progressOutlineColor) {
        this.progressOutlineColor = progressOutlineColor;
        return this;
    }

    public Color getProgressColor() {
        return progressColor;
    }

    public GuiProgressBar setProgressColor(Color progressColor) {
        this.progressColor = progressColor;
        return this;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public GuiProgressBar setVertical(boolean isVertical) {

        if (isVertical && !this.isVertical) {// switching to.
            // setLocalPositionY(getLocalPositionY() - getWidth());
            setSize(getHeight(), getWidth());

        } else if (!isVertical && this.isVertical) {// switching from.
            // setLocalPositionY(getLocalPositionY() + getHeight());
            setSize(getHeight(), getWidth());

        }
        this.isVertical = isVertical;
        return this;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public GuiProgressBar setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public float getValue() {
        return value;
    }

    public GuiProgressBar setValue(float value) {
        this.value = UtilMath.clampF(value, 0, getMaxValue());
        return this;
    }

}
