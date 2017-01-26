package com.github.maxstupo.flatengine.screen;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.Window;

/**
 *
 * @author Maxstupo
 */
public class ScreenManager {

    private final Engine engine;
    private final Map<String, AbstractScreen> screens = new HashMap<>();

    private AbstractScreen currentScreen = null;
    private boolean hasRendered;
    private boolean onActivated;

    public ScreenManager(Engine engine) {
        this.engine = engine;
    }

    public void update(double delta) {
        if (currentScreen != null) {

            currentScreen.doUpdate(delta);

            if (onActivated && hasRendered) {
                onActivated = false;
                currentScreen.onActivated();
            }

            if (Window.get().isResized() && hasRendered)
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
        AbstractScreen state = screens.get(id);
        if (state == null)
            return false;
        hasRendered = false;

        if (currentScreen != null)
            currentScreen.onDeactivated();

        currentScreen = state;
        onActivated = true;
        return true;
    }

    public void registerScreen(AbstractScreen gamestate) {
        if (gamestate == null)
            return;
        screens.put(gamestate.getKey(), gamestate);
    }

    public AbstractScreen getScreen(String key) {
        return screens.get(key);
    }

    public AbstractScreen getCurrentScreen() {
        return currentScreen;
    }

    public Engine getEngine() {
        return engine;
    }

}
