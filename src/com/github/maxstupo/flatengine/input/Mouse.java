package com.github.maxstupo.flatengine.input;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This class allows for handling of mouse buttons and mouse related things (e.g. cursor position, scroll-wheel rotation, etc) in a game oriented way.
 * 
 * @author Maxstupo
 */
public class Mouse {

    @SuppressWarnings("javadoc")
    public static final int LEFT_CLICK = MouseEvent.BUTTON1;

    @SuppressWarnings("javadoc")
    public static final int MIDDLE_CLICK = MouseEvent.BUTTON2;

    @SuppressWarnings("javadoc")
    public static final int RIGHT_CLICK = MouseEvent.BUTTON3;

    /** The total number of mouse button codes monitored. */
    public static final int TOTAL_MOUSE_BUTTONS_MONITORED = 5;

    private final boolean[] mouse = new boolean[TOTAL_MOUSE_BUTTONS_MONITORED];
    private final boolean[] lastMouse = new boolean[TOTAL_MOUSE_BUTTONS_MONITORED];
    private final Vector2i position = new Vector2i();
    private final Point point = new Point(0, 0);

    private boolean didMouseWheelMove = false;
    private int wheelRotation = 0;
    private MouseEvent mouseEvent;

    private final MouseAdapter adapter = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            mouseEvent = e;
            setMouseState(e.getButton(), true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseEvent = e;
            setMouseState(e.getButton(), false);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            mouseEvent = e;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            updateLocation(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            updateLocation(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int r = e.getWheelRotation();
            if (r != 0) {
                didMouseWheelMove = true;
                wheelRotation = r;
            }
        }
    };

    /**
     * Create a new {@link Mouse} object.
     * 
     * @param comp
     *            the component to hook into and listen for mouse events.
     * @throws IllegalArgumentException
     *             if the given component is null.
     */
    public Mouse(Component comp) throws IllegalArgumentException {
        if (comp == null)
            throw new IllegalArgumentException("The given component can't be null!");
        comp.addMouseListener(adapter);
        comp.addMouseMotionListener(adapter);
        comp.addMouseWheelListener(adapter);
    }

    /**
     * Updates the last mouse buttons pressed and resets wheel rotation related variables.
     */
    public void update() {
        for (int i = 0; i < lastMouse.length; i++)
            lastMouse[i] = getMouse(i);

        if (wheelRotation != 0) {
            didMouseWheelMove = false;
            wheelRotation = 0;
        }
    }

    private void updateLocation(MouseEvent e) {
        position.set(e.getX(), e.getY());
    }

    /**
     * Returns true if the mouse button is held down.
     * 
     * @param mouseCode
     *            the mouse button code to check.
     * @return true if the mouse button is held down.
     */
    public boolean isMouseHeld(int mouseCode) {
        return getMouse(mouseCode);
    }

    /**
     * Returns true if the mouse button is down during this update.
     * 
     * @param mouseCode
     *            the mouse button code to check.
     * @return true if the mouse button is down during this update.
     */
    public boolean isMouseDown(int mouseCode) {
        return getMouse(mouseCode) && !lastMouse[mouseCode];
    }

    /**
     * Returns true if the mouse button is up during this update.
     * 
     * @param mouseCode
     *            the mouse button code to check.
     * @return true if the mouse button is up during this update.
     */
    public boolean isMouseUp(int mouseCode) {
        return !getMouse(mouseCode) && lastMouse[mouseCode];
    }

    /**
     * Returns true if the mouse button is up.
     * 
     * @param mouseCode
     *            the mouse button code to check.
     * @return true if the mouse button is up.
     */
    public boolean isMouseReleased(int mouseCode) {
        return !getMouse(mouseCode);
    }

    private void setMouseState(int mouseCode, boolean state) {
        if (!isValid(mouseCode))
            return;
        mouse[mouseCode] = state;
    }

    private boolean getMouse(int mouseCode) {
        if (!isValid(mouseCode))
            return false;
        return mouse[mouseCode];
    }

    private boolean isValid(int mouseCode) {
        return mouseCode >= 0 && mouseCode < mouse.length;
    }

    /**
     * Returns true if the mouse wheel moved during this update.
     * 
     * @return true if the mouse wheel moved during this update.
     */
    public boolean didMouseWheelMove() {
        return didMouseWheelMove;
    }

    /**
     * Returns the number of clicks the mouse wheel turned. The sign of the number represents the rotation, positive is clockwise.
     * 
     * @return the number of clicks the mouse wheel turned. The sign of the number represents the rotation, positive is clockwise.
     */
    public int getWheelRotation() {
        return wheelRotation;
    }

    /**
     * Returns the last mouse event that was triggered.
     * 
     * @return the last mouse event that was triggered.
     */
    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }

    /**
     * Returns the position of the mouse cursor.
     * 
     * @return the position of the mouse cursor.
     */
    public Vector2i getPosition() {
        return position;
    }

    /**
     * Returns a point object representing the cursor position. This is useful for checking if a {@link Rectangle} contains the cursor or not.
     * 
     * @return a point object representing the cursor position.
     */
    public Point getPositionPoint() {
        point.setLocation(position.x, position.y);
        return point;
    }
}
