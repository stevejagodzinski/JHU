package com.jagodzinski.jhu.ajax.controller.dataformat;

public class ResponseStrategyFactory {
	public static ResponseStrategy getResponseStrategy(String responseFormat) {
		return getResponseStrategy(ResponseFormat.valueOf(responseFormat));
	}
	
	public static ResponseStrategy getResponseStrategy(ResponseFormat responseFormat) {

		ResponseStrategy strategy;

		switch (responseFormat) {
			case JSON:
				strategy = JSONResponseFormatStrategy.getInstance();
				break;
			case XML:
				strategy = XMLResponseFormatStrategy.getInstance();
				break;
			case STRING:
				strategy = StringResponseFormatStrategy.getInstance();
				break;
			default:
				throw new IllegalArgumentException(String.format("Unsupported response format %s", responseFormat));
		}
		
		return strategy;
	}
}
