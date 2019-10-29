package com.emailfilter.model;

public class LabelProperty {
	private String label;
	private String parentLabel;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getParentLabel() {
		return parentLabel;
	}

	public void setParentLabel(String parentLabel) {
		this.parentLabel = parentLabel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LabelProperties [label=");
		builder.append(label);
		builder.append(", parentLabel=");
		builder.append(parentLabel);
		builder.append("]");
		return builder.toString();
	}
}
