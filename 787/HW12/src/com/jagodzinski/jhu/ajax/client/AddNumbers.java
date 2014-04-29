package com.jagodzinski.jhu.ajax.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class AddNumbers implements EntryPoint
{
	@Override
	public void onModuleLoad()
	{
		ValueBoxBase<Double> textbox1 = createTextField("numberHolder1");
		ValueBoxBase<Double> textbox2 = createTextField("numberHolder2");
		Button addButton = createAddButton();
		HTML resultArea = createResultArea();
		addClickHandler(addButton, textbox1, textbox2, resultArea);
	}

	private DoubleBox createTextField(final String parentElementId)
	{
		DoubleBox textBox = new DoubleBox();
		RootPanel.get(parentElementId).add(textBox);
		return textBox;
	}
	
	private Button createAddButton()
	{
		Button button = new Button("Add Numbers");
		RootPanel.get("buttonHolder").add(button);
		return button;
	}
	
	private HTML createResultArea()
	{
		HTML resultArea = new HTML("<i>Result will go here</i>");
		RootPanel.get("resultHolder").add(resultArea);
		return resultArea;
	}

	private void addClickHandler(final Button button, final ValueBoxBase<Double> textbox1,
			final ValueBoxBase<Double> textbox2,
			final HTML resultRegion)
	{
		button.addClickHandler(new AddButtonClickHandler(textbox1, textbox2, resultRegion));
	}

	private static class AddButtonClickHandler implements ClickHandler
	{
		private final ValueBoxBase<Double> textbox1;
		private final ValueBoxBase<Double> textbox2;
		private final HTML resultRegion;

		public AddButtonClickHandler(final ValueBoxBase<Double> textbox1, final ValueBoxBase<Double> textbox2,
				final HTML resultRegion)
		{
			this.resultRegion = resultRegion;
			this.textbox1 = textbox1;
			this.textbox2 = textbox2;
		}

		@Override
		public void onClick(ClickEvent event)
		{
			resultRegion.setText(String.valueOf(textbox1.getValue() + textbox2.getValue()));
		}
	}
}