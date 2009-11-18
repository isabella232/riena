/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
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

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.example.client.views.LayoutSubModuleView;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;

/**
 * Controller for the {@link LayoutSubModuleView} example.
 * <p>
 * Shows how to use the {@link #layout()} method to recalculate the layout when
 * the text changes.
 */
public class LayoutSubModuleController extends SubModuleController {

	private String[] ridgets;
	private String[] captions;
	private int length = 1;

	@Override
	public void configureRidgets() {
		ISingleChoiceRidget ccGender = (ISingleChoiceRidget) getRidget("ccGender"); //$NON-NLS-1$
		ccGender.bindToModel(Arrays.asList(Person.FEMALE, Person.MALE), (List<String>) null, new StringBean(),
				StringBean.PROP_VALUE);
		ccGender.updateFromModel();

		IMultipleChoiceRidget ccPets = (IMultipleChoiceRidget) getRidget("ccPets"); //$NON-NLS-1$
		ccPets.bindToModel(Arrays.asList(Person.Pets.values()), (List<String>) null, new StringBean(),
				StringBean.PROP_VALUE);
		ccPets.updateFromModel();

		IActionRidget btnMore = (IActionRidget) getRidget("btnMore"); //$NON-NLS-1$
		final IActionRidget btnLess = (IActionRidget) getRidget("btnLess"); //$NON-NLS-1$

		btnMore.addListener(new IActionListener() {
			public void callback() {
				length++;
				btnLess.setEnabled(true);
				updateCaptions(ridgets, captions, length);
				layout();
			}
		});

		btnLess.addListener(new IActionListener() {
			public void callback() {
				length--;
				btnLess.setEnabled(length > 1);
				updateCaptions(ridgets, captions, length);
				layout();
			}
		});
		btnLess.setEnabled(false);
	}

	@Override
	public void afterBind() {
		ridgets = new String[] { "lblFirst", "lblLast", "lblGender", "lblPets" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		captions = getCaptions(ridgets);
	}

	// helping methods
	//////////////////

	private String[] getCaptions(String[] ridgets) {
		String[] result = new String[ridgets.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = ((ILabelRidget) getRidget(ridgets[i])).getText();
		}
		return result;
	}

	private void updateCaptions(String[] ridgets, String[] texts, final int length) {
		Assert.isLegal(length > 0);
		for (int i = 0; i < ridgets.length; i++) {
			ILabelRidget ridget = (ILabelRidget) getRidget(ridgets[i]);
			StringBuilder caption = new StringBuilder();
			for (int j = 0; j < length; j++) {
				caption.append(texts[i]);
			}
			ridget.setText(caption.toString());
		}
	}
}
