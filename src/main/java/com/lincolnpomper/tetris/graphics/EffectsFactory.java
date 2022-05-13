package com.lincolnpomper.tetris.graphics;

import com.lincolnpomper.tetris.util.Logger;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;

public class EffectsFactory {

    private static EffectsFactory effectsFactory;

    private EffectsFactory() {
    }

    private BufferedImage createImageWithRangeBorder(BufferedImage source, int range) {

        int widthSource = source.getWidth();
        int heightSource = source.getHeight();
        int width = source.getWidth() + (range * 2);
        int height = source.getHeight() + (range * 2);

        BufferedImage biggerImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = biggerImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        Rectangle rectangle = new Rectangle(range, range, widthSource, heightSource);
        TexturePaint texturePaint = new TexturePaint(source, rectangle);
        graphics.setPaint(texturePaint);
        graphics.fillRect(range, range, widthSource, heightSource);

        return biggerImage;
    }

    private BufferedImage createOriginalImage(String text, Color color, Color colorOutline, int range, boolean outline, float stroke, Font font) {

        Graphics2D graphics = new BufferedImage(600, 300, BufferedImage.TYPE_INT_RGB).createGraphics();
        graphics.setFont(font);

        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int imageWidth = fontMetrics.stringWidth(text);
        int imageHeight = fontMetrics.getHeight();

        int gap = 1 + range + (int) stroke;
        if (outline) {
            imageWidth += gap * 2;
            imageHeight += gap * 2;
        }

        int x = gap;
        int y = fontMetrics.getAscent() + gap;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setFont(font);

        graphics.setColor(color);
        graphics.drawString(text, x, y);

        if (outline) {
            graphics.setStroke(new BasicStroke(stroke));

            GlyphVector glyphVector = font.createGlyphVector(graphics.getFontRenderContext(), text);
            Shape textShape = glyphVector.getOutline(x, y);

            graphics.setColor(colorOutline);
            graphics.draw(textShape);

            graphics.setColor(color);
            graphics.fill(textShape);
        }

        return image;
    }

    private Kernel updateValues(float intensity, int range) {

        float intensityValue = (1f / (float) (range * range)) * intensity;
        float[] kernelData = new float[range * range];
        Arrays.fill(kernelData, intensityValue);

        return new Kernel(range, range, kernelData);
    }
    public BufferedImage[] buildImages(String text, Color color, Font font, BlurDegree blurDegree) {
        return buildImages(text, color, color.darker(), font, blurDegree);
    }

    public BufferedImage[] buildImages(String text, Color color, Color colorOutline, Font font, BlurDegree blurDegree) {

        Logger.info(String.format("Building images with %d blur degrees for text %s %n", blurDegree.getSteps(), text));

        BufferedImage[] imageAnimationFrames = new BufferedImage[1 + blurDegree.getSteps()];
        BufferedImage imageOriginal = createOriginalImage(text, color, colorOutline, blurDegree.getRange(), blurDegree.getOutline(), blurDegree.getStroke(), font);

        imageAnimationFrames[0] = imageOriginal;

        for (int step = 1; step < blurDegree.getSteps() + 1; step++) {
            Logger.info(String.format("Building image for step %d and text %s %n", step, text));
            Kernel kernel = updateValues(blurDegree.getIntensity(step), blurDegree.getRange());

            imageAnimationFrames[step] = createImageWithBlurForKernel(imageOriginal, kernel);
        }

        return imageAnimationFrames;
    }


    public BufferedImage changeImageAlpha(BufferedImage source, float alphaPercentage) {

        Logger.info(String.format("Changing image alpha %f %n", alphaPercentage));

        int width = source.getWidth();
        int height = source.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaPercentage));
        graphics.drawImage(source, 0, 0, width, height, null);
        graphics.dispose();

        return image;
    }

    public BufferedImage createImageWithBlurForKernel(BufferedImage source, Kernel kernel) {

        int width = source.getWidth();
        int height = source.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        hints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        hints.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR));

        hints.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED));
        hints.add(new RenderingHints(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE));

        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, hints);
        convolveOp.filter(source, image);

        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(source, 0, 0, null);

        return image;
    }

    public BufferedImage createImageWithBlurForKernelForIntro(BufferedImage source, Kernel kernel) {

        int width = source.getWidth();
        int height = source.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        convolveOp.filter(source, image);

        return image;
    }

    public BufferedImage createImageWithLargeRangeBlurForIntro(BufferedImage source) {

        BlurDegree blur = BlurDegreeFactory.getInstance().smoothBlock;
        Kernel kernel = updateValues(blur.getIntensity(blur.getSteps()), blur.getRange());

        BufferedImage biggerImage = createImageWithRangeBorder(source, blur.getRange());

        return createImageWithBlurForKernelForIntro(biggerImage, kernel);
    }

    public static EffectsFactory getInstance() {

        EffectsFactory instance = new EffectsFactory();

        if (effectsFactory == null) {
            effectsFactory = instance;
        }

        return effectsFactory;
    }
}