package com.emailfilter.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emailfilter.model.LabelProperty;
import com.emailfilter.utils.AppUtils;
import com.google.gson.Gson;

public class CreateLabelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(CreateLabelServlet.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		LabelProperty labelProperty = (LabelProperty) AppUtils.mapToClass(request, LabelProperty.class);
		LOG.info("Parameters: " +labelProperty);
		AppUtils.setResponseProperties(response);
		Gson gson = AppUtils.getGsonInstance();
		out.print(gson.toJson(labelProperty));
	}
}
