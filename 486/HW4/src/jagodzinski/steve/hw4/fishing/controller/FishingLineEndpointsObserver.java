package jagodzinski.steve.hw4.fishing.controller;

public interface FishingLineEndpointsObserver {
	void onFishingLineEndpointsChanged(float fishingLinePoleLocationX, float fishingLinePoleLocationY, float fishingLineFishLocationX,
			float fishingLineFishLocationY);
}
