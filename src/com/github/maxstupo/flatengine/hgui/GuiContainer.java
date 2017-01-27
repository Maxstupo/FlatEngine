package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class GuiContainer extends AbstractNode {

    protected Color backgroundColor = UtilGraphics.changeAlpha(Color.DARK_GRAY, 127);
    protected Color outlineColor = Color.BLACK;

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
            g.setColor(getOutlineColor());
            g.drawRect(gpos.x, gpos.y, getWidth(), getHeight());
        }
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public GuiContainer setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        return this;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public GuiContainer setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s [backgroundColor=%s, outlineColor=%s, isVisible=%s, getLocalPositionX()=%s, getLocalPositionY()=%s, getWidth()=%s, getHeight()=%s, isEnabled()=%s]", getClass().getSimpleName(), backgroundColor, outlineColor, isVisible, getLocalPositionX(), getLocalPositionY(), getWidth(), getHeight(), isEnabled());
    }
}
