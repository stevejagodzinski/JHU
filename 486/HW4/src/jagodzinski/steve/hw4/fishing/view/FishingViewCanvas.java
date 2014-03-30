package jagodzinski.steve.hw4.fishing.view;

import jagodzinski.steve.hw4.fishing.controller.FishingLayoutObserver;
import jagodzinski.steve.hw4.fishing.service.LineAngleService;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class FishingViewCanvas extends View implements FishingLayoutObserver {
	
	private static final String LOGGING_TAG = FishingViewCanvas.class.getName();

	private static final LineAngleService lineAngleService = LineAngleService.getInstance();

	private Paint paint;

	private float fishBitmapLocationX;
	private float fishBitmapLocationY;

	private float fishingLinePoleLocationX;
	private float fishingLinePoleLocationY;
	private float fishingLineFishLocationX;
	private float fishingLineFishLocationY;

	private short angle;

	public FishingViewCanvas(Context context) {
		super(context);
	}

	public FishingViewCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FishingViewCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	private void init() {
		paint = new Paint();
		paint.setTextSize(14f);
		paint.setColor(Color.BLACK);
	}

	private void updateFishingLineCoordinates(float imageHeight, float relativeMouthPosition) {
		fishingLineFishLocationX = fishBitmapLocationX + 2;
		fishingLineFishLocationY = fishBitmapLocationY + (relativeMouthPosition * imageHeight);
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

		drawFishingLine(canvas);
		drawAngle(canvas);
	}

	private void drawAngle(Canvas canvas) {
		canvas.drawText(Short.toString(angle) + (char) 0x00B0, fishingLinePoleLocationX - 40, fishingLinePoleLocationY + 20, paint);
	}

	private void drawFishingLine(final Canvas canvas) {
		Log.d(LOGGING_TAG, String.format("Drawing Fishing Line From %f, %f to %f, %f", fishingLinePoleLocationX, fishingLinePoleLocationY,
				fishingLineFishLocationX, fishingLineFishLocationY));
		canvas.drawLine(fishingLinePoleLocationX, fishingLinePoleLocationY, fishingLineFishLocationX, fishingLineFishLocationY, paint);
	}

	private void updateLineAngle() {
		angle = (short) Math.round(lineAngleService.calculateLineAngle(fishingLinePoleLocationX, fishingLinePoleLocationY, fishingLineFishLocationX,
				fishingLineFishLocationY));
	}

	@Override
	public void onFishLocationChanged(float newX, float newY, float imageHeight, float relativeMouthPosition) {
		fishBitmapLocationX = newX;
		fishBitmapLocationY = newY;

		updateFishingLineCoordinates(imageHeight, relativeMouthPosition);
		updateLineAngle();
		invalidate();
	}

	@Override
	public void onFishingRodLocationChanged(float newX, float newY) {
		fishingLinePoleLocationX = newX;
		fishingLinePoleLocationY = newY;

		updateLineAngle();
		invalidate();
	}
}
