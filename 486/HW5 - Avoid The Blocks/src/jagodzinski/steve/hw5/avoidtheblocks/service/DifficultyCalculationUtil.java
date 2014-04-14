package jagodzinski.steve.hw5.avoidtheblocks.service;

import jagodzinski.steve.hw5.avoidtheblocks.model.Difficulty;
import android.util.Log;

public class DifficultyCalculationUtil {
	public static Difficulty calculateDifficulty(short currentCompass, short easyLocation) {
		int accuracyBetweenPoints = Math.abs(easyLocation - currentCompass);
		int accuractAroundNorth = currentCompass + (360 - easyLocation);
		int accuracy = Math.min(accuracyBetweenPoints, accuractAroundNorth);

		Difficulty difficulty;

		if (accuracy <= 60) {
			difficulty = Difficulty.EASY;
		} else if (accuracy <= 120) {
			difficulty = Difficulty.MEDIUM;
		} else {
			difficulty = Difficulty.HARD;
		}

		Log.d("Calculating difficulty", "Current Compass: " + currentCompass);
		Log.d("Calculating difficulty", "Easy Location: " + easyLocation);
		Log.d("Calculating difficulty", "Difficulty: " + difficulty);

		return difficulty;
	}
}
