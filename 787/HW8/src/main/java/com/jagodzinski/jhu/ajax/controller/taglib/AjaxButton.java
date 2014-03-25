package com.jagodzinski.jhu.ajax.controller.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class AjaxButton extends SimpleTagSupport {

	private String resultRegion;
	private String url;
	private String value;

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		writeAjaxButton(out);
	}

	public void writeAjaxButton(final JspWriter writer) throws IOException {
		writer.print("<input type=\"button\"");
		writer.print(" value=\"" + value + '"');
		writer.print(" onclick=\"ajaxResult('" + url + "','" + resultRegion + "', showResponseText)\"");
		writer.print(" />");
	}

	public void setResultRegion(String resultRegion) {
		this.resultRegion = resultRegion;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
