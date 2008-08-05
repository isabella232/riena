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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.riena.example.client.views.ComboSubModuleView;
import org.eclipse.riena.internal.example.client.beans.PersonFactory;
import org.eclipse.riena.internal.example.client.beans.PersonModificationBean;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.util.beans.PersonManager;

/**
 * Controller for the {@link ComboSubModuleView} example.
 */
public class ComboSubModuleController extends SubModuleController {

	private IComboBoxRidget comboOne;
	private ITextFieldRidget textFirst;
	private ITextFieldRidget textLast;
	private IActionRidget buttonSave;

	/** Manages a collection of persons. */
	private final PersonManager manager;
	/** Holds editable data for a person. */
	private final PersonModificationBean value;

	public ComboSubModuleController() {
		this(null);
	}

	public ComboSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		manager = new PersonManager(PersonFactory.createPersonList());
		manager.setSelectedPerson(manager.getPersons().iterator().next());
		value = new PersonModificationBean();
	}

	public IComboBoxRidget getComboOne() {
		return comboOne;
	}

	public void setComboOne(IComboBoxRidget comboOne) {
		this.comboOne = comboOne;
	}

	public ITextFieldRidget getTextFirst() {
		return textFirst;
	}

	public void setTextFirst(ITextFieldRidget textFirst) {
		this.textFirst = textFirst;
	}

	public ITextFieldRidget getTextLast() {
		return textLast;
	}

	public void setTextLast(ITextFieldRidget textLast) {
		this.textLast = textLast;
	}

	public IActionRidget getButtonSave() {
		return buttonSave;
	}

	public void setButtonSave(IActionRidget buttonSave) {
		this.buttonSave = buttonSave;
	}

	@Override
	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		comboOne.bindToModel(manager, "persons", String.class, null, manager, "selectedPerson"); //$NON-NLS-1$ //$NON-NLS-2$
		comboOne.updateFromModel();

		value.setPerson(manager.getSelectedPerson());

		textFirst.bindToModel(value, "firstName"); //$NON-NLS-1$
		textFirst.updateFromModel();
		textLast.bindToModel(value, "lastName"); //$NON-NLS-1$
		textLast.updateFromModel();

		comboOne.addPropertyChangeListener(IComboBoxRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				value.setPerson(manager.getSelectedPerson());
				textFirst.updateFromModel();
				textLast.updateFromModel();
			}
		});

		buttonSave.setText("&Save");
		buttonSave.addListener(new IActionListener() {
			public void callback() {
				value.update();
				comboOne.updateFromModel();
			}
		});
	}
}
