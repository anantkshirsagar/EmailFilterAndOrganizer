package com.emailfilter.utils;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.objectify.Objectify;
import com.emailfilter.constants.AppConstants;
import com.emailfilter.constants.FilterLabels;
import com.emailfilter.model.LabelProperty;
import com.google.api.services.gmail.model.Label;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class AppUtils {

	private static final Logger LOG = LoggerFactory.getLogger(AppUtils.class.getName());

	private AppUtils() {
	}

	public static Object mapToClass(HttpServletRequest request, Class classType) throws IOException {
		String jsonObj = request.getReader().lines().collect(Collectors.joining());
		LOG.debug("Json {}", jsonObj);
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.fromJson(jsonObj, classType);
	}

	public static Gson getGsonInstance() {
		GsonBuilder builder = new GsonBuilder();
		return builder.create();
	}

	public static boolean isStringEmpty(String string) {
		return string == null || string.isEmpty();
	}

	public static boolean isStringNotEmpty(String string) {
		return string != null && !string.isEmpty();
	}

	public static byte[] getObject(List<String> keywordsList) throws SQLException, IOException {
		return Objectify.serialize(keywordsList);
	}

	public static boolean isUserDefinedLabel(String labelName) {
		boolean flag = false;
		if (FilterLabels.CATEGORY_FORUMS.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.CATEGORY_FORUMS.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.CATEGORY_PERSONAL.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.CATEGORY_PROMOTIONS.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.CATEGORY_SOCIAL.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.CATEGORY_UPDATES.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.CHAT.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.DRAFT.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.INBOX.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.SENT.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.SPAM.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.STARRED.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.TRASH.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.UNREAD.name().equals(labelName)) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}

	public static boolean isCollectionEmpty(final Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isCollectionNotEmpty(final Collection<?> collection) {
		return !isCollectionEmpty(collection);
	}

	public static void setResponseProperties(HttpServletResponse response, int code, String message) {
		response.setHeader("code", String.valueOf(code));
		response.setHeader("message", message);
	}

	public static void setResponseProperties(HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
	}

	public static void validateAndSetParentLabel(LabelProperty labelProperty) {
		if (isStringNotEmpty(labelProperty.getGmailParentLabelId())) {
			labelProperty.setLabel(labelProperty.getGmailParentLabelId().concat("/").concat(labelProperty.getLabel()));
		}
	}
}
