package com.lincolnpomper.tetris.graphics;

import com.lincolnpomper.tetris.BackGroundImagesFacade;
import com.lincolnpomper.tetris.util.Logger;
import com.lincolnpomper.tetris.util.Resolution;

import java.util.concurrent.Phaser;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageLoader {

    public static final int NUMBER_OF_BACKGROUND_IMAGES = 10;
    private static final String[] BORDER_IMAGES_NAMES = {"b_top", "b_left", "b_bottom", "b_right"};

    private static ImageLoader imageLoader;
    private Toolkit toolkit;

    private ImageLoader() {
        toolkit = Toolkit.getDefaultToolkit();
    }

    public Image[] getBorderImages(Resolution resolution) {

        Image[] borderImages = new Image[4];

        for (int i = 0; i < BORDER_IMAGES_NAMES.length; i++) {
            borderImages[i] = toolkit.createImage(ImageLoader.class.getResource("/images/" + BORDER_IMAGES_NAMES[i] + ".gif"));
        }

        if (!resolution.isDefault()) {

            int size = resolution.getBlockSize();
            int[][] borderSizes = new int[][]{{12 * size, size}, {size, 20 * size}, {11 * size, size}, {size, 21 * size}};
            for (int i = 0; i < BORDER_IMAGES_NAMES.length; i++) {
                borderImages[i] = borderImages[i].getScaledInstance(borderSizes[i][0], borderSizes[i][1], Image.SCALE_FAST);
            }
        }

        return borderImages;
    }

    public static ImageLoader getInstance() {

        ImageLoader instance = new ImageLoader();

        if (imageLoader == null) {
            imageLoader = instance;
        }

        return imageLoader;
    }

    public void loadBackgroundImages(Resolution resolution) {

        Logger.info(String.format("Loading background images for block size %d %n", resolution.getBlockSize()));

        Phaser phaser = new Phaser();
        phaser.register();
        final List<LoadBackgroundImageThread> loadersList = new ArrayList<>();
        List<Image> imageList = new ArrayList<>();

        int counter = 0;
        while (counter < NUMBER_OF_BACKGROUND_IMAGES) {

            LoadBackgroundImageThread loadImage = new LoadBackgroundImageThread(resolution, counter++, phaser);
            loadImage.setPriority(Thread.MAX_PRIORITY);
            loadersList.add(loadImage);

            loadImage.start();
        }

        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndDeregister();

        for (LoadBackgroundImageThread loader : loadersList) {
            imageList.add(loader.image);
        }

        BackGroundImagesFacade.getInstance().addAll(imageList);
    }

    class LoadBackgroundImageThread extends Thread {

        BufferedImage image;
        int imageNumber;
        Phaser phaser;
        Resolution resolution;

        public LoadBackgroundImageThread(Resolution resolution, int imageNumber, Phaser phaser) {
            this.resolution = resolution;
            this.imageNumber = imageNumber;
            this.phaser = phaser;

            phaser.register();
        }

        @Override
        public void run() {

            String fileName = "/images/background/" + imageNumber + ".jpg";

            Logger.info(String.format("Loading image %s %n", fileName));

            BufferedImage image = null;
            int imageWidth = 0;
            int imageHeight = 0;

            try {

                image = ImageIO.read(getClass().getResource(fileName));
                imageWidth = image.getWidth();
                imageHeight = image.getHeight();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!resolution.isDefault()) {

                int width = resolution.getWidth();
                int height = resolution.getHeight();
                int scaledHeight = (int) (height * (float) imageWidth / (float) imageHeight);

                BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

                Graphics2D graphics = scaledImage.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                graphics.drawImage(image, 0, 0, width, scaledHeight, null);
                graphics.dispose();

                image = scaledImage;
            }

            this.image = image;

            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndDeregister();
        }
    }
}