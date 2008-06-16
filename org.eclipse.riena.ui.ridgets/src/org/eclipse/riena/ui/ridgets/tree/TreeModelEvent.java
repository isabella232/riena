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
package org.eclipse.riena.ui.ridgets.tree;

import java.io.Serializable;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IObservablesListener;
import org.eclipse.core.databinding.observable.ObservableEvent;

/**
 * Encapsulates information describing changes to a tree model, and used to
 * notify tree model listeners of the change.
 */
public class TreeModelEvent extends ObservableEvent {

	static final Object TYPE = new Object();

	private TreeDiff diff;

	public static TreeModelEvent createValueDiffInstance(IObservable source, ITreeNode node, Object oldValue,
			Object newValue) {

		TreeDiff diff = new TreeNodeValueDiff(node, oldValue, newValue);
		return new TreeModelEvent(source, diff);
	}

	public static TreeModelEvent createNodesRemovedInstance(IObservable source, ITreeNode parent, int[] childIndices,
			Serializable[] children) {

		TreeDiff diff = new TreeNodesRemovedDiff(parent, childIndices, children);
		return new TreeModelEvent(source, diff);
	}

	public static TreeModelEvent createNodesInsertedInstance(IObservable source, ITreeNode parent, int[] childIndices,
			Serializable[] children) {

		TreeDiff diff = new TreeNodesInsertedDiff(parent, childIndices, children);
		return new TreeModelEvent(source, diff);
	}

	public static TreeModelEvent createStructureChangedInstance(IObservable source, ITreeNode parent,
			int[] childIndices, Serializable[] children) {

		TreeDiff diff = new TreeStructureDiff(parent, childIndices, children);
		return new TreeModelEvent(source, diff);
	}

	/**
	 * constructor. Used to create an event when nodes have been changed,
	 * inserted, or removed.
	 * 
	 * @param source -
	 *            the object that has caused the change.
	 * @param diff -
	 *            object describing the diffenrence in the tree before and after
	 *            the operation that triggered this event took place
	 */
	public TreeModelEvent(IObservable source, TreeDiff diff) {

		super(source);
		this.diff = diff;

	} // end constructor

	/*
	 * @see org.eclipse.core.databinding.observable.ObservableEvent#getListenerType()
	 */
	@Override
	protected Object getListenerType() {
		return TYPE;
	}

	/*
	 * @see org.eclipse.core.databinding.observable.ObservableEvent#dispatch(org.eclipse.core.databinding.observable.IObservablesListener)
	 */
	@Override
	protected void dispatch(IObservablesListener listener) {
		diff.dispatch(this, listener);
	}

	/**
	 * Returns the node whose children have changed.
	 * 
	 * @return node whose children have changed.
	 */
	public ITreeNode getNode() {

		return diff.getNode();

	} // end method

	/**
	 * Returns the values of the child indices. Note that for performance
	 * reasons, this implementation passes back the actual data structure in
	 * which the child indices are stored internally!
	 * 
	 * @return an array of <code>int</code> containing index locations for the
	 *         children specified by the event.
	 */
	public int[] getChildIndices() {

		// The next line causes a FindBugs warning "May expose internal
		// representation by returning reference to mutable object" that
		// was chosen to be ignored due to performance reasons.
		return diff.getChildIndices();

	} // end method

	/**
	 * Returns the objects that are children of the node identified by
	 * <code>getPath</code> at the locations specified by
	 * <code>getChildIndices</code>. Note that for performance reasons, this
	 * implementation passes back the actual data structure in which the
	 * children are stored internally!
	 * 
	 * @return an array of Object containing the children specified by the
	 *         event.
	 */
	public Object[] getChildren() {

		// The next line causes a FindBugs warning "May expose internal
		// representation by returning reference to mutable object" that
		// was chosen to be ignored due to performance reasons.
		return diff.getChildren();

	} // end method

} // end class
