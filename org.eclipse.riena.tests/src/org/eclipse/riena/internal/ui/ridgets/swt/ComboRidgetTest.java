/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.IComboBoxRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.util.beans.Person;
import org.eclipse.riena.ui.ridgets.util.beans.PersonManager;
import org.eclipse.riena.ui.ridgets.util.beans.StringManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Tests of the class {@link ComboRidget}.
 */
public class ComboRidgetTest extends AbstractSWTRidgetTest {

	private PersonManager manager;
	private Person selection1;
	private Person selection2;
	private Person selection3;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		manager = new PersonManager(createPersonList());
		Iterator<Person> it = manager.getPersons().iterator();
		selection1 = it.next();
		selection2 = it.next();
		selection3 = it.next();
	}

	@Override
	protected Control createUIControl(Composite parent) {
		return new Combo(parent, SWT.READ_ONLY);
	}

	@Override
	protected IRidget createRidget() {
		return new ComboRidget();
	}

	@Override
	protected Combo getUIControl() {
		return (Combo) super.getUIControl();
	}

	@Override
	protected ComboRidget getRidget() {
		return (ComboRidget) super.getRidget();
	}

	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(ComboRidget.class, mapper.getRidgetClass(getUIControl()));
	}

	public void testSetUIControl() {
		ComboRidget ridget = getRidget();

		ridget.setUIControl(null);
		assertNull(ridget.getUIControl());

		ridget.setUIControl(getUIControl());
		assertSame(getUIControl(), ridget.getUIControl());
	}

	public void testSetUIControlInvalid() {
		ComboRidget ridget = getRidget();

		try {
			ridget.setUIControl(getShell());
			fail();
		} catch (BindingException bex) {
			// expected
		}

		try {
			ridget.setUIControl(new Combo(getShell(), SWT.NONE));
			fail();
		} catch (BindingException bex) {
			// expected
		}
	}

	public void testGetEmptySelectionItem() {
		Combo control = getUIControl();
		ComboRidget ridget = getRidget();
		StringManager manager = new StringManager("A", "B", "C", "D", "E");
		ridget.bindToModel(manager, "items", String.class, null, manager, "selectedItem");
		ridget.updateFromModel();

		assertNull(ridget.getEmptySelectionItem());

		Object emptySelectionItem = "A";
		ridget.setEmptySelectionItem(emptySelectionItem);

		assertSame(emptySelectionItem, ridget.getEmptySelectionItem());

		ridget.setSelection("A");

		int controlSelectedItem = control.getSelectionIndex();
		assertEquals(-1, ridget.getSelectionIndex());
		assertEquals(0, controlSelectedItem);

		assertEquals(null, ridget.getSelection());
		assertEquals("A", control.getItem(controlSelectedItem));

	}

	public void testBindToModelWithDomainObjects() {
		manager.setSelectedPerson(selection1);
		Runnable runnable = new Runnable() {
			public void run() {
				Combo control = getUIControl();
				ComboRidget ridget = getRidget();

				assertEquals(null, getSelectedString(control));
				assertEquals(0, control.getItemCount());
				ridget.bindToModel(manager, "persons", String.class, null, manager, "selectedPerson");
				assertEquals(null, getSelectedString(control));
				assertEquals(0, control.getItemCount());

				ridget.updateFromModel();

				assertEquals(selection1.toString(), getSelectedString(control));
				assertEquals(manager.getPersons().size(), control.getItemCount());

				for (int i = 0; i < control.getItemCount(); i++) {
					String item = control.getItem(i);
					if (!find(manager, item)) {
						fail();
					}
				}

				manager.setSelectedPerson(selection2);

				assertEquals(selection1.toString(), getSelectedString(control));

				ridget.updateFromModel();

				assertEquals(selection2.toString(), getSelectedString(control));

				control.select(2);

				assertEquals(selection3.toString(), getSelectedString(control));
			}
		};
		getUIControl().getDisplay().syncExec(runnable);
	}

	public void testBindToModelWithDomainObjectsUsingColumnPropertyName() {
		manager.setSelectedPerson(selection1);
		Runnable runnable = new Runnable() {
			public void run() {
				Combo control = getUIControl();
				ComboRidget ridget = getRidget();

				assertEquals(null, getSelectedString(control));
				assertEquals(0, control.getItemCount());
				ridget.bindToModel(manager, "persons", String.class, "getListEntry", manager, "selectedPerson");
				assertEquals(null, getSelectedString(control));
				assertEquals(0, control.getItemCount());

				ridget.updateFromModel();

				assertEquals(selection1.getListEntry(), getSelectedString(control));
				assertEquals(selection1.getListEntry(), control.getText());
				checkPersonList(manager);
				assertEquals(selection1, manager.getSelectedPerson());

				manager.setSelectedPerson(selection2);

				assertEquals(selection1.getListEntry(), getSelectedString(control));
				assertEquals(selection1.getListEntry(), control.getText());
				assertEquals(selection2, manager.getSelectedPerson());

				ridget.updateFromModel();

				assertEquals(selection2, manager.getSelectedPerson());
				assertEquals(selection2.getListEntry(), getSelectedString(control));
				assertEquals(selection2.getListEntry(), control.getText());

				control.select(2);

				assertEquals(selection3.getListEntry(), getSelectedString(control));
				assertEquals(selection3.getListEntry(), control.getText());
				assertEquals(selection3, manager.getSelectedPerson());
			}
		};
		getUIControl().getDisplay().syncExec(runnable);
	}

	public void testBindToModelWithStrings() {
		final StringManager manager = new StringManager("A", "B", "C", "D", "E");
		Iterator<String> it = manager.getItems().iterator();
		final String selection1 = it.next();
		final String selection2 = it.next();
		final String selection3 = it.next();
		manager.setSelectedItem(selection1);

		Runnable runnable = new Runnable() {
			public void run() {
				Combo control = getUIControl();
				ComboRidget ridget = getRidget();

				assertEquals(-1, control.getSelectionIndex());
				assertEquals(0, control.getItemCount());
				ridget.bindToModel(manager, "items", String.class, null, manager, "selectedItem");
				assertEquals(-1, control.getSelectionIndex());
				assertEquals(0, control.getItemCount());

				ridget.updateFromModel();

				assertEquals(manager.getItems().size(), control.getItemCount());
				assertEquals(selection1, getSelectedString(control));

				for (int i = 0; i < control.getItemCount(); i++) {
					if (!manager.getItems().contains(control.getItem(i))) {
						fail();
					}
				}

				manager.setSelectedItem(selection2);

				assertEquals(selection1, getSelectedString(control));

				ridget.updateFromModel();

				assertEquals(selection2, getSelectedString(control));

				control.select(2);

				assertEquals(selection3, getSelectedString(control));
				assertEquals(selection3, control.getText());
				assertEquals(selection3, manager.getSelectedItem());
			}
		};
		getUIControl().getDisplay().syncExec(runnable);
	}

	public void testFirePropertyChangeSelection() {
		manager.setSelectedPerson(selection1);
		Runnable runnable = new Runnable() {
			public void run() {
				Combo control = getUIControl();
				ComboRidget ridget = getRidget();

				assertEquals(null, getSelectedString(control));
				assertEquals(0, control.getItemCount());
				ridget.bindToModel(manager, "persons", String.class, "getListEntry", manager, "selectedPerson");
				assertEquals(null, getSelectedString(control));
				assertEquals(0, control.getItemCount());

				ridget.updateFromModel();

				expectPropertyChangeEvents(new SelectionPropertyChangeEvent(selection1, selection2));
				control.select(1);
				verifyPropertyChangeEvents();

				expectNoPropertyChangeEvent();
				control.select(1);
				verifyPropertyChangeEvents();

				expectPropertyChangeEvents(new SelectionPropertyChangeEvent(selection2, selection3),
						new SelectionPropertyChangeEvent(selection3, selection1));
				control.select(2);
				control.select(0);
				verifyPropertyChangeEvents();
			}
		};
		getUIControl().getDisplay().syncExec(runnable);
	}

	public void testUpdateSelection() {
		manager.setSelectedPerson(selection1);
		Runnable runnable = new Runnable() {
			public void run() {
				Combo control = getUIControl();
				ComboRidget ridget = getRidget();

				assertEquals(null, getSelectedString(control));
				assertEquals(0, control.getItemCount());
				ridget.bindToModel(manager, "persons", String.class, null, manager, "selectedPerson");
				assertEquals(null, getSelectedString(control));
				assertEquals(0, control.getItemCount());

				ridget.updateFromModel();

				assertEquals(selection1.toString(), getSelectedString(control));

				// ComboBoxSwingRidget.RidgetBasedComboBoxModel.temporaryItem
				// will be set
				control.select(2);
				manager.setSelectedPerson(selection2);

				assertEquals(selection3.toString(), getSelectedString(control));
				assertEquals(selection2, manager.getSelectedPerson());

				ridget.updateFromModel();

				assertEquals(selection2.toString(), getSelectedString(control));
				assertEquals(selection2, manager.getSelectedPerson());
			}
		};
		getUIControl().getDisplay().syncExec(runnable);
	}

	public void testGetObservableListWithStrings() {
		Runnable runnable = new Runnable() {
			public void run() {
				StringManager manager = new StringManager("A", "B", "C", "D", "E");

				ComboRidget ridget = getRidget();
				ridget.bindToModel(manager, "items", String.class, null, manager, "selectedItem");

				assertEquals(0, ridget.getObservableList().size());

				ridget.updateFromModel();

				assertEquals(manager.getItems().size(), ridget.getObservableList().size());
				for (String item : new String[] { "A", "B", "C", "D", "E" }) {
					assertTrue(ridget.getObservableList().contains(item));
				}
			}
		};
		getUIControl().getDisplay().syncExec(runnable);
	}

	public void testGetSelectionIndex() {
		Runnable runnable = new Runnable() {
			public void run() {
				StringManager manager = new StringManager("A", "B", "C", "D", "E");

				ComboRidget ridget = getRidget();
				ridget.bindToModel(manager, "items", String.class, null, manager, "selectedItem");
				ridget.updateFromModel();

				assertEquals(-1, getRidget().getSelectionIndex());

				getUIControl().select(1);
				assertEquals(1, getRidget().getSelectionIndex());

				ridget.setUIControl(null);
				assertEquals(1, getRidget().getSelectionIndex());
			}
		};
		getUIControl().getDisplay().syncExec(runnable);
	}

	public void testGetSelection() {
		Runnable runnable = new Runnable() {
			public void run() {
				ComboRidget ridget = getRidget();
				StringManager manager = new StringManager("A", "B", "C", "D", "E");
				ridget.bindToModel(manager, "items", String.class, null, manager, "selectedItem");
				ridget.updateFromModel();

				assertEquals(null, getRidget().getSelection());

				getUIControl().select(1);

				assertEquals("B", getRidget().getSelection());

				ridget.setUIControl(null);

				assertEquals("B", getRidget().getSelection());
			}
		};
		getUIControl().getDisplay().syncExec(runnable);
	}

	public void testGetSelectionObservable() {
		Runnable runnable = new Runnable() {
			public void run() {
				ComboRidget ridget = getRidget();
				StringManager manager = new StringManager("A", "B", "C", "D", "E");
				ridget.bindToModel(manager, "items", String.class, null, manager, "selectedItem");
				ridget.updateFromModel();

				IObservableValue selectionObservable = getRidget().getSelectionObservable();

				assertEquals(null, selectionObservable.getValue());

				getUIControl().select(1);

				assertEquals("B", selectionObservable.getValue());

				ridget.setUIControl(null);

				assertEquals("B", selectionObservable.getValue());
			}
		};
		getUIControl().getDisplay().syncExec(runnable);
	}

	public void testSetSelectionInt() {
		ComboRidget ridget = getRidget();
		Combo control = ridget.getUIControl();
		StringManager manager = new StringManager("A", "B", "C", "D", "E");
		ridget.bindToModel(manager, "items", String.class, null, manager, "selectedItem");
		ridget.updateFromModel();

		assertEquals(null, ridget.getSelection());

		ridget.setSelection(0);

		assertEquals("A", ridget.getSelection());
		assertEquals("A", control.getItem(control.getSelectionIndex()));

		ridget.setSelection(1);

		assertEquals("B", ridget.getSelection());
		assertEquals("B", control.getItem(control.getSelectionIndex()));

		ridget.setUIControl(null);
		ridget.setSelection(2);

		assertEquals("C", ridget.getSelection());
		assertEquals("B", control.getItem(control.getSelectionIndex()));

		ridget.setUIControl(control);

		assertEquals("C", ridget.getSelection());
		assertEquals("C", control.getItem(control.getSelectionIndex()));

		ridget.setSelection(-1);

		assertEquals(null, ridget.getSelection());
		assertEquals(-1, control.getSelectionIndex());

		try {
			ridget.setSelection(999);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	public void testSetSelectionString() {
		ComboRidget ridget = getRidget();
		Combo control = ridget.getUIControl();
		StringManager manager = new StringManager("A", "B", "C", "D", "E");
		ridget.bindToModel(manager, "items", String.class, null, manager, "selectedItem");
		ridget.updateFromModel();

		assertEquals(null, ridget.getSelection());

		ridget.setSelection("A");

		assertEquals("A", ridget.getSelection());
		assertEquals("A", control.getItem(control.getSelectionIndex()));

		ridget.setSelection("B");

		assertEquals("B", ridget.getSelection());
		assertEquals("B", control.getItem(control.getSelectionIndex()));

		ridget.setUIControl(null);
		ridget.setSelection("C");

		assertEquals("C", ridget.getSelection());
		assertEquals("B", control.getItem(control.getSelectionIndex()));

		ridget.setUIControl(control);

		assertEquals("C", ridget.getSelection());
		assertEquals("C", control.getItem(control.getSelectionIndex()));

		ridget.setSelection("X");

		assertEquals(null, ridget.getSelection());
		assertEquals(-1, control.getSelectionIndex());

		ridget.setSelection("A");
		ridget.setSelection(null);

		assertEquals(null, ridget.getSelection());
		assertEquals(-1, control.getSelectionIndex());
	}

	// helping methods
	// ////////////////

	private String getSelectedString(Combo combo) {
		int index = combo.getSelectionIndex();
		return index == -1 ? null : combo.getItem(index);
	}

	private boolean find(PersonManager manager, String item) {
		boolean result = false;
		Iterator<Person> iter = manager.getPersons().iterator();
		while (!result && iter.hasNext()) {
			result = iter.next().toString().equals(item);
		}
		return result;
	}

	private void checkPersonList(PersonManager manager) {
		Combo control = getUIControl();

		assertEquals(manager.getPersons().size(), control.getItemCount());

		Collection<String> listEntries = new ArrayList<String>();
		for (Person person : manager.getPersons()) {
			listEntries.add(person.getListEntry());
		}
		for (int i = 0; i < control.getItemCount(); i++) {
			String item = control.getItem(i);
			if (!listEntries.contains(item)) {
				fail();
			}
		}
	}

	private Collection<Person> createPersonList() {
		Collection<Person> newList = new ArrayList<Person>();

		Person person = new Person("Doe", "John");
		person.setEyeColor(1);
		newList.add(person);

		person = new Person("Jackson", "Janet");
		person.setEyeColor(1);
		newList.add(person);

		person = new Person("Jackson", "Jermaine");
		person.setEyeColor(1);
		newList.add(person);

		person = new Person("Jackson", "John");
		person.setEyeColor(3);
		newList.add(person);

		person = new Person("JJ Jr. Shabadoo", "Joey");
		person.setEyeColor(3);
		newList.add(person);

		person = new Person("Johnson", "Jack");
		person.setEyeColor(2);
		newList.add(person);

		person = new Person("Johnson", "Jane");
		person.setEyeColor(3);
		newList.add(person);

		person = new Person("Zappa", "Frank");
		person.setEyeColor(2);
		newList.add(person);

		return newList;
	}

	private class SelectionPropertyChangeEvent extends PropertyChangeEvent {

		private static final long serialVersionUID = 4711L;

		public SelectionPropertyChangeEvent(Object oldValue, Object newValue) {
			super(getRidget(), IComboBoxRidget.PROPERTY_SELECTION, oldValue, newValue);
		}
	}
}
