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

import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationViewController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.IStatusbarRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

/**
 * Controller of the sub-module that is an example how to display something in
 * the status line.
 */
public class StatuslineSubModuleViewController extends SubModuleNodeViewController {

	/**
	 * Different severities that can display in the status line.
	 */
	public enum Severity {
		TEXT("Text only"), //$NON-NLS-1$
		INFO("Information"), //$NON-NLS-1$
		WARNING("Warning"), //$NON-NLS-1$
		ERROR("Error"); //$NON-NLS-1$

		String text;

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

	private ITextFieldRidget messageText;

	/**
	 * @return the numberText
	 */
	public ITextFieldRidget getNumberText() {
		return numberText;
	}

	/**
	 * @param numberText
	 *            the numberText to set
	 */
	public void setNumberText(ITextFieldRidget numberText) {
		this.numberText = numberText;
	}

	/**
	 * @return the showNumber
	 */
	public IActionRidget getShowNumber() {
		return showNumber;
	}

	/**
	 * @param showNumber
	 *            the showNumber to set
	 */
	public void setShowNumber(IActionRidget showNumber) {
		this.showNumber = showNumber;
	}

	private IComboBoxRidget severity;
	private IActionRidget showMessage;
	private ITextFieldRidget numberText;
	private IActionRidget showNumber;
	private StatuslineModel model;

	/**
	 * Creates a new instance of {@code StatuslineSubModuleViewController}
	 * 
	 * @param navigationNode
	 *            - node of the sub-module
	 */
	public StatuslineSubModuleViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
		model = new StatuslineModel();
	}

	/**
	 * @return the messageText
	 */
	public ITextFieldRidget getMessageText() {
		return messageText;
	}

	/**
	 * @param messageText
	 *            the messageText to set
	 */
	public void setMessageText(ITextFieldRidget messageText) {
		this.messageText = messageText;
	}

	/**
	 * @return the severity
	 */
	public IComboBoxRidget getSeverity() {
		return severity;
	}

	/**
	 * @param severity
	 *            the severity to set
	 */
	public void setSeverity(IComboBoxRidget severity) {
		this.severity = severity;
	}

	/**
	 * @return the showMessage
	 */
	public IActionRidget getShowMessage() {
		return showMessage;
	}

	/**
	 * @param showMessage
	 *            the showMessage to set
	 */
	public void setShowMessage(IActionRidget showMessage) {
		this.showMessage = showMessage;
	}

	private IStatusbarRidget getStatusBar() {
		return getSubApplicationController().getStatusbarRidget();
	}

	/**
	 * Returns the controller of the parent sub-application.
	 * 
	 * @return sub-application controller
	 */
	private SubApplicationViewController getSubApplicationController() {
		return (SubApplicationViewController) getNavigationNode().getParentOfType(ISubApplication.class)
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
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController#afterBind()
	 */
	@Override
	public void afterBind() {

		super.afterBind();
		initRidgets();

	}

	/**
	 * Initializes all ridgets of the sub-module.
	 */
	private void initRidgets() {

		getMessageText().bindToModel(getModel(), "messageText"); //$NON-NLS-1$
		getMessageText().updateFromModel();

		getSeverity().bindToModel(getModel(), "severities", Severity.class, null, getModel(), "severity"); //$NON-NLS-1$ //$NON-NLS-2$
		getSeverity().updateFromModel();

		getShowMessage().setText("Show"); //$NON-NLS-1$
		getShowMessage().addListener(new MessageListener());

		getNumberText().bindToModel(getModel(), "numberText"); //$NON-NLS-1$
		getNumberText().updateFromModel();

		getShowNumber().setText("Show"); //$NON-NLS-1$
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
			getStatusBar().clear();
			switch (getModel().getSeverity()) {
			case INFO:
				getStatusBar().info(getMessageText().getText());
				break;
			case WARNING:
				getStatusBar().warning(getMessageText().getText());
				break;
			case ERROR:
				getStatusBar().error(getMessageText().getText());
				break;
			default:
				getStatusBar().setMessage(getMessageText().getText());
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
				getStatusBar().getStatusBarNumberRidget().setNumber(intValue);
			} catch (NumberFormatException e) {
				getStatusBar().getStatusBarNumberRidget().setNumberString(text);
			}
		}

	}

	/**
	 * THe model of the sub-module.
	 */
	public class StatuslineModel {

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
