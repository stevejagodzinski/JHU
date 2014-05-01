package jagodzinski.steve.hw6.ufo.service;

import jagodzinski.steve.hw6.ufo.service.UFOLocationService.Reporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class UFOLocationServiceThread extends Thread {
	private static final String URI = "http://javadude.com/aliens/%d.json";

	private final List<Reporter> reporters;

	private HttpClient httpclient;

	private int requestNumber = 1;

	public UFOLocationServiceThread(final List<Reporter> reporters) {
		this.reporters = reporters;
		init();
	}

	private void init() {
		initHttpClient();
	}

	private void initHttpClient() {
		httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
	}

	@Override
	public void run() {
		Log.d("ServiceThread", "Running");

		while (!isInterrupted()) {

			HttpResponse response = requestUFOCoordinates();

			if (!isInvasionComplete(response)) {
				String json = getJSON(response);
				notifyReporters(json);
				requestNumber++;
				sleep();
			} else {
				interrupt();
			}
		}
		Log.d("ServiceThread", "interrupted");
	}

	private HttpResponse requestUFOCoordinates() {
		try {
			return httpclient.execute(new HttpGet(String.format(URI, requestNumber)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isInvasionComplete(final HttpResponse response) {
		return response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND;
	}

	private String getJSON(final HttpResponse response) {
		HttpEntity entity = response.getEntity();
		OutputStream out = new ByteArrayOutputStream();
		try {
			entity.writeTo(out);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return out.toString();
	}

	private void notifyReporters(String json) {
		for (Reporter reporter : reporters) {
			reporter.report(json);
		}
	}

	private void sleep() {
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			interrupt();
		}
	}
}
