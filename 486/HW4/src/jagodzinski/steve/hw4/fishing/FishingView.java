package jagodzinski.steve.hw4.fishing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class FishingView extends View {
	
	private static final String LOGGING_TAG = FishingView.class.getName();

	private static final float MARGIN_TOP = 10f;

	private Paint paint;
	
	private Bitmap boatBitmap;
	private Bitmap fishBitmap;

	private float fishBitmapLocationX;
	private float fishBitmapLocationY;

	private float fishingLinePoleLocationX;
	private float fishingLinePoleLocationY;
	private float fishingLinefishLocationX;
	private float fishingLinefishLocationY;

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
		
		initBoatBitmap();
		initFishBitmap();

		initFishingPoleCoordinates();
		initFishCoordinates();
		initFishingLineCoordinates();
	}
	
	private void initBoatBitmap() {
		Bitmap unscaledBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boat);
		
		float scale = 0.42f;
		Matrix matrix = new Matrix();
		matrix.preScale(scale, scale);
		boatBitmap = Bitmap.createBitmap(unscaledBitmap, 0, 0, unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), matrix, true);
	}
	
	private void initFishBitmap() {
		Bitmap unscaledBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fish0);
		fishBitmap = Bitmap.createBitmap(unscaledBitmap);
	}

	private void initFishingPoleCoordinates() {
		fishingLinePoleLocationX = .9f * boatBitmap.getWidth();
		fishingLinePoleLocationY = MARGIN_TOP;
	}

	private void initFishCoordinates() {
		fishBitmapLocationX = ((getWidth() - fishingLinePoleLocationX) / 2f) + fishingLinePoleLocationX - 2;
		fishBitmapLocationY = (0.75f * (getHeight() - fishingLinePoleLocationY)) + fishingLinePoleLocationY - ((28 / 61f) * fishBitmap.getHeight());
	}

	private void initFishingLineCoordinates() {
		updateFishingLineCoordinates();
	}

	private void updateFishingLineCoordinates() {
		fishingLinefishLocationX = fishBitmapLocationX + 2;
		fishingLinefishLocationY = fishBitmapLocationY + ((28 / 61f) * fishBitmap.getHeight());
	}

	private void updateFishCoordinates(float newCenterPointX, float newCenterPointY) {
		fishBitmapLocationX = newCenterPointX - (fishBitmap.getWidth() / 2f);
		fishBitmapLocationY = newCenterPointY - (fishBitmap.getHeight() / 2f);

		Log.d(LOGGING_TAG, String.format("Moving fish to new position: (%s, %s)", fishBitmapLocationX, fishBitmapLocationY));
	}

	private boolean isInitialized() {
		return paint != null;
	}
	
	@Override
	protected void onDraw(final Canvas canvas) {
		doDraw(canvas);
	}

	private void doDraw(final Canvas canvas) {
		Log.d(LOGGING_TAG, "Drawing canvas");
		if (!isInitialized()) {
			init();
		}
		
		drawBoat(canvas);
		drawFish(canvas);
		drawFishingLine(canvas);
	}
	
	private void drawBoat(final Canvas canvas) {
		canvas.drawBitmap(boatBitmap, 0, MARGIN_TOP, null);
	}

	private void drawFishingLine(final Canvas canvas) {
		Log.d(LOGGING_TAG, String.format("Drawing Fishing Line From %f, %f to %f, %f", fishingLinePoleLocationX, fishingLinePoleLocationY,
				fishingLinefishLocationX, fishingLinefishLocationY));
		canvas.drawLine(fishingLinePoleLocationX, fishingLinePoleLocationY, fishingLinefishLocationX, fishingLinefishLocationY, paint);
	}

	private void drawFish(final Canvas canvas) {
		Log.d(LOGGING_TAG, String.format("Drawing Fish At %f, %f", fishBitmapLocationX, fishBitmapLocationY));
		canvas.drawBitmap(fishBitmap, fishBitmapLocationX, fishBitmapLocationY, paint);
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
		invalidate();
	}

	private void detectIsFishSelected(final MotionEvent event) {
		boolean eventXCoordinateIsWithinFish = fishBitmapLocationX <= event.getX() && (fishBitmapLocationX + fishBitmap.getWidth()) >= event.getX();
		boolean eventYCoordinateIsWithinFish = fishBitmapLocationY <= event.getY() && (fishBitmapLocationY + fishBitmap.getHeight()) >= event.getY();

		isFishSelected = eventXCoordinateIsWithinFish && eventYCoordinateIsWithinFish;
	}

	private void handleMoveFish(final MotionEvent event) {
		updateFishCoordinates(event.getX(), event.getY());
		updateFishingLineCoordinates();
		invalidate();
	}
}
