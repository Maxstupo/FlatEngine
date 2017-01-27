package com.github.maxstupo.flatengine.input;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows for handling of keys in a game oriented way.
 * 
 * @author Maxstupo
 */
@SuppressWarnings("javadoc") // Suppress for the key constants.
public class Keyboard {

    public static final int KEY_A = KeyEvent.VK_A;
    public static final int KEY_B = KeyEvent.VK_B;
    public static final int KEY_C = KeyEvent.VK_C;
    public static final int KEY_D = KeyEvent.VK_D;
    public static final int KEY_E = KeyEvent.VK_E;
    public static final int KEY_F = KeyEvent.VK_F;
    public static final int KEY_G = KeyEvent.VK_G;
    public static final int KEY_H = KeyEvent.VK_H;
    public static final int KEY_I = KeyEvent.VK_I;
    public static final int KEY_J = KeyEvent.VK_J;
    public static final int KEY_K = KeyEvent.VK_K;
    public static final int KEY_L = KeyEvent.VK_L;
    public static final int KEY_M = KeyEvent.VK_M;
    public static final int KEY_N = KeyEvent.VK_N;
    public static final int KEY_O = KeyEvent.VK_O;
    public static final int KEY_P = KeyEvent.VK_P;
    public static final int KEY_Q = KeyEvent.VK_Q;
    public static final int KEY_R = KeyEvent.VK_R;
    public static final int KEY_S = KeyEvent.VK_S;
    public static final int KEY_T = KeyEvent.VK_T;
    public static final int KEY_U = KeyEvent.VK_U;
    public static final int KEY_V = KeyEvent.VK_V;
    public static final int KEY_W = KeyEvent.VK_W;
    public static final int KEY_Y = KeyEvent.VK_Y;
    public static final int KEY_X = KeyEvent.VK_X;
    public static final int KEY_Z = KeyEvent.VK_Z;

    public static final int KEY_0 = KeyEvent.VK_0;
    public static final int KEY_1 = KeyEvent.VK_1;
    public static final int KEY_2 = KeyEvent.VK_2;
    public static final int KEY_3 = KeyEvent.VK_3;
    public static final int KEY_4 = KeyEvent.VK_4;
    public static final int KEY_5 = KeyEvent.VK_5;
    public static final int KEY_6 = KeyEvent.VK_6;
    public static final int KEY_7 = KeyEvent.VK_7;
    public static final int KEY_8 = KeyEvent.VK_8;
    public static final int KEY_9 = KeyEvent.VK_9;

    public static final int KEY_F1 = KeyEvent.VK_F1;
    public static final int KEY_F2 = KeyEvent.VK_F2;
    public static final int KEY_F3 = KeyEvent.VK_F3;
    public static final int KEY_F4 = KeyEvent.VK_F4;
    public static final int KEY_F5 = KeyEvent.VK_F5;
    public static final int KEY_F6 = KeyEvent.VK_F6;
    public static final int KEY_F7 = KeyEvent.VK_F7;
    public static final int KEY_F8 = KeyEvent.VK_F8;
    public static final int KEY_F9 = KeyEvent.VK_F9;
    public static final int KEY_F10 = KeyEvent.VK_F10;
    public static final int KEY_F11 = KeyEvent.VK_F11;
    public static final int KEY_F12 = KeyEvent.VK_F12;

    public static final int KEY_UP = KeyEvent.VK_UP;
    public static final int KEY_DOWN = KeyEvent.VK_DOWN;
    public static final int KEY_LEFT = KeyEvent.VK_LEFT;
    public static final int KEY_RIGHT = KeyEvent.VK_RIGHT;

    public static final int KEY_ESCAPE = KeyEvent.VK_ESCAPE;
    public static final int KEY_SPACE = KeyEvent.VK_SPACE;

    private final boolean[] keys = new boolean[256];
    private final boolean[] lastKeys = new boolean[keys.length];

    private final List<IKeyListener> listeners = new ArrayList<>();

    private final KeyAdapter adapter = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            setKeyState(e, true);
            for (IKeyListener l : listeners)
                l.keyPressed(e);

        }

        @Override
        public void keyReleased(KeyEvent e) {
            setKeyState(e, false);
            for (IKeyListener l : listeners)
                l.keyReleased(e);
        }
    };

    /**
     * Create a new {@link Keyboard} object.
     * 
     * @param comp
     *            the component to hook into and listener for key events.
     * @throws IllegalArgumentException
     *             if the given component is null.
     */
    public Keyboard(Component comp) throws IllegalArgumentException {
        if (comp == null)
            throw new IllegalArgumentException("The given component can't be null!");
        comp.addKeyListener(adapter);
        comp.requestFocusInWindow();
    }

    /**
     * Called to update the last keys pressed.
     */
    public void update() {
        for (int i = 0; i < lastKeys.length; i++)
            lastKeys[i] = getKey(i);
    }

    private void setKeyState(KeyEvent e, boolean state) {
        if (!isValid(e.getKeyCode()))
            return;
        keys[e.getKeyCode()] = state;
    }

    /**
     * Returns true if the key code is held down.
     * 
     * @param keyCode
     *            the key code to check.
     * @return true if the key code is held down.
     */
    public boolean isKeyHeld(int keyCode) {
        return getKey(keyCode);
    }

    /**
     * Returns true if the key code is down during this update.
     * 
     * @param keyCode
     *            the key code to check.
     * @return true if the key code is down during this update.
     */
    public boolean isKeyDown(int keyCode) {
        if (!isValid(keyCode))
            return false;
        return getKey(keyCode) && !lastKeys[keyCode];
    }

    /**
     * Returns true if the key code is up during this update.
     * 
     * @param keyCode
     *            the key code to check.
     * @return true if the key code is up during this update.
     */
    public boolean isKeyUp(int keyCode) {
        if (!isValid(keyCode))
            return false;
        return !getKey(keyCode) && lastKeys[keyCode];
    }

    /**
     * Returns true if the key code is up.
     * 
     * @param keyCode
     *            the key code to check.
     * @return true if the key code is up.
     */
    public boolean isKeyReleased(int keyCode) {
        return !getKey(keyCode);
    }

    private boolean isValid(int id) {
        return id >= 0 && id < keys.length;
    }

    private boolean getKey(int keycode) {
        if (!isValid(keycode))
            return false;
        return keys[keycode];
    }

    /**
     * Adds a key listener to this keyboard handler.
     * 
     * @param listener
     *            the listener to add.
     */
    public void addListener(IKeyListener listener) {
        if (listener == null)
            return;
        listeners.add(listener);
    }

    /**
     * Removes a key listener from this keyboard handler.
     * 
     * @param listener
     *            the listener to remove.
     */
    public void removeListener(IKeyListener listener) {
        listeners.remove(listener);
    }

}
