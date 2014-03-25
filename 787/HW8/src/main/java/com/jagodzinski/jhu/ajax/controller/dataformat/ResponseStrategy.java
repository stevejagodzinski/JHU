package com.jagodzinski.jhu.ajax.controller.dataformat;

import java.util.Collection;

public interface ResponseStrategy {
	String toResponse(Collection<? extends Object> response);
}
