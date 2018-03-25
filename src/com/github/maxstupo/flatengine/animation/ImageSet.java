package com.github.maxstupo.flatengine.animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.UtilGraphics;

/**
 * This class splits a {@link BufferedImage} object into {@link Sprite}s.
 * 
 * @author Maxstupo
 */
public class ImageSet {

    private final String id;
    private final int frameCols; // x
    private final int frameRows; // y

    private final Sprite[] regions;

    /**
     * Creates a new {@link ImageSet} object.
     * 
     * @param id
     *            the id of this {@link ImageSet}.
     * @param tileset
     *            the image to split.
     * @param tileWidth
     *            the width of the tiles in the texture.
     * @param tileHeight
     *            the height of the tiles in the texture.
     * @param tileSpacing
     *            the spacing between each tile in pixels.
     * @param tileMargin
     *            the spacing around the outside of all the tiles in pixels.
     */
    public ImageSet(String id, BufferedImage tileset, int tileWidth, int tileHeight, int tileSpacing, int tileMargin) {
        this.id = id;
        this.frameCols = tileset.getWidth() / tileWidth;
        this.frameRows = tileset.getHeight() / tileHeight;
        this.regions = new Sprite[frameCols * frameRows];

        BufferedImage[] imgs = UtilGraphics.getTileImages(tileset, tileWidth, tileHeight, tileSpacing, tileMargin);

        for (int i = 0; i < imgs.length; i++)
            regions[i] = new Sprite(imgs[i], id + "_" + i);
    }

    /**
     * Returns a list of {@link Sprite}s between the specified start and end indexes.
     * 
     * @param start
     *            the starting index.
     * @param end
     *            the ending index.
     * @return a list of texture regions.
     */
    public List<Sprite> getRegions(int start, int end) {
        List<Sprite> list = new ArrayList<>();

        for (int i = start; i <= end; i++) {
            Sprite sprite = getSprite(i);
            if (sprite == null)
                continue;
            list.add(sprite);
        }
        return list;
    }

    /**
     * Returns the sprite at the specified index location, or null if the specified index is out of bounds.
     * 
     * @param index
     *            the regions index location.
     * @return the sprite at the specified index location, or null if the specified index is out of bounds.
     */
    public Sprite getSprite(int index) {
        if (index < 0 || index >= regions.length)
            return null;
        return regions[index];
    }

    /**
     * Returns the sprite at the specified x,y location, or null if the specified index is out of bounds.
     * 
     * @param i
     *            the index along the x-axis.
     * @param j
     *            the index along the y-axis.
     * @return the sprite at the specified index location, or null if the specified index is out of bounds.
     */
    public Sprite getSprite(int i, int j) {
        return getSprite(j * frameRows + i);
    }

    @Override
    public String toString() {
        return String.format("%s [id=%s, frameCols=%s, frameRows=%s]", getClass().getSimpleName(), id, frameCols, frameRows);
    }

    /**
     * Returns the {@link ImageSet} id.
     * 
     * @return the imageset id.
     */
    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + frameCols;
        result = prime * result + frameRows;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImageSet other = (ImageSet) obj;
        if (frameCols != other.frameCols)
            return false;
        if (frameRows != other.frameRows)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
