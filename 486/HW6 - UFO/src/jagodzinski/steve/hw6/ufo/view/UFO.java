package jagodzinski.steve.hw6.ufo.view;


import jagodzinski.steve.hw6.ufo.R;
import jagodzinski.steve.hw6.ufo.model.UFOPosition;
import jagodzinski.steve.hw6.ufo.service.UFOLocationService;
import jagodzinski.steve.hw6.ufo.service.UFOLocationServiceReporter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class UFO extends Activity {

	private static final int GOOGLE_PLAY_SETUP = 42;

	private GoogleMap googleMap;
	private UFOLocationService serviceBinder;

	private LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(38.9073, -77.0365)).build();
	private Map<Integer, Marker> shipMarkers = new HashMap<Integer, Marker>();

	private BitmapDescriptor ufoIcon;
	private int cameraPadding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ufo);

		ufoIcon = BitmapDescriptorFactory.fromResource(R.drawable.red_ufo);
		cameraPadding = (int) getResources().getDimension(R.dimen.camera_padding);
    }

	@Override
	protected void onStart() {
		Log.d("UFO", "onStart");
		if (!bindService(new Intent("jagodzinski.steve.hw6.ufo.service.UFOLocationService"), serviceConnection, Context.BIND_AUTO_CREATE)) {
			Log.e("MainActivity", "Failed to bind to UFOLocationService");
		}
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

		try {
			if (serviceBinder != null) {
				serviceBinder.remove(reporter);
			}
		} catch (RemoteException e) {
			Log.e("MainActivity", "Unable to remove self as reporter from UFOLocationService", e);
		}

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

	private void handleShipLocationUpdates(final List<UFOPosition> ufoPositions) {
		removeNoLongerTrackedShipMarkers(ufoPositions);

		for (UFOPosition ufoPosition : ufoPositions) {
			Marker previousMarker = shipMarkers.get(ufoPosition.getShipNumber());
			LatLng currentPosition = new LatLng(ufoPosition.getLat(), ufoPosition.getLon());

			drawShipPath(previousMarker == null ? null : previousMarker.getPosition(), currentPosition);
			updateMarkerLocation(ufoPosition.getShipNumber(), currentPosition);

			bounds = bounds.including(currentPosition);
		}

		googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, cameraPadding));
	}

	private void removeNoLongerTrackedShipMarkers(final List<UFOPosition> ufoPositions) {
		Set<Integer> shipsToRemove = new HashSet<Integer>(shipMarkers.keySet());

		for (UFOPosition ufoPosition : ufoPositions) {
			shipsToRemove.remove(ufoPosition.getShipNumber());
		}

		for (Integer shipToRemove : shipsToRemove) {
			shipMarkers.get(shipToRemove).remove();
			shipMarkers.remove(shipsToRemove);
		}
	}

	private void updateMarkerLocation(final int shipNumber, final LatLng currentPosition) {
		Marker shipMarker = shipMarkers.get(shipNumber);
		if (shipMarker == null) {
			shipMarker = googleMap.addMarker(new MarkerOptions().icon(ufoIcon).position(currentPosition));
			shipMarkers.put(shipNumber, shipMarker);
		} else {
			shipMarker.setPosition(currentPosition);
		}
	}

	private void drawShipPath(final LatLng previousPosition, final LatLng currentPosition) {
		if (previousPosition != null) {
			googleMap.addPolyline(new PolylineOptions().color(Color.BLUE).width(getResources().getDimension(R.dimen.path_width)).add(previousPosition)
					.add(currentPosition));
		}
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceBinder = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			UFO.this.serviceBinder = UFOLocationService.Stub.asInterface(service);

			try {
				UFO.this.serviceBinder.add(reporter);
			} catch (RemoteException e) {
				Log.e("MainActivity", "Unable to add self as reporter to UFOLocationService", e);
			}
		}
	};

	private UFOLocationServiceReporter reporter = new UFOLocationServiceReporter.Stub() {
		@Override
		public void report(final List<UFOPosition> ufoPositions) throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					handleShipLocationUpdates(ufoPositions);
				}
			});
		}
	};
}
