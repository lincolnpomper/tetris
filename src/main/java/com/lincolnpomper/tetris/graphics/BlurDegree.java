package com.lincolnpomper.tetris.graphics;

public class BlurDegree {

    public static final float MAXIMUM_INTENSITY = 3f;
    public static final float MAXIMUM_STROKE = 100.0f;
    private float intensity;
    private boolean outline;
    private int range;
    private int steps;
    private float stroke;

    public BlurDegree(float intensity, int range, int steps) {
        this(intensity, range, steps, 0.0f, false);
    }

    public BlurDegree(float intensity, int range, int steps, float stroke, boolean outline) {
        this.intensity = intensity;
        this.range = range;
        this.steps = steps;
        this.stroke = stroke;
        this.outline = outline;

        if (steps < 1 || steps > 30) {
            throw new RuntimeException("steps < 1 || steps > 30");
        }

        if (intensity < 0.1f || intensity > MAXIMUM_INTENSITY) {
            throw new RuntimeException("intensity < 0.1f || intensity > " + MAXIMUM_INTENSITY);
        }
    }

    public float getIntensity(int step) {

        if (step > steps) {
            throw new RuntimeException("step > steps");
        }

        float current = (float) step / (float) steps;
        return intensity * current;
    }

    public boolean getOutline() {
        return outline;
    }

    public int getRange() {
        return range;
    }

    public int getSteps() {
        return steps;
    }

    public float getStroke() {
        return stroke;
    }
}