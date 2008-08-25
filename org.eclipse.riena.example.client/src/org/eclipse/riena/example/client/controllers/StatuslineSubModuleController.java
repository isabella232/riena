/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.controllers;

import java.util.Arrays;
import java.util.List;

import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

/**
 * Controller of the sub-module that is an example how to display something in
 * the status line.
 */
public class StatuslineSubModuleController extends SubModuleController {

	/**
	 * Different severities that can display in the status line.
	 */
	public enum Severity {
		TEXT("Text only"), //$NON-NLS-1$
		INFO("Information"), //$NON-NLS-1$
		WARNING("Warning"), //$NON-NLS-1$
		ERROR("Error"); //$NON-NLS-1$

		private final String text;

		Severity(String text) {
			this.text = text;
		}

		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return text;
		}

		final static Severity[] SERVERITIES = { Severity.TEXT, Severity.INFO, Severity.WARNING, Severity.ERROR };

	}

	private StatuslineModel model;

	public StatuslineSubModuleController() {
		this(null);
	}

	/**
	 * Creates a new instance of {@code StatuslineSubModuleController}
	 * 
	 * @param navigationNode
	 *            - node of the sub-module
	 */
	public StatuslineSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		model = new StatuslineModel();
	}

	/**
	 * @return the messageText
	 */
	private ITextFieldRidget getMessageText() {
		return (ITextFieldRidget) getRidget("messageText"); //$NON-NLS-1$
	}

	/**
	 * @return the severity
	 */
	private IComboBoxRidget getSeverity() {
		return (IComboBoxRidget) getRidget("severity"); //$NON-NLS-1$
	}

	/**
	 * @return the showMessage
	 */
	private IActionRidget getShowMessage() {
		return (IActionRidget) getRidget("showMessage"); //$NON-NLS-1$
	}

	/**
	 * @return the numberText
	 */
	private ITextFieldRidget getNumberText() {
		return (ITextFieldRidget) getRidget("numberText"); //$NON-NLS-1$
	}

	/**
	 * @return the showNumber
	 */
	private IActionRidget getShowNumber() {
		return (IActionRidget) getRidget("showNumber"); //$NON-NLS-1$
	}

	private IStatuslineRidget getStatusLine() {
		return getSubApplicationController().getStatuslineRidget();
	}

	/**
	 * Returns the controller of the parent sub-application.
	 * 
	 * @return sub-application controller
	 */
	private SubApplicationController getSubApplicationController() {
		return (SubApplicationController) getNavigationNode().getParentOfType(ISubApplicationNode.class)
				.getPresentation();
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(StatuslineModel model) {
		this.model = model;
	}

	/**
	 * @return the model
	 */
	public StatuslineModel getModel() {
		return model;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		bindModels();
	}

	private void bindModels() {
		getMessageText().bindToModel(getModel(), "messageText"); //$NON-NLS-1$
		getMessageText().updateFromModel();

		getSeverity().bindToModel(getModel(), "severities", Severity.class, null, getModel(), "severity"); //$NON-NLS-1$ //$NON-NLS-2$
		getSeverity().updateFromModel();

		getNumberText().bindToModel(getModel(), "numberText"); //$NON-NLS-1$
		getNumberText().updateFromModel();
	}

	/**
	 * Initializes all ridgets of the sub-module.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		getShowMessage().addListener(new MessageListener());

		getShowNumber().addListener(new NumberListener());

	}

	/**
	 * Displays a message in the status line.
	 */
	private class MessageListener implements IActionListener {

		/**
		 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
		 */
		public void callback() {
			getStatusLine().clear();
			switch (getModel().getSeverity()) {
			case INFO:
				getStatusLine().info(getMessageText().getText());
				break;
			case WARNING:
				getStatusLine().warning(getMessageText().getText());
				break;
			case ERROR:
				getStatusLine().error(getMessageText().getText());
				break;
			default:
				getStatusLine().setMessage(getMessageText().getText());
				break;
			}
		}

	}

	/**
	 * Displays a number in the status line.
	 */
	private class NumberListener implements IActionListener {

		/**
		 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
		 */
		public void callback() {
			String text = getModel().getNumberText();
			try {
				int intValue = Integer.parseInt(text);
				getStatusLine().getStatuslineNumberRidget().setNumber(intValue);
			} catch (NumberFormatException e) {
				getStatusLine().getStatuslineNumberRidget().setNumberString(text);
			}
		}

	}

	/**
	 * THe model of the sub-module.
	 */
	public static class StatuslineModel {

		private String messageText;
		private String numberText;
		private Severity severity;

		/**
		 * Creates a new instance of {@code StatuslineModel} and sets the
		 * default values.
		 */
		public StatuslineModel() {
			messageText = "Message"; //$NON-NLS-1$
			numberText = "4711"; //$NON-NLS-1$
			severity = Severity.TEXT;
		}

		/**
		 * @param messageText
		 *            the messageText to set
		 */
		public void setMessageText(String messageText) {
			this.messageText = messageText;
		}

		/**
		 * @return the messageText
		 */
		public String getMessageText() {
			return messageText;
		}

		/**
		 * @param severity
		 *            the severity to set
		 */
		public void setSeverity(Severity severity) {
			this.severity = severity;
		}

		/**
		 * @return the severity
		 */
		public Severity getSeverity() {
			return severity;
		}

		public List<Severity> getSeverities() {
			return Arrays.asList(Severity.SERVERITIES);
		}

		/**
		 * @param numberText
		 *            the numberText to set
		 */
		public void setNumberText(String numberText) {
			this.numberText = numberText;
		}

		/**
		 * @return the numberText
		 */
		public String getNumberText() {
			return numberText;
		}

	}

}
