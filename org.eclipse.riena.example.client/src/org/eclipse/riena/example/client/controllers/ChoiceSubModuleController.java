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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.example.client.views.ChoiceSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IChoiceRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

/**
 * Controller for the {@link ChoiceSubModuleView} example.
 */
public class ChoiceSubModuleController extends SubModuleController {

	public ChoiceSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Binds and updates the ridgets.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {

		final ISingleChoiceRidget compositeCarModel = (ISingleChoiceRidget) getRidget("compositeCarModel"); //$NON-NLS-1$
		compositeCarModel.bindToModel(new WritableList(Arrays.asList(CarModel.values()), CarModel.class), null);

		ITextFieldRidget txtPrice = (ITextFieldRidget) getRidget("txtPrice"); //$NON-NLS-1$
		txtPrice.setOutputOnly(true);
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(BeansObservables.observeValue(txtPrice, ITextFieldRidget.PROPERTY_TEXT), BeansObservables
				.observeValue(compositeCarModel, IChoiceRidget.PROPERTY_SELECTION), null, null);

		IActionRidget buttonReset = (IActionRidget) getRidget("buttonReset"); //$NON-NLS-1$
		buttonReset.setText("&Reset"); //$NON-NLS-1$
		buttonReset.addListener(new IActionListener() {
			public void callback() {
				System.out.println("reset.callback()"); //$NON-NLS-1$
				compositeCarModel.setSelection(null);
			}
		});

		IActionRidget buttonBMW = (IActionRidget) getRidget("buttonBMW"); //$NON-NLS-1$
		buttonBMW.setText("&Gimme a Beamer"); //$NON-NLS-1$
		buttonBMW.addListener(new IActionListener() {
			public void callback() {
				System.out.println("bmw.callback()"); //$NON-NLS-1$
				compositeCarModel.setSelection(CarModel.BMW);
			}
		});
	}

	// helping classes
	// ////////////////

	private enum CarModel {
		ASTON_MARTIN("Aston Martin V-12 Vanquish"), LOTUS("Lotus Esprit Turbo"), BMW("BMW Z8"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		private String label;

		private CarModel(String label) {
			Assert.isNotNull(label);
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}

	}
}
