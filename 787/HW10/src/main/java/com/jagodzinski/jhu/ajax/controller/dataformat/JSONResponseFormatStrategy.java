package com.jagodzinski.jhu.ajax.controller.dataformat;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONResponseFormatStrategy implements ResponseStrategy {

	private static JSONResponseFormatStrategy INSTANCE = new JSONResponseFormatStrategy();

	public static JSONResponseFormatStrategy getInstance() {
		return INSTANCE;
	}

	private JSONResponseFormatStrategy() {
	}

	@Override
	public String toResponse(Collection<? extends Object> responseObjects) {
		return new JSONArray(responseObjects).toString();
	}

	public String toResponse(Object responseObject) {
		return new JSONObject(responseObject).toString();
	}
}
