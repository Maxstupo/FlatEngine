package com.github.maxstupo.flatengine.screen;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.github.maxstupo.flatengine.FlatEngine;

/**
 *
 * @author Maxstupo
 */
public class ScreenManager {

    private final FlatEngine engine;
    private final Map<String, Class<? extends AbstractScreen>> screens = new HashMap<>();

    private AbstractScreen currentScreen = null;
    private String currentId = "";

    private boolean hasRendered;
    private boolean onActivated;

    public ScreenManager(FlatEngine engine) {
        this.engine = engine;
    }

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

    public void render(Graphics2D g) {
        if (currentScreen != null) {
            currentScreen.doRender(g);
            hasRendered = true;
        }
    }

    public boolean switchTo(String id) {
        if (id.equals(currentId))
            return false;

        AbstractScreen state = createScreen(id);
        if (state == null)
            return false;
        hasRendered = false;

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

    public ScreenManager registerScreen(String id, Class<? extends AbstractScreen> screen) throws IllegalArgumentException {
        if (screen == null)
            throw new IllegalArgumentException("Screen can't be null!");
        if (screens.containsKey(id) || id == null)
            throw new IllegalArgumentException("The screen id is already registered: " + id);
        screens.put(id, screen);
        return this;
    }

    public AbstractScreen getCurrentScreen() {
        return currentScreen;
    }

    public FlatEngine getEngine() {
        return engine;
    }

    public String getCurrentId() {
        return currentId;
    }

}
