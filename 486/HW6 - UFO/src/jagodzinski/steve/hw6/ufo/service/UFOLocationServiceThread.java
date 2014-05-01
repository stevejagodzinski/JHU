package jagodzinski.steve.hw6.ufo.service;

import jagodzinski.steve.hw6.ufo.model.UFOPosition;
import jagodzinski.steve.hw6.ufo.service.UFOLocationService.Reporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		Log.d("UFOLocationServiceThread", "Running");

		while (!isInterrupted()) {

			HttpResponse response = requestUFOCoordinates();

			if (!isInvasionComplete(response)) {
				String json = getJSON(response);
				List<UFOPosition> ufoPositions = createUFOPositions(json);
				notifyReporters(ufoPositions);
				requestNumber++;
				sleep();
			} else {
				interrupt();
			}
		}
		Log.d("UFOLocationServiceThread", "interrupted");
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

	private List<UFOPosition> createUFOPositions(final String json) {
		List<UFOPosition> ufoPositions;
		try {
			ufoPositions = doCreateUFOPositions(json);
		} catch (JSONException e) {
			Log.e("UFOLocationServiceThread", "Invalid JSON", e);
			ufoPositions = Collections.emptyList();
		}
		return ufoPositions;
	}

	private List<UFOPosition> doCreateUFOPositions(final String json) throws JSONException {
		JSONArray jsonArray = new JSONArray(json);
		List<UFOPosition> ufoPositions = new ArrayList<UFOPosition>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			UFOPosition ufoPosition = createUFOPosition(jsonObject);
			ufoPositions.add(ufoPosition);
		}
		return Collections.unmodifiableList(ufoPositions);
	}

	private UFOPosition createUFOPosition(final JSONObject jsonObject) throws JSONException {
		UFOPosition ufoPosition = new UFOPosition();
		ufoPosition.setLat(jsonObject.getDouble("lat"));
		ufoPosition.setLon(jsonObject.getDouble("lon"));
		ufoPosition.setShipNumber(jsonObject.getInt("ship"));
		return ufoPosition;
	}

	private void notifyReporters(final List<UFOPosition> ufoPositions) {
		for (Reporter reporter : reporters) {
			reporter.report(ufoPositions);
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
