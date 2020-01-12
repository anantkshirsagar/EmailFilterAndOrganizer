package com.emailfilter.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.constants.CallType;
import com.emailfilter.constants.LabelStatus;
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
				List<Label> gmailLabels = labelService.getFilteredLabel();
				LabelDBService labelDBService = new LabelDBService();
				List<LabelProperty> databaseLabels = labelDBService.getAllLabels(userEmailId);

				LabelStatus labelStatus = AppUtils.getLabelStatus(gmailLabels, databaseLabels);
				Map<LabelStatus, List<LabelProperty>> dataMap = AppUtils.syncDBAndGmailLabels(gmailLabels,
						databaseLabels, userEmailId, labelStatus);
				LOG.debug("dataMap {}", dataMap);
				labelDBService.checkLabelStatusAndUpdateDatabase(labelStatus, dataMap);
			}
			AppUtils.setResponseHeaders(response, 200, "Labels sync is completed!");
		} catch (Exception e) {
			AppUtils.setResponseHeaders(response, 500, "No new labels are found! Sync completed.");
			LOG.error("Servlet Exception {}", e);
		}
	}
}