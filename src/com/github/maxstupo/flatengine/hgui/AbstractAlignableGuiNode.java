package com.github.maxstupo.flatengine.hgui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.screen.AbstractScreen;

/**
 * This GUI node can align itself based on an {@link Alignment} that is set using {@link #setAlignment(Alignment)} it will use the parent node as
 * reference.
 * 
 * @author Maxstupo
 */
public abstract class AbstractAlignableGuiNode extends AbstractNode {

    /**
     * This enum represents alignments that the {@link AbstractAlignableGuiNode} can use to position itself within a parent node.
     * 
     * @author Maxstupo
     */
    @SuppressWarnings("javadoc")
    public static enum Alignment {
        TOP_LEFT,
        TOP_MIDDLE,
        TOP_RIGHT,

        MIDDLE_LEFT,
        /** Center of the parent. */
        CENTER,
        MIDDLE_RIGHT,

        BOTTOM_LEFT,
        BOTTOM_MIDDLE,
        BOTTOM_RIGHT,
        /** Alignment is off and will not calculate. */
        OFF
    }

    private Alignment alignment = Alignment.OFF;

    /**
     * Create a {@link AbstractAlignableGuiNode}.
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
    public AbstractAlignableGuiNode(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
        setUsePercentagePositions(false);
    }

    /**
     * Calculates the alignment for this node.
     */
    protected void updateAlignment() {
        if (getAlignment() == Alignment.OFF)
            return;

        float x = getLocalPositionX();
        float y = getLocalPositionY();
        int pwidth = (getParent() != null) ? getParent().getWidth() : 0;
        int pheight = (getParent() != null) ? getParent().getHeight() : 0;

        switch (getAlignment()) {
            case TOP_LEFT:
                break;
            case TOP_MIDDLE:
                x = pwidth / 2 - getWidth() / 2;
                break;
            case TOP_RIGHT:
                x = pwidth - getWidth();
                break;
            case MIDDLE_LEFT:
                y = pheight / 2 - getHeight() / 2;
                break;
            case CENTER:
                x = pwidth / 2 - getWidth() / 2;
                y = pheight / 2 - getHeight() / 2;
                break;
            case MIDDLE_RIGHT:
                x = pwidth - getWidth();
                y = pheight / 2 - getHeight() / 2;
                break;
            case BOTTOM_LEFT:
                y = pheight - getHeight();
                break;
            case BOTTOM_MIDDLE:
                x = pwidth / 2 - getWidth() / 2;
                y = pheight - getHeight();
                break;
            case BOTTOM_RIGHT:
                x = pwidth - getWidth();
                y = pheight - getHeight();
                break;
            default:
                break;
        }
        setLocalPosition(x, y);
    }

    @Override
    protected void renderFirst(Graphics2D g) {
        super.renderFirst(g);
        updateAlignment();
    }

    @Override
    protected void onChildNodeChange(AbstractNode instigator) {
        super.onChildNodeChange(instigator);
        setGraphicsCalculationsDirty();
    }

    @Override
    protected void onParentNodeChange(AbstractNode instigator) {
        super.onParentNodeChange(instigator);
        setGraphicsCalculationsDirty();
    }

    /**
     * This method is disabled for {@link AbstractAlignableGuiNode} as it will prevent the node from calculating alignment.
     */
    @Override
    public AbstractNode setEnabled(boolean isEnabled) {
        return super.setEnabled(true); // If we can disable this GUI node alignment will not work.
    }

    /**
     * Returns the alignment currently set.
     * 
     * @return the alignment currently set.
     */
    public Alignment getAlignment() {
        return alignment;
    }

    /**
     * Sets the alignment of this node.
     * 
     * @param alignment
     *            the new alignment to set.
     * @return this object for chaining.
     */
    public AbstractAlignableGuiNode setAlignment(Alignment alignment) {
        this.alignment = alignment;
        setGraphicsCalculationsDirty();
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s [alignment=%s, isVisible=%s, getLocalPositionX()=%s, getLocalPositionY()=%s, getWidth()=%s, getHeight()=%s, isEnabled()=%s]", getClass().getSimpleName(), alignment, isVisible, getLocalPositionX(), getLocalPositionY(), getWidth(), getHeight(), isEnabled());
    }

}
