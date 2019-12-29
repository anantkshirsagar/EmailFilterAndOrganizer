package com.emailfilter.services.run;

import com.emailfilter.model.FilterWrapper;

public interface IFilterRun {

	void run(FilterWrapper filterWrapper, String userEmailId) throws Exception;

	void bodyFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception;

	void subjectFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception;

	void emailFilter(FilterWrapper filterWrapper, String userEmailId) throws Exception;
}
