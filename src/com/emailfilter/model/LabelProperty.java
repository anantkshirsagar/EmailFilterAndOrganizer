package com.emailfilter.model;

import java.io.Serializable;

public class LabelProperty implements Serializable {

	private int id;
	private String userEmailId;
	private String label;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LabelProperty [id=");
		builder.append(id);
		builder.append(", userEmailId=");
		builder.append(userEmailId);
		builder.append(", label=");
		builder.append(label);
		builder.append("]");
		return builder.toString();
	}
}
