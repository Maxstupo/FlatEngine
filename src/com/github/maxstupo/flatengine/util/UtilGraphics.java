package com.github.maxstupo.flatengine.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.util.math.AbstractBasicShape;
import com.github.maxstupo.flatengine.util.math.Circle;
import com.github.maxstupo.flatengine.util.math.Rectangle;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public final class UtilGraphics {

    private static final Dimension dimension = new Dimension(0, 0);

    private UtilGraphics() {
    }

    /**
     * Returns a list of sub-images from the given image arranged as a grid.
     * 
     * @param tileset
     *            the image.
     * @param tileWidth
     *            the width of each tile in pixels.
     * @param tileHeight
     *            the height of each tile in pixels.
     * @param tileSpacing
     *            the spacing between each tile in pixels.
     * @param tileMargin
     *            the spacing around the outside of all the tiles in pixels.
     * @return a new array of images.
     */
    public static BufferedImage[] getTileImages(BufferedImage tileset, int tileWidth, int tileHeight, int tileSpacing, int tileMargin) {
        int columns = tileset.getWidth() / tileWidth;
        int rows = tileset.getHeight() / tileHeight;

        BufferedImage[] tiles = new BufferedImage[columns * rows];

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < columns; i++) {

                int x = tileMargin + (i * (tileWidth + tileSpacing));
                int y = tileMargin + (j * (tileHeight + tileSpacing));

                BufferedImage tileImage = tileset.getSubimage(x, y, tileWidth, tileHeight);
                tiles[i + j * columns] = tileImage;

            }
        }

        return tiles;
    }

    /**
     * Renders the given shape filled with the given color.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param camera
     *            render to camera scale and location. Set to null to use pixels.
     * @param shape
     *            the shape to render.
     * @param fillColor
     *            the color the shape will be filled with.
     */
    public static void drawShape(Graphics2D g, Camera camera, AbstractBasicShape shape, Color fillColor) {
        if (fillColor != null)
            g.setColor(fillColor);

        if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;

            if (camera != null) {
                Vector2i pos = camera.getRenderLocation(rect.getX(), rect.getY());

                g.fillRect(pos.x, pos.y, (int) (rect.getWidth() * camera.getTileSize()), (int) (rect.getHeight() * camera.getTileSize()));
            } else {

                g.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            }

        } else if (shape instanceof Circle) {

            Circle circle = (Circle) shape;

            if (camera != null) {
                Vector2i pos = camera.getRenderLocation(circle.getX(), circle.getY());

                g.fillOval(pos.x, pos.y, (int) (circle.getDiameter() * camera.getTileSize()), (int) (circle.getDiameter() * camera.getTileSize()));
            } else {

                g.fillOval((int) circle.getX(), (int) circle.getY(), (int) circle.getDiameter(), (int) circle.getDiameter());
            }

        }

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
     * Returns a scaled dimension keeping aspect ratio.
     * 
     * @param originalWidth
     *            the old width.
     * @param originalHeight
     *            the old height.
     * @param newWidth
     *            the width to fit.
     * @param newHeight
     *            the height to fit.
     * @return the new dimension within the newWidth and newHeight.
     */
    public static Dimension getScaledDimension(int originalWidth, int originalHeight, int newWidth, int newHeight) {

        double ratio = (double) originalWidth / originalHeight;

        double height = newWidth / ratio;
        double width = newHeight * ratio;

        if (width > newWidth) {
            dimension.setSize(newWidth, (int) height);
        } else {
            dimension.setSize((int) width, newHeight);
        }
        return dimension;
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
