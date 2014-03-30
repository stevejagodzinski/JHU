package jagodzinski.steve.hw4.fishing.view;

import jagodzinski.steve.hw4.fishing.R;
import jagodzinski.steve.hw4.fishing.controller.FishingLayoutObserver;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FishingView extends RelativeLayout {
	
	private static final String LOGGING_TAG = FishingView.class.getName();

	private Paint paint;

	private FishingLayoutObserver fishingCanvas;

	private ImageView boatImage;

	private ImageView fishImage;
	private AnimationDrawable fishAnimation;

	private float fishImageLocationX;
	private float fishImageLocationY;

	private boolean isFishSelected;

	public FishingView(Context context) {
		super(context);
	}

	public FishingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FishingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	private void init() {
		paint = new Paint();
		paint.setTextSize(14f);
		paint.setColor(Color.BLACK);

		initBoatBitmap();
		initFishBitmap();
		initFishingViewCanvas();

		initFishingPoleCoordinates();
		initFishCoordinates();
	}
	
	private void initFishingViewCanvas() {
		fishingCanvas = (FishingLayoutObserver) findViewById(R.id.fishingViewCanvas);
	}

	private void initBoatBitmap() {
		boatImage = (ImageView) findViewById(R.id.boat_image_view);
	}
	
	private void initFishBitmap() {
		fishImage = (ImageView) findViewById(R.id.fish_image_view);
		fishImage.setBackgroundResource(R.drawable.fish);
		fishAnimation = (AnimationDrawable) fishImage.getBackground();
	}

	private void initFishingPoleCoordinates() {
		int[] resultHolder = new int[2];
		boatImage.getLocationInWindow(resultHolder);

		float fishingLinePoleLocationX = .9f * boatImage.getWidth() + resultHolder[0];
		float fishingLinePoleLocationY = resultHolder[1] + boatImage.getPaddingTop();

		getLocationInWindow(resultHolder);
		fishingLinePoleLocationX -= resultHolder[0];
		fishingLinePoleLocationY -= resultHolder[1];

		fishingCanvas.onFishingRodLocationChanged(fishingLinePoleLocationX, fishingLinePoleLocationY);
	}

	private void initFishCoordinates() {
		int[] resultHolder = new int[2];
		fishImage.getLocationOnScreen(resultHolder);

		fishImageLocationX = resultHolder[0];
		fishImageLocationY = resultHolder[1];

		getLocationInWindow(resultHolder);
		fishImageLocationX -= resultHolder[0];
		fishImageLocationY -= resultHolder[1];

		fishingCanvas.onFishLocationChanged(fishImageLocationX, fishImageLocationY, fishImage.getHeight(), (28 / 61f));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		init();
	}

	private void updateFishCoordinates(float newCenterPointX, float newCenterPointY) {
		fishImageLocationX = Math.min(Math.max(newCenterPointX - (fishImage.getWidth() / 2f), 0), getWidth() - fishImage.getWidth());
		fishImageLocationY = Math.min(Math.max(newCenterPointY - (fishImage.getHeight() / 2f), 0), getHeight() - fishImage.getHeight());

		Log.d(LOGGING_TAG, String.format("Moving fish to new position: (%s, %s)", fishImageLocationX, fishImageLocationY));

		((RelativeLayout.LayoutParams) fishImage.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
		((RelativeLayout.LayoutParams) fishImage.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		((RelativeLayout.LayoutParams) fishImage.getLayoutParams()).setMargins(((int) fishImageLocationX), ((int) fishImageLocationY), 0, 0);

		fishingCanvas.onFishLocationChanged(fishImageLocationX, fishImageLocationY, fishImage.getHeight(), (28 / 61f));
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

		fishAnimation.stop();

		invalidate();
	}

	private void detectIsFishSelected(final MotionEvent event) {
		boolean eventXCoordinateIsWithinFish = fishImageLocationX <= event.getX() && (fishImageLocationX + fishImage.getWidth()) >= event.getX();
		boolean eventYCoordinateIsWithinFish = fishImageLocationY <= event.getY() && (fishImageLocationY + fishImage.getHeight()) >= event.getY();

		isFishSelected = eventXCoordinateIsWithinFish && eventYCoordinateIsWithinFish;
	}

	private void handleMoveFish(final MotionEvent event) {
		fishAnimation.start();
		updateFishCoordinates(event.getX(), event.getY());
		requestLayout();
		invalidate();
	}
}
