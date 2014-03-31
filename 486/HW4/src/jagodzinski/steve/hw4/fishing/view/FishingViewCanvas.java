package jagodzinski.steve.hw4.fishing.view;

import jagodzinski.steve.hw4.fishing.R;
import jagodzinski.steve.hw4.fishing.controller.AngleChangeObserver;
import jagodzinski.steve.hw4.fishing.controller.FishingController;
import jagodzinski.steve.hw4.fishing.controller.FishingLineEndpointsObserver;
import jagodzinski.steve.hw4.fishing.controller.LineLengthObserver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class FishingViewCanvas extends View implements AngleChangeObserver, FishingLineEndpointsObserver, LineLengthObserver {
	
	private static final String LOGGING_TAG = FishingViewCanvas.class.getName();

	private Paint paint;

	private float fishingLinePoleLocationX;
	private float fishingLinePoleLocationY;
	private float fishingLineFishLocationX;
	private float fishingLineFishLocationY;

	private Integer lineLenght;

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

		initObservationOnController();
	}

	private void initObservationOnController() {
		FishingController.getInstance().addAngleChangeObserver(this, true);
		FishingController.getInstance().addFishingLineEndpointsObserver(this, true);
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
		drawLineLength(canvas);
	}

	private void drawLineLength(Canvas canvas) {
		String text;

		if (lineLenght == null) {
			text = getResources().getString(R.string.set_line_length);
		} else {
			text = getResources().getString(R.string.ft, lineLenght);
		}

		// TODO: Display line length text on path above line
	}

	private void drawAngle(Canvas canvas) {
		float xOffset = getResources().getDimension(R.dimen.angle_label_fishing_pole_margin_x);
		float yOffset = getResources().getDimension(R.dimen.angle_label_fishing_pole_margin_y);

		canvas.drawText(Short.toString(angle) + (char) 0x00B0, fishingLinePoleLocationX - xOffset, fishingLinePoleLocationY + yOffset, paint);
	}

	private void drawFishingLine(final Canvas canvas) {
		Log.d(LOGGING_TAG, String.format("Drawing Fishing Line From %f, %f to %f, %f", fishingLinePoleLocationX, fishingLinePoleLocationY,
				fishingLineFishLocationX, fishingLineFishLocationY));
		canvas.drawLine(fishingLinePoleLocationX, fishingLinePoleLocationY, fishingLineFishLocationX, fishingLineFishLocationY, paint);
	}

	@Override
	public void onFishingLineEndpointsChanged(float fishingLinePoleLocationX, float fishingLinePoleLocationY, float fishingLineFishLocationX,
			float fishingLineFishLocationY) {
		this.fishingLineFishLocationX = fishingLineFishLocationX;
		this.fishingLineFishLocationY = fishingLineFishLocationY;
		this.fishingLinePoleLocationX = fishingLinePoleLocationX;
		this.fishingLinePoleLocationY = fishingLinePoleLocationY;
		invalidate();
	}

	@Override
	public void onLineLengthChange(int newLineLength) {
		this.lineLenght = newLineLength;
		invalidate();
	}

	@Override
	public void onAngleChange(short newAngle) {
		this.angle = newAngle;
		invalidate();
	}
}
