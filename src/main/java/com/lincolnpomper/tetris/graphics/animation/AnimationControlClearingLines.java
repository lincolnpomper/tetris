package com.lincolnpomper.tetris.graphics.animation;

import com.lincolnpomper.tetris.core.Frame;
import com.lincolnpomper.tetris.core.Position;
import com.lincolnpomper.tetris.graphics.BlurDegree;
import com.lincolnpomper.tetris.graphics.BlurDegreeFactory;
import com.lincolnpomper.tetris.graphics.EffectsFactory;
import com.lincolnpomper.tetris.FontFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimationControlClearingLines {

    private final static Color COLOR_BACK_TO_BACK = new Color(203, 70, 1);
    private final static Color COLOR_COMBO = new Color(156, 85, 200);
    private final static Color COLOR_DOUBLE = new Color(200, 192, 52);
    private final static Color COLOR_TETRIS = new Color(200, 129, 44);
    private final static Color COLOR_TRIPLE = new Color(65, 218, 119);
    private static final Color[] ALL_COLORS = {COLOR_DOUBLE, COLOR_TRIPLE, COLOR_TETRIS, COLOR_COMBO, COLOR_BACK_TO_BACK};
    private final static int DEFAULT_DELAY = 70;
    private final static String TEXT_BACK_TO_BACK = "Back-to-back";
    private final static String TEXT_COMBO = "Combo";
    private static final String TEXT_DOUBLE = "Double";
    private static final String TEXT_TETRIS = "Tetris";
    private static final String TEXT_TRIPLE = "Triple";
    private static final String[] ALL_TEXTS = {TEXT_DOUBLE, TEXT_TRIPLE, TEXT_TETRIS, TEXT_COMBO, TEXT_BACK_TO_BACK};
    private final static int TRANSPARENCY_STEPS = 4;
    private static AnimationControlClearingLines animationControl;
    private ArrayList<TextAnimation> animations = new ArrayList<>();
    private Map<String, TextAnimation> animationsCache;
    private int rightBorderX;
    private int size;

    private AnimationControlClearingLines() {

        animationsCache = new HashMap<>();

        for (int i = 0; i < ALL_TEXTS.length; i++) {
            String text = ALL_TEXTS[i];
            animationsCache.put(text, generateAnimation(text, ALL_COLORS[i]));
        }
    }

    private void addAnimation(String text, Position position, Component component) {

        TextAnimation animation = animationsCache.get(text);

        position.setX(position.getX() - animation.getWidth());
        position.setY(position.getY() - animation.getHeight());

        animations.add(animation);
        animation.start(position, component);
    }

    private TextAnimation generateAnimation(String text, Color color) {

        final BlurDegree blurDegree = BlurDegreeFactory.getInstance().linesCleared;
        final Font font = FontFactory.getInstance().fontLinesCleared;
        BufferedImage[] images = EffectsFactory.getInstance().buildImages(text, color, font, blurDegree);

        BufferedImage[] allImages = new BufferedImage[images.length + TRANSPARENCY_STEPS];
        for (int i = 0; i < images.length; i++) {
            allImages[i] = images[i];
        }

        float alphaFactor = 1.0f / (TRANSPARENCY_STEPS + 1);
        float alpha = 1.0f;
        BufferedImage lastImage = images[images.length -1];

        for (int i = 0; i < TRANSPARENCY_STEPS; i++) {
            alpha -= alphaFactor;
            BufferedImage image = EffectsFactory.getInstance().changeImageAlpha(lastImage, alpha);
            allImages[images.length + i] = image;
        }

        return new TextAnimation(allImages, DEFAULT_DELAY);
    }

    private int getBottomY(int lowestAnimatedRow, int borderSize) {
        return lowestAnimatedRow * size + size + borderSize;
    }

    public void generateBackToBackAnimation(int lowestAnimatedRow, Component component) {
        generateComboOrBackToBackAnimation(TEXT_BACK_TO_BACK, lowestAnimatedRow, component);
    }

    public void generateComboAnimation(int lowestAnimatedRow, Component component) {
        generateComboOrBackToBackAnimation(TEXT_COMBO, lowestAnimatedRow, component);
    }

    public void generateComboOrBackToBackAnimation(String text, int lowestAnimatedRow, Component component) {

        int x = rightBorderX;
        int y = getBottomY(lowestAnimatedRow, size);
        y -= size; // one less than the cleared line animation
        Position position = new Position(x, y);
        addAnimation(text, position, component);
    }

    public void generateLinesClearedAnimation(int quantityLinesCleared, int lowestAnimatedRow, Component component) {

        int x = rightBorderX;
        int y = getBottomY(lowestAnimatedRow, size);
        Position position = new Position(x, y);

        String text = null;

        switch (quantityLinesCleared) {
            case 2:
                text = TEXT_DOUBLE;
                break;
            case 3:
                text = TEXT_TRIPLE;
                break;
            case 4:
                text = TEXT_TETRIS;
                break;
        }

        addAnimation(text, position, component);
    }

    public ArrayList<TextAnimation> getAnimations() {
        return animations;
    }

    public static AnimationControlClearingLines getInstance(int size) {

        AnimationControlClearingLines instance = new AnimationControlClearingLines();

        if (animationControl == null) {
            animationControl = instance;

            animationControl.setSize(size);

            int x = Frame.COLUMNS * size + size;
            animationControl.setRightBorderX(x);
        }

        return animationControl;
    }

    public void setRightBorderX(int rightBorderX) {
        this.rightBorderX = rightBorderX;
    }

    public void setSize(int size) {
        this.size = size;
    }
}