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

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.riena.example.client.views.TextView;
import org.eclipse.riena.internal.example.client.beans.PersonFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.util.beans.Person;
import org.eclipse.riena.ui.ridgets.util.beans.PersonManager;

/**
 * Controller for the {@link TextView} example.
 */
public class MarkerViewController extends SubModuleNodeViewController {

	private IToggleButtonRidget checkMandatory;
	private IToggleButtonRidget checkError;
	private IToggleButtonRidget checkDisabled;
	private IToggleButtonRidget checkOutput;
	private IToggleButtonRidget checkHidden;

	private ITextFieldRidget textName;
	private ITextFieldRidget textPrice;
	private IComboBoxRidget comboAge;
	private IActionRidget radioRed;
	private IActionRidget radioWhite;
	private IActionRidget radioRose;
	private ITextFieldRidget textDescr;
	private IToggleButtonRidget checkDry;
	private IToggleButtonRidget checkSweet;
	private IToggleButtonRidget checkSour;
	private IToggleButtonRidget checkSpicy;
	private ITableRidget listPersons;
	private IToggleButtonRidget buttonToggle;
	private IActionRidget buttonPush;

	private IMarkableRidget markables[];

	/** Manages a collection of persons. */
	private final PersonManager manager;

	public MarkerViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
		manager = new PersonManager(PersonFactory.createPersonList());
		manager.setSelectedPerson(manager.getPersons().iterator().next());
	}

	@Override
	public void afterBind() {
		super.afterBind();
		markables = new IMarkableRidget[] { textName, textPrice, comboAge, radioRed, radioWhite, radioRose, textDescr,
				checkDry, checkSweet, checkSour, checkSpicy, listPersons, buttonToggle, buttonPush };
		initRidgets();
	}

	public IActionRidget getButtonPush() {
		return buttonPush;
	}

	public IToggleButtonRidget getButtonToggle() {
		return buttonToggle;
	}

	public IToggleButtonRidget getCheckDisabled() {
		return checkDisabled;
	}

	public IToggleButtonRidget getCheckDry() {
		return checkDry;
	}

	public IToggleButtonRidget getCheckError() {
		return checkError;
	}

	public IToggleButtonRidget getCheckHidden() {
		return checkHidden;
	}

	public IToggleButtonRidget getCheckMandatory() {
		return checkMandatory;
	}

	public IToggleButtonRidget getCheckOutput() {
		return checkOutput;
	}

	public IToggleButtonRidget getCheckSour() {
		return checkSour;
	}

	public IToggleButtonRidget getCheckSpicy() {
		return checkSpicy;
	}

	public IToggleButtonRidget getCheckSweet() {
		return checkSweet;
	}

	public IComboBoxRidget getComboAge() {
		return comboAge;
	}

	public ITableRidget getListPersons() {
		return listPersons;
	}

	public IActionRidget getRadioRed() {
		return radioRed;
	}

	public IActionRidget getRadioRose() {
		return radioRose;
	}

	public IActionRidget getRadioWhite() {
		return radioWhite;
	}

	public ITextFieldRidget getTextDescr() {
		return textDescr;
	}

	public ITextFieldRidget getTextName() {
		return textName;
	}

	public ITextFieldRidget getTextPrice() {
		return textPrice;
	}

	public void setButtonPush(IActionRidget buttonPush) {
		this.buttonPush = buttonPush;
	}

	public void setButtonToggle(IToggleButtonRidget buttonToggle) {
		this.buttonToggle = buttonToggle;
	}

	public void setCheckDisabled(IToggleButtonRidget checkDisabled) {
		this.checkDisabled = checkDisabled;
	}

	public void setCheckDry(IToggleButtonRidget checkDry) {
		this.checkDry = checkDry;
	}

	public void setCheckError(IToggleButtonRidget checkError) {
		this.checkError = checkError;
	}

	public void setCheckHidden(IToggleButtonRidget checkHidden) {
		this.checkHidden = checkHidden;
	}

	public void setCheckMandatory(IToggleButtonRidget checkMandatory) {
		this.checkMandatory = checkMandatory;
	}

	public void setCheckOutput(IToggleButtonRidget checkOutput) {
		this.checkOutput = checkOutput;
	}

	public void setCheckSour(IToggleButtonRidget checkSour) {
		this.checkSour = checkSour;
	}

	public void setCheckSpicy(IToggleButtonRidget checkSpicy) {
		this.checkSpicy = checkSpicy;
	}

	public void setCheckSweet(IToggleButtonRidget checkSweet) {
		this.checkSweet = checkSweet;
	}

	public void setComboAge(IComboBoxRidget comboAge) {
		this.comboAge = comboAge;
	}

	public void setListPersons(ITableRidget listPersons) {
		this.listPersons = listPersons;
	}

	public void setRadioRed(IActionRidget radioRed) {
		this.radioRed = radioRed;
	}

	public void setRadioRose(IActionRidget radioRose) {
		this.radioRose = radioRose;
	}

	public void setRadioWhite(IActionRidget radioWhite) {
		this.radioWhite = radioWhite;
	}

	public void setTextDescr(ITextFieldRidget textDescr) {
		this.textDescr = textDescr;
	}

	public void setTextName(ITextFieldRidget textName) {
		this.textName = textName;
	}

	public void setTextPrice(ITextFieldRidget textPrice) {
		this.textPrice = textPrice;
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		textName.setText("Chateau Schaedelbrummer");

		textPrice.setText("-29,99");
		// TODO [ev] could use a validation rule here to add / remove the marker
		textPrice.addMarker(new NegativeMarker());

		List<String> ages = Arrays.asList(new String[] { "young", "moderate", "aged", "old" });
		comboAge.bindToModel(new WritableList(ages, String.class), String.class, null, new WritableValue());
		comboAge.updateFromModel();
		comboAge.setSelection(0);

		radioRed.setText("red");
		radioWhite.setText("white");
		radioRose.setText("rose");

		textDescr.setText("Body:\n\tcomplex, spicy, berries\nAftertaste:\n\tchocolatey");

		checkDry.setText("dry");
		checkDry.setSelected(true);
		checkSweet.setText("sweet");
		checkSour.setText("sour");
		checkSpicy.setText("spicy");

		listPersons.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		listPersons.bindToModel(manager, "persons", Person.class, new String[] { "listEntry" }, new String[] { "" }); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		listPersons.updateFromModel();

		buttonToggle.setText("Toggle Me");
		buttonToggle.setSelected(true);
		buttonPush.setText("Push Me");

		checkMandatory.setText("&mandatory");
		checkMandatory.addListener(new IActionListener() {
			public void callback() {
				boolean isMandatory = checkMandatory.isSelected();
				for (IMarkableRidget ridget : markables) {
					ridget.setMandatory(isMandatory);
				}
			}
		});

		checkError.setText("&error");
		checkError.addListener(new IActionListener() {
			public void callback() {
				boolean isError = checkError.isSelected();
				for (IMarkableRidget ridget : markables) {
					ridget.setErrorMarked(isError);
				}
			}
		});

		checkDisabled.setText("&disabled");
		checkDisabled.addListener(new IActionListener() {
			public void callback() {
				boolean isEnabled = !checkDisabled.isSelected();
				for (IMarkableRidget ridget : markables) {
					ridget.setEnabled(isEnabled);
				}
			}
		});

		checkOutput.setText("&output");
		checkOutput.addListener(new IActionListener() {
			public void callback() {
				boolean isOutput = checkOutput.isSelected();
				for (IMarkableRidget ridget : markables) {
					ridget.setOutputOnly(isOutput);
				}
			}
		});

		checkHidden.setText("&hidden");
		checkHidden.addListener(new IActionListener() {
			public void callback() {
				boolean isVisible = !checkHidden.isSelected();
				for (IMarkableRidget ridget : markables) {
					ridget.setVisible(isVisible);
				}
			}
		});
	}
}
