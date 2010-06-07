/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link ComboCompletionSubModuleView} example.
 */
public class CompletionComboSubModuleController extends SubModuleController {

	@Override
	public void configureRidgets() {
		IComboRidget combo1 = getRidget(IComboRidget.class, "combo1"); //$NON-NLS-1$
		IComboRidget combo2 = getRidget(IComboRidget.class, "combo2"); //$NON-NLS-1$
		ITextRidget selection1 = getRidget(ITextRidget.class, "selection1"); //$NON-NLS-1$
		ITextRidget selection2 = getRidget(ITextRidget.class, "selection2"); //$NON-NLS-1$
		final ITextRidget text1 = getRidget(ITextRidget.class, "text1"); //$NON-NLS-1$
		final ITextRidget text2 = getRidget(ITextRidget.class, "text2"); //$NON-NLS-1$

		ListBean input = new ListBean("Aachen", "Athens", "Austin", "Arkansas", "Ashland", "London", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				"Moskow", "New York", "Paris", "Portland", "Potzdam"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		combo1.bindToModel(input, ListBean.PROPERTY_VALUES, String.class, null, selection1, "text"); //$NON-NLS-1$
		combo1.addPropertyChangeListener(IComboRidget.PROPERTY_TEXT, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				text1.setText((String) evt.getNewValue());
			}
		});
		combo1.updateFromModel();
		combo1.setMarkSelectionMismatch(true);
		selection1.setOutputOnly(true);
		text1.setOutputOnly(true);

		combo2.bindToModel(input, ListBean.PROPERTY_VALUES, String.class, null, selection2, "text"); //$NON-NLS-1$
		combo2.addPropertyChangeListener(IComboRidget.PROPERTY_TEXT, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				text2.setText((String) evt.getNewValue());
			}
		});
		combo2.updateFromModel();
		selection2.setOutputOnly(true);
		text2.setOutputOnly(true);
	}
}
