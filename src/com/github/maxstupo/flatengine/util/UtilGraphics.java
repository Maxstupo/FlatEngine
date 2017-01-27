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

    /**
     * Returns the {@link Dimension} of the given string using the current font of the graphics context.
     * 
     * @param g
     *            the graphics context.
     * @param text
     *            the text to check.
     * @return a new {@link Dimension} object containing the width and height of the given string.
     */
    public static Dimension getStringBounds(Graphics g, String text) {
        return getStringBounds(g, text, null);
    }

    /**
     * Returns the {@link Dimension} of the given string using the given font.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param text
     *            the text to check.
     * @param font
     *            the font to use when doing the check, if null the current font of the graphics context will be used instead.
     * @return a new {@link Dimension} object containing the width and height of the given string.
     */
    public static Dimension getStringBounds(Graphics g, String text, Font font) {
        if (text == null)
            return new Dimension();
        FontMetrics metrics = (font != null) ? g.getFontMetrics(font) : g.getFontMetrics();
        int height = metrics.getHeight();
        int adv = metrics.stringWidth(text);

        return new Dimension(adv + 2, height + 2);
    }

    /**
     * Draws each element from the given string array below one another.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     * @param spacing
     *            the spacing between each line.
     * @param lines
     *            the array of each line of text.
     */
    public static void drawString(Graphics g, int x, int y, int spacing, String... lines) {
        for (int i = 0; i < lines.length; i++) {
            Dimension lineBounds = getStringBounds(g, lines[i]);

            int lx = x;
            int ly = y + (lineBounds.height + spacing) * i;

            drawString(g, lines[i], lx, ly);
        }

    }

    /**
     * Draw a string with the origin being the top-left of the string.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param text
     *            the text to draw.
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     */
    public static void drawString(Graphics g, String text, int x, int y) {
        if (text == null)
            return;
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(text, x, y + metrics.getAscent());
    }

}
