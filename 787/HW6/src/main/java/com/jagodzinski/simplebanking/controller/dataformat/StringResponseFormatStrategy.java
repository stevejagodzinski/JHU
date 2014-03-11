package com.jagodzinski.simplebanking.controller.dataformat;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StringResponseFormatStrategy implements ResponseStrategy {

	@Override
	public String toResponse(Collection<? extends Object> responseObjects) {
		StringBuilder response = new StringBuilder();
		
		for(Object responseObject : responseObjects)
		{
			response.append(
					ToStringBuilder.reflectionToString(responseObject,
							ToStringStyle.SHORT_PREFIX_STYLE)).append("\n");
		}
		
		return response.toString().trim();
	}
}
