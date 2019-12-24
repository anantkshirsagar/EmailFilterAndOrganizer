package com.emailfilter.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.objectify.Objectify;
import com.emailfilter.constants.FilterLabels;
import com.emailfilter.constants.LabelStatus;
import com.emailfilter.model.LabelGridWrapper;
import com.emailfilter.model.LabelProperty;
import com.google.api.services.gmail.model.Label;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
		} else if (FilterLabels.CATEGORY_PERSONAL.name().equals(labelName)) {
			flag = false;
		} else if (FilterLabels.IMPORTANT.name().equals(labelName)) {
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

	public static void setResponseHeaders(HttpServletResponse response, int code, String message) {
		response.setHeader("code", String.valueOf(code));
		response.setHeader("message", message);
	}

	public static void setResponseProperties(HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setStatus(200);
	}

	public static void validateAndSetParentLabel(LabelProperty labelProperty) {
		if (isStringNotEmpty(labelProperty.getGmailParentLabelId())) {
			labelProperty.setLabel(labelProperty.getGmailParentLabelId().concat("/").concat(labelProperty.getLabel()));
		}
	}

	public static LabelStatus getLabelStatus(List<Label> gmailLabels, List<LabelProperty> databaseLabels) {
		if (isCollectionEmpty(databaseLabels)) {
			if (isCollectionEmpty(gmailLabels)) {
				return LabelStatus.BOTH_DOES_NOT_CONTAINS_DATA;
			}
			return LabelStatus.ONLY_GMAIL_CONTAINS_DATA;
		} else if (isCollectionEmpty(gmailLabels)) {
			if (isCollectionEmpty(databaseLabels)) {
				return LabelStatus.BOTH_DOES_NOT_CONTAINS_DATA;
			}
			return LabelStatus.ONLY_DB_CONTAINS_DATA;
		}
		return LabelStatus.BOTH_CONTAINS_DATA;
	}

	public static Map<LabelStatus, List<LabelProperty>> syncDBAndGmailLabels(List<Label> gmailLabels,
			List<LabelProperty> databaseLabels, String userEmailId, LabelStatus labelStatus) {
		LOG.debug("LabelStatus {}", labelStatus);
		Map<LabelStatus, List<LabelProperty>> statusAndLabelsMap = new HashMap<LabelStatus, List<LabelProperty>>();
		switch (labelStatus) {
		case BOTH_CONTAINS_DATA:
			LOG.debug("BOTH_CONTAINS_DATA");
			statusAndLabelsMap.put(LabelStatus.ADD_TO_DATABASE,
					dBLabelsCompareWithGmailLabels(gmailLabels, databaseLabels, userEmailId));
			statusAndLabelsMap.put(LabelStatus.DELETE_FROM_DATABASE,
					gmailLabelsCompareWithDBLabels(gmailLabels, databaseLabels, userEmailId));
			break;
		case ONLY_DB_CONTAINS_DATA:
			LOG.debug("ONLY_DB_CONTAINS_DATA");
			statusAndLabelsMap.put(LabelStatus.DELETE_FROM_DATABASE, databaseLabels);
			break;
		case ONLY_GMAIL_CONTAINS_DATA:
			LOG.debug("ONLY_GMAIL_CONTAINS_DATA");
			statusAndLabelsMap.put(LabelStatus.ADD_TO_DATABASE,
					filterIfOnlyGmailContainsData(gmailLabels, databaseLabels, userEmailId));
			break;
		default:
		}
		return statusAndLabelsMap;
	}

	public static List<LabelProperty> filterIfOnlyGmailContainsData(List<Label> gmailLabels,
			List<LabelProperty> databaseLabels, String userEmailId) {
		LOG.debug("databaseLabels {}, gmailLabels {}", databaseLabels, gmailLabels);
		if (isCollectionEmpty(databaseLabels)) {
			databaseLabels = new ArrayList<LabelProperty>();
		}
		for (Label label : gmailLabels) {
			LabelProperty labelProperty = new LabelProperty();
			labelProperty.setLabel(label.getName());
			labelProperty.setUserEmailId(userEmailId);
			databaseLabels.add(labelProperty);
		}
		return databaseLabels;
	}

	// add in labels in the database
	public static List<LabelProperty> dBLabelsCompareWithGmailLabels(List<Label> gmailLabels,
			List<LabelProperty> databaseLabels, String userEmailId) {
		LOG.debug("databaseLabels {}, gmailLabels {}", databaseLabels, gmailLabels);
		List<LabelProperty> labelProperties = new ArrayList<LabelProperty>();
		for (Label label : gmailLabels) {
			LabelProperty newLabelProperty = getGmailLabelPropertyIfNotExistInDB(label, databaseLabels, userEmailId);
			if (newLabelProperty != null) {
				labelProperties.add(newLabelProperty);
			}
		}
		return labelProperties;
	}

	// delete from database
	public static List<LabelProperty> gmailLabelsCompareWithDBLabels(List<Label> gmailLabels,
			List<LabelProperty> databaseLabels, String userEmailId) {
		List<LabelProperty> labelProperties = new ArrayList<LabelProperty>();
		for (LabelProperty labelProperty : databaseLabels) {
			labelProperty.setUserEmailId(userEmailId);
			LabelProperty property = getDBLabelPropertyIfNotExistInGmail(labelProperty, gmailLabels);
			if (property != null) {
				labelProperties.add(property);
			}
		}
		return labelProperties;
	}

	public static LabelProperty getGmailLabelPropertyIfNotExistInDB(Label gmailLabel,
			List<LabelProperty> databaseLabels, String userEmailId) {
		for (LabelProperty property : databaseLabels) {
			if (!gmailLabel.getName().equals(property.getLabel())) {
				LabelProperty labelProperty = new LabelProperty();
				labelProperty.setLabel(gmailLabel.getName());
				labelProperty.setUserEmailId(userEmailId);
				return labelProperty;
			}
		}
		return null;
	}

	public static LabelProperty getDBLabelPropertyIfNotExistInGmail(LabelProperty labelProperty,
			List<Label> gmailLabels) {
		for (Label label : gmailLabels) {
			if (label.getName().equals(labelProperty.getLabel())) {
				return null;
			}
		}
		return labelProperty;
	}

	public static String listToStringConvertor(List<String> stringList, String delimeter) {
		if (isCollectionNotEmpty(stringList)) {
			StringBuilder stringBuilder = new StringBuilder();
			for (String string : stringList) {
				stringBuilder.append(string).append(" ").append(delimeter);
			}
			return stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1).toString();
		}
		return null;
	}
}
