package jagodzinski.steve.hw4.fishing.controller;

import android.content.res.Resources;

public interface FishingRodObserver {
	void onFishingRodLocationChanged(float newX, float newY, Resources resources);
}
