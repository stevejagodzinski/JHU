package jagodzinski.steve.hw4.fishing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class FishingView extends View {
	
	private static final float MARGIN_TOP = 10f;

	private Paint paint;
	
	private Bitmap boatBitmap;
	private Bitmap fishBitmap;

	private float fishingLinePoleLocationX;
	private float fishingLinePoleLocationY;
	private float fishingLinefishLocationX;
	private float fishingLinefishLocationY;

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

		// float scale = 0.42f;
		// Matrix matrix = new Matrix();
		// matrix.preScale(scale, scale);
		fishBitmap = Bitmap.createBitmap(unscaledBitmap);
	}

	private void initFishingLineCoordinates() {
		fishingLinePoleLocationX = .9f * boatBitmap.getWidth();
		fishingLinePoleLocationY = MARGIN_TOP;
		fishingLinefishLocationX = ((getWidth() - fishingLinePoleLocationX) / 2) + fishingLinePoleLocationX;
		fishingLinefishLocationY = (0.75f * (getHeight() - fishingLinePoleLocationY)) + fishingLinePoleLocationY;
	}

	private boolean isInitialized() {
		return paint != null;
	}
	
	@Override
	protected void onDraw(final Canvas canvas) {
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
		canvas.drawLine(fishingLinePoleLocationX, fishingLinePoleLocationY, fishingLinefishLocationX, fishingLinefishLocationY, paint);
	}

	private void drawFish(final Canvas canvas) {
		canvas.drawBitmap(fishBitmap, fishingLinefishLocationX - 2, fishingLinefishLocationY - (fishBitmap.getHeight() / 2), paint);
	}
}
