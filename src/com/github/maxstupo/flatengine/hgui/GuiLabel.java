package com.github.maxstupo.flatengine.hgui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.hgui.AbstractAlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.screen.AbstractScreen;

/**
 * @author Maxstupo
 *
 */
public class GuiLabel extends GuiContainer {

    private final GuiText text;

    protected int autoSizeSpacing = 2;
    protected int minAutoSizeWidth = 70;
    protected int minAutoSizeHeight = 0;
    protected boolean autoSizeWidth;
    protected boolean autoSizeHeight;

    public GuiLabel(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
        this.autoSizeWidth = width <= -1;
        this.autoSizeHeight = height <= -1;

        this.text = new GuiText(screen, Alignment.CENTER) {

            @Override
            protected void renderFirst(Graphics2D g) {
                super.renderFirst(g);

                if (autoSizeWidth) {
                    int width = Math.max(minAutoSizeWidth, getWidth() + autoSizeSpacing * 2);
                    GuiLabel.this.setWidth(width);
                    setLocalPositionX((width / 2 - getWidth() / 2) + 1);
                }
                if (autoSizeHeight) {
                    int height = Math.max(minAutoSizeHeight, getHeight() + autoSizeSpacing * 2);
                    GuiLabel.this.setHeight(height);
                    setLocalPositionY(height / 2 - getHeight() / 2);
                }
            }
        };
        add(text);
    }

    public int getMinAutoSizeWidth() {
        return minAutoSizeWidth;
    }

    public GuiLabel setMinAutoSizeWidth(int minAutoSizeWidth) {
        this.minAutoSizeWidth = minAutoSizeWidth;
        setGraphicsCalculationsDirty();
        return this;
    }

    public int getMinAutoSizeHeight() {
        return minAutoSizeHeight;
    }

    public GuiLabel setMinAutoSizeHeight(int minAutoSizeHeight) {
        this.minAutoSizeHeight = minAutoSizeHeight;
        setGraphicsCalculationsDirty();
        return this;
    }

    public int getAutoSizeSpacing() {
        return autoSizeSpacing;
    }

    public GuiLabel setAutoSizeSpacing(int autoSizeSpacing) {
        this.autoSizeSpacing = autoSizeSpacing;
        setGraphicsCalculationsDirty();
        return this;
    }

    public boolean isAutoSizeWidth() {
        return autoSizeWidth;
    }

    public GuiLabel setAutoSizeWidth(boolean autoSizeWidth) {
        this.autoSizeWidth = autoSizeWidth;
        setGraphicsCalculationsDirty();
        return this;
    }

    public boolean isAutoSizeHeight() {
        return autoSizeHeight;
    }

    public GuiLabel setAutoSizeHeight(boolean autoSizeHeight) {
        this.autoSizeHeight = autoSizeHeight;
        setGraphicsCalculationsDirty();
        return this;
    }

    public GuiText getTextNode() {
        return text;
    }

}
