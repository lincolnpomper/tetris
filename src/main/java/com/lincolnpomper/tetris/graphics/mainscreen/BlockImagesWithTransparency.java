package com.lincolnpomper.tetris.graphics.mainscreen;

import com.lincolnpomper.tetris.graphics.EffectsFactory;
import com.lincolnpomper.tetris.util.Logger;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Phaser;

public class BlockImagesWithTransparency extends Thread {

    private Integer[] allTransparencyValues;
    private boolean blur;
    private Map<String, BufferedImage> imageMap;
    private BufferedImage[] images;
    private Phaser phaser;

    public BlockImagesWithTransparency(BufferedImage[] images, Integer[] allTransparencyValues, boolean blur, String name, Phaser phaser) {
        super(name);
        this.images = images;
        this.allTransparencyValues = allTransparencyValues;
        this.blur = blur;
        this.phaser = phaser;

        phaser.register();

        validateTransparencyValues(allTransparencyValues);
    }

    private void validateTransparencyValues(Integer[] values) {
        for (int transparency : values) {
            if (transparency <= 0 || transparency >= 100) {
                throw new RuntimeException("Invalid Transparency");
            }
        }
    }

    public BufferedImage get(int imageNumber, int transparency) {

        if (!imageMap.containsKey(imageNumber + "-" + transparency)) {
            throw new RuntimeException(String.format("Invalid image or transparency number %d", imageNumber));
        }

        return imageMap.get(imageNumber + "-" + transparency);
    }

    @Override
    public void run() {

        Logger.info(String.format(String.format("%s - Started %n", currentThread().getName())));

        this.imageMap = new HashMap<>();
        BufferedImage image;

        for (int blockNumber = 0; blockNumber < images.length; blockNumber++) {

            this.imageMap.put(blockNumber + "-100", images[blockNumber]);
            image = images[blockNumber];

            if (blur) {
                image = EffectsFactory.getInstance().createImageWithLargeRangeBlurForIntro(image);
            }

            Logger.info(String.format("%s - Generating transparent images for original image number %d %n", currentThread().getName(), blockNumber));

            for (int transparency : allTransparencyValues) {
                float transparencyFloat = (float) transparency / 100f;
                BufferedImage transparentImage = EffectsFactory.getInstance().changeImageAlpha(image, transparencyFloat);
                this.imageMap.put(blockNumber + "-" + transparency, transparentImage);
            }
        }
        Logger.info(String.format("%s - Ended %n", currentThread().getName()));

        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndDeregister();
    }
}
