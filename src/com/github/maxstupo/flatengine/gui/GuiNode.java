package com.github.maxstupo.flatengine.gui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.states.AbstractGamestate;

/**
 * @author Maxstupo
 *
 */
public class GuiNode<T extends Enum<T>> extends AbstractGuiNode<T> {

    public GuiNode(AbstractGamestate<T> gamestate) {
        super(gamestate, null, null);
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {
        return shouldHandleInput;
    }

    @Override
    public void render(Graphics2D g) {

    }

}
