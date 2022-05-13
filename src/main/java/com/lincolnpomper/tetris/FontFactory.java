package com.lincolnpomper.tetris;

import com.lincolnpomper.tetris.util.Messages;
import com.lincolnpomper.tetris.util.Resolution;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class FontFactory {

	private final static String FONT_MAIN_NAME = Messages.getString("Font.main");
	private final static String FONT_MAIN_FULL_NAME = Messages.getString("Font.main.full");

	private static FontFactory fontManager;
	private static Resolution resolution;
	public Font fontLinesCleared;
	public Font fontReady;
	public Font fontTetrisLogo;
	public Font fontMenuKeys;
	public Font fontMenuKeysWhenEditing;
	public Font fontOtherMenu;
	public Font fontPoints;

	private FontFactory() {
	}

	public static void buildFontsByResolution(Resolution resolution) {
		FontFactory.resolution = resolution;
		FontFactory.getInstance().buildFonts();
	}

	public static FontFactory getInstance() {

		if (FontFactory.resolution == null) {
			throw new RuntimeException("Resolution == null");
		}

		FontFactory instance = new FontFactory();

		if (fontManager == null) {
			fontManager = instance;
		}

		return fontManager;
	}

	private static int getResolutionBasedSize(int minimumSize) {
		return resolution.getStandardizedHeight() * minimumSize / 480;
	}

	private void buildFonts() {

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		readFile(ge, FONT_MAIN_FULL_NAME);

		this.fontTetrisLogo = new Font(FONT_MAIN_NAME, Font.PLAIN, getResolutionBasedSize(70));
		this.fontReady = new Font(FONT_MAIN_NAME, Font.BOLD, getResolutionBasedSize(24));

		this.fontMenuKeys = new Font(Font.MONOSPACED, Font.BOLD, getResolutionBasedSize(20));
		this.fontMenuKeysWhenEditing = new Font(Font.SANS_SERIF, Font.BOLD, getResolutionBasedSize(17));
		this.fontOtherMenu = new Font(Font.MONOSPACED, Font.BOLD, getResolutionBasedSize(16));

		this.fontLinesCleared = new Font(Font.SANS_SERIF, Font.BOLD, getResolutionBasedSize(14));
		this.fontPoints = new Font(Font.MONOSPACED, Font.BOLD, getResolutionBasedSize(14));
	}

	private void readFile(GraphicsEnvironment ge, String fileName) {

		InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);

		if (stream == null) {
			throw new IllegalArgumentException("file is not found!");
		}

		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, stream));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}