package com.github.maxstupo.flatengine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.github.maxstupo.flatengine.gameloop.AbstractGameloop;
import com.github.maxstupo.flatengine.gameloop.IEngine;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.screen.ScreenManager;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 *
 * @author Maxstupo
 */
public class Engine implements IEngine {

    private final JFlatLog log;

    private final Canvas canvas;
    private BufferStrategy strategy;

    private final ScreenManager gsm;
    private final AbstractGameloop loop;

    private final Keyboard keyboard;
    private final Mouse mouse;

    private boolean hasInit = false;

    public Engine(AbstractGameloop loop, JFlatLog log) {

        this.loop = loop;
        this.log = log;
        this.loop.attachEngine(this);
        this.canvas = new Canvas() {

            private static final long serialVersionUID = 1L;

            @Override
            public void update(Graphics g) {
                if (isShowing())
                    paint(g);
            }
        };
        this.gsm = new ScreenManager(this);
        this.keyboard = new Keyboard(canvas);
        this.mouse = new Mouse(canvas);

        System.setProperty("sun.awt.noerasebackground", "true");
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
     * Register a screen to the {@link ScreenManager}. This is a convenience method of {@link ScreenManager#registerScreen(AbstractScreen)}
     * 
     * @param gamestate
     *            the screen to add.
     */
    public void registerScreen(AbstractScreen gamestate) {
        if (gamestate == null)
            return;
        getScreenManager().registerScreen(gamestate);
    }

    /**
     * Switch to a different screen based on the id. This is a convenience method of {@link ScreenManager#switchTo(String)}
     * 
     * @param id
     *            the key of a screen to switch to.
     */
    public void switchTo(String id) {
        getScreenManager().switchTo(id);
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

    public ScreenManager getScreenManager() {
        return gsm;
    }

    public AbstractGameloop getGameloop() {
        return loop;
    }

    public Component getRenderSurface() {
        return canvas;
    }
}
