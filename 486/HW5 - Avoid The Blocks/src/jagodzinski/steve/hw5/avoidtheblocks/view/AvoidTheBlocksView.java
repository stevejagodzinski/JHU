package jagodzinski.steve.hw5.avoidtheblocks.view;


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

	// TODO: Move to dimens.xml
	public static final int PLAYER_SIZE = 30;
	public static final float BLOCK_CIRCLE_RADIUS = 50;

	private static final int OFFSET_FROM_BOTTOM = 20;
	private static final int DIFFICULTY_CIRCLE_RADIUS = 100;
	private static final int DIFFICULTY_CIRCLE_WEIGHT = 5;
	private static final int DIFFICULFT_CIRCLE_MARGIN = 50;
	private static final int DIFFICULTY_CIRCLE_LOCATION_INDICATOR_RADIUSES = 20;
	
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

		gameState.setPlayerPosition(new Point(newWidth / 2, newHeight - PLAYER_SIZE - OFFSET_FROM_BOTTOM));
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
		
		canvas.drawCircle(easynessIndicatorLocation.x, easynessIndicatorLocation.y, DIFFICULTY_CIRCLE_LOCATION_INDICATOR_RADIUSES, greenPaint);
	}

	private void drawCurrentLocationOnDifficultyCircle(Canvas canvas) {
		Point indicatorLocation = calculatePointOnDifficultCircle(gameState.getCurrentLocation());

		Log.i(LOGGING_TAG, "Drawing current difficulty location indicator: (" + indicatorLocation.x + ", " + indicatorLocation.y + ")");

		canvas.drawCircle(indicatorLocation.x, indicatorLocation.y, DIFFICULTY_CIRCLE_LOCATION_INDICATOR_RADIUSES,
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
		int x = (int) ((getWidth() - DIFFICULFT_CIRCLE_MARGIN - DIFFICULTY_CIRCLE_RADIUS) + (DIFFICULTY_CIRCLE_RADIUS * Math.cos(Math.toRadians(angle))));
		int y = (int) ((DIFFICULFT_CIRCLE_MARGIN + DIFFICULTY_CIRCLE_RADIUS) + (DIFFICULTY_CIRCLE_RADIUS * Math.sin(Math.toRadians(angle))));
		return new Point(x, y);
	}

	private void drawDifficultyCircle(Canvas canvas) {
		int centerX = getWidth() - DIFFICULFT_CIRCLE_MARGIN - DIFFICULTY_CIRCLE_RADIUS;
		int centerY = DIFFICULFT_CIRCLE_MARGIN + DIFFICULTY_CIRCLE_RADIUS;

		Log.d(LOGGING_TAG, "Drawing difficulty circle at (" + centerX + ", " + centerY + "). Radius: " + DIFFICULTY_CIRCLE_RADIUS);

		canvas.drawCircle(centerX, centerY, DIFFICULTY_CIRCLE_RADIUS, whitePaint);
	}

	private void init() {
		yellowPaint = new Paint();
		yellowPaint.setColor(Color.YELLOW);

		greenPaint = new Paint();
		greenPaint.setColor(Color.GREEN);

		whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);
		whitePaint.setStyle(Style.STROKE);
		whitePaint.setStrokeWidth(DIFFICULTY_CIRCLE_WEIGHT);

		redPaint = new Paint();
		redPaint.setColor(Color.RED);
	}

	private void drawBlocks(final Canvas canvas) {
		synchronized (gameState.getBlocks()) {
			for (Point block : gameState.getBlocks()) {
				canvas.drawCircle(block.x, block.y, BLOCK_CIRCLE_RADIUS, yellowPaint);
			}
		}
	}

	private void drawPlayer(Canvas canvas) {
		canvas.drawRect(gameState.getPlayerPosition().x - PLAYER_SIZE, gameState.getPlayerPosition().y - PLAYER_SIZE, gameState.getPlayerPosition().x
				+ PLAYER_SIZE, gameState.getPlayerPosition().y + PLAYER_SIZE, greenPaint);
	}

	public void start() {
		stop();

		blockAnimationThread = new BlockAnimationThread(gameState, handler, invalidator, getWidth(), getHeight());

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
