package com.jagodzinski.jhu.ajax.controller.dataformat;

import java.util.Collection;

import com.google.gson.Gson;

public class JSONResponseFormatStrategy implements ResponseStrategy {

	private static JSONResponseFormatStrategy INSTANCE = new JSONResponseFormatStrategy();

	public static JSONResponseFormatStrategy getInstance() {
		return INSTANCE;
	}

	private JSONResponseFormatStrategy() {
	}

	@Override
	public String toResponse(Collection<? extends Object> responseObjects) {
		return new Gson().toJson(responseObjects);
	}
}
