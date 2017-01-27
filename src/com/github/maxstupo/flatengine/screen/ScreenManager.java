package com.github.maxstupo.flatengine.screen;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.github.maxstupo.flatengine.FlatEngine;

/**
 * This class manages all screens within the engine. Only one screen object can be instantiated at any given time, if the screen manager switches
 * screens the new screen object is instantiated and the old one is disposed.
 * <p>
 * The {@link ScreenManager} class is also responsible for triggering {@link AbstractScreen#onActivated() onActivated},
 * {@link AbstractScreen#onDeactivated() onDeactivated} and {@link AbstractScreen#onResize(int, int) onResize(int, int)}
 * 
 * @author Maxstupo
 */
public class ScreenManager {

    private final FlatEngine engine;
    private final Map<String, Class<? extends AbstractScreen>> screens = new HashMap<>();

    private AbstractScreen currentScreen = null;
    private String currentId = "";

    private boolean hasRendered; // true if the current screen has rendered at least once.
    private boolean onActivated; // true when onActivated() hasn't been called yet.

    /**
     * Create a new {@link ScreenManager} object.
     * 
     * @param engine
     *            the engine this screen manager is associated with.
     */
    public ScreenManager(FlatEngine engine) {
        this.engine = engine;
    }

    /**
     * Updates the current screen and handles calling of both {@link AbstractScreen#onResize(int, int) onResize(int, int)} and
     * {@link AbstractScreen#onActivated() onActivated}.
     * 
     * @param delta
     *            the delta time.
     */
    public void update(double delta) {
        if (currentScreen != null) {

            currentScreen.doUpdate(delta);

            if (onActivated && hasRendered) {
                onActivated = false;
                currentScreen.onActivated();
            }

            if (engine.isResized() && hasRendered)
                currentScreen.onResize(engine.getWidth(), engine.getHeight());
        }
    }

    /**
     * Renders the current screen.
     * 
     * @param g
     *            the graphics context to draw to.
     */
    public void render(Graphics2D g) {
        if (currentScreen != null) {
            currentScreen.doRender(g);
            hasRendered = true;
        }
    }

    /**
     * Switches to the screen that is associated with the given id.
     * 
     * @param id
     *            the id of the screen to switch to.
     * @return true if the screen was changed.
     */
    public boolean switchTo(String id) {
        if (id.equals(currentId)) {
            engine.getLog().warn(getClass().getSimpleName(), "Switch screens ignored. Screen requested already is current screen.");
            return false;
        }

        AbstractScreen state = createScreen(id);
        if (state == null) {
            engine.getLog().warn(getClass().getSimpleName(), "No screen registered with the id: '{0}'", id);
            return false;
        }

        hasRendered = false;

        engine.getLog().debug(getClass().getSimpleName(), "Switched screen: '{0}' -> '{1}'", currentId, id);

        if (currentScreen != null)
            currentScreen.onDeactivated();

        currentScreen = state;
        currentId = id;
        onActivated = true;
        return true;
    }

    private AbstractScreen createScreen(String id) {
        Class<? extends AbstractScreen> clazz = screens.get(id);
        if (clazz == null)
            return null;

        try {
            return clazz.getConstructor(ScreenManager.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Register a screen with the given id.
     * 
     * @param id
     *            the id the given screen is associated with.
     * @param screen
     *            the screen to register.
     * @return this object for chaining.
     * @throws IllegalArgumentException
     *             if the given screen or id are null, or the given id is already registered within this screen manager.
     */
    public ScreenManager registerScreen(String id, Class<? extends AbstractScreen> screen) throws IllegalArgumentException {
        if (screen == null)
            throw new IllegalArgumentException("Screen can't be null!");
        if (screens.containsKey(id) || id == null)
            throw new IllegalArgumentException("The screen id is already registered: " + id);
        screens.put(id, screen);
        engine.getLog().debug(getClass().getSimpleName(), "Registered screen: '{0}' -> '{1}'", id, screen.getName());
        return this;
    }

    /**
     * Returns the current active screen.
     * 
     * @return the current active screen.
     */
    public AbstractScreen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Returns the id of the current screen.
     * 
     * @return the id of the current screen.
     */
    public String getCurrentId() {
        return currentId;
    }

    /**
     * Returns the {@link FlatEngine} that this {@link ScreenManager} is associated with.
     * 
     * @return the {@link FlatEngine} that this {@link ScreenManager} is associated with.
     */
    public FlatEngine getEngine() {
        return engine;
    }

}
