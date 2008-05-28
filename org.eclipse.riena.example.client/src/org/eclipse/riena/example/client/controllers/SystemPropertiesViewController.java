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

import java.util.Comparator;

import org.eclipse.riena.example.client.views.SystemPropertiesView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;

/**
 * Controller for the {@link SystemPropertiesView} example.
 */
public class SystemPropertiesViewController extends SubModuleNodeViewController {

	public SystemPropertiesViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
		// manager = new PersonManager(PersonFactory.createPersonList());
		// manager.setSelectedPerson(manager.getPersons().iterator().next());
		// value = new PersonModificationBean();
	}

	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		// listPersons.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		// listPersons.setComparator(0, new StringComparator());
		// listPersons.bindToModel(manager, "persons", Person.class, new
		// String[] {
		// "listEntry" }, new String[] { "" });
		// //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		// listPersons.updateFromModel();
		//
		// listPersons.bindSingleSelectionToModel(manager,
		// PersonManager.PROPERTY_SELECTED_PERSON);
		//
		// textFirst.bindToModel(value, "firstName"); //$NON-NLS-1$
		// textFirst.updateFromModel();
		// textLast.bindToModel(value, "lastName"); //$NON-NLS-1$
		// textLast.updateFromModel();
		//
		// listPersons.addPropertyChangeListener(ITableRidget.PROPERTY_SINGLE_SELECTION,
		// new PropertyChangeListener() {
		// public void propertyChange(PropertyChangeEvent evt) {
		// value.setPerson(manager.getSelectedPerson());
		// textFirst.updateFromModel();
		// textLast.updateFromModel();
		// }
		// });
		//
		// buttonSort.setText("Sort ascending");
		// buttonSort.setSelected(true);
		// listPersons.setSortedAscending(buttonSort.isSelected());
		// buttonSort.addListener(new IActionListener() {
		// public void callback() {
		// boolean ascending = buttonSort.isSelected();
		// listPersons.setSortedAscending(ascending);
		// }
		// });
		//
		// buttonAdd.setText("&Add");
		// buttonAdd.addListener(new IActionListener() {
		// private int count = 0;
		//
		// public void callback() {
		// Person newPerson = new Person("Average", "Joe #" + ++count);
		// manager.getPersons().add(newPerson);
		// listPersons.updateFromModel();
		// manager.setSelectedPerson(newPerson);
		// listPersons.updateSingleSelectionFromModel();
		// }
		// });
		//
		// buttonRemove.setText("&Remove");
		// buttonRemove.addListener(new IActionListener() {
		// public void callback() {
		// Person selPerson = manager.getSelectedPerson();
		// if (selPerson != null) {
		// manager.getPersons().remove(selPerson);
		// listPersons.updateFromModel();
		// manager.setSelectedPerson(null);
		// }
		// }
		// });
		//
		// buttonSave.setText("&Save");
		// buttonSave.addListener(new IActionListener() {
		// public void callback() {
		// value.update();
		// listPersons.updateFromModel();
		// }
		// });
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
