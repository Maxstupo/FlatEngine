package com.github.maxstupo.flatengine.util.math;

import java.util.Random;

/**
 * A more advanced {@link java.util.Random} that adds extra methods. Such as {@link #nextIntRange(int, int)}.
 * 
 * @author Maxstupo
 */
public class Rand extends Random {

    private static final long serialVersionUID = 1L;

    public static final Rand instance = new Rand();

    public Rand() {
    }

    public Rand(long seed) {
        super(seed);
    }

    public int nextIntRange(int min, int max) {
        return nextInt((max + 1) - min) + min;
    }

    public float nextFloatRange(float min, float max) {
        return nextFloat() * (max - min) + min;
    }

    public double nextDoubleRange(double min, double max) {
        return nextDouble() * (max - min) + min;
    }

}
