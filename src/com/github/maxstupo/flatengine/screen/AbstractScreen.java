package com.github.maxstupo.flatengine.screen;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.gui.AbstractGuiNode;
import com.github.maxstupo.flatengine.gui.GuiNode;

/**
 *
 * @author Maxstupo
 */
public abstract class AbstractScreen {

    protected final ScreenManager screenManager;

    protected final AbstractGuiNode gui = new GuiNode(this);

    public AbstractScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
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
        gui.dispose();
    }

    public String getId() {
        return getScreenManager().getCurrentId();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }
}
