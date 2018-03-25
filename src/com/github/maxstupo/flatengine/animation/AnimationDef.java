package com.github.maxstupo.flatengine.animation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.math.Vector2f;

/**
 * This class defines an animation, a set of key frames and the image set used.
 * 
 * @author Maxstupo
 */
public class AnimationDef {

    private final String imageSet;
    private final float frameDuration;

    private final List<Integer> frames = new ArrayList<Integer>();
    private final List<Vector2f> offsets = new ArrayList<>();
    private final List<Vector2f> origins = new ArrayList<>();
    private final PlayMode playMode;

    private AnimationDef(String imageSet, PlayMode mode, float frameDuration) {
        this.imageSet = imageSet;
        this.playMode = mode;
        this.frameDuration = frameDuration;
    }

    /**
     * Returns a new animation object, representing this {@link AnimationDef}.
     * 
     * @param imageSets
     *            the image set store, containing the image set this {@link AnimationDef} uses.
     * @return a new animation object, representing this {@link AnimationDef}.
     */
    public Animation createAnimation(ImageSetStore imageSets) {
        List<Sprite> keyFrames = getKeyFrames(imageSets);
        if (keyFrames == null)
            return null;

        Animation animation = new Animation(frameDuration, keyFrames);
        animation.setPlayMode(playMode);
        return animation;
    }

    /**
     * Returns a list of texture regions representing this animation.
     * 
     * @param imageSets
     *            the image set store, containing the image set this {@link AnimationDef} uses.
     * @return a list of texture regions representing this animation.
     */
    public List<Sprite> getKeyFrames(ImageSetStore imageSets) {
        ImageSet imageSet = imageSets.get(getImageSetName());
        if (imageSet == null)
            return null;

        List<Sprite> keyFrames = new ArrayList<>();
        for (int index : getImageSetIndexes()) {
            Sprite region = imageSet.getSprite(index);
            keyFrames.add(region);
        }
        return keyFrames;
    }

    /**
     * Adds frame to the end of the animation.
     * 
     * @param index
     *            the image set index.
     * @param offsetX
     *            the x offset.
     * @param offsetY
     *            the y offset.
     * @param originX
     *            the x origin.
     * @param originY
     *            the y origin.
     * 
     * @return this object for chaining.
     */
    public AnimationDef frame(int index, float offsetX, float offsetY, float originX, float originY) {
        frames.add(index);
        offsets.add(new Vector2f(offsetX, offsetY));
        origins.add(new Vector2f(originX, originY));
        return this;
    }

    /**
     * Adds a list of frames to the end of the animation.
     * 
     * @param offsetX
     *            the x offset.
     * @param offsetY
     *            the y offset.
     * @param originX
     *            the x origin.
     * @param originY
     *            the y origin.
     * @param index
     *            an array of image set indexes.
     * @return this object for chaining.
     */
    public AnimationDef frames(float offsetX, float offsetY, float originX, float originY, int... index) {
        for (int i : index)
            frame(i, offsetX, offsetY, originX, originY);
        return this;
    }

    /**
     * Creates a new {@link AnimationDef} with a single frame.
     * 
     * @param imageSet
     *            the image set id containing the specified frame.
     * @param index
     *            the image set index.
     * @param offsetX
     *            the x offset.
     * @param offsetY
     *            the y offset.
     * @param originX
     *            the x origin.
     * @param originY
     *            the y origin.
     * @return a new {@link AnimationDef} with a single frame.
     */
    public static AnimationDef staticFrame(String imageSet, int index, float offsetX, float offsetY, float originX, float originY) {
        return AnimationDef.create(imageSet, 1.0f).frame(index, offsetX, offsetY, originX, originY);
    }

    /**
     * Creates a new looping {@link AnimationDef}, and specifies an animation using the frames between the specified start and end indexes.
     * 
     * @param imageSet
     *            the image set id containing the specified frames textures.
     * @param frameDuration
     *            the duration in seconds between each frame.
     * @param start
     *            the starting index for the animation.
     * @param end
     *            the ending index for the animation.
     * @param offsetX
     *            the x offset.
     * @param offsetY
     *            the y offset.
     * @param originX
     *            the x origin.
     * @param originY
     *            the y origin.
     * @return a new {@link AnimationDef}, with frames between the specified start and end indexes.
     */
    public static AnimationDef sequence(String imageSet, float frameDuration, int start, int end, float offsetX, float offsetY, float originX, float originY) {
        return AnimationDef.sequence(imageSet, PlayMode.LOOP, frameDuration, start, end, offsetX, offsetY, originX, originY);
    }

    /**
     * Creates a new {@link AnimationDef}, and specifies an animation using the frames between the specified start and end indexes.
     * 
     * @param imageSet
     *            the image set id containing the specified frames textures.
     * @param mode
     *            the play back mode for the animation.
     * @param frameDuration
     *            the duration in seconds between each frame.
     * @param start
     *            the starting index for the animation.
     * @param end
     *            the ending index for the animation.
     * @param offsetX
     *            the x offset.
     * @param offsetY
     *            the y offset.
     * @param originX
     *            the x origin.
     * @param originY
     *            the y origin.
     * @return a new {@link AnimationDef}, with frames between the specified start and end indexes.
     */
    public static AnimationDef sequence(String imageSet, PlayMode mode, float frameDuration, int start, int end, float offsetX, float offsetY, float originX, float originY) {
        AnimationDef def = AnimationDef.create(imageSet, mode, frameDuration);
        for (int i = start; i <= end; i++)
            def.frame(i, offsetX, offsetY, originX, originY);
        return def;
    }

    /**
     * Creates a new empty looping {@link AnimationDef}.
     * 
     * @param imageSet
     *            the image set id containing the specified frames textures.
     * @param frameDuration
     *            the duration in seconds between each frame.
     * @return a new empty {@link AnimationDef}.
     */
    public static AnimationDef create(String imageSet, float frameDuration) {
        return new AnimationDef(imageSet, PlayMode.LOOP, frameDuration);
    }

    /**
     * Creates a new empty {@link AnimationDef}.
     * 
     * @param imageSet
     *            the image set id containing the specified frame textures.
     * @param mode
     *            the play back mode for the animation.
     * @param frameDuration
     *            the duration in seconds between each frame.
     * @return a new empty {@link AnimationDef}.
     */
    public static AnimationDef create(String imageSet, PlayMode mode, float frameDuration) {
        return new AnimationDef(imageSet, mode, frameDuration);
    }

    @Override
    public String toString() {
        return String.format("%s [imageSet=%s, frameDuration=%s, frames=%s]", getClass().getSimpleName(), imageSet, frameDuration, frames);
    }

    /**
     * Returns an unmodifiable list of image set indexes for each key frame.
     * 
     * @return an unmodifiable list of image set indexes for each key frame.
     * @see Collections#unmodifiableList(List)
     */
    public List<Integer> getImageSetIndexes() {
        return Collections.unmodifiableList(frames);
    }

    /**
     * Returns the image set index for the specific key frame index.
     * 
     * @param keyFrameIndex
     *            the key frame index.
     * @return the image set index for the specific key frame index.
     */
    public int getImageSetIndex(int keyFrameIndex) {
        return frames.get(keyFrameIndex);
    }

    /**
     * Returns the position offset for the specific key frame.
     * 
     * @param keyFrameIndex
     *            the key frame index.
     * @return the specific key frame offset.
     */
    public Vector2f getOffsetForFrame(int keyFrameIndex) {
        return offsets.get(keyFrameIndex);
    }

    /**
     * Returns the origin for the specific key frame.
     * 
     * @param keyFrameIndex
     *            the key frame index.
     * @return the specific key frame offset.
     */
    public Vector2f getOriginForFrame(int keyFrameIndex) {
        return origins.get(keyFrameIndex);
    }

    /**
     * Returns the image set name this animation will use for each of the frame textures.
     * 
     * @return the image set name.
     */
    public String getImageSetName() {
        return imageSet;
    }

    /**
     * Returns the duration between each frame for this {@link AnimationDef}, in seconds.
     * 
     * @return the duration of each frame.
     */
    public float getFrameDuration() {
        return frameDuration;
    }

    /**
     * Returns the play mode of this {@link AnimationDef}.
     * 
     * @return the play mode of this animation def.
     */
    public PlayMode getPlayMode() {
        return playMode;
    }

}
