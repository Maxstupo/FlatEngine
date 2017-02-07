
package com.github.maxstupo.flatengine.map;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2f;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class Camera {

    private float viewportWidth;
    private float viewportHeight;
    private float tileSize;

    private final Vector2f position = new Vector2f();

    private final int[][] points = new int[2][2];

    private final Vector2i v2i = new Vector2i();
    private final Vector2f v2f = new Vector2f();

    public Camera(float tileSize) {
        this.tileSize = tileSize;
    }

    public void updatePosition(float x, float y) {
        this.position.x = x - viewportWidth / tileSize / 2;
        this.position.y = y - viewportHeight / tileSize / 2;
        this.position.x = UtilMath.clampF(position.x, 0, 1000000);
        this.position.y = UtilMath.clampF(position.y, 0, 1000000);
    }

    public void setViewport(float width, float height) {
        this.viewportWidth = width;
        this.viewportHeight = height;
    }

    public void setViewport(FlatEngine engine) {
        setViewport(engine.getWidth(), engine.getHeight());
    }

    public int[][] getGridPoints(float gridWidth, float gridHeight) {
        points[0][0] = (int) Math.max(position.x, 0);
        points[0][1] = (int) Math.min(((viewportWidth / tileSize) + Math.round(position.x)) + 1, gridWidth);
        points[1][0] = (int) Math.max(position.y, 0);
        points[1][1] = (int) Math.min(((viewportHeight / tileSize) + Math.round(position.y)) + 2, gridHeight);
        return points;
    }

    public Vector2f getTranslatedMouse(float x, float y) {
        v2f.set(x, y);
        v2f.x = (position.x * tileSize + v2i.x) / tileSize;
        v2f.y = (position.y * tileSize + v2i.y) / tileSize - 0.5f;
        return v2f;
    }

    public Vector2i getRenderLocation(float x, float y) {
        v2i.x = Math.round((x - position.x) * tileSize);
        v2i.y = Math.round((y - position.y) * tileSize);
        return v2i;
    }

    public boolean isOutOfBounds(Vector2i pos) {
        return isOutOfBounds(pos.x, pos.y);
    }

    public boolean isOutOfBounds(float x, float y) {
        return (x < -tileSize || y < -(tileSize * 3f) || x > viewportWidth || y > viewportHeight);
    }

    public float getViewportWidth() {
        return viewportWidth;
    }

    public void setViewportWidth(float viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public float getViewportHeight() {
        return viewportHeight;
    }

    public void setViewportHeight(float viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    public float getTileSize() {
        return tileSize;
    }

    public void setTileSize(float tileSize) {
        this.tileSize = tileSize;
    }

}
