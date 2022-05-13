package com.lincolnpomper.tetris.model;

import com.lincolnpomper.tetris.core.GameMode;

import java.util.ArrayList;
import java.util.List;

public class ModeProgression {

	private GameMode gameMode;
	private Round round;
	private List<Round> rounds = new ArrayList<>();

	public ModeProgression(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public void addRound(String name, int requirement, int speedFall, int speedLanding, int speedLanded) {

		Round round = new Round(name, requirement, speedFall, speedLanding, speedLanded);
		rounds.add(round);

		if (this.round == null) {
			this.round = round;
		}
	}

	public void advanceRound() {

		for (int i = 0; i < rounds.size() - 1; i++) {
			if (rounds.get(i).getName().equals(round.getName())) {
				round = rounds.get(i + 1);
				break;
			}
		}
	}

	public Round getCurrentRound() {
		return round;
	}

	public String getCurrentRoundName() {
		return round.getName();
	}

	public Round getNextRound() {

		Round next = null;

		for (int i = 0; i < rounds.size() - 1; i++) {
			if (rounds.get(i).getName().equals(round.getName())) {
				next = rounds.get(i + 1);
				break;
			}
		}

		return next;
	}

	public boolean isGameModeNormal() {
		return this.gameMode == GameMode.NORMAL;
	}

	public boolean isMovementMaximumSpeed() {
		return this.round.getSpeedFall() == 0;
	}
}
