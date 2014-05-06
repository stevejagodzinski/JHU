package com.jagodzinski.jhu.ajax.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class CustomerRPC implements EntryPoint {
  private CustomerServiceAsync serviceProxy;
  private HTML richestCustomerHTML;
  private HTML poorestCustomerHTML;

	// private IntegerBox rangeBox;

  public void onModuleLoad() { 
    serviceProxy = GWT.create(CustomerService.class);
    
    Button button1 = new Button("Get Richest Customer");
    richestCustomerHTML = new HTML("<i>Richest Customer Will Go Here</i>");
    button1.addClickHandler(new RichestCustomerButtonHandler()); 
    
    RootPanel.get("get-richest").add(button1);    
    RootPanel.get("get-richest").add(richestCustomerHTML);
    
    Button button2 = new Button("Get Poorest Customer");
    poorestCustomerHTML = new HTML("<i>Poorest Customer Will Go Here</i>");
    button2.addClickHandler(new PoorestCustomerButtonHandler());
    
    RootPanel.get("get-poorest").add(button2);
    RootPanel.get("get-poorest").add(poorestCustomerHTML);
    
//    Button button3 = new Button("Redistribute");
//    rangeBox = new IntegerBox();
//    label3 = new HTML("<i>Num will go here</i>");
//    button3.addClickHandler(new Button3Handler());
//    RootPanel.get("button3").add(button3);
//    RootPanel.get("rangeBox").add(rangeBox);
//    RootPanel.get("label3").add(label3);
//    Button button4 = new Button("Log Client Random");
//    label4 = new HTML("<i>Confirmation will go here</i>");
//    RootPanel.get("button4").add(button4);
//    RootPanel.get("label4").add(label4);
//    button4.addClickHandler(new Button4Handler());
  }

  private class RichestCustomerButtonHandler implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      serviceProxy.getRichestCustomer(new GetRichestCustomerCallback());
    }
  }

  private class PoorestCustomerButtonHandler implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
			serviceProxy.getPoorestCustomer(new GetPoorestCustomerCallback());
    }
  }

	// private class Button3Handler implements ClickHandler {
	// @Override
	// public void onClick(ClickEvent event) {
	// String range = rangeBox.getText();
	// serviceProxy.getButton3Data(range, new Button3Callback());
	// }
	// }
	//
	// private class Button4Handler implements ClickHandler {
	// @Override
	// public void onClick(ClickEvent event) {
	// String range = RootPanel.get("range4").getElement().getInnerText();
	// RandomNumber clientRandom = new RandomNumber(range);
	// serviceProxy.logClientRandom(clientRandom, new
	// LogCallback(clientRandom));
	// }
	// }

	private class GetRichestCustomerCallback implements AsyncCallback<Customer>
	{
		@Override
		public void onSuccess(Customer result)
		{
			richestCustomerHTML.setHTML(result.toString());
		}

		@Override
		public void onFailure(Throwable caught)
		{
			Window.alert("Unable to get data from server.");
		}
	}

	private class GetPoorestCustomerCallback implements AsyncCallback<Customer>
	{
		@Override
		public void onSuccess(Customer result)
		{
			poorestCustomerHTML.setHTML(result.toString());
		}

		@Override
		public void onFailure(Throwable caught)
		{
			Window.alert("Unable to get data from server.");
		}
	}

	// private class Button3Callback implements AsyncCallback<RandomNumber> {
	// @Override
	// public void onSuccess(RandomNumber number) {
	// String range = "Range: " + number.getRange();
	// if (!number.isRangeLegal()) {
	// range = range + " (default due to illegal range)";
	// }
	// String value = "Value: " + number.getValue();
	// String result = "<ul>\n" + "<li>" + range + "</li>\n" + "<li>" + value
	// + "</li>\n" + "</ul>";
	// label3.setHTML(result);
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// Window.alert("Unable to get data from server.");
	// }
	// }
	//
	// private class LogCallback implements AsyncCallback<Void> {
	// RandomNumber clientRandom;
	//
	// public LogCallback(RandomNumber clientRandom) {
	// this.clientRandom = clientRandom;
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// Window.alert("Unable to log message to the server.");
	// }
	//
	// @Override
	// public void onSuccess(Void result) {
	// String message = "Random number sent to server was: "
	// + clientRandom.getValue();
	// label4.setHTML(message);
	// }
	// }
}
