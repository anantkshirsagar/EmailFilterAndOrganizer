package com.emailfilter.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.log.Log;

import com.emailfilter.model.FilterWrapper;
import com.emailfilter.services.GmailService;
import com.emailfilter.services.LabelService;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.Message;

public class LabelServiceTest {

	public static void main(String[] args) throws Exception {
//		printGmailLabelsTest();
//		printUserDefinedLabels();
		printGmailLabelsTest();
		// createGmailLabelTest();
	}

	public static void createGmailLabelTest() throws Exception {
		LabelService labelService = new LabelService("icbm.iot@gmail.com", new GmailService().getGmailService());
		Label createLabel = labelService.createLabel("suyog/anant");
		System.out.println(" label id: " + createLabel.getId());
	}

	public static void printGmailLabelsTest() throws Exception {
		LabelService labelService = new LabelService("icbm.iot@gmail.com", new GmailService().getGmailService());
		List<Label> labels = labelService.getAllLabels();
		for (Label label : labels) {
			System.out.println("Id: " + label.getId() + "  Name: " + label.getName());
		}
	}

	public static void printUserDefinedLabels() throws Exception {
		LabelService labelService = new LabelService("icbm.iot@gmail.com", new GmailService().getGmailService());
		List<Label> filteredLabel = labelService.getFilteredLabel();
		for (Label label : filteredLabel) {
			System.out.println("ID: " + label.getId() + ", Label name: " + label.getName());
		}
	}

	public void deleteGmailLabelTest() throws Exception {
		LabelService labelService = new LabelService("icbm.iot@gmail.com", new GmailService().getGmailService());
		labelService.deleteLabel("Label_5212935834667465516");
	}

	public void updateGmailLabelTest() throws Exception {
		LabelService labelService = new LabelService("icbm.iot@gmail.com", new GmailService().getGmailService());
		labelService.updateLabel("Label_5212935834667465516", "Label1", true, true);
	}
}
