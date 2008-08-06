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

	private ISingleChoiceRidget compositeCarModel;
	private ITextFieldRidget txtPrice;
	private IActionRidget buttonReset;
	private IActionRidget buttonBMW;

	public ChoiceSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public ISingleChoiceRidget getCompositeCarModel() {
		return compositeCarModel;
	}

	public void setCompositeCarModel(ISingleChoiceRidget compositeCarModel) {
		this.compositeCarModel = compositeCarModel;
	}

	public ITextFieldRidget getTxtPrice() {
		return txtPrice;
	}

	public void setTxtPrice(ITextFieldRidget txtPrice) {
		this.txtPrice = txtPrice;
	}

	public IActionRidget getButtonReset() {
		return buttonReset;
	}

	public void setButtonReset(IActionRidget buttonReset) {
		this.buttonReset = buttonReset;
	}

	public IActionRidget getButtonBMW() {
		return buttonBMW;
	}

	public void setButtonBMW(IActionRidget buttonBMW) {
		this.buttonBMW = buttonBMW;
	}

	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		compositeCarModel.bindToModel(new WritableList(Arrays.asList(CarModel.values()), CarModel.class), null);

		txtPrice.setOutputOnly(true);
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(BeansObservables.observeValue(txtPrice, ITextFieldRidget.PROPERTY_TEXT), BeansObservables
				.observeValue(compositeCarModel, IChoiceRidget.PROPERTY_SELECTION), null, null);

		buttonReset.setText("&Reset");
		buttonReset.addListener(new IActionListener() {
			public void callback() {
				System.out.println("reset.callback()");
				compositeCarModel.setSelection(null);
			}
		});

		buttonBMW.setText("&Gimme a Beamer");
		buttonBMW.addListener(new IActionListener() {
			public void callback() {
				System.out.println("bmw.callback()");
				compositeCarModel.setSelection(CarModel.BMW);
			}
		});
	}

	// helping classes
	// ////////////////

	private enum CarModel {
		ASTON_MARTIN("Aston Martin V-12 Vanquish"), LOTUS("Lotus Esprit Turbo"), BMW("BMW Z8");

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
