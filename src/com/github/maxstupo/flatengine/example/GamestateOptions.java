package com.github.maxstupo.flatengine.example;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.example.BasicExample.State;
import com.github.maxstupo.flatengine.states.AbstractGamestate;

/**
 * @author Maxstupo
 *
 */
public class GamestateOptions extends AbstractGamestate<State> {

    public GamestateOptions(Engine<State> engine, State key) {
        super(engine, key);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics2D g) {

    }

}
