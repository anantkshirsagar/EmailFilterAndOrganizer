package com.emailfilter.constants;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public final class GmailAuth {
	public static final String APPLICATION_NAME = "Email Filter And Organizer";
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	public static final String TOKENS_DIRECTORY_PATH = "tokens";
	public static final String CREDENTIALS_FILE_PATH = "/google-credentials/google-client-credentials.json";
	
	public static final int PORT = 9090;
	
	public enum MessageVisibility {
		SHOW, HIDE
	}
	
	public enum LabelListVisibility {
		labelShow, labelHide
	}
}
