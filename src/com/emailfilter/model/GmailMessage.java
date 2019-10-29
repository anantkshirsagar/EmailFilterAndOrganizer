package com.emailfilter.model;

import java.util.Date;
import java.util.List;
import javax.mail.Address;

public class GmailMessage {
	private String subject;
	private List<Address> from;
	private List<Address> allRecipients;
	private List<Address> replyTo;
	private Date sentDate;
	private Date receivedDate;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<Address> getFrom() {
		return from;
	}

	public void setFrom(List<Address> from) {
		this.from = from;
	}

	public List<Address> getAllRecipients() {
		return allRecipients;
	}

	public void setAllRecipients(List<Address> allRecipients) {
		this.allRecipients = allRecipients;
	}

	public List<Address> getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(List<Address> replyTo) {
		this.replyTo = replyTo;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GmailMessage [subject=");
		builder.append(subject);
		builder.append(", from=");
		builder.append(from);
		builder.append(", allRecipients=");
		builder.append(allRecipients);
		builder.append(", replyTo=");
		builder.append(replyTo);
		builder.append(", sentDate=");
		builder.append(sentDate);
		builder.append(", receivedDate=");
		builder.append(receivedDate);
		builder.append("]");
		return builder.toString();
	}
}