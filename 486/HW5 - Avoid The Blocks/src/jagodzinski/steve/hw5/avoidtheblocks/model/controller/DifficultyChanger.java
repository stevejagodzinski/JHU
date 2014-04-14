package jagodzinski.steve.hw5.avoidtheblocks.model.controller;

import jagodzinski.steve.hw5.avoidtheblocks.model.Difficulty;
import jagodzinski.steve.hw5.avoidtheblocks.model.GameState;
import jagodzinski.steve.hw5.avoidtheblocks.service.DifficultyCalculationUtil;

import java.util.Random;

import android.util.Log;

public class DifficultyChanger extends Thread {

	private static final long CHANGE_DIFFICULTY_FREQUENCY_MS = 3000;
	private static final short MAX_CHANGE_EASY_LOCATION_ATTEMPTS = 50;

	private Random random = new Random();

	private final GameState gameState;

	public DifficultyChanger(GameState gameState) {
		this.gameState = gameState;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {

			short attempts = 0;

			if (gameState.getDifficulty() != Difficulty.HARD) {
				short nextLocation;

				do {
					nextLocation = (short) random.nextInt(360);
					attempts++;
				} while (attempts < MAX_CHANGE_EASY_LOCATION_ATTEMPTS
						|| DifficultyCalculationUtil.calculateDifficulty(nextLocation, gameState.getEasyLocation()) != Difficulty.HARD);

				if (attempts == MAX_CHANGE_EASY_LOCATION_ATTEMPTS) {
					Log.w("DifficultyChanger", "Attempted " + MAX_CHANGE_EASY_LOCATION_ATTEMPTS
							+ " tries to change location, but could no randomly select a new difficult point.");
				}

				gameState.setEasyLocation(nextLocation);
			}

			try {
				sleep(CHANGE_DIFFICULTY_FREQUENCY_MS);
			} catch (InterruptedException e) {
				interrupt();
			}
		}
	}
}
