package com.github.maxstupo.flatengine.input;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public class Mouse {

    public static final int LEFT_CLICK = MouseEvent.BUTTON1;
    public static final int MIDDLE_CLICK = MouseEvent.BUTTON2;
    public static final int RIGHT_CLICK = MouseEvent.BUTTON3;

    public static final int TOTAL_MOUSE_BUTTONS_MONITORED = 5;

    private final boolean[] mouse = new boolean[TOTAL_MOUSE_BUTTONS_MONITORED];
    private final boolean[] lastMouse = new boolean[TOTAL_MOUSE_BUTTONS_MONITORED];
    private final Vector2i position = new Vector2i();
    private final Rectangle bounds = new Rectangle(1, 1);

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

    public Mouse(Component comp) {
        comp.addMouseListener(adapter);
        comp.addMouseMotionListener(adapter);
        comp.addMouseWheelListener(adapter);
    }

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

    public boolean isMouseHeld(int mouseCode) {
        return getMouse(mouseCode);
    }

    public boolean isMouseDown(int mouseCode) {
        return getMouse(mouseCode) && !lastMouse[mouseCode];
    }

    public boolean isMouseUp(int mouseCode) {
        return !getMouse(mouseCode) && lastMouse[mouseCode];
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

    public boolean didMouseWheelMove() {
        return didMouseWheelMove;
    }

    public int getWheelRotation() {
        return wheelRotation;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }

    public Vector2i getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        bounds.setLocation(position.x, position.y);
        return bounds;
    }
}
