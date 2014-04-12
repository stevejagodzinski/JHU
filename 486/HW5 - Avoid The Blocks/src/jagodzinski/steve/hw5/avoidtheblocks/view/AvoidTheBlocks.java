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

	private SensorManager sensorManager;
	private Sensor accelerometer;

	// TODO: Use this
	private Sensor magnetometer;

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

		sensorManager.unregisterListener(accelerometerListener);

		// TODO:
		// sensorManager.unregisterListener(magListener);

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

		// TODO:
		// sensorManager.registerListener(magListener, magnetometer,
		// SensorManager.SENSOR_DELAY_NORMAL);

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

	SensorEventListener accelerometerListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(final SensorEvent event) {
			// Do not adjust for screen rotation. It is locked in landscape
			// mode.
			avoidTheBlocksView.acceleratePlayer((int) (event.values[1] * 100));
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			Log.d("ACCURACY CHANGE", sensor.getName() + ": " + accuracy);
		}
	};
}
