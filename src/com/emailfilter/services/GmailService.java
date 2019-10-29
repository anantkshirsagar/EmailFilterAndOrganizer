package com.emailfilter.services;

import com.emailfilter.constants.GmailAuth;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;

public class GmailService {

	public Gmail getGmailService() throws Exception {
		UserAuthenticationService authenticationService = new UserAuthenticationService();
		NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		return new Gmail.Builder(httpTransport, GmailAuth.JSON_FACTORY,
				authenticationService.getCredentials(httpTransport)).setApplicationName(GmailAuth.APPLICATION_NAME)
						.build();
	}
}
