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
import com.emailfilter.dbservices.LabelDBService;
import com.emailfilter.model.LabelProperty;
import com.emailfilter.services.LabelService;
import com.emailfilter.services.listeners.AppServletContextListener;
import com.emailfilter.utils.AppUtils;
import com.google.api.services.gmail.model.Label;

public class SyncLabelServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SyncLabelServlet.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			String callTypeStr = request.getHeader("callType");
			String userEmailId = request.getHeader("userEmailId");
			LOG.debug("callType {}, userEmailId {}", callTypeStr, userEmailId);

			if (CallType.SYNC_LABEL_SERVICE.name().equals(callTypeStr)) {
				LabelService labelService = new LabelService(userEmailId, AppServletContextListener.getService());
				List<Label> filteredLabel = labelService.getFilteredLabel();
				LOG.debug("Labels list {}", filteredLabel);

				if (AppUtils.isCollectionNotEmpty(filteredLabel)) {
					LabelDBService labelDBService = new LabelDBService();
					for (Label label : filteredLabel) {
						LabelProperty labelProperty = new LabelProperty();
						labelProperty.setLabel(label.getName());
						labelProperty.setUserEmailId(userEmailId);
						labelDBService.saveLabel(labelProperty);
					}
				}
			}
			AppUtils.setResponseProperties(response, 200, "Labels sync is completed!");
		} catch (Exception e) {
			AppUtils.setResponseProperties(response, 500, "No new labels are found! Sync completed.");
			LOG.error("Servlet Exception {}", e);
		}
	}
}