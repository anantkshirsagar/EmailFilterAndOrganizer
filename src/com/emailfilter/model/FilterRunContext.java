package com.emailfilter.model;

import com.emailfilter.dbservices.DeleteFilterDBService;
import com.emailfilter.dbservices.LabelFilterDBService;
import com.emailfilter.dbservices.RunManagerDBService;
import com.emailfilter.services.LabelService;
import com.emailfilter.services.MessageService;

public class FilterRunContext {
	
	private MessageService messageService;
	private LabelService labelService;
	private FilterWrapper filterWrapper;
	private RunManagerDBService runManagerService;
	private LabelFilterDBService labelFilterService;
	private DeleteFilterDBService deleteFilterService;
	private String userEmailId;

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public LabelService getLabelService() {
		return labelService;
	}

	public void setLabelService(LabelService labelService) {
		this.labelService = labelService;
	}

	public FilterWrapper getFilterWrapper() {
		return filterWrapper;
	}

	public void setFilterWrapper(FilterWrapper filterWrapper) {
		this.filterWrapper = filterWrapper;
	}

	public String getUserEmailId() {
		return userEmailId;
	}

	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	public RunManagerDBService getRunManagerService() {
		return runManagerService;
	}

	public void setRunManagerService(RunManagerDBService runManagerService) {
		this.runManagerService = runManagerService;
	}

	public LabelFilterDBService getLabelFilterService() {
		return labelFilterService;
	}

	public void setLabelFilterService(LabelFilterDBService labelFilterService) {
		this.labelFilterService = labelFilterService;
	}

	public DeleteFilterDBService getDeleteFilterService() {
		return deleteFilterService;
	}

	public void setDeleteFilterService(DeleteFilterDBService deleteFilterService) {
		this.deleteFilterService = deleteFilterService;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LabelRunContext [messageService=");
		builder.append(messageService);
		builder.append(", labelService=");
		builder.append(labelService);
		builder.append(", filterWrapper=");
		builder.append(filterWrapper);
		builder.append(", runManagerService=");
		builder.append(runManagerService);
		builder.append(", labelFilterService=");
		builder.append(labelFilterService);
		builder.append(", deleteFilterService=");
		builder.append(deleteFilterService);
		builder.append(", userEmailId=");
		builder.append(userEmailId);
		builder.append("]");
		return builder.toString();
	}
}
