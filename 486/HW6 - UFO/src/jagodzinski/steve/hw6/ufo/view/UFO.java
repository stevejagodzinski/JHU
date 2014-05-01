package jagodzinski.steve.hw6.ufo.view;


import jagodzinski.steve.hw6.ufo.R;
import jagodzinski.steve.hw6.ufo.service.UFOLocationService;
import jagodzinski.steve.hw6.ufo.service.UFOLocationService.Reporter;
import jagodzinski.steve.hw6.ufo.service.UFOLocationService.UFOLocationServiceBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;

public class UFO extends Activity {

	private UFOLocationServiceBinder serviceBinder;

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
		public void report(final int n) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Log.d("MainActivity reporter", n + "");
				}
			});
		}
	};

}
