package jagodzinski.steve.hw5.avoidtheblocks.view;


import jagodzinki.steve.hw5.avoidtheblocks.R;
import jagodzinski.steve.hw5.avoidtheblocks.model.GameState;
import jagodzinski.steve.hw5.avoidtheblocks.model.ICollisionObserver;
import jagodzinski.steve.hw5.avoidtheblocks.model.controller.BlockAnimationThread;
import jagodzinski.steve.hw5.avoidtheblocks.model.controller.BlockCreationThread;
import jagodzinski.steve.hw5.avoidtheblocks.model.controller.DifficultyChanger;
import jagodzinski.steve.hw5.avoidtheblocks.service.DifficultyCalculationUtil;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AvoidTheBlocksView extends View {

	private static final String LOGGING_TAG = AvoidTheBlocksView.class.getName();

	public int playerSize;
	public float blockCircleRadius;
	private int difficultyCircleRadius;
	private int difficultyCircleWeight;
	private int dificultyCircleMargin;
	private int difficultyCircleLocationIndicatorRadisuses;
	
	private GameState gameState;

	private Handler handler = new Handler();
	
	private Collection<ICollisionObserver> collisionObservers = new CopyOnWriteArraySet<ICollisionObserver>();

	private Runnable invalidator = new Runnable() {
		@Override
		public void run() {
			invalidate();
		}
	};

	private Paint yellowPaint;
	private Paint greenPaint;
	private Paint whitePaint;
	private Paint redPaint;

	private BlockAnimationThread blockAnimationThread;
	private BlockCreationThread blockCreationThread;
	private Thread difficultyChangerThread;

	public AvoidTheBlocksView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public AvoidTheBlocksView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AvoidTheBlocksView(Context context) {
		super(context);
	}

	private void initializeDimensions() {
		playerSize = (int) getContext().getResources().getDimension(R.dimen.player_size_from_center);
		blockCircleRadius = getContext().getResources().getDimension(R.dimen.block_circle_radius);
		difficultyCircleRadius = (int) getContext().getResources().getDimension(R.dimen.difficulty_circle_radius);
		difficultyCircleWeight = (int) getContext().getResources().getDimension(R.dimen.difficulty_circle_weight);
		dificultyCircleMargin = (int) getContext().getResources().getDimension(R.dimen.difficulty_circle_margin);
		difficultyCircleLocationIndicatorRadisuses = (int) getContext().getResources().getDimension(R.dimen.difficulty_circle_location_indicator_radiuses);
	}

	private boolean isInitialized() {
		return yellowPaint != null;
	}

	@Override
	protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
		super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

		if (blockCreationThread != null) {
			blockCreationThread.setViewWidth(newWidth);
		}

		if (blockAnimationThread != null) {
			blockAnimationThread.setViewHeight(newHeight);
			blockAnimationThread.setViewWidth(newWidth);
		}

		gameState.setPlayerPosition(new Point(newWidth / 2, newHeight - ((int) getContext().getResources().getDimension(R.dimen.player_size_from_center))
				- ((int) getContext().getResources().getDimension(R.dimen.offset_from_bottom))));
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		Log.d(LOGGING_TAG, "Drawing canvas");

		if (!isInitialized()) {
			init();
		}

		doDraw(canvas);
	}

	private void doDraw(final Canvas canvas) {
		drawBlocks(canvas);
		drawPlayer(canvas);
		drawDifficultyCircle(canvas);
		drawEasyLocationOnDifficultyCircle(canvas);
		drawCurrentLocationOnDifficultyCircle(canvas);
	}

	private void drawEasyLocationOnDifficultyCircle(Canvas canvas) {
		Point easynessIndicatorLocation = calculatePointOnDifficultCircle(gameState.getEasyLocation());
		
		Log.d(LOGGING_TAG, "Drawing easy indicator: (" + easynessIndicatorLocation.x + ", " + easynessIndicatorLocation.y + ")");
		
		canvas.drawCircle(easynessIndicatorLocation.x, easynessIndicatorLocation.y, difficultyCircleLocationIndicatorRadisuses, greenPaint);
	}

	private void drawCurrentLocationOnDifficultyCircle(Canvas canvas) {
		Point indicatorLocation = calculatePointOnDifficultCircle(gameState.getCurrentLocation());

		Log.i(LOGGING_TAG, "Drawing current difficulty location indicator: (" + indicatorLocation.x + ", " + indicatorLocation.y + ")");

		canvas.drawCircle(indicatorLocation.x, indicatorLocation.y, difficultyCircleLocationIndicatorRadisuses,
				getColoredPaintForCurrentDifficultyLocation());
	}

	private Paint getColoredPaintForCurrentDifficultyLocation() {
		Paint paint;

		switch (gameState.getDifficulty()) {
			case EASY:
				paint = greenPaint;
				break;
			case MEDIUM:
				paint = yellowPaint;
				break;
			case HARD:
				paint = redPaint;
				break;
			default:
				throw new UnsupportedOperationException("Unable to calculate color for difficulty indicator. Unspported difficulty: "
						+ gameState.getDifficulty());
		}

		return paint;
	}

	private Point calculatePointOnDifficultCircle(int angle) {
		int x = (int) ((getWidth() - dificultyCircleMargin - difficultyCircleRadius) + (difficultyCircleRadius * Math.cos(Math.toRadians(angle))));
		int y = (int) ((dificultyCircleMargin + difficultyCircleRadius) + (difficultyCircleRadius * Math.sin(Math.toRadians(angle))));
		return new Point(x, y);
	}

	private void drawDifficultyCircle(Canvas canvas) {
		int centerX = getWidth() - dificultyCircleMargin - difficultyCircleRadius;
		int centerY = dificultyCircleMargin + difficultyCircleRadius;

		Log.d(LOGGING_TAG, "Drawing difficulty circle at (" + centerX + ", " + centerY + "). Radius: " + difficultyCircleRadius);

		canvas.drawCircle(centerX, centerY, difficultyCircleRadius, whitePaint);
	}

	private void init() {
		yellowPaint = new Paint();
		yellowPaint.setColor(Color.YELLOW);

		greenPaint = new Paint();
		greenPaint.setColor(Color.GREEN);

		whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);
		whitePaint.setStyle(Style.STROKE);
		whitePaint.setStrokeWidth(difficultyCircleWeight);

		redPaint = new Paint();
		redPaint.setColor(Color.RED);

		initializeDimensions();
	}

	private void drawBlocks(final Canvas canvas) {
		synchronized (gameState.getBlocks()) {
			for (Point block : gameState.getBlocks()) {
				canvas.drawCircle(block.x, block.y, blockCircleRadius, yellowPaint);
			}
		}
	}

	private void drawPlayer(Canvas canvas) {
		canvas.drawRect(gameState.getPlayerPosition().x - playerSize, gameState.getPlayerPosition().y - playerSize, gameState.getPlayerPosition().x
				+ playerSize, gameState.getPlayerPosition().y + playerSize, greenPaint);
	}

	public void start() {
		stop();

		blockAnimationThread = new BlockAnimationThread(gameState, handler, invalidator, getWidth(), getHeight(), getContext());

		for (ICollisionObserver collisionObserver : collisionObservers) {
			blockAnimationThread.addCollisionObserver(collisionObserver);
		}

		blockAnimationThread.start();

		blockCreationThread = new BlockCreationThread(gameState, getWidth());
		blockCreationThread.start();
		
		difficultyChangerThread = new DifficultyChanger(gameState);
		difficultyChangerThread.start();
	}

	public void stop() {
		if (blockAnimationThread != null) {
			blockAnimationThread.interrupt();
			blockAnimationThread = null;
		}

		if (blockCreationThread != null) {
			blockCreationThread.interrupt();
			blockCreationThread = null;
		}
		
		if(difficultyChangerThread != null) {
			difficultyChangerThread.interrupt();
			difficultyChangerThread = null;
		}
	}

	public void setCompass(short compass) {
		gameState.setCurrentLocation(compass);
		updateDifficulty(compass);
	}
	
	private void updateDifficulty(short currentCompass) {
		gameState.setDifficulty(DifficultyCalculationUtil.calculateDifficulty(currentCompass, gameState.getEasyLocation()));
		blockCreationThread.updateBlockCreationRate();
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public void addCollisionObserver(ICollisionObserver observer) {
		collisionObservers.add(observer);

		if (blockAnimationThread != null) {
			blockAnimationThread.addCollisionObserver(observer);
		}
	}
}
