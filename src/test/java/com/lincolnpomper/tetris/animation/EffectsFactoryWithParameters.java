package com.lincolnpomper.tetris.animation;

import com.lincolnpomper.tetris.FontFactory;
import com.lincolnpomper.tetris.graphics.BlurDegree;
import com.lincolnpomper.tetris.graphics.ColorChange;
import com.lincolnpomper.tetris.graphics.EffectsFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class EffectsFactoryWithParameters {

    private static EffectsFactoryWithParameters customEffectsFactory;

    private static float fontSize = 50f;

    private Color color = Color.orange;
    private ColorChange currentColorChange = ColorChange.red;
    private EffectsFactory effectsFactory = EffectsFactory.getInstance();
    private Font font;
    private Font[] fonts;
    private float intensity = 0.8f;
    private boolean outlineToggle = true;
    private int range = 18;
    private float stroke = 10.0f;

    private EffectsFactoryWithParameters() {
        this.fonts = new Font[]{FontFactory.getInstance().fontTetrisLogo.deriveFont(fontSize),
                FontFactory.getInstance().fontLinesCleared.deriveFont(fontSize)};

        this.font = fonts[0];
    }

    private void changeColorIntensity(int quantity) {

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        switch (currentColorChange) {

            case red:
                red += quantity;
                red = checkColor(red);
                break;
            case green:
                green += quantity;
                green = checkColor(green);
                break;
            case blue:
                blue += quantity;
                blue = checkColor(blue);
                break;
            case alpha:
                alpha += quantity;
                alpha = checkColor(alpha);
                break;
        }

        color = new Color(red, green, blue, alpha);
    }

    private int checkColor(int value) {

        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }

        return value;
    }

    public BufferedImage[] buildImages(String label, int steps) {
        return effectsFactory.buildImages(label, color, font, new BlurDegree(intensity, range, steps, stroke, outlineToggle));
    }

    public void decreaseColorIntensity(int quantity) {
        changeColorIntensity(quantity * -1);
    }

    public void decreaseFont() {
        fontSize -= 5;
        derive();
    }

    public void decreaseIntensity() {
        intensity -= 0.01f;

        if (intensity < 0.1f) {
            intensity = 0.1f;
        }
    }

    public void decreaseRange() {
        range--;

        if (range == 0) {
            range = 1;
        }
    }

    public void decreaseStroke() {
        stroke -= 0.1f;

        if (stroke < 0.1f) {
            stroke = 0.1f;
        }
    }

    public void derive() {
        font = font.deriveFont(fontSize);
        fonts[0] = fonts[0].deriveFont(fontSize);
        fonts[1] = fonts[1].deriveFont(fontSize);
    }

    public Color getColor() {
        return color;
    }

    public int getFontSize() {
        return (int) fontSize;
    }

    public static EffectsFactoryWithParameters getInstance() {

        EffectsFactoryWithParameters instance = new EffectsFactoryWithParameters();

        if (customEffectsFactory == null) {
            customEffectsFactory = instance;
        }

        return customEffectsFactory;
    }

    public float getIntensity() {
        return intensity;
    }

    public boolean getOutlineToggle() {
        return this.outlineToggle;
    }

    public int getRange() {
        return range;
    }

    public float getStroke() {
        return this.stroke;
    }

    public void increaseColorIntensity(int quantity) {
        changeColorIntensity(quantity);
    }

    public void increaseFont() {
        fontSize += 5;
        derive();
    }

    public void increaseIntensity() {
        intensity += 0.01f;

        if (intensity > BlurDegree.MAXIMUM_INTENSITY) {
            intensity = BlurDegree.MAXIMUM_INTENSITY;
        }
    }

    public void increaseRange() {
        range++;
    }

    public void increaseStroke() {
        stroke += 0.1f;

        if (stroke > BlurDegree.MAXIMUM_STROKE) {
            stroke = BlurDegree.MAXIMUM_STROKE;
        }
    }

    public void setColorChange(ColorChange colorChange) {
        this.currentColorChange = colorChange;
    }

    public void toggleFont() {
        if (font.getName().equals(fonts[0].getName())) {
            font = fonts[1];
        } else if (font.getName().equals(fonts[1].getName())) {
            font = fonts[0];
        }
    }

    public void toggleOutline() {
        this.outlineToggle = !this.outlineToggle;
    }
}