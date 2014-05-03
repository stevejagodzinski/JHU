package jagodzinski.steve.hw6.ufo.view;


import jagodzinski.steve.hw6.ufo.R;
import jagodzinski.steve.hw6.ufo.model.UFOPosition;
import jagodzinski.steve.hw6.ufo.service.UFOLocationService;
import jagodzinski.steve.hw6.ufo.service.UFOLocationService.Reporter;
import jagodzinski.steve.hw6.ufo.service.UFOLocationService.UFOLocationServiceBinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

public class UFO extends Activity {

	private static final int GOOGLE_PLAY_SETUP = 42;

	private GoogleMap googleMap;
	private UFOLocationServiceBinder serviceBinder;

	private LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(38.9073, -77.0365)).build();
	private Map<Integer, LatLng> shipLocations = new HashMap<Integer, LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ufo);
    }

	@Override
	protected void onStart() {
		Log.d("UFO", "onStart");
		bindService(new Intent(this, UFOLocationService.class), serviceConnection, Context.BIND_AUTO_CREATE);
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initGoogleMap();
	}

	@Override
	protected void onStop() {
		Log.d("UFO", "onStop");
		serviceBinder.removeReporter(reporter);
		unbindService(serviceConnection);
		super.onStop();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ufo, menu);
        return true;
    }
    
	private boolean initGoogleMap() {
		boolean success;

		int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		switch (googlePlayServicesAvailable) {
			case ConnectionResult.SUCCESS:
				success = true;
				break;
			case ConnectionResult.SERVICE_MISSING:
			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			case ConnectionResult.SERVICE_DISABLED:
				GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable, this, GOOGLE_PLAY_SETUP).show();
				success = false;
				break;
			default:
				throw new RuntimeException("Unexpected result code from isGooglePlayServicesAvailable: " + googlePlayServicesAvailable + " ("
						+ GooglePlayServicesUtil.getErrorString(googlePlayServicesAvailable) + ")");
		}

		if (success) {
			MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			googleMap = map.getMap();
		}

		return success;
	}

	private void updateShipLocations(final List<UFOPosition> ufoPositions) {
		for (UFOPosition ufoPosition : ufoPositions) {
			LatLng previousPosition = shipLocations.get(ufoPosition.getShipNumber());
			LatLng currentPosition = new LatLng(ufoPosition.getLat(), ufoPosition.getLon());
			shipLocations.put(ufoPosition.getShipNumber(), currentPosition);
			bounds = bounds.including(currentPosition);

			if (previousPosition != null) {
				googleMap.addPolyline(new PolylineOptions().color(Color.BLUE).width(getResources().getDimension(R.dimen.path_width)).add(previousPosition)
						.add(currentPosition));
			}
		}

		googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, (int) getResources().getDimension(R.dimen.camera_padding)));
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceBinder = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			UFO.this.serviceBinder = (UFOLocationServiceBinder) service;
			UFO.this.serviceBinder.addReporter(reporter);
		}
	};

	private Reporter reporter = new Reporter() {
		@Override
		public void report(final List<UFOPosition> ufoPositions) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateShipLocations(ufoPositions);
				}
			});
		}
	};

}
