package com.example.jagodzinki.steve.hw5.avoidtheblocks.view;

import android.app.Activity;
import android.os.Bundle;

import com.example.jagodzinki.steve.hw5.avoidtheblocks.R;

public class AvoidTheBlocks extends Activity {

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
}
