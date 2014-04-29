package com.jagodzinski.jhu.ajax.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class CurrentDate implements EntryPoint
{
	@Override
	public void onModuleLoad()
	{
		buttonSetup();
	}

	private void buttonSetup()
	{
		Button randomNumberButton = new Button("Show Current Date");
		HTML randomNumberResult = new HTML("<i>Date will go here</i>");
		randomNumberButton.addClickHandler(new CurrentDateClickHandler(randomNumberResult));
		RootPanel.get("dateButtonHolder").add(randomNumberButton);
		RootPanel.get("dateResultHolder").add(randomNumberResult);
	}

	private class CurrentDateClickHandler implements ClickHandler
	{
		private HTML resultRegion;

		public CurrentDateClickHandler(HTML resultRegion)
		{
			this.resultRegion = resultRegion;
		}

		@Override
		public void onClick(ClickEvent event)
		{
			resultRegion.setText(DateTimeFormat.getFormat(PredefinedFormat.ISO_8601).format(new Date()));
		}
	}
}