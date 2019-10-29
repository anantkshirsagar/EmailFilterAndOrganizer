package com.emailfilter.model;

public class FilterWrapper {
	private String label;
	private boolean isEmailFilter;
	private String emailIds;
	private boolean isSubjectFilter;
	private String subjectKeywords;
	private boolean isBodyFilter;
	private String bodyKeywords;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isEmailFilter() {
		return isEmailFilter;
	}

	public void setEmailFilter(boolean isEmailFilter) {
		this.isEmailFilter = isEmailFilter;
	}

	public String getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}

	public boolean isSubjectFilter() {
		return isSubjectFilter;
	}

	public void setSubjectFilter(boolean isSubjectFilter) {
		this.isSubjectFilter = isSubjectFilter;
	}

	public String getSubjectKeywords() {
		return subjectKeywords;
	}

	public void setSubjectKeywords(String subjectKeywords) {
		this.subjectKeywords = subjectKeywords;
	}

	public boolean isBodyFilter() {
		return isBodyFilter;
	}

	public void setBodyFilter(boolean isBodyFilter) {
		this.isBodyFilter = isBodyFilter;
	}

	public String getBodyKeywords() {
		return bodyKeywords;
	}

	public void setBodyKeywords(String bodyKeywords) {
		this.bodyKeywords = bodyKeywords;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeleteFilterWrapper [label=");
		builder.append(label);
		builder.append(", isEmailFilter=");
		builder.append(isEmailFilter);
		builder.append(", emailIds=");
		builder.append(emailIds);
		builder.append(", isSubjectFilter=");
		builder.append(isSubjectFilter);
		builder.append(", subjectKeywords=");
		builder.append(subjectKeywords);
		builder.append(", isBodyFilter=");
		builder.append(isBodyFilter);
		builder.append(", bodyKeywords=");
		builder.append(bodyKeywords);
		builder.append("]");
		return builder.toString();
	}
}
