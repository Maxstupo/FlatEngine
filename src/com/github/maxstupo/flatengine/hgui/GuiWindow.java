package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.hgui.AbstractAlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This GUI node is a embedded draggable window that can contain other nodes.
 * 
 * @author Maxstupo
 */
public class GuiWindow extends GuiContainer implements IEventListener<GuiButton, Boolean, Integer> {

    private final GuiContainer titleNode;
    private final GuiText titleText;
    private final GuiButton btnClose;

    private final GuiContainer contents;

    private final Vector2f clickOrigin = new Vector2f();
    private boolean isDragging;
    private boolean addToContents;

    /**
     * Create a new {@link GuiWindow} object.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param title
     *            the title of the window.
     * @param localX
     *            the local x position.
     * @param localY
     *            the local y position.
     * @param width
     *            the width of this node.
     * @param height
     *            the height of this node.
     */
    public GuiWindow(AbstractScreen screen, String title, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
        setKeepWithinParent(true);
        setBackgroundColor(null);
        setOutlineColor(null);

        titleText = new GuiText(screen, Alignment.CENTER, title);

        contents = new GuiContainer(screen, 0, 0, width, height) {

            @Override
            public AbstractNode add(AbstractNode node) {
                node.setSize(UtilMath.clampI(node.getWidth(), 0, getWidth()), UtilMath.clampI(node.getHeight(), 0, getHeight()));
                node.setKeepWithinParent(true);
                return super.add(node);
            }
        };

        titleNode = new GuiContainer(screen, 0, 0, width, 0) {

            @Override
            public AbstractNode add(AbstractNode node) {
                node.setSize(UtilMath.clampI(node.getWidth(), 0, getWidth()), UtilMath.clampI(node.getHeight(), 0, getHeight()));
                node.setKeepWithinParent(true);
                return super.add(node);
            }
        };
        titleNode.add(titleText);

        btnClose = new GuiButton(screen, "X", 0, 0, 0, 0);
        btnClose.setTextColorSelected(Color.RED);
        btnClose.setBoxLess(true);
        btnClose.addListener(this);
        titleNode.add(btnClose);

        addToContents = false;
        add(titleNode);
        add(contents);
        addToContents = true;
    }

    /**
     * Redirect added nodes to the {@link #getContents()} node instead.
     */
    @Override
    public AbstractNode add(AbstractNode node) {
        if (addToContents) {
            contents.add(node);
        } else {
            super.add(node);
        }
        return this;
    }

    @Override
    public boolean update(float delta, boolean shouldHandleInput) {
        super.update(delta, shouldHandleInput);

        if (titleNode.getHeight() != titleText.getHeight() || contents.getLocalPositionY() != titleNode.getHeight()) {
            titleNode.setHeight(titleText.getHeight());
            // titleText.setGraphicsCalculationsDirty();// Update text alignment.

            contents.setLocalPositionY(titleNode.getHeight());
            setHeight(contents.getHeight() + titleNode.getHeight());

            btnClose.setSize(titleText.getHeight(), titleText.getHeight());
            btnClose.setLocalPositionX(titleNode.getWidth() - btnClose.getWidth());
            // btnClose.getText().setAlignmentDirty();// Update text alignment.
        }

        if (shouldHandleInput)
            doInputLogic();

        if (isDragging) {
            Vector2i mpos = getMouse().getPosition();

            float x = mpos.x - clickOrigin.x;
            float y = mpos.y - clickOrigin.y;

            setLocalPosition(x, y);
        }

        return !isMouseOver() && !titleNode.isMouseOver() && shouldHandleInput;
    }

    /**
     * Handles the input for dragging this window node.
     */
    protected void doInputLogic() {

        if (!isDragging && titleNode.isMouseClicked(Mouse.LEFT_CLICK)) {
            Vector2i mpos = getMouse().getPosition();
            Vector2i gpos = getGlobalPosition();

            clickOrigin.set(mpos.x - gpos.x, mpos.y - gpos.y);
            isDragging = true;
        } else if (getMouse().isMouseReleased(Mouse.LEFT_CLICK)) {
            isDragging = false;
        }

    }

    @Override
    public void onEvent(GuiButton executor, Boolean actionItem, Integer action) {
        setVisible(false);
    }

    /**
     * Returns the node containing the title and the close button.
     * 
     * @return the title node.
     */
    public GuiContainer getTitleNode() {
        return titleNode;
    }

    /**
     * Returns the title node.
     * 
     * @return the title node.
     */
    public GuiText getTitle() {
        return titleText;
    }

    /**
     * Returns true if this window is being dragged.
     * 
     * @return true if this window is being dragged.
     */
    public boolean isDragging() {
        return isDragging;
    }

    /**
     * Return the close button.
     * 
     * @return the close button.
     */
    public GuiButton getCloseButton() {
        return btnClose;
    }

    /**
     * Returns the window contents container.
     * 
     * @return the window contents container.
     */
    public GuiContainer getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return String.format("%s [titleNode=%s, titleText=%s, btnClose=%s, contents=%s, clickOrigin=%s, isDragging=%s, backgroundColor=%s, outlineColor=%s, isVisible=%s, getOutlineColor()=%s, getBackgroundColor()=%s, getParentWidth()=%s, getParentHeight()=%s, isMouseOver()=%s, getLocalPositionX()=%s, getLocalPositionY()=%s, isKeepWithinParent()=%s, getWidth()=%s, getHeight()=%s, isEnabled()=%s, usePercentagePositions()=%s]", getClass().getSimpleName(), titleNode, titleText, btnClose, contents, clickOrigin, isDragging, backgroundColor, outlineColor, isVisible, getOutlineColor(), getBackgroundColor(), getParentWidth(), getParentHeight(), isMouseOver(), getLocalPositionX(), getLocalPositionY(), isKeepWithinParent(), getWidth(), getHeight(), isEnabled(), usePercentagePositions());
    }

}
