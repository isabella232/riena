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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.util.beans.StringBean;
import org.eclipse.riena.ui.ridgets.util.beans.TypedBean;

/**
 * Controller for the {@link IDateTextRidget} example.
 */
public class TextDateSubModuleController extends SubModuleController {

	@Override
	public void afterBind() {
		super.afterBind();
		configureRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	public void configureRidgets() {
		String[] ids = { "dd.MM.yyyy", "dd.MM.yy", "dd.MM", "MM.yyyy", "yyyy", "HH:mm:ss", "HH:mm", "dd.MM.yyyy_HH:mm" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		DataBindingContext dbc = new DataBindingContext();
		for (String id : ids) {
			bind(dbc, id);
		}

		// date

		bindToModel("dd.MM.yyyy", new StringBean("01.10.2008")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("dd.MM.yy", new StringBean("01.10.08")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("dd.MM", new StringBean("01.10")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("MM.yyyy", new StringBean("10.2008")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("yyyy", new StringBean("2008")); //$NON-NLS-1$ //$NON-NLS-2$

		// time && date/time

		bindToModel("HH:mm:ss", new StringBean("23:55:00")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("HH:mm", new StringBean("23:55")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("dd.MM.yyyy_HH:mm", new StringBean("01.10.2008 23:55")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	// helping methods
	//////////////////

	private void bind(DataBindingContext dbc, String id) {
		IRidget inputRidget = (IRidget) getRidget("in" + id); //$NON-NLS-1$
		IRidget outputRidget = (IRidget) getRidget("out" + id); //$NON-NLS-1$
		dbc.bindValue(BeansObservables.observeValue(inputRidget, ITextRidget.PROPERTY_TEXT), BeansObservables
				.observeValue(outputRidget, ITextRidget.PROPERTY_TEXT), new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
	}

	private void bindToModel(String id, StringBean value) {
		IDateTextRidget ridget = (IDateTextRidget) getRidget("in" + id); //$NON-NLS-1$
		ridget.setFormat(id.replace('_', ' '));
		ridget.bindToModel(value, TypedBean.PROP_VALUE);
		ridget.updateFromModel();
	}
}
