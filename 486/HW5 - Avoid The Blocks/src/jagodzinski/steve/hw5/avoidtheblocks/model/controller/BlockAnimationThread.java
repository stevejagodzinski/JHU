package jagodzinski.steve.hw5.avoidtheblocks.model.controller;

import jagodzinki.steve.hw5.avoidtheblocks.R;
import jagodzinski.steve.hw5.avoidtheblocks.model.GameState;
import jagodzinski.steve.hw5.avoidtheblocks.model.ICollisionObserver;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;

public class BlockAnimationThread extends Thread {

	private long ANIMATION_REFRESH_INTERVAL_MS = 10;

	private static final short BRICK_FALL_RATE_MULTIPLIER_EASY = 1;
	private static final short BRICK_FALL_RATE_MULTIPLIER_MEDIUM = 2;
	private static final short BRICK_FALL_RATE_HARD_RANDOMNESS_BOUND = 5;

	private Random random = new Random();

	private final GameState gameState;

	private float viewWidth;
	private float viewHeight;

	private Handler uiThreadHandler;
	private Runnable invalidateUIRunnable;

	private int playerSizeFromCenter;
	private float blockCircleRadius;

	private Collection<ICollisionObserver> collisionObservers = new CopyOnWriteArraySet<ICollisionObserver>();

	public BlockAnimationThread(final GameState gameState, final Handler uiThreadHandler, final Runnable invalidateUIRunnable, final int viewWidth,
			final int viewHeight, final Context context) {
		this.gameState = gameState;
		this.uiThreadHandler = uiThreadHandler;
		this.invalidateUIRunnable = invalidateUIRunnable;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;

		playerSizeFromCenter = (int) context.getResources().getDimension(R.dimen.player_size_from_center);
		blockCircleRadius = context.getResources().getDimension(R.dimen.block_circle_radius);
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			movePlayer();

			synchronized (gameState.getBlocks()) {
				moveBlocksDown();
				removeOffScreenBlocks();
				checkForCollisions();
			}

			invalidateUI();

			try {
				sleep(ANIMATION_REFRESH_INTERVAL_MS);
			} catch (InterruptedException e) {
				interrupt();
			}
		}
	}

	private void movePlayer() {
		if (gameState.getPlayerPosition() != null) {
			gameState.setPlayerVelocity((float) (gameState.getPlayerVelocity() + gameState.getPlayerAcceleration() * (ANIMATION_REFRESH_INTERVAL_MS / 1000.0)));
			gameState.getPlayerPosition().x = (int) Math.max(
					Math.min(gameState.getPlayerPosition().x + gameState.getPlayerVelocity() * (ANIMATION_REFRESH_INTERVAL_MS / 1000.0), viewWidth
							- getPlayerSizeFromCenter()), getPlayerSizeFromCenter());

			if ((gameState.getPlayerPosition().x == getPlayerSizeFromCenter() && gameState.getPlayerAcceleration() <= 0)
					|| (gameState.getPlayerPosition().x == viewWidth - getPlayerSizeFromCenter() && gameState.getPlayerAcceleration() >= 0)) {
				gameState.setPlayerVelocity(0);
			}

			Log.d("Player moved", "Player acceleration: " + gameState.getPlayerAcceleration());
			Log.d("Player moved", "Player velocity: " + gameState.getPlayerVelocity());
			Log.d("Player moved", "Player position: " + gameState.getPlayerPosition());
		}
	}

	private void moveBlocksDown() {
		for (Point point : gameState.getBlocks()) {

			int amountToFall;

			switch (gameState.getDifficulty()) {
				case EASY:
					amountToFall = BRICK_FALL_RATE_MULTIPLIER_EASY;
					break;
				case MEDIUM:
					amountToFall = BRICK_FALL_RATE_MULTIPLIER_MEDIUM;
					break;
				case HARD:
					amountToFall = random.nextInt(BRICK_FALL_RATE_HARD_RANDOMNESS_BOUND) + 1;
					break;
				default:
					throw new UnsupportedOperationException("Unimplemented Fall Rate");
			}

			point.y += amountToFall;
		}

	}

	private void removeOffScreenBlocks() {
		Iterator<Point> blockIterator = gameState.getBlocks().iterator();

		while (blockIterator.hasNext()) {
			if (blockIterator.next().y > viewHeight) {
				blockIterator.remove();
				Log.i("BlockAnimationThread", "Block removed from screen");
			}
		}
	}

	private void checkForCollisions() {
		if (gameState.getPlayerPosition() != null) {
			Point topLeft = new Point(gameState.getPlayerPosition().x - getPlayerSizeFromCenter(), gameState.getPlayerPosition().y - getPlayerSizeFromCenter());
			Point topRight = new Point(gameState.getPlayerPosition().x + getPlayerSizeFromCenter(), gameState.getPlayerPosition().y - getPlayerSizeFromCenter());
			Point bottomRight = new Point(gameState.getPlayerPosition().x + getPlayerSizeFromCenter(), gameState.getPlayerPosition().y
					+ getPlayerSizeFromCenter());
			Point bottomLeft = new Point(gameState.getPlayerPosition().x - getPlayerSizeFromCenter(), gameState.getPlayerPosition().y
					+ getPlayerSizeFromCenter());

			boolean collision = false;

			for (Point block : gameState.getBlocks()) {
				if (isCollision(topLeft, block) || isCollision(topRight, block) || isCollision(bottomLeft, block) || isCollision(bottomRight, block)) {
					collision = true;
					break;
				}
			}

			if (collision) {
				notifyCollisionObservers();
			}
		}
	}

	private boolean isCollision(Point playerCorner, Point circleCenter) {
		return Math.pow(playerCorner.x - circleCenter.x, 2) + Math.pow(playerCorner.y - circleCenter.y, 2) <= Math.pow(getBlockCircleRadius(), 2);
	}

	public void addCollisionObserver(ICollisionObserver observer) {
		collisionObservers.add(observer);
	}

	private void notifyCollisionObservers() {
		for (ICollisionObserver observer : collisionObservers) {
			observer.handleCollision();
		}
	}

	private void invalidateUI() {
		uiThreadHandler.post(invalidateUIRunnable);
	}

	public void setViewWidth(float viewWidth) {
		this.viewWidth = viewWidth;
	}

	public void setViewHeight(float viewHeight) {
		this.viewHeight = viewHeight;
	}

	private int getPlayerSizeFromCenter() {
		return playerSizeFromCenter;
	}

	private double getBlockCircleRadius() {
		return blockCircleRadius;
	}
}