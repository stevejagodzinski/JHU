package jagodzinski.steve.hw4.fishing.controller;

import android.content.res.Resources;

public interface FishLocationObserver {
	void onFishLocationChanged(float newX, float newY, Resources resources);
}
