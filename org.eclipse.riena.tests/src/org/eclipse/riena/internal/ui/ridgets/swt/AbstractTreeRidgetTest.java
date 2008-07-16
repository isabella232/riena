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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget.SelectionType;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.tests.base.TestMultiSelectionBean;
import org.eclipse.riena.ui.tests.base.TestSingleSelectionBean;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Tests for the class {@link AbstractSelectableRidget} for tree- based
 * implementations.
 */
public abstract class AbstractTreeRidgetTest extends AbstractSWTRidgetTest {

	protected ITreeNode node1;
	protected ITreeNode node2;
	protected ITreeNode node3;

	private TestSingleSelectionBean singleSelectionBean;
	private TestMultiSelectionBean multiSelectionBean;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		ITreeNode[] nodes = bindRidgetToModel();
		node1 = nodes[0];
		node2 = nodes[1];
		node3 = nodes[2];
		singleSelectionBean = new TestSingleSelectionBean();
		getRidget().bindSingleSelectionToModel(singleSelectionBean, TestSingleSelectionBean.PROPERTY_SELECTION);
		multiSelectionBean = new TestMultiSelectionBean();
		getRidget().bindMultiSelectionToModel(multiSelectionBean, TestMultiSelectionBean.PROPERTY_SELECTION);
		getRidget().updateFromModel();
		UITestHelper.readAndDispatch(getUIControl());
	}

	protected abstract ITreeNode[] bindRidgetToModel();

	@Override
	protected ITreeRidget getRidget() {
		return (ITreeRidget) super.getRidget();
	}

	protected final Tree getUIControl() {
		return (Tree) super.getUIControl();
	}

	// test methods
	// /////////////

	public void testClearSelection() {
		ISelectableRidget ridget = getRidget();

		ridget.setSelectionType(SelectionType.SINGLE);
		ridget.setSelection(node1);

		assertTrue(ridget.getSelection().size() > 0);
		assertTrue(getUIControlSelectedRowCount() > 0);

		ridget.clearSelection();

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, getUIControlSelectedRowCount());

		ridget.setSelectionType(SelectionType.MULTI);
		ridget.setSelection(Arrays.asList(new Object[] { node1, node2 }));

		assertTrue(ridget.getSelection().size() > 0);
		assertTrue(getUIControlSelectedRowCount() > 0);

		ridget.clearSelection();

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, getUIControlSelectedRowCount());
	}

	public void testGetSelection() {
		ISelectableRidget ridget = getRidget();

		assertNotNull(ridget.getSelection());
		assertTrue(ridget.getSelection().isEmpty());

		ridget.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		ridget.setSelection(Arrays.asList(new Object[] { node2, node3 }));

		assertEquals(2, ridget.getSelection().size());
		assertEquals(getRowValue(1), ridget.getSelection().get(0));
		assertEquals(getRowValue(2), ridget.getSelection().get(1));
	}

	public void testSetSelectionList() {
		ISelectableRidget ridget = getRidget();

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());
		assertEquals(0, getUIControlSelectedRowCount());

		java.util.List<Object> selBeans1 = new ArrayList<Object>(2);
		selBeans1.add(getRowValue(0));
		selBeans1.add(getRowValue(1));
		ridget.setSelection(selBeans1);

		assertEquals(1, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(0);
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);
		java.util.List<Object> selBeans2 = new ArrayList<Object>(2);
		selBeans2.add(getRowValue(0));
		selBeans2.add(getRowValue(1));
		ridget.setSelection(selBeans2);

		assertEquals(2, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(0);
		assertUIControlSelectionContains(1);
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
		ISelectableRidget ridget = getRidget();

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());
		assertEquals(0, getUIControlSelectedRowCount());

		ridget.setSelection(getRowValue(0));

		assertEquals(1, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(0);
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
		ISelectableRidget ridget = (ISelectableRidget) createRidget();

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
		ISelectableRidget ridget = getRidget();

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
		ISelectableRidget ridget = getRidget();

		ridget.setSelectionType(ITableRidget.SelectionType.MULTI);

		assertNull(singleSelectionBean.getSelection());
		assertTrue(multiSelectionBean.getSelectionList().isEmpty());

		setUIControlRowSelectionInterval(1, 2);

		assertEquals(getRowValue(1), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));

		TreeItem[] items = { getUIControlItem(0), getUIControlItem(2) };
		getUIControl().setSelection(items);
		fireSelectionEvent();

		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(2, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(1));
	}

	public void testUpdateMultiSelectionFromModel() {
		ISelectableRidget ridget = getRidget();

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
		ISelectableRidget ridget = getRidget();

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
		ISelectableRidget ridget = getRidget();

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
		ISelectableRidget ridget = getRidget();

		WritableValue customSingleSelectionObservable = new WritableValue();
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(ridget.getSingleSelectionObservable(), customSingleSelectionObservable, new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_NEVER), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));

		setUIControlRowSelectionInterval(1, 1);

		assertEquals(1, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(1);
		assertNull(customSingleSelectionObservable.getValue());

		customSingleSelectionObservable.setValue(getRowValue(0));

		assertEquals(1, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(0);
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
		ISelectableRidget ridget = getRidget();

		setUIControlRowSelectionInterval(1, 1);
		singleSelectionBean.setSelection(getRowValue(0));

		assertEquals(1, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(1);
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));

		ridget.updateSingleSelectionFromModel();

		assertEquals(1, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(0);
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));
	}

	public void testUpdateSingleSelectionFromModelWhenUnbound() {
		ISelectableRidget ridget = getRidget();

		setUIControlRowSelectionInterval(1, 1);
		singleSelectionBean.setSelection(getRowValue(0));

		assertEquals(1, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(1);
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(1), multiSelectionBean.getSelectionList().get(0));

		ridget.setUIControl(null); // unbind
		ridget.updateSingleSelectionFromModel();
		ridget.setUIControl(getUIControl()); // rebind

		assertEquals(1, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(0);
		assertEquals(getRowValue(0), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(0), multiSelectionBean.getSelectionList().get(0));
	}

	public void testUpdateSingleSelectionFromRidgetOnRebind() {
		ISelectableRidget ridget = getRidget();

		setUIControlRowSelectionInterval(2, 2);
		ridget.setUIControl(null); // unbind
		clearUIControlRowSelection();

		assertEquals(0, getUIControlSelectedRowCount());
		assertEquals(getRowValue(2), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(0));

		ridget.setUIControl(getUIControl()); // rebind

		assertEquals(1, getUIControlSelectedRowCount());
		assertUIControlSelectionContains(2);
		assertEquals(getRowValue(2), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(0));
	}

	public void testSelectionEventsSelectionTypeSingle() {
		ISelectableRidget ridget = getRidget();
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
		java.util.List<?> newSelection = Arrays.asList(new Object[] { node1 });
		PropertyChangeEvent multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION,
				oldSelection, newSelection);
		PropertyChangeEvent singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION,
				null, node1);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(node1);

		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();

		ridget.setSelection(node1);

		verifyPropertyChangeEvents();

		oldSelection = newSelection;
		newSelection = Collections.EMPTY_LIST;
		multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION, oldSelection,
				newSelection);
		singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION, node1, null);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(Collections.EMPTY_LIST);

		verifyPropertyChangeEvents();

		oldSelection = Collections.EMPTY_LIST;
		newSelection = Arrays.asList(new Object[] { node2 });
		multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION, oldSelection,
				newSelection);
		singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION, null, node2);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(Arrays.asList(new Object[] { node2, node3 }));

		verifyPropertyChangeEvents();
	}

	public void testSelectionEventsSelectionTypeMulti() {
		ISelectableRidget ridget = getRidget();
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
		java.util.List<?> newSelection = Arrays.asList(new Object[] { node1 });
		PropertyChangeEvent multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION,
				oldSelection, newSelection);
		PropertyChangeEvent singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION,
				null, node1);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(node1);

		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();

		ridget.setSelection(node1);

		verifyPropertyChangeEvents();

		oldSelection = newSelection;
		newSelection = Collections.EMPTY_LIST;
		multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION, oldSelection,
				newSelection);
		singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION, node1, null);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(Collections.EMPTY_LIST);

		verifyPropertyChangeEvents();

		oldSelection = Collections.EMPTY_LIST;
		newSelection = Arrays.asList(new Object[] { node2, node3 });
		multiEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_MULTI_SELECTION, oldSelection,
				newSelection);
		singleEvent = new PropertyChangeEvent(ridget, ISelectableRidget.PROPERTY_SINGLE_SELECTION, null, node2);
		expectPropertyChangeEvents(multiEvent, singleEvent);

		ridget.setSelection(Arrays.asList(new Object[] { node2, node3 }));

		verifyPropertyChangeEvents();
	}

	public void testSetSelectionTypeNull() {
		ISelectableRidget ridget = getRidget();

		try {
			ridget.setSelectionType(null);
			fail();
		} catch (RuntimeException npe) {
			// expected
		}
	}

	public void testSetSelectionTypeNONE() {
		ISelectableRidget ridget = getRidget();

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

	/**
	 * Returns the "indices" of the selected tree items based on a mock "index"
	 * scheme (0: item for node1, 1: item for node2, 2: item for node3).
	 */
	private int[] getUIControlSelectedRows() {
		TreeItem[] selection = getUIControl().getSelection();
		int[] result = new int[selection.length];
		for (int i = 0; i < result.length; i++) {
			if (selection[i] == getUIControlItem(0)) {
				result[i] = 0;
			} else if (selection[i] == getUIControlItem(1)) {
				result[i] = 1;
			} else if (selection[i] == getUIControlItem(2)) {
				result[i] = 2;
			}
		}
		return result;
	}

	/**
	 * Select one or more TreeItems based on a mock "index" scheme (0: item for
	 * node1, 1: item for node2, 2: item for node3).
	 * 
	 * @param start
	 *            the start index (>=0)
	 * @param end
	 *            the end index (>= start index; and < 3)
	 */
	private void setUIControlRowSelectionInterval(int start, int end) {
		Assert.isLegal(0 <= start);
		Assert.isLegal(start <= end);
		Tree control = getUIControl();
		int length = (end - start) + 1;
		TreeItem[] items = new TreeItem[length];
		for (int i = 0; i < items.length; i++) {
			items[i] = getUIControlItem(i + start);
		}
		control.setSelection(items);
		fireSelectionEvent();
	}

	/**
	 * Return the TreeItem corresponding to the following mock "index" scheme:
	 * 0: item for node1, 1: item for node2, 2: item for node3.
	 * <p>
	 * This method will fully expand the tree to ensure all tree items are
	 * created.
	 */
	protected final TreeItem getUIControlItem(int index) {
		getRidget().expandTree();
		Tree control = getUIControl();
		switch (index) {
		case 0:
			return control.getItem(0);
		case 1:
			return control.getItem(0).getItem(0);
		case 2:
			return control.getItem(0).getItem(0).getItem(0);
		}
		throw new IndexOutOfBoundsException("index= " + index);
	}

	/**
	 * Returns true if the control's selection contains the TreeItem
	 * corresponding to the given mock "index" number (0: item for node1, 1:
	 * item for node2, 2: item for node3).
	 */
	private void assertUIControlSelectionContains(int itemIndex) {
		boolean result = false;
		TreeItem item = getUIControlItem(itemIndex);
		TreeItem[] selections = getUIControl().getSelection();
		for (int i = 0; !result && i < selections.length; i++) {
			result = (item == selections[i]);
		}
		assertTrue("not selected in ui control: " + item, result);
	}

	/**
	 * Return the tree node corresponding to the following mock "index" scheme:
	 * 0: item for node1, 1: item for node2, 2: item for node3.
	 */
	private ITreeNode getRowValue(int index) {
		switch (index) {
		case 0:
			return node1;
		case 1:
			return node2;
		case 2:
			return node3;
		}
		throw new IndexOutOfBoundsException("index= " + index);
	}

	/**
	 * fires a selection event manually - control.setXXX does not fire events
	 */
	protected final void fireSelectionEvent() {
		Event event = new Event();
		event.widget = getUIControl();
		event.type = SWT.Selection;
		getUIControl().notifyListeners(SWT.Selection, event);
	}

}
