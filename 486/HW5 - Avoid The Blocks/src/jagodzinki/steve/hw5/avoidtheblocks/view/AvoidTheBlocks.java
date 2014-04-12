package jagodzinki.steve.hw5.avoidtheblocks.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.example.jagodzinki.steve.hw5.avoidtheblocks.R;

public class AvoidTheBlocks extends Activity {

	private static final String LOGGING_TAG = AvoidTheBlocks.class.getName();

	private static final int KEYPAD_ACCELERATION_FACTOR = 200;

	private int dpadAcceleration;

	private AvoidTheBlocksView avoidTheBlocksView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avoid_the_blocks);

		this.avoidTheBlocksView = (AvoidTheBlocksView) findViewById(R.id.avoidTheBlocksView);
	}

	@Override
	protected void onPause() {
		avoidTheBlocksView.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		avoidTheBlocksView.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				dpadAcceleration += KEYPAD_ACCELERATION_FACTOR;
				Log.d(LOGGING_TAG, "Accelerating player right. Acceleration: " + dpadAcceleration);
				avoidTheBlocksView.acceleratePlayer(dpadAcceleration);
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				dpadAcceleration -= KEYPAD_ACCELERATION_FACTOR;
				Log.d(LOGGING_TAG, "Accelerating player left. Acceleration: " + dpadAcceleration);
				avoidTheBlocksView.acceleratePlayer(dpadAcceleration);
				break;
		}

		return true;
	}
}
