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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.ridgets.ISelectableIndexedRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget.SelectionType;
import org.eclipse.riena.ui.ridgets.util.beans.Person;
import org.eclipse.riena.ui.ridgets.util.beans.PersonManager;
import org.eclipse.riena.ui.tests.base.TestMultiSelectionBean;
import org.eclipse.riena.ui.tests.base.TestSingleSelectionBean;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

/**
 * Tests of the class {@link AbstractSelectableRidget}.
 */
public abstract class AbstractSelectableIndexedRidgetTest extends AbstractSWTRidgetTest {

	protected PersonManager manager;
	protected Person person1;
	protected Person person2;
	protected Person person3;

	private TestSingleSelectionBean singleSelectionBean;
	private TestMultiSelectionBean multiSelectionBean;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		manager = new PersonManager(createPersonList());
		Iterator<Person> it = manager.getPersons().iterator();
		person1 = it.next();
		person2 = it.next();
		person3 = it.next();
		bindRidgetToModel();
		singleSelectionBean = new TestSingleSelectionBean();
		getRidget().bindSingleSelectionToModel(singleSelectionBean, "selection");
		multiSelectionBean = new TestMultiSelectionBean();
		getRidget().bindMultiSelectionToModel(multiSelectionBean, "selectionList");
		getRidget().updateFromModel();
		UITestHelper.readAndDispatch(getUIControl());
	}

	protected abstract void bindRidgetToModel();

	@Override
	protected ISelectableIndexedRidget getRidget() {
		return (ISelectableIndexedRidget) super.getRidget();
	}

	// test methods
	// /////////////

	public void testClearSelection() {
		ISelectableIndexedRidget ridget = getRidget();

		ridget.setSelectionType(SelectionType.SINGLE);
		ridget.setSelection(person1);

		assertTrue(ridget.getSelection().size() > 0);
		assertTrue(getUIControlSelectedRowCount() > 0);

		ridget.clearSelection();

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, getUIControlSelectedRowCount());

		ridget.setSelectionType(SelectionType.MULTI);
		ridget.setSelection(Arrays.asList(new Person[] { person1, person2 }));

		assertTrue(ridget.getSelection().size() > 0);
		assertTrue(getUIControlSelectedRowCount() > 0);

		ridget.clearSelection();

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, getUIControlSelectedRowCount());
	}

	public void testGetSelection() {
		ISelectableIndexedRidget ridget = getRidget();

		assertNotNull(ridget.getSelection());
		assertTrue(ridget.getSelection().isEmpty());

		ridget.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		ridget.setSelection(new int[] { 1, 2 });

		assertEquals(2, ridget.getSelection().size());
		assertEquals(getRowValue(1), ridget.getSelection().get(0));
		assertEquals(getRowValue(2), ridget.getSelection().get(1));
	}

	public void testGetSelectionIndex() {
		ISelectableIndexedRidget ridget = getRidget();

		assertEquals(-1, ridget.getSelectionIndex());

		ridget.setSelection(1);

		assertEquals(1, ridget.getSelectionIndex());

		ridget.setSelection(new int[] { 2, 0 });

		// TODO [ev] is first the "smallest" ?
		assertEquals(2, ridget.getSelectionIndex());

		ridget.clearSelection();

		assertEquals(-1, ridget.getSelectionIndex());
	}

	public void testGetSelectionIndices() {
		ISelectableIndexedRidget ridget = getRidget();

		assertEquals(0, ridget.getSelectionIndices().length);

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);
		java.util.List<Object> selBeans = new ArrayList<Object>(2);
		selBeans.add(getRowValue(0));
		selBeans.add(getRowValue(1));
		ridget.setSelection(selBeans);

		assertEquals(2, ridget.getSelectionIndices().length);
		assertEquals(0, ridget.getSelectionIndices()[0]);
		assertEquals(1, ridget.getSelectionIndices()[1]);

		ridget.setSelection(2);

		assertEquals(1, ridget.getSelectionIndices().length);
		assertEquals(2, ridget.getSelectionIndices()[0]);
	}

	public void testSetSelectionInt() {
		ISelectableIndexedRidget ridget = getRidget();

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());
		assertEquals(0, getUIControlSelectedRowCount());

		ridget.setSelection(0);

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRow());
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));

		ridget.setSelection(1);

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRow());
		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));

		try {
			ridget.setSelection(99);
			fail();
		} catch (RuntimeException e) {
			// expected
		}

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRow());
		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));

		try {
			ridget.setSelection(-1);
			fail();
		} catch (RuntimeException e) {
			// expected
		}

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRow());
		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
	}

	public void testSetSelectionIntArray() {
		ISelectableIndexedRidget ridget = getRidget();

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());
		assertEquals(0, getUIControlSelectedRowCount());

		ridget.setSelectionType(SelectionType.SINGLE);
		ridget.setSelection(new int[] { 1, 2 });

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRow());
		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));

		ridget.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		ridget.setSelection(new int[] { 1, 2 });

		assertEquals(2, getUIControlSelectedRowCount());
		assertEquals(1, getSelectedRows()[0]);
		assertEquals(2, getSelectedRows()[1]);
		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));

		ridget.setSelection(new int[] {});

		assertEquals(0, ridget.getSelectionIndices().length);

		try {
			ridget.setSelection(new int[] { 1, -1, 2 });
			fail();
		} catch (RuntimeException e) {
			// expected
		}
	}

	public void testSetSelectionList() {
		ISelectableIndexedRidget ridget = getRidget();

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());
		assertEquals(0, getUIControlSelectedRowCount());

		java.util.List<Object> selBeans1 = new ArrayList<Object>(2);
		selBeans1.add(getRowValue(0));
		selBeans1.add(getRowValue(1));
		ridget.setSelection(selBeans1);

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRow());
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);
		java.util.List<Object> selBeans2 = new ArrayList<Object>(2);
		selBeans2.add(getRowValue(0));
		selBeans2.add(getRowValue(1));
		ridget.setSelection(selBeans2);

		assertEquals(2, getUIControlSelectedRowCount());
		assertEquals(0, getSelectedRows()[0]);
		assertEquals(1, getSelectedRows()[1]);
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(1));

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);
		java.util.List<Object> selBeans3 = new ArrayList<Object>(1);
		selBeans3.add(new Object());
		ridget.setSelection(selBeans3);

		assertEquals(0, getUIControlSelectedRowCount());
		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());

		try {
			ridget.setSelection((List<?>) null);
			fail();
		} catch (RuntimeException e) {
			// expected
		}
	}

	public void testSetSelectionObject() {
		ISelectableIndexedRidget ridget = getRidget();

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());
		assertEquals(0, getUIControlSelectedRowCount());

		ridget.setSelection(getRowValue(0));

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRow());
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));

		ridget.setSelection(new Object());

		assertEquals(0, getUIControlSelectedRowCount());
		assertNull(singleSelectionBean.getSelection());
		assertEquals(0, multiSelectionBean.getSelectionList().size());

		ridget.setSelection((Object) null);

		assertEquals(0, getUIControlSelectedRowCount());
		assertNull(singleSelectionBean.getSelection());
		assertEquals(0, multiSelectionBean.getSelectionList().size());
	}

	public void testSetSelectionWithNoModel() {
		ISelectableIndexedRidget ridget = (ISelectableIndexedRidget) createRidget();

		assertEquals(0, ridget.getOptionCount());

		try {
			ridget.setSelection(0);
			fail();
		} catch (BindingException bex) {
			// expected
		}

		try {
			ridget.setSelection(new int[] { 0 });
			fail();
		} catch (BindingException bex) {
			// expected
		}

		try {
			ridget.setSelection((Object) null);
			fail();
		} catch (BindingException bex) {
			// expected
		}

		try {
			ridget.setSelection(Collections.EMPTY_LIST);
			fail();
		} catch (BindingException bex) {
			// expected
		}
	}

	public void testUpdateMultiSelectionCustomBinding() {
		ISelectableIndexedRidget ridget = getRidget();

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);
		WritableList customMultiSelectionObservable = new WritableList();
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindList(ridget.getMultiSelectionObservable(), customMultiSelectionObservable, new UpdateListStrategy(
				UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE));

		setUIControlRowSelectionInterval(0, 2);

		assertEquals(3, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRows()[0]);
		assertEquals(1, getUIControlSelectedRows()[1]);
		assertEquals(2, getUIControlSelectedRows()[2]);
		assertEquals(3, customMultiSelectionObservable.size());
		assertEquals(getRowValue(0), customMultiSelectionObservable.get(0));
		assertEquals(getRowValue(1), customMultiSelectionObservable.get(1));
		assertEquals(getRowValue(2), customMultiSelectionObservable.get(2));

		customMultiSelectionObservable.add(getRowValue(0));
		customMultiSelectionObservable.clear();
		customMultiSelectionObservable.add(getRowValue(0));
		customMultiSelectionObservable.add(getRowValue(2));

		assertEquals(2, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRows()[0]);
		assertEquals(2, getUIControlSelectedRows()[1]);
		assertEquals(2, customMultiSelectionObservable.size());
		assertEquals(getRowValue(0), customMultiSelectionObservable.get(0));
		assertEquals(getRowValue(2), customMultiSelectionObservable.get(1));
	}

	public void testUpdateMultiSelectionFromControl() {
		ISelectableIndexedRidget ridget = getRidget();

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());

		setUIControlRowSelectionInterval(1, 2);

		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));

		setUIControlRowSelection(new int[] { 0, 2 });

		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));
	}

	public void testUpdateMultiSelectionFromModel() {
		ISelectableIndexedRidget ridget = getRidget();

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);
		setUIControlRowSelectionInterval(0, 2);

		multiSelectionBean.getSelectionList().clear();
		multiSelectionBean.getSelectionList().add(getRowValue(1));
		multiSelectionBean.getSelectionList().add(getRowValue(2));

		assertEquals(3, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRows()[0]);
		assertEquals(1, getUIControlSelectedRows()[1]);
		assertEquals(2, getUIControlSelectedRows()[2]);
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));

		ridget.updateMultiSelectionFromModel();

		assertEquals(2, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRows()[0]);
		assertEquals(2, getUIControlSelectedRows()[1]);
		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));
	}

	public void testUpdateMultiSelectionFromModelWhenUnbound() {
		ISelectableIndexedRidget ridget = getRidget();

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);
		setUIControlRowSelectionInterval(0, 2);

		multiSelectionBean.getSelectionList().clear();
		multiSelectionBean.getSelectionList().add(getRowValue(1));
		multiSelectionBean.getSelectionList().add(getRowValue(2));

		assertEquals(3, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRows()[0]);
		assertEquals(1, getUIControlSelectedRows()[1]);
		assertEquals(2, getUIControlSelectedRows()[2]);
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));

		ridget.setUIControl(null);
		ridget.updateMultiSelectionFromModel();
		ridget.setUIControl(getUIControl());

		assertEquals(2, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRows()[0]);
		assertEquals(2, getUIControlSelectedRows()[1]);
		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));
	}

	public void testUpdateMultiSelectionFromRidgetOnRebind() {
		ISelectableIndexedRidget ridget = getRidget();

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);
		setUIControlRowSelectionInterval(1, 2);
		ridget.setUIControl(null); // unbind
		clearUIControlRowSelection();

		assertEquals(0, getUIControlSelectedRowCount());
		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));

		ridget.setUIControl(getUIControl()); // rebind

		assertEquals(2, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRows()[0]);
		assertEquals(2, getUIControlSelectedRows()[1]);
		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));
	}

	public void testUpdateSingleSelectionCustomBinding() {
		ISelectableIndexedRidget ridget = getRidget();

		WritableValue customSingleSelectionObservable = new WritableValue();
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(ridget.getSingleSelectionObservable(), customSingleSelectionObservable, new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_NEVER), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));

		setUIControlRowSelectionInterval(1, 1);

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRow());
		assertNull(customSingleSelectionObservable.getValue());

		customSingleSelectionObservable.setValue(getRowValue(0));

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRow());
		assertEquals(getRowValue(0), customSingleSelectionObservable.getValue());
	}

	public void testUpdateSingleSelectionFromControl() {
		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());

		setUIControlRowSelectionInterval(1, 1);

		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
	}

	public void testUpdateSingleSelectionFromModel() {
		ISelectableIndexedRidget ridget = getRidget();

		setUIControlRowSelectionInterval(1, 1);
		singleSelectionBean.setSelection(getRowValue(0));

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRow());
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));

		ridget.updateSingleSelectionFromModel();

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRow());
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));
	}

	public void testUpdateSingleSelectionFromModelWhenUnbound() {
		ISelectableIndexedRidget ridget = getRidget();

		setUIControlRowSelectionInterval(1, 1);
		singleSelectionBean.setSelection(getRowValue(0));

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(1, getUIControlSelectedRow());
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));

		ridget.setUIControl(null); // unbind
		ridget.updateSingleSelectionFromModel();
		ridget.setUIControl(getUIControl()); // rebind

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(0, getUIControlSelectedRow());
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));
	}

	public void testUpdateSingleSelectionFromRidgetOnRebind() {
		ISelectableIndexedRidget ridget = getRidget();

		setUIControlRowSelectionInterval(2, 2);
		ridget.setUIControl(null); // unbind
		clearUIControlRowSelection();

		assertEquals(0, getUIControlSelectedRowCount());
		assertEquals(getRowValue(2), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(0));

		ridget.setUIControl(getUIControl()); // rebind

		assertEquals(1, getUIControlSelectedRowCount());
		assertEquals(2, getUIControlSelectedRow());
		assertEquals(getRowValue(2), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(0));
	}

	public void testSelectionEventsSelectionTypeSingle() {
		ISelectableIndexedRidget ridget = getRidget();
		ridget.setSelectionType(SelectionType.SINGLE);

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());
		assertEquals(0, getUIControlSelectedRowCount());

		// ridget.addPropertyChangeListener(new PropertyChangeListener() {
		// public void propertyChange(PropertyChangeEvent evt) {
		// String name = evt.getPropertyName();
		// Object old = evt.getOldValue();
		// Object newv = evt.getNewValue();
		// System.out.println(name + ", " + old + " -> " + newv);
		// }
		// });

		java.util.List<?> oldSelection = Collections.EMPTY_LIST;
		java.util.List<?> newSelection = Arrays.asList(new Object[] { person1 });
		PropertyChangeEvent multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION,
				oldSelection, newSelection);
		PropertyChangeEvent singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION,
				null, person1);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(0);

		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();

		ridget.setSelection(0);

		verifyPropertyChangeEvents();

		oldSelection = newSelection;
		newSelection = Collections.EMPTY_LIST;
		multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION, oldSelection,
				newSelection);
		singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION, person1, null);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(Collections.EMPTY_LIST);

		verifyPropertyChangeEvents();

		oldSelection = Collections.EMPTY_LIST;
		newSelection = Arrays.asList(new Object[] { person2 });
		multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION, oldSelection,
				newSelection);
		singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION, null, person2);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(new int[] { 1, 2 });

		verifyPropertyChangeEvents();
	}

	public void testSelectionEventsSelectionTypeMulti() {
		ISelectableIndexedRidget ridget = getRidget();
		ridget.setSelectionType(SelectionType.MULTI);

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());
		assertEquals(0, getUIControlSelectedRowCount());

		// ridget.addPropertyChangeListener(new PropertyChangeListener() {
		// public void propertyChange(PropertyChangeEvent evt) {
		// String name = evt.getPropertyName();
		// Object old = evt.getOldValue();
		// Object newv = evt.getNewValue();
		// System.out.println(name + ", " + old + " -> " + newv);
		// }
		// });

		java.util.List<?> oldSelection = Collections.EMPTY_LIST;
		java.util.List<?> newSelection = Arrays.asList(new Object[] { person1 });
		PropertyChangeEvent multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION,
				oldSelection, newSelection);
		PropertyChangeEvent singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION,
				null, person1);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(0);

		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();

		ridget.setSelection(0);

		verifyPropertyChangeEvents();

		oldSelection = newSelection;
		newSelection = Collections.EMPTY_LIST;
		multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION, oldSelection,
				newSelection);
		singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION, person1, null);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(Collections.EMPTY_LIST);

		verifyPropertyChangeEvents();

		oldSelection = Collections.EMPTY_LIST;
		newSelection = Arrays.asList(new Object[] { person2, person3 });
		multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION, oldSelection,
				newSelection);
		singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION, null, person2);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(new int[] { 1, 2 });

		verifyPropertyChangeEvents();
	}

	public void testIndexOfOption() {
		ISelectableIndexedRidget ridget = getRidget();

		assertEquals(-1, ridget.indexOfOption(null));
		assertEquals(-1, ridget.indexOfOption(new Object()));

		assertEquals(0, ridget.indexOfOption(person1));
		assertEquals(1, ridget.indexOfOption(person2));
		assertEquals(2, ridget.indexOfOption(person3));
	}

	public void testGetOption() {
		ISelectableIndexedRidget ridget = getRidget();

		try {
			ridget.getOption(-1);
			fail();
		} catch (RuntimeException e) {
			// expected
		}

		try {
			int tooBig = manager.getPersons().size() + 1;
			ridget.getOption(tooBig);
			fail();
		} catch (RuntimeException e) {
			// expected
		}

		assertSame(person1, ridget.getOption(0));
		assertSame(person2, ridget.getOption(1));
		assertSame(person3, ridget.getOption(2));
	}

	public void testGetOptionCount() {
		ISelectableIndexedRidget ridget = getRidget();

		int oldCount = manager.getPersons().size();
		assertEquals(oldCount, ridget.getOptionCount());

		manager.getPersons().remove(person1);
		int newCount = oldCount - 1;

		assertEquals(newCount, manager.getPersons().size());
		assertEquals(oldCount, ridget.getOptionCount());

		ridget.updateFromModel();

		assertEquals(newCount, ridget.getOptionCount());
	}

	public void testSetSelectionTypeNull() {
		ISelectableIndexedRidget ridget = getRidget();

		try {
			ridget.setSelectionType(null);
			fail();
		} catch (RuntimeException npe) {
			// expected
		}
	}

	public void testSetSelectionTypeNONE() {
		ISelectableIndexedRidget ridget = getRidget();

		try {
			ridget.setSelectionType(ISelectableRidget.SelectionType.NONE);
			fail();
		} catch (RuntimeException iae) {
			// expected
		}
	}

	// helping methods
	// ////////////////

	abstract protected void clearUIControlRowSelection();

	abstract protected int getUIControlSelectedRowCount();

	abstract protected int getUIControlSelectedRow();

	abstract protected Object getRowValue(int i);

	abstract protected int[] getSelectedRows();

	abstract protected int[] getUIControlSelectedRows();

	abstract protected void setUIControlRowSelection(int[] indices);

	abstract protected void setUIControlRowSelectionInterval(int start, int end);

	protected final void fireSelectionEvent() {
		// fire selection event manually - control.setXXX does not fire events
		Event event = new Event();
		event.widget = getUIControl();
		event.type = SWT.Selection;
		getUIControl().notifyListeners(SWT.Selection, event);
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

}
