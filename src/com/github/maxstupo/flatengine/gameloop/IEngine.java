package com.github.maxstupo.flatengine.gameloop;

/**
 * This interface represents the minimum methods needed for a {@link AbstractGameloop} to update a engine.
 * 
 * @author Maxstupo
 */
public interface IEngine {

    /**
     * Called when the game engine should update all logic.
     * 
     * @param delta
     *            the delta time.
     */
    void update(float delta);

    /**
     * Called when the game engine should render the game.
     */
    void render();

}
