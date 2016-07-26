package com.github.maxstupo.flatengine.input.filter;

import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOLLAR;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Y;
import static java.awt.event.KeyEvent.VK_Z;

import java.awt.event.KeyEvent;

/**
 * The {@link BasicKeycodeFilter} class allows only the alphabet and a few basic keys (such as brackets, semicolons, 1, 2, 3, etc..)
 * 
 * @author Maxstupo
 */
public class BasicKeycodeFilter implements IKeycodeFilter {

    @Override
    public boolean filterKeycode(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_A:
                return true;
            case VK_B:
                return true;
            case VK_C:
                return true;
            case VK_D:
                return true;
            case VK_E:
                return true;
            case VK_CLOSE_BRACKET:
                return true;
            case VK_OPEN_BRACKET:
                return true;
            case VK_F:
                return true;
            case VK_G:
                return true;
            case VK_H:
                return true;
            case VK_I:
                return true;
            case VK_J:
                return true;
            case VK_K:
                return true;
            case VK_L:
                return true;
            case VK_M:
                return true;
            case VK_N:
                return true;
            case VK_O:
                return true;
            case VK_P:
                return true;
            case VK_Q:
                return true;
            case VK_R:
                return true;
            case VK_S:
                return true;
            case VK_T:
                return true;
            case VK_U:
                return true;
            case VK_V:
                return true;
            case VK_W:
                return true;
            case VK_X:
                return true;
            case VK_Y:
                return true;
            case VK_Z:
                return true;
            case VK_SPACE:
                return true;
            case VK_1:
                return true;
            case VK_2:
                return true;
            case VK_3:
                return true;
            case VK_4:
                return true;
            case VK_5:
                return true;
            case VK_6:
                return true;
            case VK_7:
                return true;
            case VK_8:
                return true;
            case VK_9:
                return true;
            case VK_0:
                return true;
            case VK_PERIOD:
                return true;
            case VK_COMMA:
                return true;
            case VK_SLASH:
                return true;
            case VK_DOLLAR:
                return true;
            case VK_MINUS:
                return true;
            default:
                return false;
        }
    }
}
