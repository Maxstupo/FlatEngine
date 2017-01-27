package com.github.maxstupo.flatengine.input.filter;

import java.awt.event.KeyEvent;

import com.github.maxstupo.flatengine.gui.GuiTextbox;

/**
 * The {@link IKeycodeFilter} class allows you disable/enable keys for any keyboard related GUI (e.g. {@link GuiTextbox}).
 * 
 * @author Maxstupo
 */
public interface IKeycodeFilter {

    /**
     * Returns true if the key code is allowed.
     * 
     * @param evt
     *            the key event to check.
     * @return true if the key is allowed, or false otherwise.
     */
    public boolean filterKeycode(KeyEvent evt);
}
