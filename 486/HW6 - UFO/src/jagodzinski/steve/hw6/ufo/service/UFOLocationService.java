package jagodzinski.steve.hw6.ufo.service;

import jagodzinski.steve.hw6.ufo.model.UFOPosition;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class UFOLocationService extends Service {

	private Thread serviceThread;
	private List<Reporter> reporters = new CopyOnWriteArrayList<Reporter>();
	private IBinder binder = new UFOLocationServiceBinder();

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

	public interface Reporter {
		void report(List<UFOPosition> ufoPositions);
	}

	public class UFOLocationServiceBinder extends Binder {
		public void addReporter(Reporter reporter) {
			reporters.add(reporter);
		}

		public void removeReporter(Reporter reporter) {
			reporters.remove(reporter);
		}
	}
}
