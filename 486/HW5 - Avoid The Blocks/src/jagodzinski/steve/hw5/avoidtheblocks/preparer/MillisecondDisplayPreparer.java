package jagodzinski.steve.hw5.avoidtheblocks.preparer;

import java.util.concurrent.TimeUnit;

public class MillisecondDisplayPreparer {
	public static String prepareMilliseconds(Long millis, boolean round) {
		long hours = TimeUnit.MILLISECONDS.toHours(millis);

		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long displayMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);

		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		long displaySeconds = seconds - TimeUnit.MINUTES.toSeconds(minutes);

		long remainingMS = millis - TimeUnit.SECONDS.toMillis(seconds);

		if (round && remainingMS >= 500) {
			displaySeconds++;
		}

		return String.format("%02d:%02d:%02d", hours, displayMinutes, displaySeconds);
	}
}
