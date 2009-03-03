/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
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
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.example.client.views.CompositeTableSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ICompositeTableRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link CompositeTableSubModuleView} example.
 */
public class CompositeTableSubModuleController extends SubModuleController {

	public static class RowRidget extends AbstractCompositeRidget implements IRowRidget {
		private Person rowData;

		private static String[] GENDER = { Person.FEMALE, Person.MALE };

		public void setData(Object rowData) {
			this.rowData = (Person) rowData;
		}

		@Override
		public void configureRidgets() {
			ITextRidget txtFirst = (ITextRidget) getRidget("first"); //$NON-NLS-1$
			txtFirst.bindToModel(rowData, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();

			ITextRidget txtLast = (ITextRidget) getRidget("last"); //$NON-NLS-1$
			txtLast.bindToModel(rowData, Person.PROPERTY_LASTNAME);
			txtLast.updateFromModel();

			ISingleChoiceRidget gender = (ISingleChoiceRidget) getRidget("gender"); //$NON-NLS-1$
			gender.bindToModel(Arrays.asList(GENDER), (List<String>) null, rowData, Person.PROPERTY_GENDER);
			gender.updateFromModel();

			IMultipleChoiceRidget pets = (IMultipleChoiceRidget) getRidget("pets"); //$NON-NLS-1$
			pets.bindToModel(Arrays.asList(Person.Pets.values()), (List<String>) null, rowData, Person.PROPERTY_PETS);
			pets.updateFromModel();
		}
	}

	private List<Person> input = PersonFactory.createPersonList();

	public CompositeTableSubModuleController() {
		this(null);
	}

	public CompositeTableSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public void configureRidgets() {
		final ICompositeTableRidget table = (ICompositeTableRidget) getRidget("table"); //$NON-NLS-1$
		final IActionRidget buttonAdd = (IActionRidget) getRidget("buttonAdd"); //$NON-NLS-1$
		final IActionRidget buttonDelete = (IActionRidget) getRidget("buttonDelete"); //$NON-NLS-1$
		final IActionRidget buttonDump = (IActionRidget) getRidget("buttonDump"); //$NON-NLS-1$

		table.bindToModel(new WritableList(input, Person.class), Person.class, RowRidget.class);
		table.setComparator(0, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				Person p1 = (Person) o1;
				Person p2 = (Person) o2;
				int result = p1.getLastname().compareTo(p2.getLastname());
				if (result == 0) {
					result = p1.getFirstname().compareTo(p2.getFirstname());
				}
				return result;
			}
		});
		table.setComparator(1, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				Person p1 = (Person) o1;
				Person p2 = (Person) o2;
				return p1.getGender().compareTo(p2.getGender());
			}
		});
		table.setSortedColumn(0);

		buttonAdd.setText("&Add"); //$NON-NLS-1$
		buttonAdd.addListener(new IActionListener() {
			private int i = 0;

			public void callback() {
				i++;
				Person person = new Person("Doe #" + i, "John"); //$NON-NLS-1$ //$NON-NLS-2$
				person.setHasCat(true);
				input.add(person);
				table.updateFromModel();
				table.setSelection(person);
			}
		});

		buttonDelete.setText("&Delete"); //$NON-NLS-1$
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				Person person = (Person) table.getSingleSelectionObservable().getValue();
				input.remove(person);
				table.updateFromModel();
			}
		});

		buttonDump.setText("&Console dump"); //$NON-NLS-1$
		buttonDump.addListener(new IActionListener() {
			public void callback() {
				System.out.println("\nPersons:"); //$NON-NLS-1$
				for (Person p : input) {
					System.out.println(p);
				}
			}
		});

		final IObservableValue viewerSelection = table.getSingleSelectionObservable();
		IObservableValue hasSelection = new ComputedValue(Boolean.TYPE) {
			@Override
			protected Object calculate() {
				return Boolean.valueOf(viewerSelection.getValue() != null);
			}
		};
		DataBindingContext dbc = new DataBindingContext();
		bindEnablementToValue(dbc, buttonDelete, hasSelection);
	}

	// helping methods
	//////////////////

	private void bindEnablementToValue(DataBindingContext dbc, IMarkableRidget ridget, IObservableValue value) {
		dbc.bindValue(BeansObservables.observeValue(ridget, IMarkableRidget.PROPERTY_ENABLED), value, null, null);
	}

}
