package com.emailfilter.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.model.GmailMessage;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.ModifyMessageRequest;

public class MessageService {

	private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
	private final String userEmailId;
	private final Gmail gmailService;

	public MessageService(String userEmailId, Gmail gmailService) {
		this.userEmailId = userEmailId;
		this.gmailService = gmailService;
	}

	public List<Message> getMessagesByLabelIds(List<String> labelIds) throws Exception {
		LOG.debug("Label Ids {}", labelIds);
		ListMessagesResponse response = gmailService.users().messages().list(userEmailId).setLabelIds(labelIds)
				.execute();
		LOG.debug("Response {}", response);

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = gmailService.users().messages().list(userEmailId).setLabelIds(labelIds)
						.setPageToken(pageToken).execute();
			} else {
				break;
			}
		}
		return messages;
	}

	public MimeMessage getMimeMessage(String messageId) throws Exception {
		LOG.debug("Message id: {}", messageId);
		Message message = gmailService.users().messages().get(userEmailId, messageId).setFormat("raw").execute();
		Base64 base64Url = new Base64(true);
		byte[] emailBytes = base64Url.decodeBase64(message.getRaw());
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		return new MimeMessage(session, new ByteArrayInputStream(emailBytes));
	}

	/**
	 * Move message (mail) from one label to another label <br>
	 * labelToAdd: This list contains label ids where we want to move the mail. <br>
	 * labelsToRemove: This list contains label ids where we want to remove the
	 * mail.
	 * 
	 * @param messageId
	 * @param labelsToAdd
	 * @param labelsToRemove
	 * @throws IOException
	 */
	public void moveMessageFromLabel(String messageId, List<String> labelsToAdd, List<String> labelsToRemove)
			throws IOException {
		LOG.debug("Message id: {}", messageId);
		ModifyMessageRequest modifyMessageRequest = new ModifyMessageRequest().setAddLabelIds(labelsToAdd)
				.setRemoveLabelIds(labelsToRemove);
		gmailService.users().messages().modify(userEmailId, messageId, modifyMessageRequest).execute();
	}

	public GmailMessage getGmailMessage(MimeMessage mimeMessage) throws MessagingException, IOException {
		GmailMessage gmailMessage = new GmailMessage();
		gmailMessage.setAllRecipients(Arrays.asList(mimeMessage.getAllRecipients()));
		gmailMessage.setFrom(Arrays.asList(mimeMessage.getFrom()));
		gmailMessage.setReceivedDate(mimeMessage.getReceivedDate());
		gmailMessage.setReplyTo(Arrays.asList(mimeMessage.getReplyTo()));
		gmailMessage.setSentDate(mimeMessage.getSentDate());
		gmailMessage.setSubject(mimeMessage.getSubject());
		gmailMessage.setBody(new String(IOUtils.toByteArray(mimeMessage.getInputStream())));
		return gmailMessage;
	}

	public void trashMessage(String messageId) throws IOException {
		gmailService.users().messages().trash(userEmailId, messageId).execute();
		LOG.debug("Message with id: {} has been trashed.", messageId);
	}
}
