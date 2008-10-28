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

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.example.client.views.ComboSubModuleView;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.util.beans.StringBean;
import org.eclipse.riena.ui.ridgets.util.beans.StringManager;

/**
 * Controller for the {@link ComboSubModuleView} example.
 */
public class LogCollectorSubModuleController extends SubModuleController {

	private final StringManager logLevels;
	private final StringBean messageBean = new StringBean("Hallo!"); //$NON-NLS-1$
	private final StringBean exceptionBean = new StringBean(NullPointerException.class.getName());
	private IComboBoxRidget logLevelCombo;
	private ITextFieldRidget message;
	private ITextFieldRidget exception;

	private final static Logger LOGGER = Activator.getDefault().getLogger(
			LogCollectorSubModuleController.class.getName());

	public LogCollectorSubModuleController() {
		this(null);
	}

	public LogCollectorSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		logLevels = new StringManager("DEBUG", "INFO", "WARN", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		logLevels.setSelectedItem("DEBUG"); //$NON-NLS-1$
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
		logLevelCombo.bindToModel(logLevels, "items", String.class, null, logLevels, "selectedItem"); //$NON-NLS-1$ //$NON-NLS-2$ 
		logLevelCombo.updateFromModel();

		message.bindToModel(messageBean, "value"); //$NON-NLS-1$
		message.updateFromModel();

		exception.bindToModel(exceptionBean, "value"); //$NON-NLS-1$
		exception.updateFromModel();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		logLevelCombo = (IComboBoxRidget) getRidget("logLevelCombo"); //$NON-NLS-1$
		message = (ITextFieldRidget) getRidget("message"); //$NON-NLS-1$
		exception = (ITextFieldRidget) getRidget("exception"); //$NON-NLS-1$

		final IActionRidget buttonSave = (IActionRidget) getRidget("buttonLog"); //$NON-NLS-1$
		buttonSave.setText("&Log"); //$NON-NLS-1$
		buttonSave.addListener(new IActionListener() {
			public void callback() {
				Class<?> exceptionClass;
				Throwable throwable;
				try {
					exceptionClass = Class.forName(exception.getText());
					throwable = (Throwable) exceptionClass.newInstance();
					throwable.fillInStackTrace();
				} catch (Exception e) {
					throwable = new IllegalArgumentException("Can not instantiate exception: " + exception.getText(), e); //$NON-NLS-1$
				}
				LOGGER.log(4 - logLevelCombo.getSelectionIndex(), message.getText(), throwable);
			}
		});
	}
}
