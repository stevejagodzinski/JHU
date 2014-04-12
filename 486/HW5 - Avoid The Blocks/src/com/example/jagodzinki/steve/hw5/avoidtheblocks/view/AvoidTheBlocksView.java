package com.example.jagodzinki.steve.hw5.avoidtheblocks.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import android.content.Context;
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

	private Collection<Point> blocks = new LinkedList<Point>();

	private Handler handler = new Handler();

	private Runnable invalidator = new Runnable() {
		@Override
		public void run() {
			invalidate();
		}
	};

	private Paint paint;

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

		drawBlocks(canvas);
	}

	private void init() {
		paint = new Paint();
		paint.setColor(Color.YELLOW);
	}

	private void drawBlocks(final Canvas canvas) {
		synchronized (blocks) {
			for (Point block : blocks) {
				canvas.drawCircle(block.x, block.y, 20, paint);
			}
		}
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
				synchronized (blocks) {
					moveBlocksDown();
					removeOffScreenBlocks();
				}

				handler.post(invalidator);
				try {
					sleep(10);
				} catch (InterruptedException e) {
					interrupt();
				}
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
}
