package com.emailfilter.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.emailfilter.model.GmailMessage;
import com.emailfilter.services.GmailService;
import com.emailfilter.services.MessageService;
import com.google.api.services.gmail.model.Message;

public class MessageServiceTest {
	public static void main(String[] args) throws Exception {
		printMessagesByLabelIds();
//		moveMessage();
	}

	public static void moveMessage() throws Exception {
		List<String> labelsToAdd = Arrays.asList("Label_36");
		List<String> labelsToRemove = Arrays.asList("INBOX");
		String messageId = "16f50f14710edf42";
		String userId = "icbm.iot@gmail.com";
		MessageService messageService = new MessageService(userId, new GmailService().getGmailService());
		messageService.moveMessageFromLabel(messageId, labelsToAdd, labelsToRemove);
//		System.out.println("Message moved!");
	}

	public static void printMessagesByLabelIds() throws Exception {
		String userId = "icbm.iot@gmail.com";
		MessageService messageService = new MessageService(userId, new GmailService().getGmailService());
		List<String> labels = new ArrayList<String>();
		labels.add("INBOX");
		List<Message> messages = messageService.getMessagesByLabelIds(labels);
		System.out.println(" Length: " +messages.size());
		
		for (Message message : messages) {
			MimeMessage mimeMessage = messageService.getMimeMessage(message.getId());
			GmailMessage gmailMessage = messageService.getGmailMessage(mimeMessage);
//			System.out.println(gmailMessage.getSubject());
			printAllReceipients(gmailMessage.getAllRecipients());

//			System.out.println(gmailMessage.getSentDate());
//			System.out.println(gmailMessage.getReceivedDate());
		}
	}

	public static void printAllReceipients(List<Address> receipients) {
		System.out.println(receipients);
		for (Address address : receipients) {
			InternetAddress internetAddress = new InternetAddress();
			internetAddress.setAddress("anantkshirsagar38@gmail.com");
			System.out.println(address.equals(internetAddress));
		}
	}
}
