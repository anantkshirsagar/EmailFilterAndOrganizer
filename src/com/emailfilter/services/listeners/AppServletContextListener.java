package com.emailfilter.services.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.services.GmailService;
import com.google.api.services.gmail.Gmail;

public class AppServletContextListener implements ServletContextListener {

	private static Gmail service;
	private static final Logger LOG = LoggerFactory.getLogger(AppServletContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		GmailService gmailService = new GmailService();
		try {
			setService(gmailService.getGmailService());
		} catch (Exception e) {
			LOG.error("Service exception {}", e);
		}
	}

	public static Gmail getService() {
		return service;
	}

	public static void setService(Gmail service) {
		AppServletContextListener.service = service;
	}
}
