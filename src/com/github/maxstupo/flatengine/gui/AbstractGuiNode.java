package com.github.maxstupo.flatengine.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public abstract class AbstractGuiNode {

    protected final AbstractScreen screen;

    private AbstractGuiNode parent;
    private final List<AbstractGuiNode> children = new ArrayList<>();

    private final Vector2i globalPosition = new Vector2i();
    protected final Vector2i localPosition = new Vector2i();
    protected final Vector2i size = new Vector2i();

    protected boolean isEnabled = true;
    protected boolean isDebug = false;

    private final Rectangle bounds = new Rectangle();
    private final Rectangle totalBounds = new Rectangle();

    private boolean isDirty = true;

    public AbstractGuiNode(AbstractScreen screen, Vector2i localPosition, Vector2i size) {
        this.screen = screen;
        if (localPosition != null)
            this.setLocalPosition(localPosition.x, localPosition.y);
        if (size != null)
            this.size.set(size);
    }

    public abstract boolean update(double delta, boolean shouldHandleInput);

    public abstract void render(Graphics2D g);

    public void renderPost(Graphics2D g) {
    }

    public void renderAll(Graphics2D g) {
        render(g);
        for (AbstractGuiNode node : children)
            node.renderAll(g);
        renderPost(g);
    }

    public boolean updateAll(double delta, boolean shouldHandleInput) {
        for (int i = children.size() - 1; i >= 0; i--) {
            AbstractGuiNode node = children.get(i);

            shouldHandleInput = node.updateAll(delta, shouldHandleInput);
        }

        shouldHandleInput = update(delta, shouldHandleInput);

        return shouldHandleInput;
    }

    protected Mouse getMouse() {
        return screen.getScreenManager().getEngine().getMouse();
    }

    protected Keyboard getKeyboard() {
        return screen.getScreenManager().getEngine().getKeyboard();
    }

    public AbstractGuiNode addChild(AbstractGuiNode node) {
        children.add(node);

        node.setParent(this);
        node.onAdded();
        return this;
    }

    public AbstractGuiNode removeChild(AbstractGuiNode node) {
        children.remove(node);
        node.setParent(null);
        node.onRemoved();
        return this;
    }

    public Vector2i getGlobalPosition() {
        if (isDirty) {
            int x = (parent != null) ? parent.getGlobalPosition().x : 0;
            int y = (parent != null) ? parent.getGlobalPosition().y : 0;

            globalPosition.set(x + localPosition.x, y + localPosition.y);
            isDirty = false;
        }
        return globalPosition;
    }

    public void setLocalPosition(int x, int y) {
        if (x != localPosition.x || y != localPosition.y)
            setDirty();
        localPosition.set(x, y);
    }

    public void setDirty() {
        this.isDirty = true;
        for (AbstractGuiNode node : children)
            node.setDirty();
    }

    public synchronized void bringToFront(AbstractGuiNode node) {
        children.remove(node);
        children.add(node);
    }

    protected void onAdded() {
    }

    protected void onRemoved() {
    }

    public boolean isMouseOver() {
        return isMouseOver(getBounds());
    }

    public boolean isMouseOver(Rectangle bounds) {
        return getMouse().getBounds().intersects(bounds);
    }

    public boolean isMouseClicked(int buttonCode) {
        return isMouseClicked(getBounds(), buttonCode);
    }

    public boolean isMouseClicked(Rectangle bounds, int buttonCode) {
        return isMouseOver(bounds) && getMouse().isMouseDown(buttonCode);
    }

    public boolean isMouseHeld(int buttonCode) {
        return isMouseHeld(getBounds(), buttonCode);
    }

    public boolean isMouseHeld(Rectangle bounds, int buttonCode) {
        return isMouseOver(bounds) && getMouse().isMouseHeld(buttonCode);
    }

    public Rectangle getBounds(Vector2i gpos) {
        bounds.setBounds(gpos.x, gpos.y, size.x, size.y);
        return bounds;
    }

    public Rectangle getBounds() {
        return getBounds(getGlobalPosition());
    }

    /**
     * Returns a rectangle encompassing all children gui objects.
     */
    public Rectangle getBoundsTotal() {
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        int width = -Integer.MAX_VALUE;
        int height = -Integer.MAX_VALUE;

        for (AbstractGuiNode child : children) {
            Vector2i gpos = child.getGlobalPosition();
            Vector2i size = child.getSize();

            x = Math.min(gpos.x, x);
            y = Math.min(gpos.y, y);
            width = Math.max((gpos.x + size.x) - x, width);
            height = Math.max((gpos.y + size.y) - y, height);
        }

        Vector2i gpos = getGlobalPosition();
        Vector2i size = getSize();

        x = Math.min(gpos.x, x);
        y = Math.min(gpos.y, y);
        width = Math.max((gpos.x + size.x) - x, width);
        height = Math.max((gpos.y + size.y) - y, height);

        totalBounds.setBounds(x, y, width, height);
        return totalBounds;
    }

    public AbstractScreen getScreen() {
        return screen;
    }

    protected void setParent(AbstractGuiNode parent) {
        this.parent = parent;
    }

    public AbstractGuiNode getParent() {
        return parent;
    }

    public List<AbstractGuiNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void setLocalPosition(Vector2i pos) {
        setLocalPosition(pos.x, pos.y);
    }

    public int getLocalPositionX() {
        return localPosition.x;
    }

    public int getLocalPositionY() {
        return localPosition.y;
    }

    public Vector2i getSize() {
        return size;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean isDirty() {
        return isDirty;
    }

    /***
     * This method should unregister all events and/or listeners.
     * <p>
     * The method is called when the gui is no longer needed. E.g. when the screen changes.
     */
    protected abstract void onDispose();

    /**
     * Disposes this node and all children nodes.
     */
    public void dispose() {
        this.onDispose();
        for (AbstractGuiNode node : children)
            node.dispose();
    }
}
