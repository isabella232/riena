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
package org.eclipse.riena.ui.ridgets.tree;

import java.util.EventListener;

import org.eclipse.core.databinding.observable.IObservablesListener;

/**
 * Defines the interface for an object that listens to changes in a
 * <code>ITreeModel</code>.
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
