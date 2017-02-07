package com.github.maxstupo.flatengine.util.math;

/**
 * This class contains common math util methods.
 * 
 * @author Maxstupo
 */
public final class UtilMath {

    private UtilMath() {

    }

    /**
     * Returns the percentage of n.
     * 
     * @param n
     *            the value.
     * @param total
     *            the total the value can be.
     * @return a percentage.
     */
    public static int percentageI(int n, int total) {
        return n * 100 / total;
    }

    /**
     * Checks if two floats are equal using an epsilon of 0.0000001.
     * 
     * @param n1
     *            the first number.
     * @param n2
     *            the second number.
     * @return true if the difference of the two numbers are less than 0.0000001.
     */
    public static boolean equals(float n1, float n2) {
        return equals(n1, n2, 0.0000001f);
    }

    /**
     * Checks if two floats are equal using an epsilon.
     * 
     * @param n1
     *            the first number.
     * @param n2
     *            the second number.
     * @param epsilon
     *            the epsilon.
     * @return true if the difference of the two numbers are less than the epsilon.
     */
    public static boolean equals(float n1, float n2, float epsilon) {
        return Math.abs(n1 - n2) < epsilon;
    }

    /**
     * Returns the percentage of n.
     * 
     * @param n
     *            the value.
     * @param total
     *            the total the value can be.
     * @return a percentage.
     */
    public static float percentageF(float n, float total) {
        return n * 100f / total;
    }

    /**
     * Returns the percentage of n.
     * 
     * @param n
     *            the value.
     * @param total
     *            the total the value can be.
     * @return a percentage.
     */
    public static double percentageD(double n, double total) {
        return n * 100.0 / total;
    }

    /**
     * Returns a scaled version of n.
     * 
     * @param n
     *            the value to scale.
     * @param oldMax
     *            the old max of n.
     * @param newMax
     *            the new max of n.
     * @return a scaled version of n.
     */
    public static int scaleI(int n, int oldMax, int newMax) {
        return (n / oldMax) * newMax;
    }

    /**
     * Returns a scaled version of n.
     * 
     * @param n
     *            the value to scale.
     * @param oldMax
     *            the old max of n.
     * @param newMax
     *            the new max of n.
     * @return a scaled version of n.
     */
    public static float scaleF(float n, float oldMax, float newMax) {
        return (n / oldMax) * newMax;
    }

    /**
     * Returns a scaled version of n.
     * 
     * @param n
     *            the value to scale.
     * @param oldMax
     *            the old max of n.
     * @param newMax
     *            the new max of n.
     * @return a scaled version of n.
     */
    public static double scaleD(double n, double oldMax, double newMax) {
        return (n / oldMax) * newMax;
    }

    /**
     * Returns the value if it's between min and max, else return min or max.
     * 
     * @param n
     *            the value to clamp.
     * @param min
     *            the minimum the given number can be.
     * @param max
     *            the maximum the given number can be.
     * @return the value if it's between min and max, else return min or max.
     */
    public static int clampI(int n, int min, int max) {
        return Math.max(Math.min(n, max), min);
    }

    /**
     * Returns the value if it's between min and max, else return min or max.
     * 
     * @param n
     *            the value to clamp.
     * @param min
     *            the minimum the given number can be.
     * @param max
     *            the maximum the given number can be.
     * @return the value if it's between min and max, else return min or max.
     */
    public static float clampF(float n, float min, float max) {
        return Math.max(Math.min(n, max), min);
    }

    /**
     * Returns the value if it's between min and max, else return min or max.
     * 
     * @param n
     *            the value to clamp.
     * @param min
     *            the minimum the given number can be.
     * @param max
     *            the maximum the given number can be.
     * @return the value if it's between min and max, else return min or max.
     */
    public static double clampD(double n, double min, double max) {
        return Math.max(Math.min(n, max), min);
    }

    /**
     * Returns a value between a and b lerped by f.
     * 
     * @param a
     *            the first value.
     * @param b
     *            the second value.
     * @param f
     *            lerp amount.
     * @return a value between a and b lerped by f.
     */
    public static int lerpI(int a, int b, int f) {
        return (a * (1 - f)) + (b * f);
    }

    /**
     * Returns a value between fromValue and toValue.
     * 
     * @param fromValue
     *            the first value.
     * @param toValue
     *            the second value.
     * @param progress
     *            lerp amount.
     * @return a value between fromValue and toValue.
     */
    public static float lerpF(float fromValue, float toValue, float progress) {
        return fromValue + (toValue - fromValue) * progress;
    }

    /**
     * Returns a value between a and b lerped by f.
     * 
     * @param a
     *            the first value.
     * @param b
     *            the second value.
     * @param f
     *            lerp amount.
     * @return a value between a and b lerped by f.
     */
    public static double lerpD(double a, double b, double f) {
        return (a * (1.0 - f)) + (b * f);
    }

    /**
     * Returns true if the given string can be parsed as an integer.
     * 
     * @param i
     *            the string to check.
     * @return true if the given string can be parsed as an integer.
     */
    public static boolean isInt(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the given string can be parsed as an long.
     * 
     * @param i
     *            the string to check.
     * @return true if the given string can be parsed as an long.
     */
    public static boolean isLong(String i) {
        try {
            Long.parseLong(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the given string can be parsed as an float.
     * 
     * @param i
     *            the string to check.
     * @return true if the given string can be parsed as an float.
     */
    public static boolean isFloat(String i) {
        try {
            Float.parseFloat(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the given string can be parsed as an double.
     * 
     * @param i
     *            the string to check.
     * @return true if the given string can be parsed as an double.
     */
    public static boolean isDouble(String i) {
        try {
            Double.parseDouble(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns a integer of the given string, else 0.
     * 
     * @param i
     *            the string to parse.
     * @return a integer of the given string, else 0.
     */
    public static int toInt(String i) {
        return toInt(i, 0);
    }

    /**
     * Returns a long of the given string, else 0.
     * 
     * @param i
     *            the string to parse.
     * @return a long of the given string, else 0.
     */
    public static long toLong(String i) {
        return toLong(i, 0L);
    }

    /**
     * Returns a float of the given string, else 0.
     * 
     * @param i
     *            the string to parse.
     * @return a float of the given string, else 0.
     */
    public static float toFloat(String i) {
        return toFloat(i, 0f);
    }

    /**
     * Returns a double of the given string, else 0.
     * 
     * @param i
     *            the string to parse.
     * @return a double of the given string, else 0.
     */
    public static double toDouble(String i) {
        return toDouble(i, 0D);
    }

    /**
     * Returns a boolean of the given string, else false.
     * 
     * @param i
     *            the string to parse.
     * @return a boolean of the given string, else false.
     */
    public static boolean toBoolean(String i) {
        return toBoolean(i, false);
    }

    /**
     * Returns a integer of the given string, else the defaultValue.
     * 
     * @param i
     *            the string to parse.
     * @param defaultValue
     *            the value returned if the given string isn't a integer.
     * @return a integer of the given string, else the defaultValue.
     */
    public static int toInt(String i, int defaultValue) {
        try {
            return Integer.parseInt(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Returns a long of the given string, else the defaultValue.
     * 
     * @param i
     *            the string to parse.
     * @param defaultValue
     *            the value returned if the given string isn't a long.
     * @return a long of the given string, else the defaultValue.
     */
    public static long toLong(String i, long defaultValue) {
        try {
            return Long.parseLong(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Returns a float of the given string, else the defaultValue.
     * 
     * @param i
     *            the string to parse.
     * @param defaultValue
     *            the value returned if the given string isn't a float.
     * @return a float of the given string, else the defaultValue.
     */
    public static float toFloat(String i, float defaultValue) {
        try {
            return Float.parseFloat(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Returns a double of the given string, else the defaultValue.
     * 
     * @param i
     *            the string to parse.
     * @param defaultValue
     *            the value returned if the given string isn't a double.
     * @return a double of the given string, else the defaultValue.
     */
    public static double toDouble(String i, double defaultValue) {
        try {
            return Double.parseDouble(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Returns a boolean of the given string, else the defaultValue.
     * 
     * @param i
     *            the string to parse.
     * @param defaultValue
     *            the value returned if the given string isn't a boolean.
     * @return a boolean of the given string, else the defaultValue.
     */
    public static boolean toBoolean(String i, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
