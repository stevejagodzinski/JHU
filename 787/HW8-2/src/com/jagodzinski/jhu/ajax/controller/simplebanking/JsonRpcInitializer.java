package com.jagodzinski.jhu.ajax.controller.simplebanking;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.jabsorb.JSONRPCBridge;

@WebListener
public class JsonRpcInitializer implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		JSONRPCBridge globalBridge = JSONRPCBridge.getGlobalBridge();
		globalBridge.registerObject("customerLookupService", CustomerLookupService.getInstance());
	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}
