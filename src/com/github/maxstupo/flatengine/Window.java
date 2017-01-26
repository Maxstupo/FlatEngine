package com.github.maxstupo.flatengine;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

/**
 *
 * @author Maxstupo
 */
public class Window {

    public static final int EXIT_ON_CLOSE = JFrame.EXIT_ON_CLOSE;
    public static final int DO_NOTHING_ON_CLOSE = JFrame.DO_NOTHING_ON_CLOSE;
    public static final int DISPOSE_ON_CLOSE = JFrame.DISPOSE_ON_CLOSE;
    public static final int HIDE_ON_CLOSE = JFrame.HIDE_ON_CLOSE;

    private static Window instance;

    private JFrame frame;
    private boolean isFullscreen = false;
    private boolean windowResized;

    private final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private final GraphicsDevice dev = env.getDefaultScreenDevice();

    private Window() {

    }

    public void create(String title, int width, int height, boolean resizable, int closeOperation, Engine engine) {
        if (frame != null)
            return;
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
        frame.add(engine.getRenderSurface());
        frame.setVisible(true);

        engine.init();
    }

    protected void update() {
        windowResized = false;
    }

    public boolean isResized() {
        return windowResized;
    }

    public void setFullscreen(boolean fullscreen) {
        if (frame != null)
            dev.setFullScreenWindow(fullscreen ? frame : null);
    }

    public void toggleFullscreen() {
        setFullscreen(!isFullscreen);
    }

    public Window setTitle(String title) {
        if (frame != null)
            frame.setTitle(title);
        return this;
    }

    public Window setMinimumSize(int width, int height) {
        if (frame == null)
            return this;
        frame.setMinimumSize(new Dimension(width, height));
        return this;
    }

    public Window setMaximumSize(int width, int height) {
        if (frame == null)
            return this;
        frame.setMaximumSize(new Dimension(width, height));
        return this;
    }

    public Window addWindowListener(WindowAdapter w) {
        if (frame == null || w == null)
            return this;
        frame.addWindowListener(w);
        return this;
    }

    public static final Window get() {
        if (instance == null)
            instance = new Window();
        return instance;
    }

}
