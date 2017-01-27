package com.github.maxstupo.flatengine.gameloop;

import com.github.maxstupo.flatengine.FlatEngine;

/**
 * This abstract class represents the foundation of a game loop.
 * 
 * @author Maxstupo
 */
public abstract class AbstractGameloop implements Runnable {

    /** The frames per second the game loop is trying to achieve. */
    protected final double targetFps;

    private Thread thread;

    /** True if the game loop thread is running. */
    protected boolean isRunning = false;

    /**
     * The attached game engine interface used to update the game engine. Both {@link IEngine#render()} and {@link IEngine#update(float)} need to be
     * called within the game loop logic.
     */
    protected IEngine engine;

    /**
     * Create a new game loop object with a target frames per second.
     * 
     * @param targetFps
     *            the frames per second the game loop will try and achieve.
     */
    public AbstractGameloop(double targetFps) {
        this.targetFps = targetFps;
    }

    /**
     * Returns the current frames per second.
     * 
     * @return the current frames per second.
     */
    public abstract int getFPS();

    /**
     * Starts the game loop thread.
     * 
     * @return true if the game loop thread was started.
     */
    public boolean start() {
        if (isRunning)
            return false;

        isRunning = true;
        thread = new Thread(this);
        thread.start();
        return true;
    }

    /**
     * Stops the game loop thread.
     * 
     * @return true if the game loop thread was stopped.
     */
    public boolean stop() {
        if (!isRunning)
            return false;

        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread = null;
        return true;
    }

    /**
     * Attaches the given game engine interface to this game loop.
     * <p>
     * Note: If using a {@link FlatEngine} object for this game loop don't call this method, let the {@link FlatEngine} object do it when you pass
     * this game loop to it's constructor.
     * 
     * @param engine
     *            the engine interface to attach.
     * @throws IllegalArgumentException
     *             if a engine interface is already attached to this game loop.
     */
    public void attachEngine(IEngine engine) throws IllegalArgumentException {
        if (this.engine != null)
            throw new IllegalArgumentException("A engine is already attached to this gameloop!");
        this.engine = engine;
    }

    /**
     * Detaches the engine interface from this game loop. This method will call {@link #stop()}.
     */
    public void detachEngine() {
        stop();
        this.engine = null;
    }

    /**
     * Returns true if the game loop thread is running.
     * 
     * @return true if the game loop thread is running.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Returns the frames per second the game loop is trying to achieve.
     * 
     * @return the frames per second the game loop is trying to achieve.
     */
    public double getTargetFps() {
        return targetFps;
    }
}
