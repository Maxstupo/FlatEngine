package com.github.maxstupo.flatengine.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * @author Maxstupo
 *
 */
public final class Util {

    private Util() {
    }

    public static BufferedImage createImage(String path) throws IOException {
        Image img = null;
        try (InputStream file = Util.class.getResourceAsStream(path)) {
            if (file == null)
                throw new FileNotFoundException("Failed to find image:" + path);

            img = ImageIO.read(file);
        }

        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(img, 0, 0, null);

        return bufferedImage;
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
}