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

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.example.client.views.TableXSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link TableXSubModuleView} example.
 */
public class TableXSubModuleController extends SubModuleController {

	public static class RowRidget extends AbstractCompositeRidget {
		private Person bean;
		private ITextRidget first;
		private ITextRidget last;

		public void setBean(Person bean) {
			this.bean = bean;
		}

		public ITextRidget getFirst() {
			return first;
		}

		public void setFirst(ITextRidget first) {
			this.first = first;
			first.bindToModel(bean, "firstname");
			first.updateFromModel();
		}

		public ITextRidget getLast() {
			return last;
		}

		public void setLast(ITextRidget last) {
			this.last = last;
			last.bindToModel(bean, "lastname");
			last.updateFromModel();
		}
	}

	private ITableRidget table;
	private List<Person> input;

	public TableXSubModuleController() {
		this(null);
	}

	public TableXSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public void configureRidgets() {
		// table = (ITableRidget) getRidget("table"); //$NON-NLS-1$
		final IActionRidget buttonAdd = (IActionRidget) getRidget("buttonAdd"); //$NON-NLS-1$
		final IActionRidget buttonDelete = (IActionRidget) getRidget("buttonDelete"); //$NON-NLS-1$

		buttonAdd.setText("&Add"); //$NON-NLS-1$
		buttonAdd.addListener(new IActionListener() {
			private int i = 0;

			public void callback() {
				//				i++;
				//				Person person = new Person("Doe #" + i, "John"); //$NON-NLS-1$ //$NON-NLS-2$
				//				input.add(person);
				//				table.updateFromModel();
				//				table.setSelection(person);
			}
		});

		buttonDelete.setText("&Delete"); //$NON-NLS-1$
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				//				Person person = (Person) table.getSingleSelectionObservable().getValue();
				//				input.remove(person);
				//				table.updateFromModel();
			}
		});

		//		final IObservableValue viewerSelection = table.getSingleSelectionObservable();
		//		IObservableValue hasSelection = new ComputedValue(Boolean.TYPE) {
		//			@Override
		//			protected Object calculate() {
		//				return Boolean.valueOf(viewerSelection.getValue() != null);
		//			}
		//		};
		//		DataBindingContext dbc = new DataBindingContext();
		//		bindEnablementToValue(dbc, buttonDelete, hasSelection);
		//
		//		String[] columnPropertyNames = { "firstname", "gender", "hasCat" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//		// Object[] columnPropertyNames = { new String[] { "firstname", "lastname" }, "gender", new String[] { "hasCat", "hasDog", "hasFish"}}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//		// Object[] columnPropertyNames = { new NameCompositeFactory(), "gender", "hasCat" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//		String[] columnHeaders = { "Name", "Gender", "Pets" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//		input = PersonFactory.createPersonList();
		//		table.bindToModel(new WritableList(input, Person.class), Person.class, columnPropertyNames, columnHeaders);
		//		table.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		//
		//		TableRidget tableRidget = (TableRidget) table;
	}

	// helping methods
	//////////////////

	private void bindEnablementToValue(DataBindingContext dbc, IMarkableRidget ridget, IObservableValue value) {
		dbc.bindValue(BeansObservables.observeValue(ridget, IMarkableRidget.PROPERTY_ENABLED), value, null, null);
	}

}
