package com.github.maxstupo.flatengine;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 *
 * @author Maxstupo
 */
public class Sprite {

    private final BufferedImage image;
    private final String key;

    public Sprite(Sprite spr) {
        this(spr.image, spr.key);
    }

    public Sprite(Sprite spr, String key) {
        this(spr.image, key);
    }

    public Sprite(BufferedImage image, String key) {
        this.image = image;
        this.key = key;
    }

    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(image, x, y, null);
    }

    public void draw(Graphics2D g, int x, int y, int width, int height) {
        g.drawImage(image, x, y, width, height, null);
    }

    public void draw(Graphics2D g, float x, float y) {
        g.drawImage(image, (int) x, (int) y, null);
    }

    public void draw(Graphics2D g, float x, float y, float width, float height) {
        g.drawImage(image, (int) x, (int) y, (int) width, (int) height, null);
    }

    public void draw(Graphics2D g, int x, int y, double rotation, double rotationX, double rotationY) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.rotate(rotation, rotationX, rotationY);
        draw(gg, x, y);
        gg.dispose();
    }

    public void draw(Graphics2D g, int x, int y, int width, int height, double rotation, double rotationX, double rotationY) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.rotate(rotation, rotationX, rotationY);
        draw(gg, x, y, width, height);
        gg.dispose();
    }

    public Sprite rotate(float angleDegrees) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(), 0);
        tx.rotate(Math.toRadians(angleDegrees), image.getWidth() / 2, image.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage img = op.filter(image, null);
        return new Sprite(img, key);
    }

    /**
     * Returns the width in pixels of this sprite
     */
    public int getWidth() {
        return image.getWidth(null);
    }

    /**
     * Returns the height in pixels of this sprite
     */
    public int getHeight() {
        return image.getHeight(null);
    }

    public String getKey() {
        return key;
    }
}
