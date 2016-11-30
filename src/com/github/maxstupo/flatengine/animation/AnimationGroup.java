package com.github.maxstupo.flatengine.animation;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maxstupo
 */
public class AnimationGroup {

    private Map<String, SpriteAnimation> animations = new HashMap<>();

    public void addAnimation(String id, SpriteAnimation ani) {
        animations.put(id, ani);
    }

    public SpriteAnimation getAnimation(String currentAnimation) {
        SpriteAnimation ani = animations.get(currentAnimation);
        if (ani == null)
            return null;
        return ani.create();
    }

}
