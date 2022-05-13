package com.lincolnpomper.tetris.graphics;

import com.lincolnpomper.tetris.util.Resolution;

public class BlurDegreeFactory {

    private static BlurDegreeFactory blurDegreeFactory;
    private static Resolution resolution;
    public final BlurDegree tetrisLogo;
    public final BlurDegree menu;
    public final BlurDegree other;
    public final BlurDegree smoothBlock;
    public final BlurDegree editKeys;
    public final BlurDegree linesCleared;

    private BlurDegreeFactory() {
        this.tetrisLogo = new BlurDegree(1.8f, rangeBasedOnResolution(12), 20, strokeBasedOnResolution(10.0f), true);
        this.menu = new BlurDegree(1.3f, rangeBasedOnResolution(6), 4, strokeBasedOnResolution(2.7f), true);
        this.editKeys = new BlurDegree(1.4f, rangeBasedOnResolution(10), 4, strokeBasedOnResolution(1.8f), true);
        this.smoothBlock = new BlurDegree(1.8f, rangeBasedOnResolution(12), 20);
        this.other = new BlurDegree(0.9f, rangeBasedOnResolution(8), 6, strokeBasedOnResolution(4.0f), true);
        this.linesCleared = new BlurDegree(1.2f, rangeBasedOnResolution(5), 12, strokeBasedOnResolution(2.0f), true);
    }

    private static int rangeBasedOnResolution(int value) {
        return resolution.getStandardizedHeight() * value / 480;
    }

    private static float strokeBasedOnResolution(float value) {
        return resolution.getStandardizedHeight() * value / 480;
    }

    public static BlurDegreeFactory getInstance() {

        if (BlurDegreeFactory.resolution == null) {
            throw new RuntimeException("Resolution == null");
        }

        BlurDegreeFactory instance = new BlurDegreeFactory();

        if (blurDegreeFactory == null) {
            blurDegreeFactory = instance;
        }

        return blurDegreeFactory;
    }

    public static void setResolution(Resolution resolution) {
        BlurDegreeFactory.resolution = resolution;
    }
}