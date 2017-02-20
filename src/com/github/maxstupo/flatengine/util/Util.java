package com.github.maxstupo.flatengine.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.github.maxstupo.flatengine.util.math.UtilMath;

/**
 * This class contains general util methods that dont fit in {@link UtilGraphics}, {@link UtilXML} or {@link UtilMath}.
 * 
 * @author Maxstupo
 */
public final class Util {

    private Util() {
    }

    /**
     * Create a new path object.
     * 
     * @param parent
     *            the parent path.
     * @param src
     *            the path.
     * @return the new path.
     */
    public static Path path(String parent, String src) {
        Path parentPath = Paths.get(parent).getParent().normalize();
        return Paths.get(parentPath.toString(), src).normalize();
    }

    /**
     * Returns a {@link BufferedImage} using {@link Class#getResourceAsStream(String)}
     * 
     * @param file
     *            the path to the image.
     * @param transparentColor
     *            the color that will be transparent.
     * @return the {@link BufferedImage} of the given path.
     * @throws IOException
     *             if an error occurs.
     */
    public static BufferedImage loadImage(String file, Color transparentColor) throws IOException {

        try (InputStream is = Util.class.getClassLoader().getResourceAsStream(file)) {
            Image img = ImageIO.read(is);
            // TODO: Implement transparent color.

            BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            bufferedImage.getGraphics().drawImage(img, 0, 0, null);

            return bufferedImage;
        }
    }

    /**
     * Returns true if <code>i</code> is a valid array index for the given 1D array.
     * 
     * @param array
     *            The array to compare the index with.
     * @param i
     *            The index value to compare.
     * @return true if <code>i</code> is a valid index.
     */
    public static <T> boolean isValid(T[] array, int i) {
        if (array == null)
            return false;
        return i >= 0 && i < array.length;
    }

    /**
     * Returns true if <code>i,j</code> are valid array indexes for the given 2D array.
     * 
     * @param array
     *            The array to compare the index with.
     * @param i
     *            The index value to compare.
     * @param j
     *            The index value to compare.
     * @return true if <code>i,j</code> are valid indexes.
     */
    public static <T> boolean isValid(T[][] array, int i, int j) {
        if (array == null)
            return false;
        return i >= 0 && i < array.length && j >= 0 && j < array[0].length;
    }

    /**
     * Returns true if <code>i,j</code> are valid array indexes for the given 2D array.
     * 
     * @param array
     *            The array to compare the index with.
     * @param i
     *            The index value to compare.
     * @param j
     *            The index value to compare.
     * @return true if <code>i,j</code> are valid indexes.
     */
    public static boolean isValid(int[][] array, int i, int j) {
        if (array == null)
            return false;
        return i >= 0 && i < array.length && j >= 0 && j < array[0].length;
    }

    /**
     * Returns a new color representing the given hex value. If the given string is null or empty, null will be returned.
     * 
     * @param hex
     *            the hex value representing the new color.
     * @return a new color representing the given hex value.
     */
    public static Color hexToColor(String hex) {
        if (hex == null || hex.isEmpty())
            return null;

        if (hex.startsWith("#"))
            hex = hex.substring(1);

        int colorInt = Integer.parseInt(hex, 16);
        return new Color(colorInt);
    }
}