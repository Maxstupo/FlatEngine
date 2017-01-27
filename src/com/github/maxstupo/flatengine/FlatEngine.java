package com.github.maxstupo.flatengine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.github.maxstupo.flatengine.gameloop.AbstractGameloop;
import com.github.maxstupo.flatengine.gameloop.IEngine;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.screen.ScreenManager;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * This class is the game engine, it handles all sub-components that make a game engine such as rendering, IO and game states.
 * 
 * @author Maxstupo
 */
public class FlatEngine implements IEngine {

    @SuppressWarnings("javadoc")
    public static final int EXIT_ON_CLOSE = JFrame.EXIT_ON_CLOSE;
    @SuppressWarnings("javadoc")
    public static final int DO_NOTHING_ON_CLOSE = JFrame.DO_NOTHING_ON_CLOSE;

    private final JFlatLog log;

    private final Canvas canvas;
    private BufferStrategy strategy;

    private final ScreenManager gsm;
    private final AbstractGameloop loop;
    private final AssetManager am;

    private final Keyboard keyboard;
    private final Mouse mouse;

    private JFrame frame;
    private boolean isFullscreen = false;
    private boolean windowResized;

    private final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private final GraphicsDevice dev = env.getDefaultScreenDevice();

    /**
     * Create a new {@link FlatEngine} object.
     * 
     * @param loop
     *            the implementation of the game loop.
     * @param log
     *            the logger this engine will use to log information to, if null {@link JFlatLog#get()} is used.
     */
    public FlatEngine(AbstractGameloop loop, JFlatLog log) {
        this.loop = loop;
        this.log = (log != null) ? log : JFlatLog.get();
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
        this.am = new AssetManager(this);

        System.setProperty("sun.awt.noerasebackground", "true");
    }

    @Override
    public void update(double delta) {
        gsm.update(delta);
        keyboard.update();
        mouse.update();
        windowResized = false;
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
     * Creates a window for this engine. This method can only be called once.
     * 
     * @param title
     *            the title of the game window.
     * @param width
     *            the width of the game window.
     * @param height
     *            the height of the game window.
     * @param resizable
     *            true if the window can be resized.
     * @param closeOperation
     *            what occurs when the window is closed.
     * @throws RuntimeException
     *             if this method has already been called previously.
     */
    public void createWindow(String title, int width, int height, boolean resizable, int closeOperation) throws RuntimeException {
        if (frame != null)
            throw new RuntimeException("createWindow() has already been called!");

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(closeOperation);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(resizable);
        frame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                windowResized = true;
            }
        });
        frame.add(canvas);
        frame.setVisible(true);

        log.debug(getClass().getSimpleName(), "Initializing: Double buffering.");

        canvas.setIgnoreRepaint(true);
        canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();

        canvas.requestFocusInWindow();
    }

    /**
     * Returns true if the game window has been resized during this update.
     * 
     * @return true if the game window has been resized during this update.
     */
    public boolean isResized() {
        return windowResized;
    }

    /**
     * Sets if the game window is fullscreen.
     * <p>
     * Note: If {@link #createWindow(String, int, int, boolean, int)} hasn't been called yet this method does nothing.
     * 
     * @param fullscreen
     *            true to set the game window to fullscreen.
     * @return this object for chaining.
     */
    public FlatEngine setFullscreen(boolean fullscreen) {
        if (frame != null)
            dev.setFullScreenWindow(fullscreen ? frame : null);
        return this;
    }

    /**
     * Toggles the game window between windowed and fullscreen.
     * <p>
     * Note: If {@link #createWindow(String, int, int, boolean, int)} hasn't been called yet this method does nothing.
     * 
     * @return this object for chaining.
     */
    public FlatEngine toggleFullscreen() {
        return setFullscreen(!isFullscreen);
    }

    /**
     * Sets the title of the game window.
     * <p>
     * Note: If {@link #createWindow(String, int, int, boolean, int)} hasn't been called yet this method does nothing.
     * 
     * @param title
     *            the title of the game window.
     * @return this object for chaining.
     */
    public FlatEngine setTitle(String title) {
        if (frame != null)
            frame.setTitle(title);
        return this;
    }

    /**
     * Sets the minimum size of the game window.
     * <p>
     * Note: If {@link #createWindow(String, int, int, boolean, int)} hasn't been called yet this method does nothing.
     * 
     * @param width
     *            the minimum width.
     * @param height
     *            the minimum height.
     * @return this object for chaining.
     */
    public FlatEngine setMinimumSize(int width, int height) {
        if (frame == null)
            return this;
        frame.setMinimumSize(new Dimension(width, height));
        return this;
    }

    /**
     * Sets the maximum size of the game window.
     * <p>
     * Note: If {@link #createWindow(String, int, int, boolean, int)} hasn't been called yet this method does nothing.
     * 
     * @param width
     *            the maximum width.
     * @param height
     *            the maximum height.
     * @return this object for chaining.
     */
    public FlatEngine setMaximumSize(int width, int height) {
        if (frame == null)
            return this;
        frame.setMaximumSize(new Dimension(width, height));
        return this;
    }

    /**
     * Adds a window listener to the game window.
     * <p>
     * Note: If {@link #createWindow(String, int, int, boolean, int)} hasn't been called yet this method does nothing.
     * 
     * @param w
     *            the listener to add.
     * @return this object for chaining.
     */
    public FlatEngine addWindowListener(WindowListener w) {
        if (frame == null)
            return this;
        frame.addWindowListener(w);
        return this;
    }

    /**
     * Register a screen to the {@link ScreenManager}. This is a convenience method of {@link ScreenManager#registerScreen(String, Class)}
     * 
     * @param id
     *            the id of the screen.
     * 
     * @param screen
     *            the screen to add.
     * @throws IllegalArgumentException
     *             if id is null, screen is null or id is already registered.
     */
    public void registerScreen(String id, Class<? extends AbstractScreen> screen) throws IllegalArgumentException {
        getScreenManager().registerScreen(id, screen);
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
     * Start the game loop.
     */
    public void start() {
        loop.start();
    }

    /**
     * Stop the game loop.
     */
    public void stop() {
        loop.stop();
    }

    /**
     * Returns the width of the render area.
     * 
     * @return width of the render area.
     */
    @Override
    public int getWidth() {
        return canvas.getWidth();
    }

    /**
     * Returns the height of the render area.
     * 
     * @return height of the render area.
     */
    @Override
    public int getHeight() {
        return canvas.getHeight();
    }

    /**
     * Returns the keyboard handler, this object allows checking if keys are pressed/held.
     * 
     * @return the keyboard handler.
     */
    public Keyboard getKeyboard() {
        return keyboard;
    }

    /**
     * Returns the mouse handler, this object allows checking if mouse buttons are pressed or gettting the position of the mouse cursor.
     * 
     * @return the mouse handler.
     */
    public Mouse getMouse() {
        return mouse;
    }

    /**
     * The manager for all screens used within this engine.
     * 
     * @return the screen manager this engine uses.
     */
    public ScreenManager getScreenManager() {
        return gsm;
    }

    /**
     * Returns the logger.
     * 
     * @return the logger.
     */
    public JFlatLog getLog() {
        return log;
    }

    /**
     * Returns the game loop.
     * 
     * @return the game loop.
     */
    public AbstractGameloop getGameloop() {
        return loop;
    }

    /**
     * Returns the {@link AssetManager} of this engine.
     * 
     * @return the asset manager of this engine.
     */
    public AssetManager getAssetManager() {
        return am;
    }

}
