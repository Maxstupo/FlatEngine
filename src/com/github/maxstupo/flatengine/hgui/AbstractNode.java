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
 * This class represents the base of a GUI node, all GUI components are derived from this class.
 * 
 * @author Maxstupo
 */
public abstract class AbstractNode {

    /** The screen that owns this GUI node. */
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

    /** True if this GUI node will be rendered along with all children of this node. */
    protected boolean isVisible = true;

    private final Rectangle bounds = new Rectangle();

    private boolean usePercentagePositions = false;
    private boolean keepWithinParent = false;

    private boolean isGraphicsCalculationsDirty;

    /**
     * Create a {@link AbstractNode} object.
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
    public AbstractNode(AbstractScreen screen, float localX, float localY, int width, int height) {
        this.screen = screen;
        this.setLocalPosition(localX, localY);

        this.setSize(width, height);

        setPositionDirty();
    }

    /**
     * Called once before {@link #render(Graphics2D)} and after all child nodes have had this method called as well. <br>
     * This method can be requested to be called next render using {@link #setGraphicsCalculationsDirty()}.
     * <p>
     * This method shouldn't draw anything, it should just use the graphics context to calculate variables (e.g. the dimensions of a string).
     * 
     * @param g
     *            the graphics context.
     */
    protected void renderFirst(Graphics2D g) {

    }

    /**
     * Update the node and logic.
     * 
     * @param delta
     *            the delta time.
     * @param shouldHandleInput
     *            true if this node should handle input (e.g. mouse clicks).
     * @return true if the next node should handle input.
     */
    protected abstract boolean update(float delta, boolean shouldHandleInput);

    /**
     * Render the node.
     * 
     * @param g
     *            the graphics context to draw to.
     */
    protected abstract void render(Graphics2D g);

    /**
     * Called whenever this node changes size.
     * 
     * @param width
     *            the new width of this node.
     * @param height
     *            the new height of this node.
     */
    protected void onResize(int width, int height) {
    }

    /***
     * This method should unregister all events and/or listeners.
     * <p>
     * The method is called when the GUI object is no longer needed (e.g. when the screen changes).
     */
    protected void onDispose() {
    }

    /**
     * This method is called when a child node is added to this node.
     * 
     * @param node
     *            the node that was added.
     */
    protected void onAdded(AbstractNode node) {
    }

    /**
     * This method is called when a child node is removed from this node.
     * 
     * @param node
     *            the node that was removed.
     */
    protected void onRemoved(AbstractNode node) {
    }

    /**
     * This method is called when a child node changes local position, size, or adds / removes a node.
     * 
     * @param instigator
     *            the node that caused this event.
     */
    protected void onChildNodeChange(AbstractNode instigator) {
    }

    /**
     * This method is called when A parent node changes local position, size, or adds / removes a node.
     * <p>
     * Note: This method does not just get triggered from changes to the parent of this node, but can be triggered by other parent nodes within this
     * tree branch.
     * 
     * @param instigator
     *            the node that caused this event.
     */
    protected void onParentNodeChange(AbstractNode instigator) {
    }

    /**
     * This method is called after both this and all child nodes are rendered.
     * 
     * @param g
     *            the graphics context to draw to.
     */
    protected void renderPost(Graphics2D g) {
    }

    /**
     * Renders both this node and all children nodes.
     * 
     * @param g
     *            the graphics context to draw to.
     */
    public void renderAll(Graphics2D g) {
        if (!isVisible())
            return;

        if (isGraphicsCalculationsDirty) {
            doRenderFirst(g);
            isGraphicsCalculationsDirty = false;
        }
        render(g);

        for (AbstractNode node : children)
            node.renderAll(g);

        renderPost(g);
    }

    private void doRenderFirst(Graphics2D g) {
        for (AbstractNode node : children)
            node.doRenderFirst(g);
        renderFirst(g);
    }

    /**
     * Updates both this node and all children nodes. In order of first added first updated.
     * 
     * @param delta
     *            the delta time.
     * @param shouldHandleInput
     *            should the first node handle input (e.g. mouse button). This will be true most of the time when calling this method from a non GUI
     *            node (e.g. {@link AbstractScreen}).
     * @return true if the next node should handle input. This return value can be ignored if calling this method from a non GUI node (e.g.
     *         {@link AbstractScreen}).
     */
    public boolean updateAll(float delta, boolean shouldHandleInput) {
        for (int i = children.size() - 1; i >= 0; i--) {
            AbstractNode node = children.get(i);

            shouldHandleInput = node.updateAll(delta, shouldHandleInput);
        }

        if (isEnabled())
            shouldHandleInput = update(delta, shouldHandleInput);

        return shouldHandleInput;
    }

    /**
     * Calls the {@link #onDispose()} method for both this node and all children nodes.
     */
    public void dispose() {
        onDispose();
        for (AbstractNode node : children)
            node.dispose();
    }

    private void notifyOfChange(AbstractNode instigator) {
        notifyParentOfChange(instigator);
        notifyChildrenOfChange(instigator);
    }

    private void notifyParentOfChange(AbstractNode instigator) {
        if (!this.equals(instigator))
            onChildNodeChange(instigator);

        if (getParent() != null)
            getParent().notifyParentOfChange(instigator);
    }

    private void notifyChildrenOfChange(AbstractNode instigator) {
        if (!this.equals(instigator)) {
            onParentNodeChange(instigator);

            if (isKeepWithinParent()) {
                if (usePercentagePositions()) {
                    localPositionX = UtilMath.clampF(localPositionX, 0, 1f - (getWidth() / getParentWidth()));
                    localPositionY = UtilMath.clampF(localPositionY, 0, 1f - (getHeight() / getParentHeight()));
                } else {
                    localPositionX = UtilMath.clampF(localPositionX, 0, getParentWidth() - getWidth());
                    localPositionY = UtilMath.clampF(localPositionY, 0, getParentHeight() - getHeight());
                    setPositionDirty();
                }

            }
            if (usePercentagePositions()) {
                setPositionDirty();
            }

        }

        for (AbstractNode node : children)
            node.notifyChildrenOfChange(instigator);
    }

    public void bringToFront(AbstractNode node) {
        children.remove(node);
        children.add(node);
    }

    /**
     * Adds a child node to this node, this method calls {@link #onAdded(AbstractNode)}, notifies the parent node of a change via
     * {@link #onChildNodeChange(AbstractNode)} and notifies all children nodes via {@link #onParentNodeChange(AbstractNode)}.
     * 
     * @param node
     *            the node to add.
     * @return this object for chaining.
     */
    public AbstractNode add(AbstractNode node) {
        children.add(node);

        node.setParent(this);
        node.onAdded(node);

        notifyOfChange(this);
        return this;
    }

    /**
     * Removes a child node from this node, this method calls {@link #onRemoved(AbstractNode)}, notifies the parent node of a change via
     * {@link #onChildNodeChange(AbstractNode)} and notifies all children nodes via {@link #onParentNodeChange(AbstractNode)}.
     * 
     * @param node
     *            the node to remove.
     * @return this object for chaining.
     */
    public AbstractNode remove(AbstractNode node) {
        if (children.remove(node)) {
            node.setParent(null);
            node.onRemoved(node);

            notifyOfChange(this);
        }
        return this;
    }

    /**
     * Returns the global position of this node based on the parent position and the local position of this node.
     * <p>
     * If {@link #usePercentagePositions()} is true the local position represents a percentage of the parent dimensions, if this node has no parent
     * the game screen dimensions are used.
     * <p>
     * This method will calculate the position when this node is dirty (set by {@link #setPositionDirty()}), else it will return the previous
     * calculated value.
     * 
     * @return the global position of this node. The vector object is cached and reused.
     */
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

    /**
     * Sets the local x position of this node.
     * 
     * @see #setLocalPosition(float, float)
     * @param x
     *            the local x position.
     * @return this object for chaining.
     */
    public AbstractNode setLocalPositionX(float x) {
        return setLocalPosition(x, getLocalPositionY());
    }

    /**
     * Sets the local y position of this node.
     * 
     * @see #setLocalPosition(float, float)
     * @param y
     *            the local y position.
     * @return this object for chaining.
     */
    public AbstractNode setLocalPositionY(float y) {
        return setLocalPosition(getLocalPositionX(), y);
    }

    /**
     * Sets the width of this node.
     * 
     * @see #setSize(int, int)
     * @param width
     *            the width of this node.
     * @return this object for chaining.
     */
    public AbstractNode setWidth(int width) {
        return setSize(width, getHeight());
    }

    /**
     * Sets the height of this node.
     * 
     * @see #setSize(int, int)
     * @param height
     *            the height of this node.
     * @return this object for chaining.
     */
    public AbstractNode setHeight(int height) {
        return setSize(getWidth(), height);
    }

    /**
     * Sets the local position of this node, if the given x and y values are the same as the ones already set this method does nothing.
     * <p>
     * This method will notify the parent node of a change via {@link #onChildNodeChange(AbstractNode)} and will notify all children nodes via
     * {@link #onParentNodeChange(AbstractNode)}.
     * <p>
     * If {@link #isKeepWithinParent()} is true the given x and y values will be clamped to keep this node within the parent node.
     * 
     * @param x
     *            the local x position.
     * @param y
     *            the local y position.
     * @return this object for chaining.
     */
    public AbstractNode setLocalPosition(float x, float y) {
        if (!UtilMath.equals(x, localPositionX) || !UtilMath.equals(y, localPositionY)) {

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
            notifyOfChange(this);
        }
        return this;
    }

    /**
     * Sets the size of this node, if the given width and height values are the same as the ones already set this method does nothing.
     * <p>
     * This method will notify {@link #onResize(int, int)} and will notify the parent node of a change via {@link #onChildNodeChange(AbstractNode)}
     * and will notify all children nodes via {@link #onParentNodeChange(AbstractNode)}.
     * 
     * @param width
     *            the new width of this node.
     * @param height
     *            the new height of this node.
     * @return this object for chaining.
     */
    public AbstractNode setSize(int width, int height) {
        if (width != this.width || height != this.height) {
            this.width = width;
            this.height = height;

            onResize(width, height);
            notifyOfChange(this);
        }
        return this;
    }

    /**
     * Requests that {@link #renderFirst(Graphics2D)} be called to recalculate variables dependent on the graphics context.
     * 
     * @return this object for chaining.
     */
    public AbstractNode setGraphicsCalculationsDirty() {
        isGraphicsCalculationsDirty = true;
        return this;
    }

    /**
     * Notifies both this node and all children nodes to update the global position next time {@link #getGlobalPosition()} is called.
     * 
     * @return this object for chaining.
     */
    public AbstractNode setPositionDirty() {
        this.isPositionDirty = true;
        for (AbstractNode node : children)
            node.setPositionDirty();
        return this;
    }

    /**
     * Returns true if the mouse is over this node.
     * 
     * @return true if the mouse is over this node.
     */
    public boolean isMouseOver() {
        return isMouseOver(getBounds());
    }

    /**
     * Returns true if the mouse is over the given rectangle bounds.
     * 
     * @param bounds
     *            the rectangle to check if the mouse is within it.
     * @return true if the mouse is over the given bounds.
     */
    public boolean isMouseOver(Rectangle bounds) {
        return bounds.contains(getMouse().getPositionPoint());
    }

    /**
     * Returns true if the mouse clicked the given button over this node.
     * 
     * @param buttonCode
     *            the mouse button code to check is down.
     * @return true if the mouse clicked the given button over this node.
     */
    public boolean isMouseClicked(int buttonCode) {
        return isMouseClicked(getBounds(), buttonCode);
    }

    /**
     * Returns true if the mouse was released when over this node.
     * 
     * @param buttonCode
     *            the mouse button code to check is up.
     * @return true if the mouse was released when over this node.
     */
    public boolean isMouseUp(int buttonCode) {
        return isMouseUp(getBounds(), buttonCode);
    }

    /**
     * Returns true if the mouse was released when over the given rectangle bounds.
     * 
     * @param bounds
     *            the rectangle to check if the mouse is within it.
     * @param buttonCode
     *            the mouse button code to check is up.
     * @return true if the mouse was released when over the given rectangle bounds.
     */
    public boolean isMouseUp(Rectangle bounds, int buttonCode) {
        return isMouseOver(bounds) && getMouse().isMouseUp(buttonCode);
    }

    /**
     * Returns true if the mouse clicked the given button over the given rectangle bounds.
     * 
     * @param bounds
     *            the rectangle to check if the mouse is within it.
     * @param buttonCode
     *            the mouse button code to check is down.
     * @return true if the mouse clicked the given button over the given rectangle bounds.
     */
    public boolean isMouseClicked(Rectangle bounds, int buttonCode) {
        return isMouseOver(bounds) && getMouse().isMouseDown(buttonCode);
    }

    /**
     * Returns the rectangle bounds of this node. The rectangle object is cached and is recalculated upon calling this method.
     * 
     * @return the rectangle bounds of this node.
     */
    public Rectangle getBounds() {
        Vector2i gpos = getGlobalPosition();
        bounds.setBounds(gpos.x, gpos.y, getWidth(), getHeight());
        return bounds;
    }

    /**
     * Returns true if {@link #isVisible()} is true and {@link #setEnabled(boolean)} has been set to true.
     * <p>
     * If this node has a parent node it must be enabled for this method to return true.
     * 
     * @return true if {@link #isVisible()} is true and {@link #setEnabled(boolean)} has been set to true.
     */
    public boolean isEnabled() {
        if (getParent() != null)
            return isEnabled && isVisible && getParent().isEnabled();
        return isEnabled && isVisible;
    }

    /**
     * If set true the local position will be a percentage of the parent dimensions. Calling this method will preserve the previous local position by
     * converting it to either a percentage or a pixel position.
     * 
     * @param usePercentagePositions
     *            true to use percentage positions.
     * @return this object for chaining.
     */
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

    /**
     * Removes all children nodes from this node.
     * 
     * @return this object for chaining.
     */
    public AbstractNode removeChildren() {
        children.clear();
        return this;
    }

    /**
     * Returns the width of the parent node or the width of the screen if the parent node is null.
     * 
     * @return the width of the parent node or the width of the screen if the parent node is null.
     */
    public float getParentWidth() {
        return (getParent() != null) ? getParent().getWidth() : screen.getWidth();
    }

    /**
     * Returns the height of the parent node or the height of the screen if the parent node is null.
     * 
     * @return the height of the parent node or the height of the screen if the parent node is null.
     */
    public float getParentHeight() {
        return (getParent() != null) ? getParent().getHeight() : screen.getHeight();
    }

    /**
     * Sets this node as enabled.
     * 
     * @see #isEnabled()
     * 
     * @param isEnabled
     *            true to enable.
     * @return this object for chaining.
     */
    public AbstractNode setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    /**
     * Sets the parent of this node.
     * 
     * @param parent
     *            the parent node of this node.
     * @return this object for chaining.
     */
    protected AbstractNode setParent(AbstractNode parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Returns the local x position of this node.
     * 
     * @return the local x position of this node.
     */
    public float getLocalPositionX() {
        return localPositionX;
    }

    /**
     * Returns the local y position of this node.
     * 
     * @return the local y position of this node.
     */
    public float getLocalPositionY() {
        return localPositionY;
    }

    /**
     * Returns true if this node will be kept within the parent node.
     * 
     * @return true if this node will be kept within the parent node.
     */
    public boolean isKeepWithinParent() {
        return keepWithinParent;
    }

    /**
     * Sets if this node will be kept within the parent node, if this node has no parent the game screen dimensions will be used instead.
     * 
     * @param keepWithinParent
     *            true to keep node within the parent node.
     * @return this object for chaining.
     */
    public AbstractNode setKeepWithinParent(boolean keepWithinParent) {
        this.keepWithinParent = keepWithinParent;
        return this;
    }

    /**
     * Returns the width of this node.
     * 
     * @return the width of this node.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of this node.
     * 
     * @return the height of this node.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the parent node of this node.
     * 
     * @return the parent node of this node.
     */
    public AbstractNode getParent() {
        return parent;
    }

    /**
     * Returns the screen that owns this node.
     * 
     * @return the screen that owns this node.
     */
    public AbstractScreen getScreen() {
        return screen;
    }

    /**
     * Returns true if this node is visible.
     * 
     * @return true of this node is visible.
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Sets if this node is visible, if true this node will be rendered along with all child nodes.
     * 
     * @param isVisible
     *            true if visible.
     * @return this object for chaining.
     */

    public AbstractNode setVisible(boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    /**
     * Returns an unmodifiable list of child nodes this node contains.
     * 
     * @see Collections#unmodifiableList(List)
     * @return an unmodifiable list of child nodes this node contains.
     */
    public List<AbstractNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Returns the mouse handler from {@link #getScreen()}.
     * 
     * @return the mouse handler.
     */
    protected Mouse getMouse() {
        return screen.getScreenManager().getEngine().getMouse();
    }

    /**
     * Returns the keyboard handler from {@link #getScreen()}.
     * 
     * @return the keyboard handler.
     */
    protected Keyboard getKeyboard() {
        return screen.getScreenManager().getEngine().getKeyboard();
    }

    /**
     * Returns true if the local position represent a percentage of the parent dimensions.
     * 
     * @return true if the local position represent a percentage of the parent dimensions.
     */
    public boolean usePercentagePositions() {
        return usePercentagePositions;
    }

    @Override
    public String toString() {
        return String.format("%s [screen=%s, parent=%s, children=%s, globalPosition=%s, localPositionX=%s, localPositionY=%s, width=%s, height=%s, isPositionDirty=%s, isEnabled=%s, isVisible=%s]", getClass().getSimpleName(), screen, parent, children, globalPosition, localPositionX, localPositionY, width, height, isPositionDirty, isEnabled, isVisible);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + height;
        result = prime * result + (isEnabled ? 1231 : 1237);
        result = prime * result + (isPositionDirty ? 1231 : 1237);
        result = prime * result + (isVisible ? 1231 : 1237);
        result = prime * result + (keepWithinParent ? 1231 : 1237);
        result = prime * result + Float.floatToIntBits(localPositionX);
        result = prime * result + Float.floatToIntBits(localPositionY);
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + (usePercentagePositions ? 1231 : 1237);
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractNode other = (AbstractNode) obj;
        if (children == null) {
            if (other.children != null)
                return false;
        } else if (!children.equals(other.children))
            return false;
        if (height != other.height)
            return false;
        if (isEnabled != other.isEnabled)
            return false;
        if (isPositionDirty != other.isPositionDirty)
            return false;
        if (isVisible != other.isVisible)
            return false;
        if (keepWithinParent != other.keepWithinParent)
            return false;
        if (Float.floatToIntBits(localPositionX) != Float.floatToIntBits(other.localPositionX))
            return false;
        if (Float.floatToIntBits(localPositionY) != Float.floatToIntBits(other.localPositionY))
            return false;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (usePercentagePositions != other.usePercentagePositions)
            return false;
        if (width != other.width)
            return false;
        return true;
    }

}
