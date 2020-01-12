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
import com.emailfilter.dbservices.DeleteFilterDBService;
import com.emailfilter.model.FilterWrapper;
import com.emailfilter.model.LabelFilterGridWrapper;
import com.emailfilter.utils.AppUtils;
import com.google.gson.Gson;

public class DeleteFilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DeleteFilterServlet.class);

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
			DeleteFilterDBService filterDBService = new DeleteFilterDBService();

			switch (callType) {
			case SAVE_DELETE_FILTER:
				FilterWrapper labelFilterWrapper = (FilterWrapper) AppUtils.mapToClass(request, FilterWrapper.class);
				LOG.info("labelFilterWrapper: {}", labelFilterWrapper);
				filterDBService.saveDeleteFilter(labelFilterWrapper, userEmailId);
				break;
			case DELETE_FILTER_GRID_INFO_SERVICE:
				String filterNameSearchStr = request.getHeader("searchFilter");
				LOG.debug("searchFilter {}", filterNameSearchStr);
				List<LabelFilterGridWrapper> labelFilterGridInfo = filterDBService.getDeleteFilterGridInfo(userEmailId,
						filterNameSearchStr);
				out.print(gson.toJson(labelFilterGridInfo));
				break;
			case EDIT_DELETE_FILTER:
				FilterWrapper editDeleteFilterWrapper = (FilterWrapper) AppUtils.mapToClass(request,
						FilterWrapper.class);
				LOG.info("editDeleteFilterWrapper: {}", editDeleteFilterWrapper);
				filterDBService.updateDeleteFilter(editDeleteFilterWrapper, userEmailId);
				break;
			case DELETE_FILTER_SERVICE:
				String filterId = request.getHeader("filterId");
				LOG.debug("filterId {}", filterId);
				filterDBService.deleteFilter(Integer.parseInt(filterId));
				break;
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error("Filter Exception: {}", e);
			out.print("Error while saving filter " + e);
		}
	}
}
