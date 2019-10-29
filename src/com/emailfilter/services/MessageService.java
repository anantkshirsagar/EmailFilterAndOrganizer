package com.emailfilter.services;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;

import com.emailfilter.model.GmailMessage;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

public class MessageService {

	private static final Logger LOG = Logger.getLogger(MessageService.class.getName());
	private final String userEmailId;
	private final Gmail gmailService;

	public MessageService(String userEmailId, Gmail gmailService) {
		this.userEmailId = userEmailId;
		this.gmailService = gmailService;
	}

	public List<Message> getMessagesByLabelIds(List<String> labelIds) throws Exception {
		LOG.info("Label Ids " + labelIds);
		ListMessagesResponse response = gmailService.users().messages().list(userEmailId).setLabelIds(labelIds).execute();
		LOG.info("Response " + response);

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = gmailService.users().messages().list(userEmailId).setLabelIds(labelIds).setPageToken(pageToken)
						.execute();
			} else {
				break;
			}
		}
		return messages;
	}

	public MimeMessage getMimeMessage(String messageId) throws Exception {
		LOG.info("Message Id: " + messageId);
		Message message = gmailService.users().messages().get(userEmailId, messageId).setFormat("raw").execute();
		Base64 base64Url = new Base64(true);
		byte[] emailBytes = base64Url.decodeBase64(message.getRaw());
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
		return email;
	}

	public GmailMessage getGmailMessage(MimeMessage mimeMessage) throws MessagingException {
		GmailMessage gmailMessage = new GmailMessage();
		gmailMessage.setAllRecipients(Arrays.asList(mimeMessage.getAllRecipients()));
		gmailMessage.setFrom(Arrays.asList(mimeMessage.getFrom()));
		gmailMessage.setReceivedDate(mimeMessage.getReceivedDate());
		gmailMessage.setReplyTo(Arrays.asList(mimeMessage.getReplyTo()));
		gmailMessage.setSentDate(mimeMessage.getSentDate());
		gmailMessage.setSubject(mimeMessage.getSubject());
		return gmailMessage;
	}
}
