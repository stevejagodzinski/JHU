package jagodzinski.steve.hw4.fishing.view;

import jagodzinski.steve.hw4.fishing.R;
import jagodzinski.steve.hw4.fishing.controller.FishLocationObserver;
import jagodzinski.steve.hw4.fishing.controller.FishingController;
import jagodzinski.steve.hw4.fishing.controller.FishingRodObserver;
import jagodzinski.steve.hw4.fishing.controller.LineDepthObserver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FishingView extends RelativeLayout implements LineDepthObserver {
	
	private static final String LOGGING_TAG = FishingView.class.getName();

	private List<FishLocationObserver> fishLocationObservers;
	private List<FishingRodObserver> fishingRodObserver;

	private Paint paint;

	private ImageView boatImage;

	private ImageView fishImage;
	private AnimationDrawable fishAnimation;

	private float fishImageLocationX;
	private float fishImageLocationY;

	private boolean isFishSelected;

	private TextView lineDepthTextView;

	public FishingView(Context context) {
		super(context);
	}

	public FishingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FishingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	private boolean isInitialized() {
		return paint != null;
	}

	private void init() {
		paint = new Paint();
		paint.setTextSize(14f);
		paint.setColor(Color.BLACK);

		initBoatBitmap();
		initFishBitmap();
		initLineDepthTextView();

		initFishingRodLocationObservers();
		initFishLocationChangedObservers();
		initObservationOnController();

		initFishingPoleCoordinates();
		initFishCoordinates();
	}
	
	private void initObservationOnController() {
		FishingController.getInstance().addLineDepthObserverObserver(this);
	}

	private void initFishLocationChangedObservers() {
		fishLocationObservers = new CopyOnWriteArrayList<FishLocationObserver>();
		fishLocationObservers.add(FishingController.getInstance());
	}

	private void initFishingRodLocationObservers() {
		fishingRodObserver = new CopyOnWriteArrayList<FishingRodObserver>();
		fishingRodObserver.add(FishingController.getInstance());
	}

	private void notifyFishLocationChangedObservers() {
		for (FishLocationObserver observer : fishLocationObservers) {
			observer.onFishLocationChanged(fishImageLocationX, fishImageLocationY, getResources());
		}
	}

	private void notifyFishingRodLocationChangedObservers(float fishingLinePoleLocationX, float fishingLinePoleLocationY) {
		for (FishingRodObserver observer : fishingRodObserver) {
			observer.onFishingRodLocationChanged(fishingLinePoleLocationX, fishingLinePoleLocationY, getResources());
		}
	}

	private void initLineDepthTextView() {
		lineDepthTextView = (TextView) findViewById(R.id.line_depth_text_view);
	}

	private void initBoatBitmap() {
		boatImage = (ImageView) findViewById(R.id.boat_image_view);
	}
	
	private void initFishBitmap() {
		fishImage = (ImageView) findViewById(R.id.fish_image_view);
		makeFishBlackAndWhite();
	}

	private void makeFishBlackAndWhite() {
		Log.d(LOGGING_TAG, "Making fish black and white");
		fishImage.setBackgroundResource(R.drawable.fish_grayscale);
	}

	private void makeFishColor() {
		Log.d(LOGGING_TAG, "Making fish colored");
		fishImage.setBackgroundResource(R.drawable.fish);
		fishAnimation = (AnimationDrawable) fishImage.getBackground();
	}

	private void animateFish() {
		Log.d(LOGGING_TAG, "Animating fish");
		fishAnimation.start();
	}

	private void initFishingPoleCoordinates() {
		int[] resultHolder = new int[2];
		boatImage.getLocationInWindow(resultHolder);

		float fishingLinePoleLocationX = getResources().getDimension(R.dimen.fishing_pole_location) + resultHolder[0];
		float fishingLinePoleLocationY = resultHolder[1] + boatImage.getPaddingTop();

		getLocationInWindow(resultHolder);
		fishingLinePoleLocationX -= resultHolder[0];
		fishingLinePoleLocationY -= resultHolder[1];

		notifyFishingRodLocationChangedObservers(fishingLinePoleLocationX, fishingLinePoleLocationY);
	}

	private void initFishCoordinates() {
		int[] resultHolder = new int[2];
		fishImage.getLocationOnScreen(resultHolder);

		fishImageLocationX = resultHolder[0];
		fishImageLocationY = resultHolder[1];

		getLocationInWindow(resultHolder);
		fishImageLocationX -= resultHolder[0];
		fishImageLocationY -= resultHolder[1];

		notifyFishLocationChangedObservers();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if (!isInitialized()) {
			init();
		}
	}

	private void updateFishCoordinates(float newCenterPointX, float newCenterPointY) {
		fishImageLocationX = Math.min(Math.max(newCenterPointX - (fishImage.getWidth() / 2f), 0), getWidth() - fishImage.getWidth());
		fishImageLocationY = Math.min(Math.max(newCenterPointY - (fishImage.getHeight() / 2f), 0), getHeight() - fishImage.getHeight());

		Log.d(LOGGING_TAG, String.format("Moving fish to new position: (%s, %s)", fishImageLocationX, fishImageLocationY));

		((RelativeLayout.LayoutParams) fishImage.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
		((RelativeLayout.LayoutParams) fishImage.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		((RelativeLayout.LayoutParams) fishImage.getLayoutParams()).setMargins(((int) fishImageLocationX), ((int) fishImageLocationY), 0, 0);

		notifyFishLocationChangedObservers();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				handleDownActionEvent(event);
				break;
			case MotionEvent.ACTION_MOVE:
				handleMoveActionEvent(event);
				break;
			case MotionEvent.ACTION_UP:
				handleUpActionEvent(event);
				break;
		}
		return true;
	}

	private void handleDownActionEvent(final MotionEvent event) {
		Log.d(LOGGING_TAG, "Handling ACTION_DOWN");
		detectIsFishSelected(event);

		if (isFishSelected) {
			handleFishClicked(event);
		}
	}

	private void handleFishClicked(final MotionEvent event) {
		makeFishColor();
	}

	private void handleMoveActionEvent(final MotionEvent event) {
		if (isFishSelected()) {
			handleMoveFish(event);
		}
	}

	private boolean isFishSelected() {
		Log.d(LOGGING_TAG, "Checking if fish is selected.");
		Log.d(LOGGING_TAG, isFishSelected ? "Fish is selected." : "Fish is not selected.");
		return isFishSelected;
	}

	private void handleUpActionEvent(final MotionEvent event) {
		isFishSelected = false;
		makeFishBlackAndWhite();
		invalidate();
	}

	private void detectIsFishSelected(final MotionEvent event) {
		boolean eventXCoordinateIsWithinFish = fishImageLocationX <= event.getX() && (fishImageLocationX + fishImage.getWidth()) >= event.getX();
		boolean eventYCoordinateIsWithinFish = fishImageLocationY <= event.getY() && (fishImageLocationY + fishImage.getHeight()) >= event.getY();

		isFishSelected = eventXCoordinateIsWithinFish && eventYCoordinateIsWithinFish;
	}

	private void handleMoveFish(final MotionEvent event) {
		animateFish();
		updateFishCoordinates(event.getX(), event.getY());
		requestLayout();
		invalidate();
	}

	@Override
	public void onLineDepthChanged(Integer newLineDepth) {
		lineDepthTextView.setText(getResources().getString(R.string.line_depth, newLineDepth));
		lineDepthTextView.setVisibility(TextView.VISIBLE);
	}
}
