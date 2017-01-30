package com.github.maxstupo.flatengine.hgui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.hgui.AbstractAlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.screen.AbstractScreen;

/**
 * This GUI node represents a container containing text with the ability to auto adjust the container size depending on the size of the text.
 * 
 * @author Maxstupo
 */
public class GuiLabel extends GuiContainer {

    private final GuiText text;

    private int autoSizeSpacing = 2;
    private int minAutoSizeWidth = 70;
    private int minAutoSizeHeight = 0;
    private boolean autoSizeWidth;
    private boolean autoSizeHeight;

    /**
     * Create a new {@link GuiLabel} object.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param localX
     *            the local x position.
     * @param localY
     *            the local y position.
     * @param width
     *            the width of this node, if -1 {@link #isAutoSizeWidth()} will be true.
     * @param height
     *            the height of this node, if -1 {@link #isAutoSizeHeight()} will be true.
     * @param text
     *            the text of this label.
     */
    public GuiLabel(AbstractScreen screen, float localX, float localY, int width, int height, String text) {
        super(screen, localX, localY, width, height);
        this.autoSizeWidth = width <= -1;
        this.autoSizeHeight = height <= -1;

        this.text = new GuiText(screen, Alignment.CENTER, text) {

            @Override
            protected void renderFirst(Graphics2D g) {
                super.renderFirst(g);

                if (isAutoSizeWidth()) {
                    int width = Math.max(getMinAutoSizeWidth(), getWidth() + getAutoSizeSpacing() * 2);
                    GuiLabel.this.setWidth(width);
                    setLocalPositionX((width / 2 - getWidth() / 2) + 1);
                }
                if (isAutoSizeHeight()) {
                    int height = Math.max(getMinAutoSizeHeight(), getHeight() + getAutoSizeSpacing() * 2);
                    GuiLabel.this.setHeight(height);
                    setLocalPositionY(height / 2 - getHeight() / 2);
                }
            }
        };
        add(this.text);
    }

    /**
     * Returns the minimum size the auto size will allow in width.
     * 
     * @return the minimum size the auto size will allow in width.
     */
    public int getMinAutoSizeWidth() {
        return minAutoSizeWidth;
    }

    /**
     * Sets the minimum size the auto size will allow in width.
     * 
     * @param minAutoSizeWidth
     *            the minimum size the auto size will allow.
     * @return this object for chaining.
     */
    public GuiLabel setMinAutoSizeWidth(int minAutoSizeWidth) {
        this.minAutoSizeWidth = minAutoSizeWidth;
        setGraphicsCalculationsDirty();
        return this;
    }

    /**
     * Returns the minimum size the auto size will allow in height.
     * 
     * @return the minimum size the auto size will allow in height.
     */
    public int getMinAutoSizeHeight() {
        return minAutoSizeHeight;
    }

    /**
     * Sets the minimum size the auto size will allow in height.
     * 
     * @param minAutoSizeHeight
     *            the minimum size the auto size will allow.
     * @return this object for chaining.
     */
    public GuiLabel setMinAutoSizeHeight(int minAutoSizeHeight) {
        this.minAutoSizeHeight = minAutoSizeHeight;
        setGraphicsCalculationsDirty();
        return this;
    }

    /**
     * Returns the spacing between the label container and the text.
     * 
     * @return the spacing between the label container and the text.
     */
    public int getAutoSizeSpacing() {
        return autoSizeSpacing;
    }

    /**
     * Sets the spacing between the label container and the text.
     * 
     * @param autoSizeSpacing
     *            the spacing between the label container and the text.
     * @return this object for chaining.
     */
    public GuiLabel setAutoSizeSpacing(int autoSizeSpacing) {
        this.autoSizeSpacing = autoSizeSpacing;
        setGraphicsCalculationsDirty();
        return this;
    }

    /**
     * Returns true if the width of this node will be auto sized when the text changes.
     * 
     * @return true if the width of this node will be auto sized when the text changes.
     */
    public boolean isAutoSizeWidth() {
        return autoSizeWidth;
    }

    /**
     * Sets if the width of this node will be auto sized when the text changes.
     * 
     * @param autoSizeWidth
     *            true to auto size node width when text changes.
     * @return this object for chaining.
     */
    public GuiLabel setAutoSizeWidth(boolean autoSizeWidth) {
        this.autoSizeWidth = autoSizeWidth;
        setGraphicsCalculationsDirty();
        return this;
    }

    /**
     * Returns true if the height of this node will be auto sized when the text changes.
     * 
     * @return true if the height of this node will be auto sized when the text changes.
     */
    public boolean isAutoSizeHeight() {
        return autoSizeHeight;
    }

    /**
     * Sets if the height of this node will be auto sized when the text changes.
     * 
     * @param autoSizeHeight
     *            true to auto size node height when text changes.
     * @return this object for chaining.
     */
    public GuiLabel setAutoSizeHeight(boolean autoSizeHeight) {
        this.autoSizeHeight = autoSizeHeight;
        setGraphicsCalculationsDirty();
        return this;
    }

    /**
     * Returns the text node within this label.
     * 
     * @return the text node within this label.
     */
    public GuiText getTextNode() {
        return text;
    }

}
