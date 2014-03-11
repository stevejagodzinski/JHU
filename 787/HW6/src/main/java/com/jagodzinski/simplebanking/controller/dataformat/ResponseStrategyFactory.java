package com.jagodzinski.simplebanking.controller.dataformat;

public class ResponseStrategyFactory {
	public static ResponseStrategy getResponseStrategy(String responseFormat) {
		return getResponseStrategy(ResponseFormat.valueOf(responseFormat));
	}
	
	public static ResponseStrategy getResponseStrategy(ResponseFormat responseFormat) {

		// TODO: singletons?
		ResponseStrategy strategy;

		switch (responseFormat) {
			case JSON:
				strategy = new JSONResponseFormatStrategy();
				break;
			case XML:
				strategy = new XMLResponseFormatStrategy();
				break;
			case STRING:
				strategy = new StringResponseFormatStrategy();
				break;
			default:
				throw new IllegalArgumentException(String.format("Unsupported response format %s", responseFormat));
		}
		
		return strategy;
	}
}
