package com.github.maxstupo.flatengine.hgui;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This GUI node allows for an image within a container. The image can be resized to fill the node dimensions.
 * 
 * @author Maxstupo
 */
public class GuiImage extends GuiContainer {

    /** The sprite icon. */
    protected Sprite icon;

    /** If true the icon will be resized to fit this container. */
    protected boolean isIconResized = true;

    /** If true the aspect ratio is kept when using {@link #isIconResized()}. */
    protected boolean isAspectRatioKept = false;

    /** The space between the container and the icon. */
    protected int iconSpacing = 3;

    /**
     * Create a new {@link GuiImage} object.
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
    public GuiImage(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
    }

    @Override
    protected void render(Graphics2D g) {
        super.render(g);

        if (getIcon() != null) {

            Vector2i gpos = getGlobalPosition();

            if (isIconResized()) {
                int iconWidth = getWidth() - getIconSpacing() * 2;
                int iconHeight = getHeight() - getIconSpacing() * 2;

                if (isAspectRatioKept()) {
                    Dimension aspectDimension = UtilGraphics.getScaledDimension(getIcon().getWidth(), getIcon().getHeight(), iconWidth, iconHeight);
                    // System.out.println(aspectDimension + ", " + iconWidth + ", " + iconHeight + ", " + getIcon().getWidth());
                    iconWidth = aspectDimension.width;
                    iconHeight = aspectDimension.height;
                }

                getIcon().draw(g, gpos.x + getWidth() / 2 - iconWidth / 2, gpos.y + getHeight() / 2 - iconHeight / 2, iconWidth, iconHeight);

            } else {
                int iconWidth = getIcon().getWidth();
                int iconHeight = getIcon().getHeight();

                getIcon().draw(g, gpos.x + getWidth() / 2 - iconWidth / 2, gpos.y + getHeight() / 2 - iconHeight / 2);
            }
        }
    }

    /**
     * Returns true if the aspect ratio is kept when using {@link #isIconResized()}.
     * 
     * @return true if the aspect ratio is kept when using {@link #isIconResized()}.
     */
    public boolean isAspectRatioKept() {
        return isAspectRatioKept;
    }

    /**
     * Sets if the aspect ratio is kept when using {@link #isIconResized()}.
     * 
     * @param isAspectRatioKept
     *            true to keep aspect ratio when {@link #isIconResized()} is true.
     * @return this object for chaining.
     */
    public GuiImage setAspectRatioKept(boolean isAspectRatioKept) {
        this.isAspectRatioKept = isAspectRatioKept;
        return this;
    }

    /**
     * Returns the sprite that is rendered within this node.
     * 
     * @return the sprite that is rendered within this node.
     */
    public Sprite getIcon() {
        return icon;
    }

    /**
     * Sets the icon sprite of this node, setting the icon to null will disable rendering.
     * 
     * @param icon
     *            the icon sprite.
     * @return this object for chaining.
     */
    public GuiImage setIcon(Sprite icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Returns true if the icon will be rendered at the size of this node.
     * 
     * @return true if the icon will be rendered at the size of this node.
     */
    public boolean isIconResized() {
        return isIconResized;
    }

    /**
     * Sets if the icon will be rendered at the size of this node.
     * 
     * @param isIconResized
     *            if true the icon will be rendered at the size of this node.
     * @return this object for chaining.
     */
    public GuiImage setIconResized(boolean isIconResized) {
        this.isIconResized = isIconResized;
        return this;
    }

    /**
     * Returns the spacing between the icon and the node. {@link #isIconResized()} must be true for this to have any effect.
     * 
     * @return the spacing between the icon and the node.
     */
    public int getIconSpacing() {
        return iconSpacing;
    }

    /**
     * Sets the spacing between the icon and the node. {@link #isIconResized()} must be true for this to have any effect.
     * 
     * @param iconSpacing
     *            the spacing between the icon and the node.
     * @return this object for chaining.
     */
    public GuiImage setIconSpacing(int iconSpacing) {
        this.iconSpacing = iconSpacing;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((icon == null) ? 0 : icon.hashCode());
        result = prime * result + iconSpacing;
        result = prime * result + (isAspectRatioKept ? 1231 : 1237);
        result = prime * result + (isIconResized ? 1231 : 1237);
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
        GuiImage other = (GuiImage) obj;
        if (icon == null) {
            if (other.icon != null)
                return false;
        } else if (!icon.equals(other.icon))
            return false;
        if (iconSpacing != other.iconSpacing)
            return false;
        if (isAspectRatioKept != other.isAspectRatioKept)
            return false;
        if (isIconResized != other.isIconResized)
            return false;
        return true;
    }
}
