package com.emailfilter.tests;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import com.emailfilter.model.GmailMessage;
import com.emailfilter.services.GmailService;
import com.emailfilter.services.MessageService;
import com.google.api.services.gmail.model.Message;

public class MessageServiceTest {
	public static void main(String[] args) throws Exception {
		printMessagesByLabelIds();
	}

	public static void printMessagesByLabelIds() throws Exception {
		String userId = "icbm.iot@gmail.com";
		MessageService messageService = new MessageService(userId, new GmailService().getGmailService());
		List<String> labels = new ArrayList<String>();
		labels.add("INBOX");
		List<Message> messages = messageService.getMessagesByLabelIds(labels);

		for (Message message : messages) {
			MimeMessage mimeMessage = messageService.getMimeMessage(message.getId());
			GmailMessage gmailMessage = messageService.getGmailMessage(mimeMessage);
			System.out.println(gmailMessage.getSubject());
			System.out.println(gmailMessage.getSentDate());
			System.out.println(gmailMessage.getReceivedDate());
		}
	}
}
