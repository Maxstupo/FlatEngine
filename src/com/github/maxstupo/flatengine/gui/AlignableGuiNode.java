package com.github.maxstupo.flatengine.gui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class AlignableGuiNode extends AbstractGuiNode {

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

    public AlignableGuiNode(AbstractScreen screen, Vector2i localPosition, Vector2i size) {
        super(screen, localPosition, size);
    }

    protected void updateAlignment() {
        if (alignment == Alignment.OFF) {
            isAlignmentDirty = false;
            return;
        }

        int x = getLocalPositionX();
        int y = getLocalPositionY();
        Vector2i parentSize = (getParent() != null) ? getParent().getSize() : Vector2i.ZERO;
        Vector2i size = getSize();

        switch (alignment) {
            case TOP_LEFT:
                break;
            case TOP_MIDDLE:
                x = parentSize.x / 2 - size.x / 2;
                break;
            case TOP_RIGHT:
                x = parentSize.x - size.x;
                break;
            case MIDDLE_LEFT:
                y = parentSize.y / 2 - size.y / 2;
                break;
            case CENTER:
                x = parentSize.x / 2 - size.x / 2;
                y = parentSize.y / 2 - size.y / 2;
                break;
            case MIDDLE_RIGHT:
                x = parentSize.x - size.x;
                y = parentSize.y / 2 - size.y / 2;
                break;
            case BOTTOM_LEFT:
                y = parentSize.y - size.y;
                break;
            case BOTTOM_MIDDLE:
                x = parentSize.x / 2 - size.x / 2;
                y = parentSize.y - size.y;
                break;
            case BOTTOM_RIGHT:
                x = parentSize.x - size.x;
                y = parentSize.y - size.y;
                break;
            default:
                break;
        }

        setLocalPosition(x, y);
        isAlignmentDirty = false;
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {
        if (isAlignmentDirty)
            updateAlignment();
        return shouldHandleInput;
    }

    @Override
    public AbstractGuiNode setEnabled(boolean isEnabled) {
        return this;
    }

    @Override
    public void render(Graphics2D g) {

    }

    @Override
    protected void onDispose() {

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
        return String.format("AlignableGuiNode [isAlignmentDirty=%s, alignment=%s, screen=%s, localPosition=%s, size=%s, isEnabled=%s, isDebug=%s]", isAlignmentDirty, alignment, screen, localPosition, size, isEnabled(), isDebug());
    }

    @Override
    public void onResize(int width, int height) {

    }

}
