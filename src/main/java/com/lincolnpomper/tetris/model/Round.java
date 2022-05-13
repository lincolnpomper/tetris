package com.lincolnpomper.tetris.model;

public class Round {

	private final String name;
	private final int requirement;
	private int speedFall;
	private int speedLanding;
	private int speedLanded;

	public Round(String name, int requirement, int speedFall, int speedLanding, int speedLanded) {

		this.name = name;
		this.requirement = requirement;
		this.speedFall = speedFall;
		this.speedLanding = speedLanding;
		this.speedLanded = speedLanded;
	}

	public String getName() {
		return name;
	}

	public int getRequirement() {
		return requirement;
	}

	public int getSpeedFall() {
		return speedFall;
	}

	public int getSpeedLanded() {
		return speedLanded;
	}

	public int getSpeedLanding() {
		return speedLanding;
	}
}
