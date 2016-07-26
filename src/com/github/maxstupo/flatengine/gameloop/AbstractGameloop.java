package com.github.maxstupo.flatengine.gameloop;

/**
 *
 * @author Maxstupo
 */
public abstract class AbstractGameloop implements Runnable {

    protected final double targetFps;

    private Thread thread;
    protected boolean isRunning = false;

    protected IEngine engine;

    public AbstractGameloop(double targetFps) {
        this.targetFps = targetFps;
    }

    public abstract int getFPS();

    public void start() {
        isRunning = true;
        if (thread == null)
            thread = new Thread(this);
        if (!thread.isAlive())
            thread.start();
    }

    public void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void attachEngine(IEngine engine) {
        this.engine = engine;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public double getTargetFps() {
        return targetFps;
    }
}
