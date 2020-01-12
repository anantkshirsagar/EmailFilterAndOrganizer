package com.emailfilter.services;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.constants.GmailAuth;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;

public class UserAuthenticationService {

	private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationService.class.getName());
	private static final java.util.List<String> SCOPES = Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);

	public Credential getCredentials(final NetHttpTransport httpTransport) throws Exception {
		LOG.debug("Checking credentials...");
		InputStream in = UserAuthenticationService.class.getResourceAsStream(GmailAuth.CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + GmailAuth.CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GmailAuth.JSON_FACTORY, new InputStreamReader(in));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
				GmailAuth.JSON_FACTORY, clientSecrets, SCOPES)
						.setDataStoreFactory(
								new FileDataStoreFactory(new java.io.File(GmailAuth.TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(GmailAuth.PORT).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}
}
