package com.github.maxstupo.flatengine.animation;

import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.Sprite;
import com.github.maxstupo.flatengine.util.math.UtilMath;

/**
 * @author Maxstupo
 *
 */
public class SpriteAnimation {

    private final List<AnimationFrame> frames = new ArrayList<>();
    private int index = 0;

    public SpriteAnimation(List<AnimationFrame> frames) {
        this(frames, 0);
    }

    public SpriteAnimation(List<AnimationFrame> frames, int index) {
        this.frames.addAll(frames);
        this.index = index;
    }

    public SpriteAnimation create() {
        return new SpriteAnimation(frames, index);
    }

    public void update() {
        if (frames.get(index).update())
            index = ++index % frames.size();
    }

    public void reset() {
        index = 0;
    }

    public void set(int i) {
        index = UtilMath.clampI(i, 0, frames.size() - 1);
    }

    public Sprite getCurrentSprite() {
        return frames.get(index).getSprite();
    }
}
