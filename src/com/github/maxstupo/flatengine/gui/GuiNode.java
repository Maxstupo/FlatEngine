package com.github.maxstupo.flatengine.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class GuiNode extends AbstractGuiNode {

    protected Color backgroundColor = Color.GRAY;
    protected Color outlineColor = Color.BLACK;

    public GuiNode(AbstractScreen screen, Vector2i localPosition, Vector2i size) {
        super(screen, localPosition, size);
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {
        return shouldHandleInput;
    }

    @Override
    public void render(Graphics2D g) {
        Vector2i gpos = getGlobalPosition();
        if (backgroundColor != null) {
            g.setColor(getBackgroundColor());
            g.fillRect(gpos.x, gpos.y, size.x, size.y);
        }

        if (outlineColor != null) {
            g.setColor(getOutlineColor());
            g.drawRect(gpos.x, gpos.y, size.x, size.y);
        }
    }

    @Override
    protected void onDispose() {

    }

    @Override
    public void onResize(int width, int height) {

    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public GuiNode setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        return this;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public GuiNode setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

}
