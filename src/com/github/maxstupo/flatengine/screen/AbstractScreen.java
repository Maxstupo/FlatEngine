package com.github.maxstupo.flatengine.screen;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.hgui.GuiContainer;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;

/**
 * This class represents a game screen (e.g. main-menu, options menu, in-game, etc).
 * <p>
 * Each screen contains a root GUI node allowing for a GUIs to be created for each game screen.
 * <p>
 * Note: When overriding methods that do not start with the prefix 'on' (e.g. {@link #onActivated()}) make sure to call the super method, as failing
 * to do so may result in a GUI that doesn't work correctly.
 * 
 * @author Maxstupo
 */
public abstract class AbstractScreen {

    /** The screen manager this screen is registered with. */
    protected final ScreenManager screenManager;

    /** The root GUI object used for creating GUIs for this screen. */
    protected final GuiContainer guiRoot = new GuiContainer(this, 0, 0, 0, 0);

    /**
     * Creates a {@link AbstractScreen}.
     * 
     * @param screenManager
     *            the screen manager this screen is registered with.
     */
    protected AbstractScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.guiRoot.setOutlineColor(null);
        this.guiRoot.setSize(getWidth(), getHeight());
    }

    /**
     * All logic code for this screen goes within this method.
     * 
     * @param delta
     *            the delta time.
     */
    protected abstract void update(float delta);

    /**
     * All rendering code for this screen goes within this method.
     * 
     * @param g
     *            the graphics context to draw to.
     */
    protected abstract void render(Graphics2D g);

    /**
     * Called when the window is resized.
     * 
     * @param width
     *            the new width of the window.
     * @param height
     *            the new height of the window.
     */
    protected void onResize(int width, int height) {

    }

    /**
     * Called when this window has become the current window.
     */
    protected void onActivated() {
    }

    /**
     * Called when this window is no longer the current window.
     */
    protected void onDeactivated() {
    }

    /**
     * This method calls {@link #update(double)} and also updates the GUI. It's called by the {@link ScreenManager} that owns this screen.
     * <p>
     * Note: Don't override this unless you know what you are doing.
     * 
     * @param delta
     *            the delta time.
     */
    protected void doUpdate(float delta) {
        update(delta);
        guiRoot.updateAll(delta, true);
    }

    /**
     * This method calls {@link #render(Graphics2D)} and also renders the GUI. It's called by the {@link ScreenManager} that owns this screen.
     * <p>
     * Note: Don't override this method unless you know what you are doing.
     * 
     * @param g
     *            the graphics context to draw to.
     */
    protected void doRender(Graphics2D g) {
        guiRoot.renderAll(g);
        render(g);
    }

    /**
     * This method calls {@link #onResize(int, int)} and also notifies the GUI. It's called by the {@link ScreenManager} that owns this screen.
     * 
     * @param width
     *            the new width of the window.
     * @param height
     *            the new height of the window.
     */
    protected void notifyResize(int width, int height) {
        guiRoot.setSize(width, height);
        onResize(width, height);
    }

    /**
     * This method calls {@link #onDeactivated()} and also disposes the GUI. It's called by the {@link ScreenManager} that owns this screen.
     * <p>
     * Note: Don't override or call this method unless you know what you are doing.
     */
    protected void notifyDeactivated() {
        guiRoot.dispose();
        onDeactivated();
    }

    /**
     * Returns the id of the current screen in the screen manager. Because the screen manager can only have one screen instantiated at a time, the id
     * will be the id of this screen object.
     * 
     * @return the id of this screen.
     */
    public String getId() {
        return getScreenManager().getCurrentId();
    }

    /**
     * Returns the mouse handler for convenience.
     * 
     * @return the mouse handler.
     */
    public Mouse getMouse() {
        return getScreenManager().getEngine().getMouse();
    }

    /**
     * Returns the keyboard handler for convenience.
     * 
     * @return the keyboard handler.
     */
    public Keyboard getKeyboard() {
        return getScreenManager().getEngine().getKeyboard();
    }

    /**
     * Returns the current width of the game window.
     * 
     * @return the current width of the game window.
     */
    public int getWidth() {
        return getScreenManager().getEngine().getWidth();
    }

    /**
     * Returns the current height of the game window.
     * 
     * @return the current height of the game window.
     */
    public int getHeight() {
        return getScreenManager().getEngine().getHeight();
    }

    /**
     * Returns the screen manager that owns this screen.
     * 
     * @return the screen manager that owns this screen.
     */
    public ScreenManager getScreenManager() {
        return screenManager;
    }
}
