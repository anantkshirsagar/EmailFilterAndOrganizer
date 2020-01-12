package com.emailfilter.model;

import java.io.Serializable;
import java.util.List;

public class FilterWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String label;
	private String filterName;
	private boolean isEmailFilter;
	private List<String> emailIds;
	private boolean isSubjectFilter;
	private List<String> subjectKeywords;
	private boolean isBodyFilter;
	private List<String> bodyKeywords;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public boolean isEmailFilter() {
		return isEmailFilter;
	}

	public void setEmailFilter(boolean isEmailFilter) {
		this.isEmailFilter = isEmailFilter;
	}

	public List<String> getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(List<String> emailIds) {
		this.emailIds = emailIds;
	}

	public boolean isSubjectFilter() {
		return isSubjectFilter;
	}

	public void setSubjectFilter(boolean isSubjectFilter) {
		this.isSubjectFilter = isSubjectFilter;
	}

	public List<String> getSubjectKeywords() {
		return subjectKeywords;
	}

	public void setSubjectKeywords(List<String> subjectKeywords) {
		this.subjectKeywords = subjectKeywords;
	}

	public boolean isBodyFilter() {
		return isBodyFilter;
	}

	public void setBodyFilter(boolean isBodyFilter) {
		this.isBodyFilter = isBodyFilter;
	}

	public List<String> getBodyKeywords() {
		return bodyKeywords;
	}

	public void setBodyKeywords(List<String> bodyKeywords) {
		this.bodyKeywords = bodyKeywords;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FilterWrapper [id=");
		builder.append(id);
		builder.append(", label=");
		builder.append(label);
		builder.append(", filterName=");
		builder.append(filterName);
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