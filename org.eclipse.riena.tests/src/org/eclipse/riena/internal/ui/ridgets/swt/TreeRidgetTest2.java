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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;
import org.eclipse.riena.ui.ridgets.util.beans.Person;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;

/**
 * Tests for the class {@link TreeRidget}.
 * <p>
 * These tests use a TreeNode that wraps a {@link Person} and focus on methods
 * inherited from the {@link ISelectableRidget}.
 * 
 * @see TableRidgetTest
 */
public class TreeRidgetTest2 extends AbstractTreeRidgetTest {

	@Override
	protected Control createUIControl(Composite parent) {
		return new Tree(parent, SWT.MULTI);
	}

	@Override
	protected IRidget createRidget() {
		return new TreeRidget();
	}

	@Override
	protected ITreeNode[] bindRidgetToModel() {
		Collection<Person> persons = createPersonList();
		Iterator<Person> iter = persons.iterator();
		Person person1 = iter.next();
		Person person2 = iter.next();
		Person person3 = iter.next();
		Person person4 = iter.next();

		// node1 is the root
		ITreeNode node1 = new TreeNode(person1);
		// node2 and node4 will be visible by default because node1 gets
		// autoexpanded
		ITreeNode node2 = new TreeNode(node1, person2);
		ITreeNode node4 = new TreeNode(node1, person4);
		// node3 is on level-2, so it does not get autoexpanded
		ITreeNode node3 = new TreeNode(node2, person3);

		ITreeNode[] roots = { node1 };
		getRidget().bindToModel(roots, ITreeNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT,
				ITreeNode.PROPERTY_VALUE);
		return new ITreeNode[] { node1, node2, node3, node4 };
	}

	@Override
	protected void clearUIControlRowSelection() {
		getUIControl().deselectAll();
		fireSelectionEvent();
	}

	@Override
	protected int getUIControlSelectedRowCount() {
		return getUIControl().getSelectionCount();
	}

	// test methods
	// /////////////

	public void testGetUIControl() throws Exception {
		Tree control = getUIControl();
		assertEquals(control, getRidget().getUIControl());
	}

	public void testUpdateFromModelPreservesSelection() {
		ITreeRidget ridget = getRidget();

		ridget.setSelection(rootChild2);

		assertSame(rootChild2, ridget.getSelection().get(0));

		List<ITreeNode> children = root.getChildren();
		children.remove(rootChild1);
		root.setChildren(children);
		ridget.updateFromModel();

		assertSame(rootChild2, ridget.getSelection().get(0));
	}

	public void testUpdateFromModelRemovesSelection() {
		ITreeRidget ridget = getRidget();

		ridget.setSelection(rootChild1Child1);

		assertSame(rootChild1Child1, ridget.getSelection().get(0));

		List<ITreeNode> children = root.getChildren();
		children.remove(rootChild1);
		root.setChildren(children);
		ridget.updateFromModel();

		assertTrue(ridget.getSelection().isEmpty());
	}

	public void testContainsOption() {
		ITreeRidget ridget = getRidget();

		assertTrue(ridget.containsOption(root));
		assertTrue(ridget.containsOption(rootChild1));
		assertTrue(ridget.containsOption(rootChild1Child1));
		assertTrue(ridget.containsOption(rootChild2));

		TreeNode[] newRoots = { new TreeNode("newRoot") };

		assertFalse(ridget.containsOption(null));
		assertFalse(ridget.containsOption(newRoots[0]));

		getRidget().bindToModel(newRoots, ITreeNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT,
				ITreeNode.PROPERTY_VALUE);

		assertFalse(ridget.containsOption(root));
		assertTrue(ridget.containsOption(newRoots[0]));
	}

	public void testSelectionType() throws Exception {
		ITreeRidget ridget = getRidget();
		Tree control = getUIControl();

		assertEquals(ITreeRidget.SelectionType.SINGLE, ridget.getSelectionType());

		ridget.setSelectionType(ITreeRidget.SelectionType.MULTI);

		assertEquals(ITreeRidget.SelectionType.MULTI, ridget.getSelectionType());

		try {
			ridget.setSelectionType(ITreeRidget.SelectionType.NONE);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException expected) {
			// ok, expected
		}

		assertEquals(ITreeRidget.SelectionType.MULTI, ridget.getSelectionType());

		ridget.setUIControl(null); // unbind
		ridget.setSelectionType(ITreeRidget.SelectionType.SINGLE);

		assertEquals(ITreeRidget.SelectionType.SINGLE, ridget.getSelectionType());

		ridget.setUIControl(control); // rebind

		assertEquals(ITreeRidget.SelectionType.SINGLE, ridget.getSelectionType());
	}

	public void testSetSelectionTypeIsEnforced() {
		ITreeRidget ridget = getRidget();
		Tree control = getUIControl();

		assertEquals(ISelectableRidget.SelectionType.SINGLE, ridget.getSelectionType());
		assertTrue((control.getStyle() & SWT.MULTI) != 0);

		ridget.setSelection(Arrays.asList(root, rootChild1));

		// single selection is enforced
		assertEquals(1, ridget.getSelection().size());
		assertEquals(1, control.getSelectionCount());

		// multiple selection is now allowed
		ridget.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		ridget.setSelection(Arrays.asList(root, rootChild1));

		assertEquals(2, ridget.getSelection().size());
		assertEquals(2, control.getSelectionCount());
	}

	// helping methods
	// ////////////////

	private Collection<Person> createPersonList() {
		Collection<Person> newList = new ArrayList<Person>();

		Person person = new Person("Doe", "One");
		person.setEyeColor(1);
		newList.add(person);

		person = new Person("Jackson", "Two");
		person.setEyeColor(1);
		newList.add(person);

		person = new Person("Jackson", "Three");
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
