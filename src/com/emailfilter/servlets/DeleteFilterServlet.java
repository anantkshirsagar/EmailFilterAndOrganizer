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

public class DeleteFilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(DeleteFilterServlet.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		FilterWrapper deleteFilterWrapper = (FilterWrapper) AppUtils.mapToClass(request,
				FilterWrapper.class);
		LOG.info("Parameters: " + deleteFilterWrapper);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		Gson gson = AppUtils.getGsonInstance();
		out.print(gson.toJson(deleteFilterWrapper));
	}
}
