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
import java.util.Comparator;

import org.eclipse.riena.example.client.views.TextSubModuleView;
import org.eclipse.riena.internal.example.client.beans.PersonFactory;
import org.eclipse.riena.internal.example.client.beans.PersonModificationBean;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.util.beans.Person;
import org.eclipse.riena.ui.ridgets.util.beans.PersonManager;

/**
 * Controller for the {@link TextSubModuleView} example.
 */
public class ListSubModuleController extends SubModuleController {

	/** Manages a collection of persons. */
	private final PersonManager manager;
	/** Holds editable data for a person. */
	private final PersonModificationBean value;

	public ListSubModuleController() {
		this(null);
	}

	public ListSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		manager = new PersonManager(PersonFactory.createPersonList());
		manager.setSelectedPerson(manager.getPersons().iterator().next());
		value = new PersonModificationBean();
	}

	/**
	 * Binds and updates the ridgets.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		final ITableRidget listPersons = (ITableRidget) getRidget("listPersons"); //$NON-NLS-1$
		listPersons.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		listPersons.setComparator(0, new StringComparator());
		listPersons.setSortedColumn(0);
		listPersons.bindToModel(manager, "persons", Person.class, new String[] { "listEntry" }, new String[] { "" }); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		listPersons.updateFromModel();

		listPersons.bindSingleSelectionToModel(manager, PersonManager.PROPERTY_SELECTED_PERSON);

		final ITextFieldRidget textFirst = (ITextFieldRidget) getRidget("textFirst"); //$NON-NLS-1$
		textFirst.bindToModel(value, "firstName"); //$NON-NLS-1$
		textFirst.updateFromModel();
		final ITextFieldRidget textLast = (ITextFieldRidget) getRidget("textLast"); //$NON-NLS-1$
		textLast.bindToModel(value, "lastName"); //$NON-NLS-1$
		textLast.updateFromModel();

		listPersons.addPropertyChangeListener(ITableRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				value.setPerson(manager.getSelectedPerson());
				textFirst.updateFromModel();
				textLast.updateFromModel();
			}
		});

		final IToggleButtonRidget buttonSort = (IToggleButtonRidget) getRidget("buttonSort"); //$NON-NLS-1$
		buttonSort.setText("Sort ascending"); //$NON-NLS-1$
		buttonSort.setSelected(true);
		listPersons.setSortedAscending(buttonSort.isSelected());
		buttonSort.addListener(new IActionListener() {
			public void callback() {
				boolean ascending = buttonSort.isSelected();
				listPersons.setSortedAscending(ascending);
			}
		});

		final IActionRidget buttonAdd = (IActionRidget) getRidget("buttonAdd"); //$NON-NLS-1$
		buttonAdd.setText("&Add"); //$NON-NLS-1$
		buttonAdd.addListener(new IActionListener() {
			private int count = 0;

			public void callback() {
				Person newPerson = new Person("Average", "Joe #" + ++count); //$NON-NLS-1$ //$NON-NLS-2$
				manager.getPersons().add(newPerson);
				listPersons.updateFromModel();
				manager.setSelectedPerson(newPerson);
				listPersons.updateSingleSelectionFromModel();
			}
		});

		final IActionRidget buttonRemove = (IActionRidget) getRidget("buttonRemove"); //$NON-NLS-1$
		buttonRemove.setText("&Remove"); //$NON-NLS-1$
		buttonRemove.addListener(new IActionListener() {
			public void callback() {
				Person selPerson = manager.getSelectedPerson();
				if (selPerson != null) {
					manager.getPersons().remove(selPerson);
					listPersons.updateFromModel();
					manager.setSelectedPerson(null);
				}
			}
		});

		final IActionRidget buttonSave = (IActionRidget) getRidget("buttonSave"); //$NON-NLS-1$
		buttonSave.setText("&Save"); //$NON-NLS-1$
		buttonSave.addListener(new IActionListener() {
			public void callback() {
				value.update();
				listPersons.updateFromModel();
			}
		});
	}

	// helping classes
	// ////////////////

	/**
	 * Compares two strings.
	 */
	private static final class StringComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			return s1.compareTo(s2);
		}
	}
}
