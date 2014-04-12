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
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AvoidTheBlocksView extends View {

	private static final String LOGGING_TAG = AvoidTheBlocksView.class.getName();

	// TODO: Move to dimens.xml
	private static final float CIRCLE_RADIUS = 20;
	private static final int PLAYER_SIZE = 5;
	
	// TODO: Variate with manometer readings
	private long animationRefreshIntervalMS = 10;

	private Collection<Point> blocks = new LinkedList<Point>();

	private Point playerPosition;
	private float playerVelocity;
	private float playerAcceleration;

	private Handler handler = new Handler();

	private Runnable invalidator = new Runnable() {
		@Override
		public void run() {
			invalidate();
		}
	};

	private Runnable collisionHandler = new CollissionHandler();

	private Paint yellowPaint;
	private Paint greenPaint;

	private Thread animationThread;
	private Thread blockCreationThread;

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
		doDraw(canvas);
	}

	private void doDraw(final Canvas canvas) {
		Log.d(LOGGING_TAG, "Drawing canvas");

		if (!isInitialized()) {
			init();
		}

		drawBlocks(canvas);
		drawPlayer(canvas);
	}

	private void init() {
		yellowPaint = new Paint();
		yellowPaint.setColor(Color.YELLOW);

		greenPaint = new Paint();
		greenPaint.setColor(Color.GREEN);

		playerPosition = new Point(20, getHeight() / 2);
	}

	private void drawBlocks(final Canvas canvas) {
		synchronized (blocks) {
			for (Point block : blocks) {
				canvas.drawCircle(block.x, block.y, CIRCLE_RADIUS, yellowPaint);
			}
		}
	}

	private void drawPlayer(Canvas canvas) {
		canvas.drawRect(playerPosition.x - PLAYER_SIZE, playerPosition.y - PLAYER_SIZE, playerPosition.x + PLAYER_SIZE, playerPosition.y + PLAYER_SIZE,
				greenPaint);
	}

	public void start() {
		stop();

		animationThread = new AvoidTheBlocksAnimationThread();
		animationThread.start();

		blockCreationThread = new AvoidTheBlocksCreationThread();
		blockCreationThread.start();
	}

	public void stop() {
		if (animationThread != null) {
			animationThread.interrupt();
			animationThread = null;
		}

		if (blockCreationThread != null) {
			blockCreationThread.interrupt();
			blockCreationThread = null;
		}
	}

	private class AvoidTheBlocksAnimationThread extends Thread {
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
				playerPosition.y = (int) Math.max(
Math.min(playerPosition.y + playerVelocity * (animationRefreshIntervalMS / 1000.0),
								getWidth()), 0);

				if (playerPosition.y == 0 || playerPosition.y == getWidth()) {
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
				point.x--;
			}

		}

		private void removeOffScreenBlocks() {
			Iterator<Point> blockIterator = blocks.iterator();

			while (blockIterator.hasNext()) {
				if (blockIterator.next().y > getHeight()) {
					blockIterator.remove();
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
					sleep(2000);
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
			return new Point(getWidth(), new Random().nextInt(getHeight()));
		}
	}

	public void acceleratePlayer(int acceleration) {
		playerAcceleration = acceleration;
	}

	private void checkForCollisions() {
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
			handler.post(collisionHandler);
		}
	}

	private boolean isCollision(Point playerCorner, Point circleCenter) {
		return Math.pow(playerCorner.x - circleCenter.x, 2) + Math.pow(playerCorner.y - circleCenter.y, 2) <= Math.pow(CIRCLE_RADIUS, 2);
	}

	private class CollissionHandler implements Runnable {
		public void run() {
			stop();

			AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

			// TODO: Internationalize
			// String title = getResources().getString("Avoid the Blocks");
			alert.setTitle("Avoid the Blocks");
			alert.setMessage("You Loose! Play Again?");

			alert.setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					blocks.clear();
					start();
				}
			});

			alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});

			alert.show();
		}
	}
}
