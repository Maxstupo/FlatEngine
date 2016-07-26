package com.github.maxstupo.flatengine.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 *
 * @author Maxstupo
 */
public final class UtilGraphics {

    private UtilGraphics() {
    }

    /**
     * Returns a new Color object with a changed alpha value.
     * 
     * @param c
     *            The color to copy.
     * @param alpha
     *            The new alpha.
     * @return A new color object containing the new alpha value.
     */
    public static Color changeAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public static Dimension getStringBounds(Graphics g, String text) {
        return getStringBounds(g, text, g.getFont());
    }

    public static Dimension getStringBounds(Graphics g, String text, Font font) {
        if (text == null)
            return new Dimension();
        FontMetrics metrics = g.getFontMetrics(font);
        int height = metrics.getHeight();
        int adv = metrics.stringWidth(text);

        return new Dimension(adv + 2, height + 2);
    }

    public static void drawString(Graphics g, int x, int y, int spacing, String... lines) {
        for (int i = 0; i < lines.length; i++) {
            Dimension lineBounds = getStringBounds(g, lines[i]);

            int lx = x;
            int ly = y + (lineBounds.height + spacing) * i;

            drawString(g, lines[i], lx, ly);
        }

    }

    public static void drawString(Graphics g, String text, int x, int y) {
        if (text == null)
            return;
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(text, x, y + metrics.getAscent());
    }

}
