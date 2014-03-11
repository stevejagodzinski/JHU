package com.jagodzinski.simplebanking.controller.dataformat;

import java.util.Collection;

import com.google.gson.Gson;

public class JSONResponseFormatStrategy implements ResponseStrategy {
	@Override
	public String toResponse(Collection<? extends Object> responseObjects) {
		return new Gson().toJson(responseObjects);
	}
}
