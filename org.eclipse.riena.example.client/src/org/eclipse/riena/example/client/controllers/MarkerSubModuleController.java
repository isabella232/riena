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
import org.eclipse.riena.example.client.views.TextSubModuleView;
import org.eclipse.riena.internal.example.client.beans.PersonFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.util.beans.Person;
import org.eclipse.riena.ui.ridgets.util.beans.PersonManager;
import org.eclipse.riena.ui.ridgets.util.beans.TestBean;

/**
 * Controller for the {@link TextSubModuleView} example.
 */
public class MarkerSubModuleController extends SubModuleController {

	/** Manages a collection of persons. */
	private final PersonManager manager;
	private IComboBoxRidget comboAge;
	private ITableRidget listPersons;

	public MarkerSubModuleController() {
		this(null);
	}

	public MarkerSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		manager = new PersonManager(PersonFactory.createPersonList());
		manager.setSelectedPerson(manager.getPersons().iterator().next());
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

		List<String> ages = Arrays.asList(new String[] { "young", "moderate", "aged", "old" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		comboAge.bindToModel(new WritableList(ages, String.class), String.class, null, new WritableValue());
		comboAge.updateFromModel();
		comboAge.setSelection(0);

		listPersons.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		listPersons.bindToModel(manager, "persons", Person.class, new String[] { "listEntry" }, new String[] { "" }); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		listPersons.updateFromModel();

	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		final ITextFieldRidget textName = (ITextFieldRidget) getRidget("textName"); //$NON-NLS-1$
		textName.setText("Chateau Schaedelbrummer"); //$NON-NLS-1$

		final ITextFieldRidget textPrice = (ITextFieldRidget) getRidget("textPrice"); //$NON-NLS-1$
		textPrice.setText("-29,99"); //$NON-NLS-1$
		// TODO [ev] could use a validation rule here to add / remove the marker
		textPrice.addMarker(new NegativeMarker());

		comboAge = (IComboBoxRidget) getRidget("comboAge"); //$NON-NLS-1$

		final IToggleButtonRidget radioRed = (IToggleButtonRidget) getRidget("radioRed"); //$NON-NLS-1$
		radioRed.setText("red"); //$NON-NLS-1$
		final IToggleButtonRidget radioWhite = (IToggleButtonRidget) getRidget("radioWhite"); //$NON-NLS-1$
		radioWhite.setText("white"); //$NON-NLS-1$
		final IToggleButtonRidget radioRose = (IToggleButtonRidget) getRidget("radioRose"); //$NON-NLS-1$
		radioRose.setText("rose"); //$NON-NLS-1$

		final ISingleChoiceRidget choiceType = (ISingleChoiceRidget) getRidget("choiceType"); //$NON-NLS-1$
		choiceType.bindToModel(Arrays.asList("red", "white", "rose"), (List<String>) null, new TestBean(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				TestBean.PROPERTY);
		choiceType.updateFromModel();
		choiceType.setSelection("red"); //$NON-NLS-1$

		final IToggleButtonRidget checkDry = (IToggleButtonRidget) getRidget("checkDry"); //$NON-NLS-1$
		checkDry.setText("dry"); //$NON-NLS-1$
		checkDry.setSelected(true);
		final IToggleButtonRidget checkSweet = (IToggleButtonRidget) getRidget("checkSweet"); //$NON-NLS-1$
		checkSweet.setText("sweet"); //$NON-NLS-1$
		final IToggleButtonRidget checkSour = (IToggleButtonRidget) getRidget("checkSour"); //$NON-NLS-1$
		checkSour.setText("sour"); //$NON-NLS-1$
		final IToggleButtonRidget checkSpicy = (IToggleButtonRidget) getRidget("checkSpicy"); //$NON-NLS-1$
		checkSpicy.setText("spicy"); //$NON-NLS-1$

		final IMultipleChoiceRidget choiceFlavor = (IMultipleChoiceRidget) getRidget("choiceFlavor"); //$NON-NLS-1$
		choiceFlavor.bindToModel(Arrays.asList("dry", "sweet", "sour", "spicy"), (List<String>) null, new TestBean(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				TestBean.PROPERTY);
		choiceFlavor.updateFromModel();
		choiceFlavor.setSelection(Arrays.asList("dry")); //$NON-NLS-1$

		listPersons = (ITableRidget) getRidget("listPersons"); //$NON-NLS-1$

		final IToggleButtonRidget buttonToggle = (IToggleButtonRidget) getRidget("buttonToggle"); //$NON-NLS-1$
		buttonToggle.setText("Toggle Me"); //$NON-NLS-1$
		buttonToggle.setSelected(true);
		final IActionRidget buttonPush = (IActionRidget) getRidget("buttonPush"); //$NON-NLS-1$
		buttonPush.setText("Push Me"); //$NON-NLS-1$

		final IMarkableRidget markables[] = new IMarkableRidget[] { textName, textPrice, comboAge, radioRed,
				radioWhite, radioRose, choiceType, checkDry, checkSweet, checkSour, checkSpicy, choiceFlavor,
				listPersons, buttonToggle, buttonPush };

		final IToggleButtonRidget checkMandatory = (IToggleButtonRidget) getRidget("checkMandatory"); //$NON-NLS-1$
		final IToggleButtonRidget checkError = (IToggleButtonRidget) getRidget("checkError"); //$NON-NLS-1$
		final IToggleButtonRidget checkDisabled = (IToggleButtonRidget) getRidget("checkDisabled"); //$NON-NLS-1$
		final IToggleButtonRidget checkOutput = (IToggleButtonRidget) getRidget("checkOutput"); //$NON-NLS-1$
		final IToggleButtonRidget checkHidden = (IToggleButtonRidget) getRidget("checkHidden"); //$NON-NLS-1$

		checkMandatory.setText("&mandatory"); //$NON-NLS-1$
		checkMandatory.addListener(new IActionListener() {
			public void callback() {
				boolean isMandatory = checkMandatory.isSelected();
				for (IMarkableRidget ridget : markables) {
					ridget.setMandatory(isMandatory);
				}
				// TODO [ev] consider using a value object instead
				if (isMandatory) {
					textName.setText(""); //$NON-NLS-1$
					textPrice.setText(""); //$NON-NLS-1$
					comboAge.setSelection(-1);
					radioRed.setSelected(false);
					radioWhite.setSelected(false);
					radioRose.setSelected(false);
					choiceType.setSelection(null);
					checkDry.setSelected(false);
					checkSweet.setSelected(false);
					checkSour.setSelected(false);
					checkSpicy.setSelected(false);
					choiceFlavor.setSelection(null);
					listPersons.setSelection((Object) null);
					buttonToggle.setSelected(false);
				}
			}
		});

		checkError.setText("&error"); //$NON-NLS-1$
		checkError.addListener(new IActionListener() {
			public void callback() {
				boolean isError = checkError.isSelected();
				for (IMarkableRidget ridget : markables) {
					ridget.setErrorMarked(isError);
				}
			}
		});

		checkDisabled.setText("&disabled"); //$NON-NLS-1$
		checkDisabled.addListener(new IActionListener() {
			public void callback() {
				boolean isEnabled = !checkDisabled.isSelected();
				for (IMarkableRidget ridget : markables) {
					ridget.setEnabled(isEnabled);
				}
			}
		});

		checkOutput.setText("&output"); //$NON-NLS-1$
		checkOutput.addListener(new IActionListener() {
			public void callback() {
				boolean isOutput = checkOutput.isSelected();
				for (IMarkableRidget ridget : markables) {
					ridget.setOutputOnly(isOutput);
				}
			}
		});

		checkHidden.setText("&hidden"); //$NON-NLS-1$
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
