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

import com.emailfilter.constants.AppConstants;
import com.emailfilter.constants.CallType;
import com.emailfilter.dbservices.LabelDBService;
import com.emailfilter.model.GridLabelWrapper;
import com.emailfilter.model.LabelProperty;
import com.emailfilter.services.GmailService;
import com.emailfilter.services.LabelService;
import com.emailfilter.services.listeners.AppServletContextListener;
import com.emailfilter.utils.AppUtils;
import com.google.api.services.gmail.model.Label;
import com.google.gson.Gson;

public class CreateLabelServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CreateLabelServlet.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String callTypeStr = request.getHeader("callType");
			String userEmailId = request.getHeader("userEmailId");
			LOG.debug("params userEmailId {}, header {}", userEmailId, callTypeStr);
			CallType callType = CallType.valueOf(callTypeStr);
			PrintWriter out = response.getWriter();
			Gson gson = AppUtils.getGsonInstance();
			LabelService labelService = new LabelService(userEmailId, AppServletContextListener.getService());
			LabelDBService labelDBService = null;
			switch (callType) {
			case GET_ALL_LABEL:
				List<Label> labels = labelService.getFilteredLabel();
				LOG.debug("Available labels {}", labels);
				AppUtils.setResponseProperties(response);
				out.print(gson.toJson(labels));
				break;
			case CREATE_LABEL_SERVICE:
				LabelProperty labelProperty = (LabelProperty) AppUtils.mapToClass(request, LabelProperty.class);
				AppUtils.validateAndSetParentLabel(labelProperty);
				LOG.debug("Params {}", labelProperty);
				labelDBService = new LabelDBService();
				labelDBService.saveLabel(labelProperty);
				Label labelCreated = labelService.createLabel(labelProperty.getLabel());
				LOG.debug("Label created successfully Id: {}, Name: {}", labelCreated.getId(), labelCreated.getName());
				AppUtils.setResponseProperties(response);
				out.print(gson.toJson(labelProperty));
				break;
			case LABEL_GRID_INFO_SERVICE:
				String searchFilter = request.getHeader("searchFilter");
				LOG.debug("searchFilter {}", searchFilter);
				labelDBService = new LabelDBService();
				List<GridLabelWrapper> gridInfoList = labelDBService.getLabelGridInfo(userEmailId, searchFilter);
				AppUtils.setResponseProperties(response);
				out.print(gson.toJson(gridInfoList));
				break;
			default:
				LOG.debug("No matching case found!");
			}
		} catch (Exception e) {
			LOG.error("Servlet Exception {}", e);
		}
	}
}