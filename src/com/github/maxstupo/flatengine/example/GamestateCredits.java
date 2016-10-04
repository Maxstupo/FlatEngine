package com.github.maxstupo.flatengine.example;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.example.BasicExample.State;
import com.github.maxstupo.flatengine.states.AbstractGamestate;

/**
 * @author Maxstupo
 *
 */
public class GamestateCredits extends AbstractGamestate<State> {

    public GamestateCredits(Engine<State> engine, State key) {
        super(engine, key);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics2D g) {

    }

}
