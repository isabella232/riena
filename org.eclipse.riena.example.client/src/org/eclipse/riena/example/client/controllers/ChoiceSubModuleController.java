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
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.util.beans.AbstractBean;

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
		final CarConfig carConfig = new CarConfig();

		final ISingleChoiceRidget compositeCarModel = (ISingleChoiceRidget) getRidget("compositeCarModel"); //$NON-NLS-1$
		compositeCarModel.bindToModel(toList(CarModels.values()), BeansObservables.observeValue(carConfig,
				CarConfig.PROP_MODEL));
		compositeCarModel.addMarker(new MandatoryMarker());

		final ISingleChoiceRidget compositeCarExtras = (ISingleChoiceRidget) getRidget("compositeCarExtras"); //$NON-NLS-1$
		String[] labels = { "Front Machine Guns", "Self Destruct Button", "Underwater Package",
				"Park Distance Control System", };
		compositeCarExtras.bindToModel(toList(CarOptions.values()), Arrays.asList(labels), carConfig,
				CarConfig.PROP_OPTION);
		compositeCarExtras.addMarker(new MandatoryMarker()); // TODO [ev] remove

		final ISingleChoiceRidget compositeCarWarranty = (ISingleChoiceRidget) getRidget("compositeCarWarranty"); //$NON-NLS-1$
		compositeCarWarranty.bindToModel(toList(CarWarranties.values()), BeansObservables.observeValue(carConfig,
				CarConfig.PROP_WARRANTY));
		compositeCarWarranty.addMarker(new MandatoryMarker());

		final ISingleChoiceRidget compositeCarPlates = (ISingleChoiceRidget) getRidget("compositeCarPlates"); //$NON-NLS-1$
		compositeCarPlates.bindToModel(toList(carPlates), BeansObservables
				.observeValue(carConfig, CarConfig.PROP_PLATE));
		compositeCarPlates.addMarker(new MandatoryMarker());

		ITextFieldRidget txtPrice = (ITextFieldRidget) getRidget("txtPrice"); //$NON-NLS-1$
		txtPrice.setOutputOnly(true);
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(BeansObservables.observeValue(txtPrice, ITextFieldRidget.PROPERTY_TEXT), BeansObservables
				.observeValue(carConfig, CarConfig.PROP_PRICE), null, null);

		IActionRidget buttonPreset = (IActionRidget) getRidget("buttonPreset"); //$NON-NLS-1$
		buttonPreset.setText("&Quick Config");
		buttonPreset.addListener(new IActionListener() {
			public void callback() {
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
				carConfig.reset();
				compositeCarModel.updateFromModel();
				compositeCarExtras.updateFromModel();
				compositeCarWarranty.updateFromModel();
				compositeCarPlates.updateFromModel();
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

	/**
	 * Bean that holds a single car configuration composed of: model, option(s),
	 * warranty, plate(s).
	 */
	private static final class CarConfig extends AbstractBean {
		public static final String PROP_MODEL = "model"; //$NON-NLS-1$
		public static final String PROP_OPTION = "option"; //$NON-NLS-1$
		public static final String PROP_WARRANTY = "warranty"; //$NON-NLS-1$
		public static final String PROP_PLATE = "plate"; //$NON-NLS-1$
		public static final String PROP_PRICE = "price"; //$NON-NLS-1$

		private CarModels model;
		private CarOptions option;
		private CarWarranties warranty;
		private String plate;

		public CarModels getModel() {
			return model;
		}

		public void setModel(CarModels model) {
			firePropertyChanged(PROP_MODEL, this.model, this.model = model);
			firePropertyChanged(PROP_PRICE, null, getPrice());
		}

		public CarOptions getOption() {
			return option;
		}

		public void setOption(CarOptions option) {
			firePropertyChanged(PROP_OPTION, this.option, this.option = option);
			firePropertyChanged(PROP_PRICE, null, getPrice());
		}

		public CarWarranties getWarranty() {
			return warranty;
		}

		public void setWarranty(CarWarranties warranty) {
			firePropertyChanged(PROP_WARRANTY, this.warranty, this.warranty = warranty);
			firePropertyChanged(PROP_PRICE, null, getPrice());
		}

		public String getPlate() {
			return plate;
		}

		public void setPlate(String plate) {
			firePropertyChanged(PROP_PLATE, this.plate, this.plate = plate);
			firePropertyChanged(PROP_PRICE, null, getPrice());
		}

		public void reset() {
			setModel(null);
			setOption(null);
			setWarranty(null);
			setPlate(null);
		}

		public long getPrice() {
			long price = 0;
			if (model != null) {
				price += 100000;
			}
			if (option != null) {
				price += 25000;
			}
			if (warranty == CarWarranties.EXTENDED) {
				price += 10000;
			}
			if (plate != null) {
				price += 200;
			}
			return price;
		}
	}

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
