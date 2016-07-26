package com.github.maxstupo.flatengine.states;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.Window;

/**
 *
 * @author Maxstupo
 */
public class GamestateManager {

    private final Engine engine;
    private final Map<String, AbstractGamestate> states = new HashMap<>();

    private AbstractGamestate currentState = null;
    private boolean hasRendered;
    private boolean onActivated;

    public GamestateManager(Engine engine) {
        this.engine = engine;
    }

    public void update(double delta) {
        if (currentState != null) {
            if (onActivated && hasRendered) {
                onActivated = false;
                currentState.onActivated();
            }

            currentState.doUpdate(delta);

            if (Window.get().isResized() && hasRendered)
                currentState.onResize(engine.getWidth(), engine.getHeight());
        }
    }

    public void render(Graphics2D g) {
        if (currentState != null) {
            currentState.doRender(g);
            hasRendered = true;
        }
    }

    public boolean switchTo(String id) {
        AbstractGamestate state = states.get(id);
        if (state == null)
            return false;
        hasRendered = false;

        if (currentState != null)
            currentState.onDeactivated();

        currentState = state;
        onActivated = true;
        return true;
    }

    public void registerState(AbstractGamestate gamestate) {
        if (gamestate == null)
            return;
        states.put(gamestate.getKey(), gamestate);
    }

    public AbstractGamestate getState(String key) {
        return states.get(key);
    }

    public AbstractGamestate getCurrentState() {
        return currentState;
    }

    public Engine getEngine() {
        return engine;
    }

}
