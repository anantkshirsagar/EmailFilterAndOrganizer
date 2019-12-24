package com.emailfilter.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.constants.CallType;
import com.emailfilter.dbservices.LabelFilterDBService;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.model.LabelFilterGridWrapper;
import com.emailfilter.utils.AppUtils;
import com.google.gson.Gson;

public class LabelFilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(LabelFilterServlet.class);

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
			LabelFilterDBService filterDBService = new LabelFilterDBService();

			switch (callType) {
			case SAVE_LABEL_FILTER:
				FilterWrapper labelFilterWrapper = (FilterWrapper) AppUtils.mapToClass(request, FilterWrapper.class);
				LOG.info("labelFilterWrapper: {}", labelFilterWrapper);
				filterDBService.saveLabelFilter(labelFilterWrapper, userEmailId);
				break;
			case LABEL_FILTER_GRID_INFO_SERVICE:
				String filterNameSearchStr = request.getHeader("searchFilter");
				LOG.debug("searchFilter {}", filterNameSearchStr);
				List<LabelFilterGridWrapper> labelFilterGridInfo = filterDBService.getLabelFilterGridInfo(userEmailId,
						filterNameSearchStr);
				out.print(gson.toJson(labelFilterGridInfo));
				break;
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error("Filter Exception: {}", e);
			out.print("Error while saving filter " + e);
			return;
		}
	}
}
