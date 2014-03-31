package jagodzinski.steve.hw4.fishing.controller;

public interface FishingLayoutObserver {	

	void onFishLocationChanged(float newX, float newY);

	void onFishingRodLocationChanged(float newX, float newY);
}
