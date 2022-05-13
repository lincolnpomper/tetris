package com.lincolnpomper.tetris.graphics;

import com.lincolnpomper.tetris.model.Piece;

import com.lincolnpomper.tetris.util.Logger;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockFactory {

    public final static float DEFAULT_BLOCK_THICKNESS_FACTOR = 0.16f;
    public final static float MAXIMUM_BLOCK_THICKNESS_FACTOR = 0.49999f;
    public final static float MINIMUM_BLOCK_THICKNESS_FACTOR = 0.06f;
    public final static Color blue = new Color(25, 69, 245);
    public final static Color cyan = new Color(31, 211, 232);
    public final static Color green = new Color(78, 196, 27);
    public final static Color magenta = new Color(200, 40, 210);
    public final static Color orange = new Color(255, 156, 0);
    public final static Color red = new Color(220, 52, 28);
    public final static Color yellow = new Color(215, 197, 36);

    public static final BlockModel[] blocks = new BlockModel[]{
        new BlockModel(new String[]{".... ..#. .... ..#.", "#### ..#. #### ..#.", ".... ..#. .... ..#.", ".... ..#. .... ..#."}, red),
        new BlockModel(new String[]{".... .#.. .#.. .#..", "###. .##. ###. ##..", ".#.. .#.. .... .#..", ".... .... .... ...."}, cyan),
        new BlockModel(new String[]{".... .#.. .... ##..", "###. .#.. ..#. .#..", "#... .##. ###. .#..", ".... .... .... ...."}, orange),
        new BlockModel(new String[]{".... .##. .... .#..", "###. .#.. #... .#..", "..#. .#.. ###. ##..", ".... .... .... ...."}, blue),
        new BlockModel(new String[]{".... .#.. .... .#..", ".##. .##. .##. .##.", "##.. ..#. ##.. ..#.", ".... .... .... ...."}, magenta),
        new BlockModel(new String[]{".... ..#. .... ..#.", "##.. .##. ##.. .##.", ".##. .#.. .##. .#..", ".... .... .... ...."}, green),
        new BlockModel(new String[]{".... .... .... ....", ".##. .##. .##. .##.", ".##. .##. .##. .##.", ".... .... .... ...."}, yellow)};

    public static final int NUMBER_OF_PIECES = blocks.length;
    private static final double DARKER_MULTIPLIER = 0.6;
    private static BlockFactory blockFactory;
    private float blockThicknessFactor = 0.0f;

    private BlockFactory() {
    }

    private BufferedImage createBlockImage(final Color blockColor, int size) {

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = image.getRaster();

        int x;
        int y;

        final int BLOCK_THICKNESS = getBlockThickness(size);
        final int QUANTITY_COLORS = size - 2;

        ColorArray colorArray;

        // first rows
        colorArray = getColorsFromSaturation(blockColor, QUANTITY_COLORS, Saturation.brightest, Saturation.brighter);
        for (y = 0; y < BLOCK_THICKNESS; y++) {
            colorArray.reset();
            for (x = y + 1; x < size - y - 1; x++) {
                raster.setPixel(x, y, colorArray.next());
            }
        }

        // first columns
        colorArray = getColorsFromSaturation(blockColor, QUANTITY_COLORS, Saturation.brighter, Saturation.mediumDark);
        for (x = 0; x < BLOCK_THICKNESS; x++) {
            colorArray.reset();
            for (y = x + 1; y < size - x - 1; y++) {
                raster.setPixel(x, y, colorArray.next());
            }
        }

        // last rows
        colorArray = getColorsFromSaturation(blockColor, QUANTITY_COLORS, Saturation.dark, Saturation.darker);
        for (y = 0; y < BLOCK_THICKNESS; y++) {
            colorArray.reset();
            for (x = BLOCK_THICKNESS - y; x < size - BLOCK_THICKNESS + y; x++) {
                raster.setPixel(x, size - BLOCK_THICKNESS + y, colorArray.next());
            }
        }

        // last columns
        colorArray = getColorsFromSaturation(blockColor, QUANTITY_COLORS, Saturation.standard, Saturation.darker);
        for (x = 0; x < BLOCK_THICKNESS; x++) {
            colorArray.reset();
            for (y = x + 1; y < size - x - 1; y++) {
                raster.setPixel(size - (x + 1), y, colorArray.next());
            }
        }

        // inside
        int quantity = size - BLOCK_THICKNESS * 2;
        colorArray = getColorsFromSaturation(blockColor, quantity, Saturation.mediumBright, Saturation.dark);

        int[] color = colorArray.next();
        final float gradientX = 0.35f;
        final float gradientY = 0.65f;
        int pos = BLOCK_THICKNESS;

        final int[] zero_pixels = {0, 0, 0, 0};
        int[] pixels = {0, 0, 0, 0};

        do {

            for (y = BLOCK_THICKNESS; y < size - BLOCK_THICKNESS; y++) {
                for (x = BLOCK_THICKNESS; x < size - BLOCK_THICKNESS; x++) {
                    if (x * gradientX + y * gradientY <= pos) {
                        if (Arrays.equals(raster.getPixel(x, y, pixels), zero_pixels)) {
                            raster.setPixel(x, y, color);
                        }
                    }
                }
            }
            color = colorArray.next();
            pos++;

        } while (color != null);

        // diagonals
        ColorArray colorArrayUpperLeft = getColorsFromSaturation(blockColor, BLOCK_THICKNESS, Saturation.brightest, Saturation.shiny);
        ColorArray colorArrayUpperRight = getColorsFromSaturation(blockColor, BLOCK_THICKNESS, Saturation.brighter, Saturation.mediumBright);
        ColorArray colorArrayLowerLeft = getColorsFromSaturation(blockColor, BLOCK_THICKNESS, Saturation.mediumDark, Saturation.dark);
        ColorArray colorArrayLowerRight = getColorsFromSaturation(blockColor, BLOCK_THICKNESS, Saturation.darker, Saturation.darkest);

        for (y = 0; y < size; y++) {
            for (x = 0; x < size; x++) {
                if (x == y && x < BLOCK_THICKNESS) {
                    raster.setPixel(x, y, colorArrayUpperLeft.next());
                }
                if (x >= size - BLOCK_THICKNESS && y < BLOCK_THICKNESS && x == size - y - 1) {
                    raster.setPixel(x, y, colorArrayUpperRight.next());
                }
                if (x < BLOCK_THICKNESS && y >= size - BLOCK_THICKNESS && y == size - x - 1) {
                    raster.setPixel(x, y, colorArrayLowerLeft.next());
                }
                if (x == y && x >= size - BLOCK_THICKNESS) {
                    raster.setPixel(x, y, colorArrayLowerRight.next());
                }
            }
        }

        return image;
    }

    private int getBlockThickness(int size) {

        final float FACTOR = blockThicknessFactor != 0.0f ? blockThicknessFactor : DEFAULT_BLOCK_THICKNESS_FACTOR;
        final int factor = (int) (size * FACTOR);
        return Math.max(factor, 2);
    }

    private ColorArray getColorsFromSaturation(Color color, int quantity, Saturation start, Saturation end) {

        if (quantity < 1) {
            throw new RuntimeException("Quantity < 1");
        }

        ColorArray colorArray = new ColorArray(quantity);

        if (quantity == 1) {

            colorArray.add(color.getRed(), color.getGreen(), color.getBlue());

        } else {

            double difference = end.getValue() - start.getValue();
            double factor = difference / (quantity - 1);

            for (int i = 0; i < quantity; i++) {

                double proportion = start.getValue() + factor * i;

                final double red = color.getRed() * proportion;
                final double green = color.getGreen() * proportion;
                final double blue = color.getBlue() * proportion;
                colorArray.add((int) red, (int) green, (int) blue);
            }
        }

        return colorArray;
    }

    private ArrayList<boolean[][][]> getPiecesFrames() {

        StringBuilder lines = new StringBuilder();

        for (BlockModel block : blocks) {
            for (int j = 0; j < block.metadata.length; j++) {
                lines.append(block.metadata[j].replace(" ", ""));
            }
        }

        ArrayList<boolean[][][]> list = new ArrayList<>(NUMBER_OF_PIECES);
        boolean[][][] shapeGroup = new boolean[4][4][4];
        char[] c = lines.toString().toCharArray();
        int pos = 0;

        for (int i = 0; i < NUMBER_OF_PIECES; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    for (int l = 0; l < 4; l++) {
                        shapeGroup[k][j][l] = c[pos++] == '#';
                    }
                }
            }

            list.add(shapeGroup);
            shapeGroup = new boolean[4][4][4];
        }

        return list;
    }

    public BufferedImage[] createBlockImages(int size) {

        BufferedImage[] blockImages = new BufferedImage[NUMBER_OF_PIECES * 2 + 1];
        Logger.info(String.format("Creating %d block images for size %d %n", blockImages.length, size));

        int pos = 0;

        for (BlockModel block : blocks) {

            blockImages[pos] = createBlockImage(block.color, size);
            blockImages[pos + NUMBER_OF_PIECES] = darkerImage(blockImages[pos++], DARKER_MULTIPLIER);
        }

        blockImages[pos + NUMBER_OF_PIECES] = createBlockImage(Color.gray, size);

        return blockImages;
    }

    public BufferedImage[] createBlockImagesPrimary(int size) {

        BufferedImage[] blockImages = new BufferedImage[NUMBER_OF_PIECES];
        Logger.info(String.format("Creating %d block images for size %d %n", blockImages.length, size));

        int pos = 0;

        for (BlockModel block : blocks) {
            blockImages[pos++] = createBlockImage(block.color, size);
        }

        return blockImages;
    }

    public BufferedImage darkerImage(BufferedImage source, double multiplier) {

        int width = source.getWidth();
        int height = source.getHeight();

        WritableRaster rasterSource = source.getRaster();

        BufferedImage destination = new BufferedImage(width, height, source.getType());
        WritableRaster raster = destination.getRaster();

        int[] color = new int[]{0, 0, 0, 0};

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                rasterSource.getPixel(i, j, color);
                raster.setPixel(i, j, new int[]{(int) (color[0] * multiplier), (int) (color[1] * multiplier),
                        (int) (color[2] * multiplier), color[3]});
            }
        }

        return destination;
    }

    public static BlockFactory getInstance() {

        BlockFactory instance = new BlockFactory();

        if (blockFactory == null) {
            blockFactory = instance;
        }

        return blockFactory;
    }

    public static BlockFactory getInstance(float blockThicknessFactor) {

        BlockFactory instance = new BlockFactory();

        if (blockFactory == null) {
            blockFactory = instance;
        }

        if (blockThicknessFactor < MINIMUM_BLOCK_THICKNESS_FACTOR) {
            throw new RuntimeException("blockThicknessFactor < DEFAULT_BLOCK_THICKNESS_FACTOR");
        }

        instance.blockThicknessFactor = blockThicknessFactor;

        return instance;
    }

    public Piece[] getPieces() {

        ArrayList<boolean[][][]> piecesFrames = BlockFactory.getInstance().getPiecesFrames();
        Piece[] pieces = new Piece[BlockFactory.NUMBER_OF_PIECES];

        for (int i = 0; i < BlockFactory.NUMBER_OF_PIECES; i++) {
            pieces[i] = new Piece(piecesFrames.get(i), i + 100);
        }

        return pieces;
    }
}