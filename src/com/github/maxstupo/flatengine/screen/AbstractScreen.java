package com.github.maxstupo.flatengine.screen;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.gui.GuiContainer;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;

/**
 *
 * @author Maxstupo
 */
public abstract class AbstractScreen {

    protected final ScreenManager screenManager;

    protected final GuiContainer gui = new GuiContainer(this, null, null);

    public AbstractScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.gui.setOutlineColor(null);
    }

    public abstract void update(double delta);

    public abstract void render(Graphics2D g);

    public void doUpdate(double delta) {
        update(delta);
        gui.getSize().set(getWidth(), getHeight());
        gui.updateAll(delta, true);
    }

    public void doRender(Graphics2D g) {
        render(g);
        gui.renderAll(g);
    }

    public void onResize(int width, int height) {
        gui.resize(width, height);
    }

    public void onActivated() {
    }

    public void onDeactivated() {
        gui.dispose();
    }

    public String getId() {
        return getScreenManager().getCurrentId();
    }

    public Mouse getMouse() {
        return screenManager.getEngine().getMouse();
    }

    public Keyboard getKeyboard() {
        return screenManager.getEngine().getKeyboard();
    }

    public int getWidth() {
        return screenManager.getEngine().getWidth();
    }

    public int getHeight() {
        return screenManager.getEngine().getHeight();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }
}
