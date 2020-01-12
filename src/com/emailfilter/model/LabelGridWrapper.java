package com.emailfilter.model;

import java.util.Date;

public class LabelGridWrapper {
	
	private int id;
	private String labelName;
	private String oldLabelName;
	private Date creationDate;

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOldLabelName() {
		return oldLabelName;
	}

	public void setOldLabelName(String oldLabelName) {
		this.oldLabelName = oldLabelName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GridLabelWrapper [id=");
		builder.append(id);
		builder.append(", labelName=");
		builder.append(labelName);
		builder.append(", oldLabelName=");
		builder.append(oldLabelName);
		builder.append(", creationDate=");
		builder.append(creationDate);
		builder.append("]");
		return builder.toString();
	}
}
