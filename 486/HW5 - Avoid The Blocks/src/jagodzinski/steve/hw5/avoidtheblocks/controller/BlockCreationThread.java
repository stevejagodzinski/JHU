package jagodzinski.steve.hw5.avoidtheblocks.controller;

import jagodzinski.steve.hw5.avoidtheblocks.model.GameState;

import java.util.Random;

import android.graphics.Point;
import android.util.Log;

public class BlockCreationThread extends Thread {

	private static final long BRICK_CREATION_RATE_MS_EASY = 2000;
	private static final long BRICK_CREATION_RATE_MS_MEDIUM = BRICK_CREATION_RATE_MS_EASY / 2;
	private static final long BRICK_CREATION_RATE_MS_HARD = BRICK_CREATION_RATE_MS_MEDIUM / 2;

	private final Random random = new Random();

	private final GameState gameState;
	private int viewWidth;

	public long brickCreationRate = BRICK_CREATION_RATE_MS_EASY;

	public BlockCreationThread(final GameState gameState, final int viewWidth) {
		this.gameState = gameState;
		this.viewWidth = viewWidth;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {

			if (viewWidth > 0) {
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
		synchronized (gameState.getBlocks()) {
			gameState.getBlocks().add(createNewBlock());
		}
	}

	private Point createNewBlock() {
		return new Point(random.nextInt(viewWidth), 0);
	}

	public void setViewWidth(Integer viewWidth) {
		this.viewWidth = viewWidth;
	}

	public void updateBlockCreationRate() {
		switch (gameState.getDifficulty()) {
			case EASY:
				brickCreationRate = BRICK_CREATION_RATE_MS_EASY;
				break;
			case MEDIUM:
				brickCreationRate = BRICK_CREATION_RATE_MS_MEDIUM;
				break;
			case HARD:
				brickCreationRate = BRICK_CREATION_RATE_MS_HARD;
				break;
			default:
				Log.e("BlockCreationThread", "Error updating difficulty. Unspported difficulty: " + gameState.getDifficulty());
		}
	}
}
