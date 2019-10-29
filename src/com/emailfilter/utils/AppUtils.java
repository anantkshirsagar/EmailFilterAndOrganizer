package com.emailfilter.utils;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AppUtils {

	private static final Logger LOG = Logger.getLogger(AppUtils.class.getName());

	private AppUtils() {
	}

	public static Object mapToClass(HttpServletRequest request, Class classType) throws IOException {
		String jsonObj = request.getReader().lines().collect(Collectors.joining());
		LOG.info("Json: " + jsonObj);
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.fromJson(jsonObj, classType);
	}

	public static Gson getGsonInstance() {
		GsonBuilder builder = new GsonBuilder();
		return builder.create();
	}
	
	public static void setResponseProperties(HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
	}
}
