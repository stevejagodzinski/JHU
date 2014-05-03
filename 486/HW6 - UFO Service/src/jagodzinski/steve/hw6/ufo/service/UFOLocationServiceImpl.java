package jagodzinski.steve.hw6.ufo.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UFOLocationServiceImpl extends Service {

	private Thread serviceThread;
	private List<UFOLocationServiceReporter> reporters = new CopyOnWriteArrayList<UFOLocationServiceReporter>();

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("UFOLocationService", "onBind(" + intent + ")");
		serviceThread = new UFOLocationServiceThread(reporters);
		serviceThread.start();
		return binder;
	}

	@Override
	public void onDestroy() {
		if (serviceThread != null) {
			serviceThread.interrupt();
			serviceThread = null;
		}
		Log.d("UFOLocationService", "onDestroy");
		super.onDestroy();
	}

	private UFOLocationService.Stub binder = new UFOLocationService.Stub() {
		@Override
		public void add(UFOLocationServiceReporter reporter) {
			reporters.add(reporter);
		}

		@Override
		public void remove(UFOLocationServiceReporter reporter) {
			reporters.remove(reporter);
		}
	};
}
