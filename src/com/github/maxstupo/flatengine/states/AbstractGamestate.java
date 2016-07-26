package com.github.maxstupo.flatengine.states;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.gui.AbstractGuiNode;
import com.github.maxstupo.flatengine.gui.GuiNode;

/**
 *
 * @author Maxstupo
 */
public abstract class AbstractGamestate {

    protected final GamestateManager gsm;
    protected final String key;

    protected final AbstractGuiNode gui = new GuiNode(this);

    public AbstractGamestate(Engine engine, String key) {
        this.gsm = engine.getGamestateManager();
        this.key = key;
    }

    public abstract void update(double delta);

    public abstract void render(Graphics2D g);

    public void doUpdate(double delta) {
        update(delta);
        gui.updateAll(delta, true);
    }

    public void doRender(Graphics2D g) {
        render(g);
        gui.renderAll(g);
    }

    public void onResize(int width, int height) {
    }

    public void onActivated() {
    }

    public void onDeactivated() {
    }

    public String getKey() {
        return key;
    }

    public GamestateManager getGamestateManager() {
        return gsm;
    }
}
