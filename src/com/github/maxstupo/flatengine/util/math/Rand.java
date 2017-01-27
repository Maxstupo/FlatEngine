package com.github.maxstupo.flatengine.util.math;

import java.util.Random;

/**
 * A more advanced {@link java.util.Random} that adds extra methods. Such as {@link #nextIntRange(int, int)}.
 * 
 * @author Maxstupo
 */
public class Rand extends Random {

    private static final long serialVersionUID = 1L;

    /** A static instance for quick access. */
    public static final Rand INSTANCE = new Rand();

    /**
     * Creates a new {@link Rand} object.
     */
    public Rand() {
        super();
    }

    /**
     * Creates a new {@link Rand} object using a given seed.
     * 
     * @param seed
     *            the seed to use with {@link Random#Random(long)}.
     */
    public Rand(long seed) {
        super(seed);
    }

    /**
     * Returns a random integer between min and max.
     * 
     * @param min
     *            the minimum number.
     * @param max
     *            the maximum number.
     * @return the random number.
     */
    public int nextIntRange(int min, int max) {
        return nextInt((max + 1) - min) + min;
    }

    /**
     * Returns a random float between min and max.
     * 
     * @param min
     *            the minimum number.
     * @param max
     *            the maximum number.
     * @return the random number.
     */
    public float nextFloatRange(float min, float max) {
        return nextFloat() * (max - min) + min;
    }

    /**
     * Returns a random double between min and max.
     * 
     * @param min
     *            the minimum number.
     * @param max
     *            the maximum number.
     * @return the random number.
     */
    public double nextDoubleRange(double min, double max) {
        return nextDouble() * (max - min) + min;
    }

}
