package jagodzinski.steve.hw5.avoidtheblocks.view;

import jagodzinki.steve.hw5.avoidtheblocks.R;
import jagodzinski.steve.hw5.avoidtheblocks.model.GameState;
import jagodzinski.steve.hw5.avoidtheblocks.model.ICollisionObserver;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;

public class AvoidTheBlocks extends Activity implements ICollisionObserver {

	private static final String LOGGING_TAG = AvoidTheBlocks.class.getName();

	private static final long[] COLLISION_VIBRAION_PATTERN = new long[] { 0, 250, 200, 250, 150, 150, 75, 150, 75, 150 };

	private static final int KEYPAD_ACCELERATION_FACTOR = 200;

	private int dpadAcceleration;

	private AvoidTheBlocksView avoidTheBlocksView;

	private GameState gameState = new GameState();

	private SensorManager sensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;

	private float[] gravity = new float[3];
	private float[] geomagnetic = new float[3];
	private float[] rotationMatrix = new float[9];
	private float[] orientation = new float[3];
	private double compass;

	private Vibrator vibrator;

	private boolean dialogVisible;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avoid_the_blocks);

		avoidTheBlocksView = (AvoidTheBlocksView) findViewById(R.id.avoidTheBlocksView);
		avoidTheBlocksView.setGameState(gameState);
		avoidTheBlocksView.addCollisionObserver(this);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	protected void onPause() {
		avoidTheBlocksView.stop();
		unregisterSensors();
		super.onPause();
	}

	private void unregisterSensors() {
		sensorManager.unregisterListener(sensorListener);
		sensorManager.unregisterListener(sensorListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerSensors();
		avoidTheBlocksView.start();
	}

	private void registerSensors() {
		sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
	}

	// For testing in emulator
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_D:
				dpadAcceleration += KEYPAD_ACCELERATION_FACTOR;
				Log.d(LOGGING_TAG, "Accelerating player right. Acceleration: " + dpadAcceleration);
				gameState.setPlayerAcceleration(dpadAcceleration);
				break;
			case KeyEvent.KEYCODE_A:
				dpadAcceleration -= KEYPAD_ACCELERATION_FACTOR;
				Log.d(LOGGING_TAG, "Accelerating player left. Acceleration: " + dpadAcceleration);
				gameState.setPlayerAcceleration(dpadAcceleration);
				break;
		}

		return true;
	}

	SensorEventListener sensorListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(final SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				System.arraycopy(event.values, 0, gravity, 0, 3);

				// Do not adjust for screen rotation. It is locked in landscape mode.
				gameState.setPlayerAcceleration((int) (event.values[1] * 100));
				Log.d("Acceleration Changed", "Value read from accelerometer:" + event.values[1]);
			} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				System.arraycopy(event.values, 0, geomagnetic, 0, 3);

				if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
					SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationMatrix);
					SensorManager.getOrientation(rotationMatrix, orientation);
					compass = (Math.toDegrees(orientation[0]) + 360) % 360;

					Log.d(LOGGING_TAG, "Setting compass to:" + compass);

					avoidTheBlocksView.setCompass((short) compass);
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			Log.d("ACCURACY CHANGE", sensor.getName() + ": " + accuracy);
		}
	};

	SensorEventListener magnetometerListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(final SensorEvent event) {
			double compass = (Math.toDegrees(event.values[0]) + 360) % 360;

			Log.d(LOGGING_TAG, "Setting compass to:" + compass);

			avoidTheBlocksView.setCompass((short) compass);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			Log.d("ACCURACY CHANGE", sensor.getName() + ": " + accuracy);
		}
	};

	@Override
	public void handleCollision() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				avoidTheBlocksView.stop();
				unregisterSensors();
				avoidTheBlocksView.setKeepScreenOn(false);
				vibrator.vibrate(COLLISION_VIBRAION_PATTERN, -1);
				showCollisionDialog();
			}
		});
	}

	private void showCollisionDialog() {
		if (!dialogVisible) {
			AlertDialog.Builder alert = new AlertDialog.Builder(AvoidTheBlocks.this);

			// TODO: Internationalize
			// String title = getResources().getString("Avoid the Blocks");
			alert.setTitle("Avoid the Blocks");
			alert.setMessage("You Lose! Play Again?");

			alert.setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialogVisible = false;
					gameState.getBlocks().clear();
					registerSensors();
					avoidTheBlocksView.start();
				}
			});

			alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialogVisible = false;
				}
			});

			alert.show();
		}
	}
}
