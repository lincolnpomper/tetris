package com.lincolnpomper.tetris.util;

public class AvailableResolutions {

    public static final Resolution[] RESOLUTIONS = new Resolution[]{
            new Resolution(640, 480, 16, false),
            new Resolution(800, 600, 20, false),
            new Resolution(1024, 768, 25, true),
            new Resolution(1280, 768, 25, false),
            new Resolution(1440, 900, 30, false),
            new Resolution(1920, 1080, 36, false),
            new Resolution(3840, 2160, 72, false)};

    public static Resolution getDefaultResolution() {

        Resolution resolution = null;

        for (Resolution value : RESOLUTIONS) {
            if (value.isDefault()) {
                resolution = value;
            }
        }

        return resolution;
    }

    public static Resolution getResolutionByKeyName(String keyName) {

        Resolution resolution = null;

        for (Resolution value : RESOLUTIONS) {
            if (value.getResolutionKeyName().equals(keyName)) {
                resolution = value;
            }
        }

        return resolution;
    }
}