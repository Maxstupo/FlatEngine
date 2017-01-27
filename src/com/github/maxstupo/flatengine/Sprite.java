package com.github.maxstupo.flatengine;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * This class represents a image linked with an id.
 * 
 * @author Maxstupo
 */
public class Sprite {

    private final BufferedImage image;
    private final String id;

    /**
     * Create a new {@link Sprite} object.
     * 
     * @param image
     *            the image of this sprite.
     * @param id
     *            the id of this sprite.
     */
    public Sprite(BufferedImage image, String id) {
        this.image = image;
        this.id = id;
    }

    /**
     * Draw this sprite at the given x,y position.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     */
    public void draw(Graphics2D g, float x, float y) {
        g.drawImage(image, (int) x, (int) y, null);
    }

    /**
     * Draw this sprite at the given x,y position with the size of width and height.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     * @param width
     *            the width to draw this sprite.
     * @param height
     *            the height to draw this sprite.
     */
    public void draw(Graphics2D g, float x, float y, float width, float height) {
        g.drawImage(image, (int) x, (int) y, (int) width, (int) height, null);
    }

    /**
     * Draw this sprite at the given x,y position rotated by rotation at the origin of rotationX,rotationY.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     * @param rotation
     *            the rotation in radians.
     * @param rotationX
     *            the x origin for rotation.
     * @param rotationY
     *            the y origin for rotation.
     */
    public void draw(Graphics2D g, float x, float y, double rotation, double rotationX, double rotationY) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.rotate(rotation, rotationX, rotationY);
        draw(gg, x, y);
        gg.dispose();
    }

    /**
     * Draw this sprite at the given x,y position with the size of width,height rotated by rotation at the origin of rotationX,rotationY.
     * 
     * @param g
     *            the graphics context to draw to.
     * @param x
     *            the x position.
     * @param y
     *            the y position.
     * @param width
     *            the width to draw this sprite.
     * @param height
     *            the height to draw this sprite.
     * @param rotation
     *            the rotation in radians.
     * @param rotationX
     *            the x origin for rotation.
     * @param rotationY
     *            the y origin for rotation.
     */
    public void draw(Graphics2D g, float x, float y, float width, float height, double rotation, double rotationX, double rotationY) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.rotate(rotation, rotationX, rotationY);
        draw(gg, x, y, width, height);
        gg.dispose();
    }

    /**
     * Returns a new sprite object with a rotated image.
     * 
     * @param angleDegrees
     *            the angle in degrees.
     * @return a new sprite.
     * @deprecated Creates a new sprite object which makes calling this method bad for a game engine.
     */
    @Deprecated
    public Sprite rotate(float angleDegrees) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(), 0);
        tx.rotate(Math.toRadians(angleDegrees), image.getWidth() / 2, image.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage img = op.filter(image, null);
        return new Sprite(img, id);
    }

    /**
     * Returns the width in pixels of this sprite.
     * 
     * @return the width in pixels of this sprite.
     */
    public int getWidth() {
        return image.getWidth(null);
    }

    /**
     * Returns the height in pixels of this sprite
     * 
     * @return the height in pixels of this sprite.
     */
    public int getHeight() {
        return image.getHeight(null);
    }

    /**
     * Returns the id of this sprite.
     * 
     * @return the id of this sprite.
     */
    public String getId() {
        return id;
    }
}
