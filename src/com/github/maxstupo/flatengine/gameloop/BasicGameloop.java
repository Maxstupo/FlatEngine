package com.github.maxstupo.flatengine.gameloop;

/**
 * @author Maxstupo
 *
 */
public class BasicGameloop extends AbstractGameloop {

    private final long optimalTime;
    private long lastFpsTime;
    private int fps;
    private int realFps;

    public BasicGameloop(double targetFps) {
        super(targetFps);
        this.optimalTime = 1000000000 / (int) targetFps;
    }

    @Override
    public void run() {
        long lastLoopTime = System.nanoTime();
        while (isRunning()) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) optimalTime);

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000) {
                realFps = fps;
                lastFpsTime = 0;
                fps = 0;
            }

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
