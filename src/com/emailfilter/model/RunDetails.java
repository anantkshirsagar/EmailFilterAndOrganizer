package com.emailfilter.model;

import com.emailfilter.constants.RunStatus;

public class RunDetails {
	
	private int id;
	private int labelFilterId;
	private int deleteFilterId;
	private String runName;
	private RunStatus runStatus;
	private String failureReason;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLabelFilterId() {
		return labelFilterId;
	}

	public void setLabelFilterId(int labelFilterId) {
		this.labelFilterId = labelFilterId;
	}

	public int getDeleteFilterId() {
		return deleteFilterId;
	}

	public void setDeleteFilterId(int deleteFilterId) {
		this.deleteFilterId = deleteFilterId;
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
		builder.append("RunDetails [id=");
		builder.append(id);
		builder.append(", labelFilterId=");
		builder.append(labelFilterId);
		builder.append(", deleteFilterId=");
		builder.append(deleteFilterId);
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
