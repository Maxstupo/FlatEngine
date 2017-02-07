
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

    /** The default smoothing value. This is set by default. */
    public static final float DEFAULT_SMOOTHING = 0.05f;

    private float viewportWidth;
    private float viewportHeight;
    private float tileSize;

    private final int[][] points = new int[2][2];

    private final Vector2f cameraPosition = new Vector2f();

    private final Vector2i v2i = new Vector2i();
    private final Vector2f v2f = new Vector2f();
    private final Vector2f oldTargetPosition = new Vector2f();

    /**
     * If the distance to target is greater than this value (in tiles), {@link #targetPositionUsingLerp(OrthographicCamera, Vector2, float)} will use
     * {@link #targetPosition(OrthographicCamera, Vector2)} instead of lerp.
     */
    private float lerpMaxDistance = 16;

    private float smoothing = DEFAULT_SMOOTHING;

    /**
     * Create a new {@link Camera} object.
     * 
     * @param tileSize
     *            the size of each tile in pixels.
     */
    public Camera(float tileSize) {
        this.tileSize = tileSize;
    }

    /**
     * Returns the max distance in tiles before {@link #targetPositionUsingLerp(Vector2f, float)} directly targets the position instead of using lerp.
     * 
     * @return the max distance in tiles.
     */
    public float getLerpMaxDistance() {
        return lerpMaxDistance;
    }

    /**
     * Sets the max distance in tiles before {@link #targetPositionUsingLerp(Vector2f, float)} directly targets the position instead of using lerp.
     * 
     * @param lerpMaxDistance
     *            the max distance in tiles.
     */
    public void setLerpMaxDistance(float lerpMaxDistance) {
        this.lerpMaxDistance = lerpMaxDistance;
    }

    /**
     * Targets the camera at the given position using {@link UtilMath#lerpF(float, float, float) lerp}.
     * <p>
     * If the target position is greater than {@link #getLerpMaxDistance()} this method will use {@link #targetPosition(Vector2f)} instead of using
     * lerp.
     * 
     * @param targetPosition
     *            the target position.
     * @param deltaTime
     *            the time span between the current frame and the last frame in seconds.
     */
    public void targetPositionUsingLerp(Vector2f targetPosition, float deltaTime) {
        targetPositionUsingLerp(targetPosition.x, targetPosition.y, deltaTime);
    }

    /**
     * Targets the camera at the given position using {@link UtilMath#lerpF(float, float, float) lerp}.
     * <p>
     * If the target position is greater than {@link #getLerpMaxDistance()} this method will use {@link #targetPosition(Vector2f)} instead of using
     * lerp.
     * 
     * @param x
     *            the target x position.
     * @param y
     *            the target y position.
     * @param deltaTime
     *            the time span between the current frame and the last frame in seconds.
     */
    public void targetPositionUsingLerp(float x, float y, float deltaTime) {

        if (Math.abs(oldTargetPosition.x - x) > lerpMaxDistance || Math.abs(oldTargetPosition.y - y) > lerpMaxDistance) {
            targetPosition(x, y);

        } else {

            // float n = 1f - (float) Math.pow(smoothing, 1f - deltaTime);

            float n = smoothing;

            targetPosition(UtilMath.lerpF(oldTargetPosition.x, x, n), UtilMath.lerpF(oldTargetPosition.y, y, n));
        }

    }

    /**
     * Targets the camera at the given position.
     * 
     * @param targetPosition
     *            the target position, in tiles.
     */
    public void targetPosition(Vector2f targetPosition) {
        targetPosition(targetPosition.x, targetPosition.y);
    }

    /**
     * Targets the camera at the given position.
     * 
     * 
     * @param x
     *            the target x position, in tiles.
     * @param y
     *            the target y position, in tiles.
     */
    public void targetPosition(float x, float y) {

        cameraPosition.x = x - viewportWidth / tileSize / 2f;
        cameraPosition.y = y - viewportHeight / tileSize / 2f;
        oldTargetPosition.set(x, y);

    }

    /**
     * Clamps this camera within the given grid size. This will prevent the camera scrolling of the map.
     * 
     * @param gridWidth
     *            the number of tiles in width.
     * @param gridHeight
     *            the number of tile in height.
     */
    public void clamp(int gridWidth, int gridHeight) {
        cameraPosition.x = UtilMath.clampF(cameraPosition.x, 0, gridWidth - viewportWidth / tileSize);
        cameraPosition.y = UtilMath.clampF(cameraPosition.y, 0, gridHeight - viewportHeight / tileSize);
    }

    /**
     * Sets the viewport size.
     * 
     * @param width
     *            the width in pixels.
     * @param height
     *            the height in pixels.
     */
    public void setViewport(float width, float height) {
        this.viewportWidth = width;
        this.viewportHeight = height;
    }

    /**
     * Sets the viewport size to equal the given game engine window.
     * 
     * @param engine
     *            the game engine.
     */
    public void setViewport(FlatEngine engine) {
        setViewport(engine.getWidth(), engine.getHeight());
    }

    /**
     * Returns the x and y tile ranges for the visible grid tiles in the viewport. Used to render/update the visible tiles only.
     * <p>
     * array[0][0] = minimum x<br>
     * array[0][1] = maximum x<br>
     * <br>
     * array[1][0] = minimum y<br>
     * array[1][1] = maximum y<br>
     * 
     * @param gridWidth
     *            the number of tiles in width.
     * @param gridHeight
     *            the number of tiles in height.
     * @return an array containing x and y tile ranges. The array is cached, and updated upon each call.
     */
    public int[][] getGridPoints(float gridWidth, float gridHeight) {
        points[0][0] = (int) Math.max(cameraPosition.x, 0);
        points[0][1] = (int) Math.min(((viewportWidth / tileSize) + Math.round(cameraPosition.x)) + 1, gridWidth);
        points[1][0] = (int) Math.max(cameraPosition.y, 0);
        points[1][1] = (int) Math.min(((viewportHeight / tileSize) + Math.round(cameraPosition.y)) + 2, gridHeight);
        return points;
    }

    /**
     * Returns the tile position of the viewport mouse cursor position.
     * 
     * @param x
     *            the x mouse cursor position, in pixels.
     * @param y
     *            the y mouse cursor position, in pixels.
     * @return a cached {@link Vector2f} object representing the tile position of the given mouse cursor position.
     */
    public Vector2f getTranslatedMouse(float x, float y) {
        v2f.set(x, y);
        v2f.x = (cameraPosition.x * tileSize + v2i.x) / tileSize;
        v2f.y = (cameraPosition.y * tileSize + v2i.y) / tileSize - 0.5f;
        return v2f;
    }

    /**
     * Returns the draw location for the given tile position.
     * 
     * @param x
     *            the x position, in tiles.
     * @param y
     *            the y position, in tiles.
     * @return a cached {@link Vector2i} object representing a tile position in pixels.
     */
    public Vector2i getRenderLocation(float x, float y) {
        v2i.x = Math.round((x - cameraPosition.x) * tileSize);
        v2i.y = Math.round((y - cameraPosition.y) * tileSize);
        return v2i;
    }

    /**
     * Returns true if the given position is out of the viewport.
     * 
     * @param pos
     *            the position in pixels.
     * @return true if the given position is out of the viewport.
     */
    public boolean isOutOfBounds(Vector2i pos) {
        return isOutOfBounds(pos.x, pos.y);
    }

    /**
     * Returns true if the given position is out of the viewport.
     * 
     * @param x
     *            the x position, in pixels.
     * @param y
     *            the y position, in pixels.
     * @return true if the given position is out of the viewport.
     */
    public boolean isOutOfBounds(float x, float y) {
        return (x < -tileSize || y < -(tileSize * 3f) || x > viewportWidth || y > viewportHeight);
    }

    /**
     * Sets the smoothing value to the given value.
     * 
     * @param smoothing
     *            the value to set.
     */
    public void setSmoothing(float smoothing) {
        this.smoothing = smoothing;
    }

    /**
     * Sets the smoothing to {@link #DEFAULT_SMOOTHING} via {@link #setSmoothing(float)}.
     */
    public void setDefaultSmoothing() {
        setSmoothing(DEFAULT_SMOOTHING);
    }

    /**
     * Returns the smoothing value used by {@link #targetPositionUsingLerp(Vector2f, float)}
     * 
     * @return the smoothing value.
     */
    public float getSmoothing() {
        return smoothing;
    }

    /**
     * Returns the width of the viewport in pixels.
     * 
     * @return the width of the viewport in pixels.
     */
    public float getViewportWidth() {
        return viewportWidth;
    }

    /**
     * Sets the width of the viewport in pixels.
     * 
     * @param viewportWidth
     *            the width in pixels.
     */
    public void setViewportWidth(float viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    /**
     * Returns the height of the viewport in pixels.
     * 
     * @return the height of the viewport in pixels.
     */
    public float getViewportHeight() {
        return viewportHeight;
    }

    /**
     * Sets the height of the viewport in pixels.
     * 
     * @param viewportHeight
     *            the height in pixels.
     */
    public void setViewportHeight(float viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    /**
     * Returns the tile size in pixels.
     * 
     * @return the tile size in pixels.
     */
    public float getTileSize() {
        return tileSize;
    }

    /**
     * Sets the tile size in pixels.
     * 
     * @param tileSize
     *            the tile size in pixels.
     */
    public void setTileSize(float tileSize) {
        this.tileSize = tileSize;
    }

}
