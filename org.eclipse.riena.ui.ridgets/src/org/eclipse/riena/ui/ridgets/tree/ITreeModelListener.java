/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.util.EventListener;

import org.eclipse.core.databinding.observable.IObservablesListener;

/**
 * Defines the interface for an object that listens to changes in a
 * <code>ITreeModel</code>.
 * 
 * @author Thorsten Schenkel
 */
public interface ITreeModelListener extends EventListener, IObservablesListener {

	/**
	 * Invoked after the tree has changed structure.
	 * 
	 * @param e
	 *            event object reference.
	 */
	void treeStructureChanged(TreeModelEvent e);

	/**
	 * Invoked after a node (or a set of siblings) has changed in some way.
	 * 
	 * @param e
	 *            event object reference.
	 */
	void treeNodesChanged(TreeModelEvent e);

	/**
	 * Invoked after nodes have been inserted into the tree.
	 * 
	 * @param e
	 *            event object reference.
	 */
	void treeNodesInserted(TreeModelEvent e);

	/**
	 * Invoked after nodes have been removed from the tree.
	 * 
	 * @param e
	 *            event object reference.
	 */
	void treeNodesRemoved(TreeModelEvent e);

} // end interface
