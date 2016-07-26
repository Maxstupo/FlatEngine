package com.github.maxstupo.flatengine.gui;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.states.AbstractGamestate;

/**
 * @author Maxstupo
 *
 */
public class GuiNode extends AbstractGuiNode {

    public GuiNode(AbstractGamestate gamestate) {
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
