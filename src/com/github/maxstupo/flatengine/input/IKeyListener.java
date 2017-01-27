package com.github.maxstupo.flatengine.input;

import java.awt.event.KeyEvent;

/**
 * This interface is used for events called by the {@link Keyboard} class.
 * 
 * @author Maxstupo
 */
public interface IKeyListener {

    /**
     * Called when a key is pressed.
     * 
     * @param e
     *            the key event.
     */
    void keyPressed(KeyEvent e);

    /**
     * Called when a key is released.
     * 
     * @param e
     *            the key event.
     */
    void keyReleased(KeyEvent e);
}
