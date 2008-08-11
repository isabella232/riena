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
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
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
		compositeCarModel.bindToModel(toList(CarModels.values()), null);
		compositeCarModel.addMarker(new MandatoryMarker());

		final ISingleChoiceRidget compositeCarExtras = (ISingleChoiceRidget) getRidget("compositeCarExtras"); //$NON-NLS-1$
		String[] labels = { "Front Machine Guns", "Self Destruct Button", "Underwater Package",
				"Park Distance Control System", };
		compositeCarExtras.bindToModel(toList(CarOptions.values()), Arrays.asList(labels), null, null);

		final ISingleChoiceRidget compositeCarWarranty = (ISingleChoiceRidget) getRidget("compositeCarWarranty"); //$NON-NLS-1$
		compositeCarWarranty.bindToModel(toList(CarWarranties.values()), null);

		final ISingleChoiceRidget compositeCarPlates = (ISingleChoiceRidget) getRidget("compositeCarPlates"); //$NON-NLS-1$
		compositeCarPlates.bindToModel(toList(carPlates), null);
		compositeCarPlates.addMarker(new MandatoryMarker());

		ITextFieldRidget txtPrice = (ITextFieldRidget) getRidget("txtPrice"); //$NON-NLS-1$
		txtPrice.setOutputOnly(true);
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(BeansObservables.observeValue(txtPrice, ITextFieldRidget.PROPERTY_TEXT), BeansObservables
				.observeValue(compositeCarModel, IChoiceRidget.PROPERTY_SELECTION), null, null);

		IActionRidget buttonPreset = (IActionRidget) getRidget("buttonPreset"); //$NON-NLS-1$
		buttonPreset.setText("&Quick Config");
		buttonPreset.addListener(new IActionListener() {
			public void callback() {
				System.out.println("preset.callback()");
				compositeCarModel.setSelection(CarModels.BMW);
				compositeCarExtras.setSelection(CarOptions.PDCS);
				compositeCarWarranty.setSelection(CarWarranties.EXTENDED);
				compositeCarPlates.setSelection(carPlates[0]);
			}
		});
		IActionRidget buttonReset = (IActionRidget) getRidget("buttonReset"); //$NON-NLS-1$

		buttonReset.setText("&Reset");
		buttonReset.addListener(new IActionListener() {
			public void callback() {
				System.out.println("reset.callback()");
				compositeCarModel.setSelection(null);
				compositeCarExtras.setSelection(null);
				compositeCarWarranty.setSelection(null);
				compositeCarPlates.setSelection(null);
			}
		});
	}

	// helping methods
	// ////////////////

	private WritableList toList(Object[] values) {
		return new WritableList(Arrays.asList(values), Object.class);
	}

	// helping classes
	// ////////////////

	private enum CarModels {
		ASTON_MARTIN("Aston Martin V-12 Vanquish"), LOTUS("Lotus Esprit Turbo"), BMW("BMW Z8");

		private String label;

		private CarModels(String label) {
			Assert.isNotNull(label);
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	private enum CarOptions {
		FRONT_GUNS, SELF_DESTRUCT, UNDERWATER, PDCS
	}

	private enum CarWarranties {
		STANDARD, EXTENDED;

		public String toString() {
			char[] result = super.toString().toLowerCase().toCharArray();
			result[0] = Character.toUpperCase(result[0]);
			return String.valueOf(result);
		}
	}

	private String[] carPlates = { "JM5B0ND", "1 SPY", "MNY PNY", "BN D07", "Q RULE2", "MI64EVR" };
}
