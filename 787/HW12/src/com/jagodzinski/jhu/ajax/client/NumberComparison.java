package com.jagodzinski.jhu.ajax.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

public class NumberComparison implements EntryPoint
{
	private static final short[] DROP_DOWN_VALUES = new short[]
	{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	@Override
	public void onModuleLoad()
	{
		ListBox listBox1 = createListBox("dropDownHolder1");
		ListBox listBox2 = createListBox("dropDownHolder2");
		HTML resultArea = createResultArea(listBox1, listBox2);
		addChangeListner(listBox1, listBox2, resultArea);
	}

	private ListBox createListBox(final String parentElementId)
	{
		ListBox listBox = new ListBox();
		listBox.setVisibleItemCount(1);

		for (Short value : DROP_DOWN_VALUES)
		{
			listBox.addItem(String.valueOf(value));
		}

		RootPanel.get(parentElementId).add(listBox);
		return listBox;
	}
	
	private HTML createResultArea(final ListBox leftDropDown, final ListBox rightDropDown)
	{
		HTML resultArea = new HTML("<i>" + getValueForResultRegion(leftDropDown, rightDropDown) + "</i>");
		RootPanel.get("resultHolder").add(resultArea);
		return resultArea;
	}

	private void addChangeListner(final ListBox leftDropDown, final ListBox rightDropDown,
			final HTML resultRegion)
	{
		ChangeHandler changeHandler = new ListBoxValueChangeHandler(leftDropDown, rightDropDown, resultRegion);
		leftDropDown.addChangeHandler(changeHandler);
		rightDropDown.addChangeHandler(changeHandler);
	}

	private String getValueForResultRegion(final ListBox leftDropDown, final ListBox rightDropDown)
	{
		short leftSideValue = Short.valueOf(leftDropDown.getItemText(leftDropDown.getSelectedIndex()));
		short rightSideValue = Short.valueOf(rightDropDown.getItemText(rightDropDown.getSelectedIndex()));

		String result;

		if (leftSideValue > rightSideValue)
		{
			result = "Left side is greater than the right!";
		}
		else if (leftSideValue < rightSideValue)
		{
			result = "Left side is less than the right!";
		}
		else
		{
			result = "Left side is equal to the right!";
		}

		return result;
	}

	private class ListBoxValueChangeHandler implements ChangeHandler
	{
		private final ListBox leftDropDown;
		private final ListBox rightDropDown;
		private final HTML resultRegion;

		public ListBoxValueChangeHandler(final ListBox leftDropDown, final ListBox rightDropDown,
				final HTML resultRegion)
		{
			this.leftDropDown = leftDropDown;
			this.rightDropDown = rightDropDown;
			this.resultRegion = resultRegion;
		}

		@Override
		public void onChange(ChangeEvent event)
		{
			resultRegion.setText(getValueForResultRegion(leftDropDown, rightDropDown));
		}
	}
}