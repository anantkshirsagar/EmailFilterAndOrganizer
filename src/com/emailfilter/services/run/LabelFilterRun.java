package com.emailfilter.services.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.model.FilterWrapper;
import com.emailfilter.services.MessageService;

public class LabelFilterRun implements IFilterRun {

	private static final Logger LOG = LoggerFactory.getLogger(LabelFilterRun.class);
	private final MessageService messageService;

	public LabelFilterRun(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public void run(FilterWrapper filterWrapper, String userEmailId) throws Exception {
		LOG.debug("filterWrapper {}, userEmailId {}", filterWrapper, userEmailId);
		if (filterWrapper.isEmailFilter()) {
			LOG.debug("Email filter in process...");
			emailFilter(filterWrapper, userEmailId);
		}

		if (filterWrapper.isBodyFilter()) {
			LOG.debug("Body filter in process...");
			bodyFilter(filterWrapper, userEmailId);
		}

		if (filterWrapper.isSubjectFilter()) {
			LOG.debug("Subject filter in process...");
			subjectFilter(filterWrapper, userEmailId);
		}
	}

	@Override
	public void bodyFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception {

	}

	@Override
	public void subjectFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception {

	}

	@Override
	public void emailFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception {
		
	}
}
