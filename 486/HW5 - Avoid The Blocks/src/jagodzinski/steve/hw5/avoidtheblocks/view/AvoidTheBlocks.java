package jagodzinski.steve.hw5.avoidtheblocks.view;

import jagodzinki.steve.hw5.avoidtheblocks.R;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class AvoidTheBlocks extends Activity {

	private static final String LOGGING_TAG = AvoidTheBlocks.class.getName();

	private static final int KEYPAD_ACCELERATION_FACTOR = 200;

	private int dpadAcceleration;

	private AvoidTheBlocksView avoidTheBlocksView;

	// TODO: Unregister sensors when collision detected. Re-register when ok pressed on dialog.
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;

	private float[] gravity = new float[3];
	private float[] geomagnetic = new float[3];
	private float[] rotationMatrix = new float[9];
	private float[] orientation = new float[3];
	private double compass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avoid_the_blocks);

		avoidTheBlocksView = (AvoidTheBlocksView) findViewById(R.id.avoidTheBlocksView);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	@Override
	protected void onPause() {
		avoidTheBlocksView.stop();

		sensorManager.unregisterListener(sensorListener);
		sensorManager.unregisterListener(sensorListener);

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_GAME);

		avoidTheBlocksView.start();
	}

	// For testing in emulator
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_D:
				dpadAcceleration += KEYPAD_ACCELERATION_FACTOR;
				Log.d(LOGGING_TAG, "Accelerating player right. Acceleration: " + dpadAcceleration);
				avoidTheBlocksView.acceleratePlayer(dpadAcceleration);
				break;
			case KeyEvent.KEYCODE_A:
				dpadAcceleration -= KEYPAD_ACCELERATION_FACTOR;
				Log.d(LOGGING_TAG, "Accelerating player left. Acceleration: " + dpadAcceleration);
				avoidTheBlocksView.acceleratePlayer(dpadAcceleration);
				break;
		}

		return true;
	}

	SensorEventListener sensorListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(final SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				System.arraycopy(event.values, 0, gravity, 0, 3);

				// Do not adjust for screen rotation. It is locked in landscape
				// mode.
				avoidTheBlocksView.acceleratePlayer((int) (event.values[1] * 100));
			} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				System.arraycopy(event.values, 0, geomagnetic, 0, 3);

				if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
					SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationMatrix);
					SensorManager.getOrientation(rotationMatrix, orientation);
					compass = (Math.toDegrees(orientation[0]) + 360) % 360;

					Log.d(LOGGING_TAG, "Setting compass to:" + compass);

					avoidTheBlocksView.setCompass((int) compass);
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

			avoidTheBlocksView.setCompass((int) compass);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			Log.d("ACCURACY CHANGE", sensor.getName() + ": " + accuracy);
		}
	};
}
