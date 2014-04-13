package jagodzinski.steve.hw5.avoidtheblocks.view;


import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AvoidTheBlocksView extends View {

	private static final String LOGGING_TAG = AvoidTheBlocksView.class.getName();

	private static final long CHANGE_DIFFICULTY_FREQUENCY_MS = 3000;
	
	private static final long BRICK_FALL_RATE_MS_EASY = 10;
	private static final long BRICK_FALL_RATE_MS_MEDIUM = BRICK_FALL_RATE_MS_EASY / 2;
	private static final long BRICK_FALL_RATE_MS_HARD = BRICK_FALL_RATE_MS_MEDIUM / 2;

	private static final long BRICK_CREATION_RATE_MS_EASY = 2000;
	private static final long BRICK_CREATION_RATE_MS_MEDIUM = BRICK_CREATION_RATE_MS_EASY / 2;
	private static final long BRICK_CREATION_RATE_MS_HARD = BRICK_CREATION_RATE_MS_MEDIUM / 2;

	// TODO: Move to dimens.xml
	private static final float BLOCK_CIRCLE_RADIUS = 50;
	private static final int PLAYER_SIZE = 30;
	private static final int OFFSET_FROM_BOTTOM = 20;
	private static final int DIFFICULTY_CIRCLE_RADIUS = 100;
	private static final int DIFFICULTY_CIRCLE_WEIGHT = 5;
	private static final int DIFFICULFT_CIRCLE_MARGIN = 50;
	private static final int DIFFICULTY_CIRCLE_LOCATION_INDICATOR_RADIUSES = 20;
	
	private static final long[] COLLISION_VIBRAION_PATTERN = new long[] { 0, 250, 200, 250, 150, 150, 75, 150, 75, 150 };

	private Difficulty difficulty;

	private long animationRefreshIntervalMS = BRICK_FALL_RATE_MS_EASY;
	public long brickCreationRate = BRICK_CREATION_RATE_MS_EASY;

	private Collection<Point> blocks = new LinkedList<Point>();

	private Point playerPosition;
	private float playerVelocity;
	private float playerAcceleration;
	
	private int easyLocation;
	private int currentLocation;

	private Handler handler = new Handler();
	
	private Random random = new Random();

	private Runnable invalidator = new Runnable() {
		@Override
		public void run() {
			invalidate();
		}
	};

	private Runnable collisionHandler = new CollissionHandler();

	private Paint yellowPaint;
	private Paint greenPaint;
	private Paint whitePaint;
	private Paint redPaint;

	private Thread blockAnimationThread;
	private Thread blockCreationThread;
	private Thread difficultyChangerThread;

	private Vibrator vibrator;

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
		Point easynessIndicatorLocation = calculatePointOnDifficultCircle(easyLocation);
		
		Log.d(LOGGING_TAG, "Drawing easy indicator: (" + easynessIndicatorLocation.x + ", " + easynessIndicatorLocation.y + ")");
		
		canvas.drawCircle(easynessIndicatorLocation.x, easynessIndicatorLocation.y, DIFFICULTY_CIRCLE_LOCATION_INDICATOR_RADIUSES, greenPaint);
	}

	private void drawCurrentLocationOnDifficultyCircle(Canvas canvas) {
		Point indicatorLocation = calculatePointOnDifficultCircle(currentLocation);

		Log.i(LOGGING_TAG, "Drawing current difficulty location indicator: (" + indicatorLocation.x + ", " + indicatorLocation.y + ")");

		canvas.drawCircle(indicatorLocation.x, indicatorLocation.y, DIFFICULTY_CIRCLE_LOCATION_INDICATOR_RADIUSES,
				getColoredPaintForCurrentDifficultyLocation());
	}

	private Paint getColoredPaintForCurrentDifficultyLocation() {
		Paint paint;

		switch (difficulty) {
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
				throw new UnsupportedOperationException("Unable to calculate color for difficulty indicator. Unspported difficulty: " + difficulty);
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

		// TODO: move to dimens
		playerPosition = new Point(getWidth() / 2, getHeight() - PLAYER_SIZE - OFFSET_FROM_BOTTOM);

		vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
	}

	private void drawBlocks(final Canvas canvas) {
		synchronized (blocks) {
			for (Point block : blocks) {
				canvas.drawCircle(block.x, block.y, BLOCK_CIRCLE_RADIUS, yellowPaint);
			}
		}
	}

	private void drawPlayer(Canvas canvas) {
		canvas.drawRect(playerPosition.x - PLAYER_SIZE, playerPosition.y - PLAYER_SIZE, playerPosition.x + PLAYER_SIZE, playerPosition.y + PLAYER_SIZE,
				greenPaint);
	}

	public void start() {
		stop();

		blockAnimationThread = new BlockAnimationThread();
		blockAnimationThread.start();

		blockCreationThread = new AvoidTheBlocksCreationThread();
		blockCreationThread.start();
		
		difficultyChangerThread = new DifficultyChanger();
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

	private class BlockAnimationThread extends Thread {
		@Override
		public void run() {
			while (!isInterrupted()) {
				movePlayer();

				synchronized (blocks) {
					moveBlocksDown();
					removeOffScreenBlocks();
					checkForCollisions();
				}

				handler.post(invalidator);
				try {
					sleep(animationRefreshIntervalMS);
				} catch (InterruptedException e) {
					interrupt();
				}
			}
		}

		private void movePlayer() {
			if (playerPosition != null) {
				playerVelocity += playerAcceleration * (animationRefreshIntervalMS / 1000.0);
				playerPosition.x = (int) Math.max(
						Math.min(playerPosition.x + playerVelocity * (animationRefreshIntervalMS / 1000.0), getWidth() - PLAYER_SIZE), PLAYER_SIZE);

				if ((playerPosition.x == PLAYER_SIZE && playerAcceleration <= 0) || (playerPosition.x == getWidth() - PLAYER_SIZE && playerAcceleration >= 0)) {
					playerVelocity = 0;
				}

				Log.d(LOGGING_TAG, "Player moved.");
				Log.d(LOGGING_TAG, "Player acceleration: " + playerAcceleration);
				Log.d(LOGGING_TAG, "Player velocity: " + playerVelocity);
				Log.d(LOGGING_TAG, "Player position: (" + playerPosition.x + ", " + playerPosition.y + ")");
			}
		}

		private void moveBlocksDown() {
			for (Point point : blocks) {
				point.y++;
			}

		}

		private void removeOffScreenBlocks() {
			Iterator<Point> blockIterator = blocks.iterator();

			while (blockIterator.hasNext()) {
				if (blockIterator.next().y > getHeight()) {
					blockIterator.remove();
					Log.i(LOGGING_TAG, "Block removed from screen");
				}
			}
		}
	}

	private class AvoidTheBlocksCreationThread extends Thread {
		@Override
		public void run() {
			while (!isInterrupted()) {
				if (getHeight() > 0) {
					addNewBlock();
				}
				try {
					sleep(brickCreationRate);
				} catch (InterruptedException e) {
					interrupt();
				}
			}
		}

		private void addNewBlock() {
			synchronized (blocks) {
				blocks.add(createNewBlock());
			}
		}

		private Point createNewBlock() {
			return new Point(random.nextInt(getWidth()), 0);
		}
	}

	public void acceleratePlayer(int acceleration) {
		playerAcceleration = acceleration;
	}

	public void setCompass(int compass) {
		currentLocation = compass;
		updateDifficulty(compass);
	}
	
	private void updateDifficulty(int currentCompass) {
		difficulty = calculateDifficulty(currentCompass);

		switch (difficulty) {
			case EASY:
				animationRefreshIntervalMS = BRICK_FALL_RATE_MS_EASY;
				brickCreationRate = BRICK_CREATION_RATE_MS_EASY;
				break;
			case MEDIUM:
				animationRefreshIntervalMS = BRICK_FALL_RATE_MS_MEDIUM;
				brickCreationRate = BRICK_CREATION_RATE_MS_MEDIUM;
				break;
			case HARD:
				animationRefreshIntervalMS = BRICK_FALL_RATE_MS_HARD;
				brickCreationRate = BRICK_CREATION_RATE_MS_HARD;
				break;
			default:
				Log.e(LOGGING_TAG, "Error updating difficulty. Unspported difficulty: " + difficulty);
		}
	}

	private Difficulty calculateDifficulty(int currentCompass) {
		int accuracyBetweenPoints = Math.abs(easyLocation - currentCompass);
		int accuractAroundNorth = currentCompass + (360 - easyLocation);
		int accuracy = Math.min(accuracyBetweenPoints, accuractAroundNorth);

		Difficulty difficulty;
		
		if (accuracy <= 120) {
			difficulty = Difficulty.EASY;
		} else if (accuracy <= 240) {
			difficulty = Difficulty.MEDIUM;
		} else {
			difficulty = Difficulty.HARD;
		}

		Log.d(LOGGING_TAG, "Calculating difficulty");
		Log.d(LOGGING_TAG, "Current Compass: " + currentCompass);
		Log.d(LOGGING_TAG, "Easy Location: " + easyLocation);
		Log.d(LOGGING_TAG, "Difficulty: " + difficulty);

		return difficulty;
	}

	private void checkForCollisions() {
		if (playerPosition != null) {
			Point topLeft = new Point(playerPosition.x - PLAYER_SIZE, playerPosition.y - PLAYER_SIZE);
			Point topRight = new Point(playerPosition.x + PLAYER_SIZE, playerPosition.y - PLAYER_SIZE);
			Point bottomRight = new Point(playerPosition.x + PLAYER_SIZE, playerPosition.y + PLAYER_SIZE);
			Point bottomLeft = new Point(playerPosition.x - PLAYER_SIZE, playerPosition.y + PLAYER_SIZE);

			boolean collision = false;

			for (Point block : blocks) {
				if (isCollision(topLeft, block) || isCollision(topRight, block) || isCollision(bottomLeft, block) || isCollision(bottomRight, block)) {
					collision = true;
					break;
				}
			}

			if (collision) {
				vibrator.vibrate(COLLISION_VIBRAION_PATTERN, -1);
				handler.post(collisionHandler);
			}
		}
	}

	private boolean isCollision(Point playerCorner, Point circleCenter) {
		return Math.pow(playerCorner.x - circleCenter.x, 2) + Math.pow(playerCorner.y - circleCenter.y, 2) <= Math.pow(BLOCK_CIRCLE_RADIUS, 2);
	}

	private class CollissionHandler implements Runnable {
		@Override
		public void run() {
			stop();

			AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

			// TODO: Internationalize
			// String title = getResources().getString("Avoid the Blocks");
			alert.setTitle("Avoid the Blocks");
			alert.setMessage("You Lose! Play Again?");

			alert.setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					blocks.clear();
					start();
				}
			});

			alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					setKeepScreenOn(false);
				}
			});

			alert.show();
		}
	}
	
	private class DifficultyChanger extends Thread {
		@Override
		public void run() {
			while (!isInterrupted()) {
				
				easyLocation = random.nextInt(360);
				
				try {
					sleep(CHANGE_DIFFICULTY_FREQUENCY_MS);
				} catch (InterruptedException e) {
					interrupt();
				}
			}
		}
	}

	private static enum Difficulty {
		EASY, MEDIUM, HARD;
	}
}
