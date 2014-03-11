package com.jagodzinski.simplebanking.controller.dataformat;

import java.util.Collection;

public interface ResponseStrategy {
	String toResponse(Collection<? extends Object> response);
}
