package com.jagodzinski.jhu.ajax.controller.dataformat;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StringResponseFormatStrategy implements ResponseStrategy {

	private static StringResponseFormatStrategy INSTANCE = new StringResponseFormatStrategy();

	public static StringResponseFormatStrategy getInstance() {
		return INSTANCE;
	}

	private StringResponseFormatStrategy() {
	}

	@Override
	public String toResponse(Collection<? extends Object> responseObjects) {
		StringBuilder response = new StringBuilder();

		for (Object responseObject : responseObjects) {
			response.append(ToStringBuilder.reflectionToString(responseObject, ToStringStyle.SHORT_PREFIX_STYLE))
					.append("\n");
		}

		return response.toString().trim();
	}
}
