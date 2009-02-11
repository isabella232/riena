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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.example.client.views.TableXSubModuleView;
import org.eclipse.riena.internal.ui.ridgets.swt.CompositeTableRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link TableXSubModuleView} example.
 */
public class TableXSubModuleController extends SubModuleController {

	public static class RowRidget extends AbstractCompositeRidget {
		private Person rowBean;

		private static String[] GENDER = { Person.FEMALE, Person.MALE };

		public void setBean(Person rowBean) {
			this.rowBean = rowBean;
		}

		@Override
		public void configureRidgets() {
			ITextRidget txtFirst = (ITextRidget) getRidget("first"); //$NON-NLS-1$
			txtFirst.bindToModel(rowBean, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();

			ITextRidget txtLast = (ITextRidget) getRidget("last"); //$NON-NLS-1$
			txtLast.bindToModel(rowBean, Person.PROPERTY_LASTNAME);
			txtLast.updateFromModel();

			ISingleChoiceRidget gender = (ISingleChoiceRidget) getRidget("gender"); //$NON-NLS-1$
			gender.bindToModel(Arrays.asList(GENDER), (List<String>) null, rowBean, Person.PROPERTY_GENDER);
			gender.updateFromModel();

			IMultipleChoiceRidget pets = (IMultipleChoiceRidget) getRidget("pets"); //$NON-NLS-1$
			pets.bindToModel(Arrays.asList(Person.Pets.values()), (List<String>) null, rowBean, Person.PROPERTY_PETS);
			pets.updateFromModel();
		}
	}

	private List<Person> input = PersonFactory.createPersonList();

	public TableXSubModuleController() {
		this(null);
	}

	public TableXSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public void configureRidgets() {
		final CompositeTableRidget table = (CompositeTableRidget) getRidget("table"); //$NON-NLS-1$
		final IActionRidget buttonAdd = (IActionRidget) getRidget("buttonAdd"); //$NON-NLS-1$
		final IActionRidget buttonDelete = (IActionRidget) getRidget("buttonDelete"); //$NON-NLS-1$

		table.bindToModel(new WritableList(input, Person.class), Person.class, RowRidget.class);

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
