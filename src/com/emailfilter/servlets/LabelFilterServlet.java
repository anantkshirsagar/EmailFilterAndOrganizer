package com.emailfilter.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emailfilter.model.FilterWrapper;
import com.emailfilter.utils.AppUtils;
import com.google.gson.Gson;

public class LabelFilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(LabelFilterServlet.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		FilterWrapper deleteFilterWrapper = (FilterWrapper) AppUtils.mapToClass(request,
				FilterWrapper.class);
		LOG.info("Parameters: " + deleteFilterWrapper);
		AppUtils.setResponseProperties(response);
		Gson gson = AppUtils.getGsonInstance();
		out.print(gson.toJson(deleteFilterWrapper));
	}
}
