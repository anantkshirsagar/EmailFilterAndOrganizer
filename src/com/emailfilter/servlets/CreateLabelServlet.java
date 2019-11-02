package com.emailfilter.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emailfilter.model.LabelProperty;
import com.emailfilter.utils.AppUtils;
import com.google.gson.Gson;

public class CreateLabelServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CreateLabelServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			LabelProperty labelProperty = (LabelProperty) AppUtils.mapToClass(request, LabelProperty.class);
			LOG.debug("Params {}", labelProperty);
			AppUtils.setResponseProperties(response);
			Gson gson = AppUtils.getGsonInstance();
			out.print(gson.toJson(labelProperty));
		} catch (Exception e) {
			LOG.error("Servlet Exception {}", e);
		}
	}
}