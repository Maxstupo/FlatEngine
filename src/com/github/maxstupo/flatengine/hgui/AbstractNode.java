package com.github.maxstupo.flatengine.hgui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public abstract class AbstractNode {

    protected final AbstractScreen screen;

    private AbstractNode parent;
    private final List<AbstractNode> children = new ArrayList<>();

    private final Vector2i globalPosition = new Vector2i();
    private float localPositionX = 0;
    private float localPositionY = 0;
    private int width = 0;
    private int height = 0;

    private boolean isPositionDirty = false;
    private boolean isEnabled = true;
    protected boolean isVisible = true;

    private final Rectangle bounds = new Rectangle();

    private boolean usePercentagePositions = false;
    private boolean keepWithinParent = false;

    public AbstractNode(AbstractScreen screen, float localX, float localY, int width, int height) {
        this.screen = screen;
        this.setLocalPosition(localX, localY);
        this.setSize(width, height);
    }

    protected abstract boolean update(float delta, boolean shouldHandleInput);

    protected abstract void render(Graphics2D g);

    protected void onResize(int width, int height) {
    }

    /***
     * This method should unregister all events and/or listeners.
     * <p>
     * The method is called when the GUI object is no longer needed (e.g. when the screen changes).
     */
    protected void onDispose() {
    }

    protected void onAdded() {
    }

    protected void onRemoved() {
    }

    protected void onChildNodeChange(AbstractNode instigator) {
    }

    protected void renderPost(Graphics2D g) {
    }

    public void renderAll(Graphics2D g) {
        if (!isVisible())
            return;

        render(g);
        for (AbstractNode node : children)
            node.renderAll(g);

        renderPost(g);
    }

    public boolean updateAll(float delta, boolean shouldHandleInput) {
        for (int i = children.size() - 1; i >= 0; i--) {
            AbstractNode node = children.get(i);

            shouldHandleInput = node.updateAll(delta, shouldHandleInput);
        }

        if (isEnabled())
            shouldHandleInput = update(delta, shouldHandleInput);

        return shouldHandleInput;
    }

    public void resize(int width, int height) {
        onResize(width, height);

        if (usePercentagePositions())
            setPositionDirty();

        if (isKeepWithinParent()) {
            if (usePercentagePositions()) {
                localPositionX = UtilMath.clampF(localPositionX, 0, 1f - (getWidth() / getParentWidth()));
                localPositionY = UtilMath.clampF(localPositionY, 0, 1f - (getHeight() / getParentHeight()));
            } else {
                localPositionX = UtilMath.clampF(localPositionX, 0, getParentWidth() - getWidth());
                localPositionY = UtilMath.clampF(localPositionY, 0, getParentHeight() - getHeight());
            }
            setPositionDirty();
        }

        for (AbstractNode node : children)
            node.resize(width, height);
    }

    public void dispose() {
        onDispose();
        for (AbstractNode node : children)
            node.dispose();
    }

    private void nodeChange(AbstractNode instigator) {
        onChildNodeChange(instigator);

        if (getParent() == null)
            return;

        getParent().nodeChange(instigator);
    }

    public AbstractNode add(AbstractNode node) {
        children.add(node);

        node.setParent(this);
        node.onAdded();

        nodeChange(this);
        return this;
    }

    public AbstractNode remove(AbstractNode node) {
        if (children.remove(node)) {
            node.setParent(null);
            node.onRemoved();

            nodeChange(this);
        }
        return this;
    }

    public float getParentWidth() {
        return (getParent() != null) ? getParent().getWidth() : screen.getWidth();
    }

    public float getParentHeight() {
        return (getParent() != null) ? getParent().getHeight() : screen.getHeight();
    }

    public Vector2i getGlobalPosition() {
        if (isPositionDirty) {
            float x = (getParent() != null) ? getParent().getGlobalPosition().x : 0;
            float y = (getParent() != null) ? getParent().getGlobalPosition().y : 0;

            if (usePercentagePositions()) {
                float locX = getLocalPositionX() * getParentWidth();
                float locY = getLocalPositionY() * getParentHeight();

                globalPosition.set((int) (x + locX), (int) (y + locY));

            } else {
                globalPosition.set((int) (x + getLocalPositionX()), (int) (y + getLocalPositionY()));
            }

            isPositionDirty = false;
        }
        return globalPosition;
    }

    public AbstractNode setLocalPositionX(float x) {
        return setLocalPosition(x, getLocalPositionY());
    }

    public AbstractNode setLocalPositionY(float y) {
        return setLocalPosition(getLocalPositionX(), y);
    }

    public AbstractNode setLocalPosition(float x, float y) {
        if (x != localPositionX || y != localPositionY) {

            localPositionX = usePercentagePositions() ? (x / getParentWidth()) : x;
            localPositionY = usePercentagePositions() ? (y / getParentHeight()) : y;

            if (isKeepWithinParent()) {
                if (usePercentagePositions()) {
                    localPositionX = UtilMath.clampF(localPositionX, 0, 1f - (getWidth() / getParentWidth()));
                    localPositionY = UtilMath.clampF(localPositionY, 0, 1f - (getHeight() / getParentHeight()));
                } else {
                    localPositionX = UtilMath.clampF(localPositionX, 0, getParentWidth() - getWidth());
                    localPositionY = UtilMath.clampF(localPositionY, 0, getParentHeight() - getHeight());
                }
            }

            setPositionDirty();
            nodeChange(this);
        }
        return this;
    }

    public AbstractNode setWidth(int width) {
        return setSize(width, getHeight());
    }

    public AbstractNode setHeight(int height) {
        return setSize(getWidth(), height);
    }

    public AbstractNode setSize(int width, int height) {
        if (width != this.width || height != this.height) {
            this.width = width;
            this.height = height;

            nodeChange(this);
        }
        return this;
    }

    public AbstractNode setPositionDirty() {
        this.isPositionDirty = true;
        for (AbstractNode node : children)
            node.setPositionDirty();
        return this;
    }

    protected AbstractNode setParent(AbstractNode parent) {
        this.parent = parent;
        return this;
    }

    public boolean isMouseOver() {
        return isMouseOver(getBounds());
    }

    public boolean isMouseOver(Rectangle bounds) {
        return bounds.contains(getMouse().getPositionPoint());
    }

    public boolean isMouseClicked(int buttonCode) {
        return isMouseClicked(getBounds(), buttonCode);
    }

    public boolean isMouseClicked(Rectangle bounds, int buttonCode) {
        return isMouseOver(bounds) && getMouse().isMouseDown(buttonCode);
    }

    public float getLocalPositionX() {
        return localPositionX;
    }

    public float getLocalPositionY() {
        return localPositionY;
    }

    public Rectangle getBounds() {
        Vector2i gpos = getGlobalPosition();
        bounds.setBounds(gpos.x, gpos.y, getWidth(), getHeight());
        return bounds;
    }

    public boolean isKeepWithinParent() {
        return keepWithinParent;
    }

    public AbstractNode setKeepWithinParent(boolean keepWithinParent) {
        this.keepWithinParent = keepWithinParent;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public AbstractNode getParent() {
        return parent;
    }

    public AbstractScreen getScreen() {
        return screen;
    }

    public boolean isEnabled() {
        if (getParent() != null)
            return isEnabled && isVisible && getParent().isEnabled();
        return isEnabled && isVisible;
    }

    public AbstractNode setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public AbstractNode setVisible(boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    public List<AbstractNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    protected Mouse getMouse() {
        return screen.getScreenManager().getEngine().getMouse();
    }

    protected Keyboard getKeyboard() {
        return screen.getScreenManager().getEngine().getKeyboard();
    }

    public boolean usePercentagePositions() {
        return usePercentagePositions;
    }

    public AbstractNode setUsePercentagePositions(boolean usePercentagePositions) {

        if (usePercentagePositions && !this.usePercentagePositions) { // Switching on
            setLocalPosition(getLocalPositionX() / getParentWidth(), getLocalPositionY() / getParentHeight());

            this.usePercentagePositions = usePercentagePositions;

        } else if (!usePercentagePositions && this.usePercentagePositions) { // Switching off
            this.usePercentagePositions = usePercentagePositions;

            setLocalPosition(getLocalPositionX() * getParentWidth(), getLocalPositionY() * getParentHeight());

        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s [screen=%s, parent=%s, children=%s, globalPosition=%s, localPositionX=%s, localPositionY=%s, width=%s, height=%s, isPositionDirty=%s, isEnabled=%s, isVisible=%s]", getClass().getSimpleName(), screen, parent, children, globalPosition, localPositionX, localPositionY, width, height, isPositionDirty, isEnabled, isVisible);
    }

}
