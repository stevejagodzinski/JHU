package jagodzinski.steve.hw4.fishing.controller;

import jagodzinski.steve.hw4.fishing.R;
import jagodzinski.steve.hw4.fishing.service.LineAngleService;
import jagodzinski.steve.hw4.fishing.service.LineDepthService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.res.Resources;

public class FishingController implements FishLocationObserver, FishingRodObserver, FishingLineEndpointsObserver, LineLengthObserver, AngleChangeObserver {

	private static final FishingController INSTANCE = new FishingController();

	private static final LineAngleService lineAngleService = LineAngleService.getInstance();
	private static final LineDepthService lineDepthService = LineDepthService.getInstance();

	private List<AngleChangeObserver> angleChangeObservers = new CopyOnWriteArrayList<AngleChangeObserver>();
	private List<FishingLineEndpointsObserver> lineEndpointObservers = new CopyOnWriteArrayList<FishingLineEndpointsObserver>();
	private List<LineDepthObserver> lineDepthObservers = new CopyOnWriteArrayList<LineDepthObserver>();

	private FishingController() {
		angleChangeObservers.add(this);
		lineEndpointObservers.add(this);
	}

	public static FishingController getInstance() {
		return INSTANCE;
	}

	private float fishImageLocationX;
	private float fishImageLocationY;

	private float fishingLinePoleLocationX;
	private float fishingLinePoleLocationY;
	private float fishingLineFishLocationX;
	private float fishingLineFishLocationY;

	private Integer lineLenght;

	private short angle;

	private Integer lineDepth;

	@Override
	public void onLineLengthChange(int newLineLength) {
		lineLenght = newLineLength;

		updateLineDepth();
	}

	@Override
	public void onFishLocationChanged(float newX, float newY, Resources resources) {
		fishImageLocationX = newX;
		fishImageLocationY = newY;

		updateFishingLineCoordinates(resources);
	}

	@Override
	public void onFishingRodLocationChanged(float newX, float newY, Resources resources) {
		fishingLinePoleLocationX = newX;
		fishingLinePoleLocationY = newY;

		notifyFishingLineEndpointObservers();
	}

	@Override
	public void onAngleChange(short newAngle) {
		updateLineDepth();
	}

	@Override
	public void onFishingLineEndpointsChanged(float fishingLinePoleLocationX, float fishingLinePoleLocationY, float fishingLineFishLocationX,
			float fishingLineFishLocationY) {
		updateLineAngle();
	}

	private void updateFishingLineCoordinates(Resources resources) {
		float imageHeight = resources.getDimension(R.dimen.fish_height);
		float relativeMouthPosition = resources.getDimension(R.dimen.fish_mouth_location) / imageHeight;

		fishingLineFishLocationX = fishImageLocationX + 2;
		fishingLineFishLocationY = fishImageLocationY + (relativeMouthPosition * imageHeight);

		notifyFishingLineEndpointObservers();
	}

	private void updateLineAngle() {
		angle = (short) Math.round(lineAngleService.calculateLineAngle(fishingLinePoleLocationX, fishingLinePoleLocationY, fishingLineFishLocationX,
				fishingLineFishLocationY));

		notifyAngleChangeObservers();
	}

	private void updateLineDepth() {
		if (lineLenght != null) {
			lineDepth = (int) lineDepthService.calculateLineDepth(lineLenght, angle);
			notifyLineLengthObservers();
		}
	}

	private void notifyFishingLineEndpointObservers() {
		for (FishingLineEndpointsObserver observer : lineEndpointObservers) {
			observer.onFishingLineEndpointsChanged(fishingLinePoleLocationX, fishingLinePoleLocationY, fishingLineFishLocationX, fishingLineFishLocationY);
		}
	}

	private void notifyAngleChangeObservers() {
		for (AngleChangeObserver observer : angleChangeObservers) {
			observer.onAngleChange(angle);
		}
	}

	private void notifyLineLengthObservers() {
		for (LineDepthObserver observer : lineDepthObservers) {
			observer.onLineDepthChanged(lineDepth);
		}
	}

	public void addAngleChangeObserver(AngleChangeObserver observer, boolean triggerNotify) {
		angleChangeObservers.add(observer);

		if (triggerNotify) {
			observer.onAngleChange(angle);
		}
	}

	public void addFishingLineEndpointsObserver(FishingLineEndpointsObserver observer, boolean triggerNotify) {
		lineEndpointObservers.add(observer);

		if (triggerNotify) {
			observer.onFishingLineEndpointsChanged(fishingLinePoleLocationX, fishingLinePoleLocationY, fishingLineFishLocationX, fishingLineFishLocationY);
		}
	}

	public void addLineDepthObserverObserver(LineDepthObserver observer) {
		lineDepthObservers.add(observer);
	}
}
