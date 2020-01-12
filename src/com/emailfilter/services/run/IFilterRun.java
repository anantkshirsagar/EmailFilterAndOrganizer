package com.emailfilter.services.run;

import com.emailfilter.model.FilterWrapper;

public interface IFilterRun extends Runnable {

	void execute() throws Exception;

	void bodyFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception;

	void subjectFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception;

	void emailFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception;
}
