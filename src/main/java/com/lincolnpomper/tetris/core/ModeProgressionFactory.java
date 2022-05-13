package com.lincolnpomper.tetris.core;

import com.lincolnpomper.tetris.model.ModeProgression;

public class ModeProgressionFactory {

	private final static int NONE = -1;

	public static ModeProgression buildFrom(GameMode gameMode) {

		ModeProgression modeProgression;

		switch (gameMode) {
			case NORMAL: {
				modeProgression = buildNormal();
				break;
			}
			case MASTER: {
				modeProgression = buildMaster();
				break;
			}
			case ULTRA: {
				modeProgression = buildUltra();
				break;
			}
			default:
				throw new IllegalStateException("Unexpected value: " + gameMode);
		}

		return modeProgression;
	}

	private static ModeProgression buildMaster() {

		ModeProgression modeProgression = new ModeProgression(GameMode.MASTER);

		final int speedLanded = 50;

		modeProgression.addRound("1", 0, 800, 60, speedLanded);
		modeProgression.addRound("2", 25, 700, 60, speedLanded);
		modeProgression.addRound("3", 50, 600, 60, speedLanded);
		modeProgression.addRound("4", 75, 500, 60, speedLanded);
		modeProgression.addRound("5", 100, 400, 60, speedLanded);
		modeProgression.addRound("6", 125, 200, 40, speedLanded);
		modeProgression.addRound("7", 150, 150, 40, speedLanded);
		modeProgression.addRound("8", 175, 100, 40, speedLanded);
		modeProgression.addRound("9", 200, 100, 40, speedLanded);
		modeProgression.addRound("10", 250, 100, 40, speedLanded);

		modeProgression.addRound("S1", 300, 0, 35, 40);
		modeProgression.addRound("S2", 350, 0, 35, 40);
		modeProgression.addRound("S3", 400, 0, 35, 40);
		modeProgression.addRound("S4", 450, 0, 30, 40);
		modeProgression.addRound("S5", 500, NONE, NONE, NONE);

		return modeProgression;
	}

	private static ModeProgression buildNormal() {

		ModeProgression modeProgression = new ModeProgression(GameMode.NORMAL);

		final int speedLanding = 90;
		final int speedLanded = 60;

		modeProgression.addRound("1", 0, 1200, speedLanding, speedLanded);
		modeProgression.addRound("2", 8, 1100, speedLanding, speedLanded);
		//modeProgression.addRound("2", 20, 1100, speedLanding, speedLanded);
//		modeProgression.addRound("3", 40, 1000, speedLanding, speedLanded);
//		modeProgression.addRound("4", 60, 900, speedLanding, speedLanded);
//		modeProgression.addRound("5", 80, 800, speedLanding, speedLanded);
//		modeProgression.addRound("6", 100, 500, speedLanding, speedLanded);
//		modeProgression.addRound("7", 120, 400, speedLanding, speedLanded);
//		modeProgression.addRound("8", 140, 300, speedLanding, speedLanded);
//		modeProgression.addRound("9", 160, 200, speedLanding, speedLanded);
//		modeProgression.addRound("10", 180, NONE, NONE, NONE);

		return modeProgression;
	}

	private static ModeProgression buildUltra() {

		ModeProgression modeProgression = new ModeProgression(GameMode.ULTRA);

		modeProgression.addRound("S1", 0, 0, 60, 40);
		modeProgression.addRound("S2", 50, 0, 55, 40);
		modeProgression.addRound("S3", 100, 0, 50, 40);
		modeProgression.addRound("S4", 150, 0, 45, 35);
		modeProgression.addRound("S5", 200, 0, 45, 30);
		modeProgression.addRound("S6", 250, 0, 40, 25);
		modeProgression.addRound("S7", 300, NONE, NONE, NONE);

		return modeProgression;
	}
}


