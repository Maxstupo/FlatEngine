package com.github.maxstupo.flatengine.animation;

import com.github.maxstupo.flatengine.AssetManager;
import com.github.maxstupo.flatengine.Sprite;

/**
 * @author Maxstupo
 *
 */
public class AnimationFrame {

    private final String spriteKey;
    private final int duration;

    private int ticks;

    public AnimationFrame(String spriteKey, int duration) {
        this.spriteKey = spriteKey;
        this.duration = duration;
    }

    public boolean update() {
        if (ticks >= duration) {
            ticks = 0;
            return true;
        } else {
            ticks++;
            return false;
        }
    }

    public Sprite getSprite() {
        return AssetManager.get().getSprite(spriteKey);
    }

    public String getSpriteKey() {
        return spriteKey;
    }

    public int getDuration() {
        return duration;
    }

}
