package jagodzinski.steve.hw6.ufo.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class UFOLocationService extends Service {

	private ServiceThread serviceThread;
	private List<Reporter> reporters = new ArrayList<Reporter>();
	private IBinder binder = new UFOLocationServiceBinder();

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("UFOLocationService", "onBind(" + intent + ")");
		serviceThread = new ServiceThread();
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

	
	private class ServiceThread extends Thread {
		@Override
		public void run() {
			Log.d("ServiceThread", "Running");

			while (!isInterrupted()) {

				// TODO: Get Alien Positions

				for (Reporter reporter : reporters) {
					// TODO: Pass loc to reporter
					reporter.report(0);
				}
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					interrupt();
				}
			}
			Log.d("ServiceThread", "interrupted");
		}
	}

	public interface Reporter {
		void report(int n);
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
