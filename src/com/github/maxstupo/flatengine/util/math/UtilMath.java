package com.github.maxstupo.flatengine.util.math;

/**
 *
 * @author Maxstupo
 */
public final class UtilMath {

    private UtilMath() {

    }

    public static int percentageI(int n, int total) {
        return n * 100 / total;
    }

    public static float percentageF(float n, float total) {
        return n * 100f / total;
    }

    public static double percentageD(double n, double total) {
        return n * 100.0 / total;
    }

    public static int scaleI(int n, int oldMax, int newMax) {
        return (n / oldMax) * newMax;
    }

    public static float scaleF(float n, float oldMax, float newMax) {
        return (n / oldMax) * newMax;
    }

    public static double scaleD(double n, double oldMax, double newMax) {
        return (n / oldMax) * newMax;
    }

    public static int clampI(int n, int min, int max) {
        return Math.max(Math.min(n, max), min);
    }

    public static float clampF(float n, float min, float max) {
        return Math.max(Math.min(n, max), min);
    }

    public static double clampD(double n, double min, double max) {
        return Math.max(Math.min(n, max), min);
    }

    public static int lerpI(int a, int b, int f) {
        return (a * (1 - f)) + (b * f);
    }

    public static float lerpF(float a, float b, float f) {
        return (a * (1.0f - f)) + (b * f);
    }

    public static double lerpD(double a, double b, double f) {
        return (a * (1.0 - f)) + (b * f);
    }

    public static boolean isInt(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isLong(String i) {
        try {
            Long.parseLong(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isFloat(String i) {
        try {
            Float.parseFloat(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDouble(String i) {
        try {
            Double.parseDouble(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int toInt(String i) {
        return toInt(i, 0);
    }

    public static int toInt(String i, int defaultValue) {
        try {
            return Integer.parseInt(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long toLong(String i) {
        return toLong(i, 0L);
    }

    public static long toLong(String i, long defaultValue) {
        try {
            return Long.parseLong(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static float toFloat(String i) {
        return toFloat(i, 0f);
    }

    public static float toFloat(String i, float defaultValue) {
        try {
            return Float.parseFloat(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static double toDouble(String i) {
        return toDouble(i, 0D);
    }

    public static double toDouble(String i, double defaultValue) {
        try {
            return Double.parseDouble(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean toBoolean(String i) {
        return toBoolean(i, false);
    }

    public static boolean toBoolean(String i, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(i);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
