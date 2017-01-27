package com.github.maxstupo.flatengine.hgui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.screen.AbstractScreen;

/**
 * @author Maxstupo
 *
 */
public class AlignableGuiNode extends AbstractNode {

    public static enum Alignment {
        TOP_LEFT,
        TOP_MIDDLE,
        TOP_RIGHT,

        MIDDLE_LEFT,
        CENTER,
        MIDDLE_RIGHT,

        BOTTOM_LEFT,
        BOTTOM_MIDDLE,
        BOTTOM_RIGHT,
        OFF
    }

    private boolean isAlignmentDirty = true;
    private Alignment alignment = Alignment.OFF;

    public AlignableGuiNode(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
        setUsePercentagePositions(false);
    }

    protected void updateAlignment() {
        if (getAlignment() == Alignment.OFF) {
            isAlignmentDirty = false;
            return;
        }

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
        isAlignmentDirty = false;
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {
        if (isAlignmentDirty)
            updateAlignment();
        return shouldHandleInput;
    }

    @Override
    protected void onChildNodeChange(AbstractNode instigator) {
        setAlignmentDirty();
    }

    @Override
    protected void render(Graphics2D g) {

    }

    @Override
    public AbstractNode setEnabled(boolean isEnabled) {
        return super.setEnabled(true); // If we can disable this GUI node alignment will not work.
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public AlignableGuiNode setAlignmentDirty() {
        isAlignmentDirty = true;
        return this;
    }

    public AlignableGuiNode setAlignment(Alignment alignment) {
        this.alignment = alignment;
        setAlignmentDirty();
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s [isAlignmentDirty=%s, alignment=%s, isVisible=%s, getLocalPositionX()=%s, getLocalPositionY()=%s, getWidth()=%s, getHeight()=%s, isEnabled()=%s]", getClass().getSimpleName(), isAlignmentDirty, alignment, isVisible, getLocalPositionX(), getLocalPositionY(), getWidth(), getHeight(), isEnabled());
    }

}
