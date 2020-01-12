package com.emailfilter.model;

import java.io.Serializable;

public class LabelProperty implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String userEmailId;
	private String label;
	private String gmailParentLabelId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserEmailId() {
		return userEmailId;
	}

	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getGmailParentLabelId() {
		return gmailParentLabelId;
	}

	public void setGmailParentLabelId(String gmailParentLabelId) {
		this.gmailParentLabelId = gmailParentLabelId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LabelProperty [id=");
		builder.append(id);
		builder.append(", userEmailId=");
		builder.append(userEmailId);
		builder.append(", label=");
		builder.append(label);
		builder.append(", gmailParentLabelId=");
		builder.append(gmailParentLabelId);
		builder.append("]");
		return builder.toString();
	}
}
