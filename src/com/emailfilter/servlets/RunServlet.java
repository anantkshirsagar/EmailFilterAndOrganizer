package com.emailfilter.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.constants.CallType;
import com.emailfilter.dbservices.RunManagerDBService;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.model.FilterRunContext;
import com.emailfilter.model.RunDetails;
import com.emailfilter.model.RunDetailsWrapper;
import com.emailfilter.services.run.DeleteFilterRun;
import com.emailfilter.services.run.IFilterRun;
import com.emailfilter.services.run.LabelFilterRun;
import com.emailfilter.services.run.RunManager;
import com.emailfilter.utils.AppUtils;
import com.emailfilter.utils.RunUtils;
import com.google.gson.Gson;

public class RunServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(RunServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = null;
		try {
			String callTypeStr = request.getHeader("callType");
			String userEmailId = request.getHeader("userEmailId");
			LOG.debug("Header params userEmailId {}, header {}", userEmailId, callTypeStr);
			CallType callType = CallType.valueOf(callTypeStr);
			out = response.getWriter();
			Gson gson = AppUtils.getGsonInstance();
			RunManagerDBService runService = new RunManagerDBService();

			switch (callType) {
			case RUN_GRID_INFO_SERVICE:
				List<RunDetails> allRunDetails = runService.getAllRunDetails();
				List<RunDetailsWrapper> runDetailsWrapperList = runService
						.convertRunDetailsListIntoWrapperList(allRunDetails);
				LOG.debug("runDetailsWrapperList {}", runDetailsWrapperList);
				out.print(gson.toJson(runDetailsWrapperList));
				break;
			case LABEL_RUN_SERVICE:
				FilterWrapper labelFilterWrapper = (FilterWrapper) AppUtils.mapToClass(request, FilterWrapper.class);
				FilterRunContext runContext = RunUtils.getLabelFilterRunContext(labelFilterWrapper, userEmailId);
				IFilterRun labelFilterRun = new LabelFilterRun(runContext);
				new RunManager(labelFilterRun).execute();
				break;
			case DELETE_RUN_SERVICE:
				FilterWrapper deleteFilterWrapper = (FilterWrapper) AppUtils.mapToClass(request, FilterWrapper.class);
				FilterRunContext deleteRunContext = RunUtils.getDeleteFilterRunContext(deleteFilterWrapper, userEmailId);
				IFilterRun deleteFilterRun = new DeleteFilterRun(deleteRunContext);
				new RunManager(deleteFilterRun).execute();
				break;
			}
		} catch (Exception e) {
			LOG.error("Servlet Exception {}", e);
		}
	}
}
