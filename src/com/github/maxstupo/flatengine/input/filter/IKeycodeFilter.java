package com.github.maxstupo.flatengine.input.filter;

import java.awt.event.KeyEvent;

import com.github.maxstupo.flatengine.gui.GuiTextbox;

/**
 * The {@link IKeycodeFilter} class allows you disable/enable keys for any keyboard related gui (example. {@link GuiTextbox}).
 * 
 * @author Maxstupo
 */
public interface IKeycodeFilter {

    /**
     * 
     * 
     * @return true if the key is allowed, or false otherwise.
     */
    public boolean filterKeycode(KeyEvent evt);
}
