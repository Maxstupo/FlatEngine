package com.github.maxstupo.flatengine.states;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.gui.AbstractGuiNode;
import com.github.maxstupo.flatengine.gui.GuiNode;

/**
 *
 * @author Maxstupo
 */
public abstract class AbstractGamestate<T extends Enum<T>> {

    protected final GamestateManager<T> gsm;
    protected final T key;

    protected final AbstractGuiNode<T> gui = new GuiNode<>(this);

    public AbstractGamestate(Engine<T> engine, T key) {
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

    public T getKey() {
        return key;
    }

    public GamestateManager<T> getGamestateManager() {
        return gsm;
    }
}
