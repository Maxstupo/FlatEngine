package com.github.maxstupo.flatengine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.github.maxstupo.flatengine.gameloop.AbstractGameloop;
import com.github.maxstupo.flatengine.gameloop.IEngine;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.states.GamestateManager;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 *
 * @author Maxstupo
 */
public class Engine<T extends Enum<T>> implements IEngine {

    private final JFlatLog log;

    private final Canvas canvas;
    private BufferStrategy strategy;

    private final GamestateManager<T> gsm;
    private final AbstractGameloop loop;

    private final Keyboard keyboard;
    private final Mouse mouse;

    private boolean hasInit = false;

    public Engine(AbstractGameloop loop, JFlatLog log) {
        this.loop = loop;
        this.log = log;
        this.loop.attachEngine(this);
        this.canvas = new Canvas();
        this.gsm = new GamestateManager<>(this);
        this.keyboard = new Keyboard(canvas);
        this.mouse = new Mouse(canvas);
    }

    protected void init() {
        if (hasInit)
            return;
        if (log != null)
            log.debug(getClass().getSimpleName(), "Initializing...");

        initGraphics();
        canvas.requestFocusInWindow();
        hasInit = true;
    }

    private void initGraphics() {
        if (log != null)
            log.debug(getClass().getSimpleName(), "Initializing: Double buffering.");

        canvas.setIgnoreRepaint(true);
        canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();
    }

    @Override
    public void update(double delta) {
        gsm.update(delta);
        keyboard.update();
        mouse.update();
        Window.get().update();
    }

    @Override
    public void render() {
        do {
            do {
                Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                gsm.render(g);

                g.dispose();
            } while (strategy.contentsRestored());

            strategy.show();

        } while (strategy.contentsLost());
    }

    /**
     * Register a game state to the {@link GamestateManager} (GSM). This is a convenience method of
     * {@link GamestateManager#registerState(AbstractGamestate)}
     * 
     * @param gamestate
     *            the game state to add.
     */
    public void registerState(AbstractGamestate<T> gamestate) {
        if (gamestate == null)
            return;
        getGamestateManager().registerState(gamestate);
    }

    /**
     * Switch to a different game state based on the gamestate key. This is a convenience method of {@link GamestateManager#switchTo(Enum)}
     * 
     * @param id
     *            the key of a gamestate to switch to.
     */
    public void switchTo(T id) {
        getGamestateManager().switchTo(id);
    }

    /**
     * Start the gameloop.
     */
    public void start() {
        loop.start();
    }

    /**
     * Stop the gameloop.
     */
    public void stop() {
        loop.stop();
    }

    /**
     * Returns the width of the render area.
     * 
     * @return width of the render area.
     */
    public int getWidth() {
        return canvas.getWidth();
    }

    /**
     * Returns the height of the render area.
     * 
     * @return height of the render area.
     */
    public int getHeight() {
        return canvas.getHeight();
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public GamestateManager<T> getGamestateManager() {
        return gsm;
    }

    public AbstractGameloop getGameloop() {
        return loop;
    }

    public Component getRenderSurface() {
        return canvas;
    }
}
