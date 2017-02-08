package com.github.maxstupo.flatengine.gameloop;

/**
 * This is a basic game loop implementation that supports delta time.
 * 
 * @author Maxstupo
 */
public class BasicGameloop extends AbstractGameloop {

    private final long optimalTime;
    private long lastFpsTime;
    private int fps;
    private int realFps;

    /**
     * Create a new {@link BasicGameloop} object with the given target frames per second.
     * 
     * @param targetFps
     *            the frames per second the game loop will try and achieve.
     */
    public BasicGameloop(double targetFps) {
        super(targetFps);
        this.optimalTime = 1000000000 / (int) targetFps;
    }

    @Override
    public void run() {
        long lastLoopTime = System.nanoTime();
        while (isRunning()) {
            long elapsedTime = System.nanoTime() - lastLoopTime;
            lastLoopTime = System.nanoTime();

            float delta = elapsedTime / 1000000000f;

            // update the frame counter
            lastFpsTime += elapsedTime;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000) {
                realFps = fps;
                lastFpsTime = 0;
                fps = 0;
            }
            // System.out.println(delta);
            engine.update(delta);

            engine.render();

            try {
                long sleep = (lastLoopTime - System.nanoTime() + optimalTime) / 1000000;
                if (sleep > 0)
                    Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getFPS() {
        return realFps;
    }

}
