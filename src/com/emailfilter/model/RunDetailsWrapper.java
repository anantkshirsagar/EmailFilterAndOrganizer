package com.emailfilter.model;

import com.emailfilter.constants.RunStatus;

public class RunDetailsWrapper {
	
	private int id;
	private String labelFilterName;
	private String deleteFilterName;
	private String runName;
	private RunStatus runStatus;
	private String failureReason;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabelFilterName() {
		return labelFilterName;
	}

	public void setLabelFilterName(String labelFilterName) {
		this.labelFilterName = labelFilterName;
	}

	public String getDeleteFilterName() {
		return deleteFilterName;
	}

	public void setDeleteFilterName(String deleteFilterName) {
		this.deleteFilterName = deleteFilterName;
	}

	public String getRunName() {
		return runName;
	}

	public void setRunName(String runName) {
		this.runName = runName;
	}

	public RunStatus getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(RunStatus runStatus) {
		this.runStatus = runStatus;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RunDetailsWrapper [id=");
		builder.append(id);
		builder.append(", labelFilterName=");
		builder.append(labelFilterName);
		builder.append(", deleteFilterName=");
		builder.append(deleteFilterName);
		builder.append(", runName=");
		builder.append(runName);
		builder.append(", runStatus=");
		builder.append(runStatus);
		builder.append(", failureReason=");
		builder.append(failureReason);
		builder.append("]");
		return builder.toString();
	}
}
