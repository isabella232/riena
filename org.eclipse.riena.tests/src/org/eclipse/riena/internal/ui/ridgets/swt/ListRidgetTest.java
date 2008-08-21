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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.tests.FTActionListener;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISortableByColumn;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget.SelectionType;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.util.beans.Person;
import org.eclipse.riena.ui.ridgets.util.beans.PersonManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;

/**
 * Tests of the class {@link ListRidget}.
 */
public class ListRidgetTest extends AbstractTableRidgetTest {

	@Override
	protected Control createUIControl(Composite parent) {
		return new List(parent, SWT.MULTI);
	}

	@Override
	protected IRidget createRidget() {
		return new ListRidget();
	}

	@Override
	protected List getUIControl() {
		return (List) super.getUIControl();
	}

	@Override
	protected ListRidget getRidget() {
		return (ListRidget) super.getRidget();
	}

	@Override
	protected void bindRidgetToModel() {
		getRidget().bindToModel(manager, "persons", Person.class, new String[] { "firstname" }, new String[] { "" });
	}

	// test methods
	// /////////////

	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(ListRidget.class, mapper.getRidgetClass(getUIControl()));
	}

	public void testUpdateFromModel() {
		List control = getUIControl();
		ITableRidget ridget = getRidget();

		int oldCount = manager.getPersons().size();

		assertEquals(oldCount, ridget.getObservableList().size());
		assertEquals(oldCount, control.getItemCount());

		manager.getPersons().remove(person1);

		int newCount = oldCount - 1;

		assertEquals(newCount, manager.getPersons().size());
		assertEquals(oldCount, ridget.getObservableList().size());
		assertEquals(oldCount, control.getItemCount());

		ridget.updateFromModel();

		assertEquals(newCount, manager.getPersons().size());
		assertEquals(newCount, ridget.getObservableList().size());
		assertEquals(newCount, control.getItemCount());
	}

	public void testUpdateFromModelPreservesSelection() {
		ITableRidget ridget = getRidget();

		ridget.setSelection(person2);

		assertSame(person2, ridget.getSelection().get(0));

		manager.getPersons().remove(person1);
		ridget.updateFromModel();

		assertSame(person2, ridget.getSelection().get(0));
	}

	public void testContainsOption() {
		ITableRidget ridget = getRidget();

		assertTrue(ridget.containsOption(person1));
		assertTrue(ridget.containsOption(person2));
		assertTrue(ridget.containsOption(person3));

		assertFalse(ridget.containsOption(null));
		assertFalse(ridget.containsOption(new Person("", "")));

		java.util.List<Person> persons = Arrays.asList(new Person[] { person3 });
		PersonManager manager = new PersonManager(persons);
		getRidget().bindToModel(manager, "persons", Person.class, new String[] { "firstname" }, new String[] { "" });

		assertFalse(ridget.containsOption(person1));
		assertTrue(ridget.containsOption(person3));
	}

	public void testSetSelectionType() {
		ITableRidget ridget = getRidget();
		List control = getUIControl();

		assertEquals(SelectionType.SINGLE, ridget.getSelectionType());
		assertTrue((control.getStyle() & SWT.MULTI) != 0);

		ridget.setSelection(new int[] { 0, 1 });

		// single selection is enforced
		assertEquals(1, ridget.getSelectionIndices().length);
		assertEquals(1, control.getSelectionCount());

		// multiple selection is now allowed
		ridget.setSelectionType(SelectionType.MULTI);
		ridget.setSelection(new int[] { 0, 1 });

		assertEquals(2, ridget.getSelectionIndices().length);
		assertEquals(2, control.getSelectionCount());
	}

	public void testAddDoubleClickListener() {
		ListRidget ridget = getRidget();
		List control = getUIControl();

		try {
			ridget.addDoubleClickListener(null);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		FTActionListener listener1 = new FTActionListener();
		ridget.addDoubleClickListener(listener1);

		FTActionListener listener2 = new FTActionListener();
		ridget.addDoubleClickListener(listener2);
		ridget.addDoubleClickListener(listener2);

		Event doubleClick = new Event();
		doubleClick.widget = control;
		doubleClick.type = SWT.MouseDoubleClick;
		control.notifyListeners(SWT.MouseDoubleClick, doubleClick);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeDoubleClickListener(listener1);

		control.notifyListeners(SWT.MouseDoubleClick, doubleClick);

		assertEquals(1, listener1.getCount());
	}

	public void testSetComparator() {
		ListRidget ridget = getRidget();
		List control = getUIControl();

		// sorts from a to z
		Comparator<Object> comparator = new StringComparator();

		try {
			ridget.setComparator(-1, comparator);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		try {
			ridget.setComparator(1, comparator);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		int lastItemIndex = control.getItemCount() - 1;

		assertEquals("John", control.getItem(0));
		assertEquals("Frank", control.getItem(lastItemIndex));

		ridget.setComparator(0, comparator);
		ridget.setSortedColumn(0);

		assertEquals("Frank", control.getItem(0));
		assertEquals("John", control.getItem(lastItemIndex));

		ridget.setComparator(0, null);

		assertEquals("John", control.getItem(0));
		assertEquals("Frank", control.getItem(lastItemIndex));
	}

	public void testGetSortedColumn() {
		ListRidget ridget = getRidget();

		try {
			ridget.setSortedColumn(1);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		try {
			ridget.setSortedColumn(-2);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		assertEquals(-1, ridget.getSortedColumn());

		ridget.setComparator(0, new StringComparator());

		assertEquals(-1, ridget.getSortedColumn());

		ridget.setSortedColumn(0);

		assertEquals(0, ridget.getSortedColumn());

		ridget.setComparator(0, null);

		assertEquals(-1, ridget.getSortedColumn());

		ridget.setSortedColumn(-1);

		assertEquals(-1, ridget.getSortedColumn());

		// no comparator in column 0
		ridget.setSortedColumn(0);

		assertEquals(-1, ridget.getSortedColumn());
	}

	public void testIsColumnSortable() {
		ListRidget ridget = getRidget();

		try {
			assertFalse(ridget.isColumnSortable(-1));
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		try {
			assertFalse(ridget.isColumnSortable(1));
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		assertFalse(ridget.isColumnSortable(0));

		ridget.setComparator(0, new StringComparator());

		assertTrue(ridget.isColumnSortable(0));

		ridget.setComparator(0, null);

		assertFalse(ridget.isColumnSortable(0));
	}

	public void testSetColumnSortable() {
		ListRidget ridget = getRidget();

		try {
			ridget.setColumnSortable(0, true);
			fail();
		} catch (UnsupportedOperationException uoe) {
			// expected
		}
	}

	public void testSetSortedAscending() {
		List control = getUIControl();
		ListRidget ridget = getRidget();

		ridget.bindToModel(manager, "persons", Person.class, new String[] { "lastname" }, new String[] { "" });
		int lastItemIndex = control.getItemCount() - 1;

		assertEquals(-1, ridget.getSortedColumn());
		assertTrue(ridget.isSortedAscending());

		ridget.setSortedAscending(false);

		assertFalse(ridget.isSortedAscending());

		ridget.setComparator(0, new StringComparator());
		ridget.setSortedColumn(0);

		assertFalse(ridget.isSortedAscending());

		assertEquals("Zappa", control.getItem(0));
		assertEquals("Doe", control.getItem(lastItemIndex));

		ridget.setSortedAscending(true);

		assertTrue(ridget.isSortedAscending());
		assertEquals("Doe", control.getItem(0));
		assertEquals("Zappa", control.getItem(lastItemIndex));

		ridget.setComparator(0, null);

		assertEquals(-1, ridget.getSortedColumn());
		assertTrue(ridget.isSortedAscending());
	}

	public void testSetSortedAscendingFiresEvents() {
		ListRidget ridget = getRidget();

		assertTrue(ridget.isSortedAscending());

		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORT_ASCENDING,
				Boolean.TRUE, Boolean.FALSE));

		ridget.setSortedAscending(false);

		verifyPropertyChangeEvents();
		expectNoPropertyChangeEvent();

		ridget.setSortedAscending(false);

		verifyPropertyChangeEvents();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORT_ASCENDING,
				Boolean.FALSE, Boolean.TRUE));

		ridget.setSortedAscending(true);

		verifyPropertyChangeEvents();
		expectNoPropertyChangeEvent();

		ridget.setSortedAscending(true);

		verifyPropertyChangeEvents();
	}

	public void testSetSortedColumnFiresEvents() {
		ListRidget ridget = getRidget();

		assertEquals(-1, ridget.getSortedColumn());

		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORTED_COLUMN, Integer
				.valueOf(-1), Integer.valueOf(0)));

		ridget.setSortedColumn(0);

		verifyPropertyChangeEvents();
		expectNoPropertyChangeEvent();

		ridget.setSortedColumn(0);

		verifyPropertyChangeEvents();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORTED_COLUMN, Integer
				.valueOf(0), Integer.valueOf(-1)));

		ridget.setSortedColumn(-1);

		verifyPropertyChangeEvents();
	}

	public void testHasMoveableColumns() {
		ListRidget ridget = getRidget();

		assertFalse(ridget.hasMoveableColumns());

		try {
			ridget.setMoveableColumns(true);
			fail();
		} catch (UnsupportedOperationException ex) {
			// expected
		}
	}

	// helping methods
	// ////////////////

	@Override
	protected void clearUIControlRowSelection() {
		getUIControl().deselectAll();
		fireSelectionEvent();
	}

	@Override
	protected int getUIControlSelectedRowCount() {
		return getUIControl().getSelectionCount();
	}

	@Override
	protected int getUIControlSelectedRow() {
		return getUIControl().getSelectionIndex();
	}

	@Override
	protected Object getRowValue(int i) {
		// return getRidget().getRowObservables().get(i);
		IObservableList rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables");
		return rowObservables.get(i);
	}

	@Override
	protected int[] getSelectedRows() {
		// IObservableList rowObservables = getRidget().getRowObservables();
		IObservableList rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables");
		Object[] elements = getRidget().getMultiSelectionObservable().toArray();
		int[] result = new int[elements.length];
		for (int i = 0; i < elements.length; i++) {
			Object element = elements[i];
			result[i] = rowObservables.indexOf(element);
		}
		return result;
	}

	@Override
	protected int[] getUIControlSelectedRows() {
		return getUIControl().getSelectionIndices();
	}

	@Override
	protected void setUIControlRowSelection(int[] indices) {
		getUIControl().setSelection(indices);
		fireSelectionEvent();
	}

	@Override
	protected void setUIControlRowSelectionInterval(int start, int end) {
		getUIControl().setSelection(start, end);
		fireSelectionEvent();
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
