/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.treetable;

import org.eclipse.riena.ui.ridgets.tree.DynamicTreeNode;

/**
 * A node of a tree table that is dynamic.
 *
 * @author Carsten Drossel
 */
public class DynamicTreeTableNode extends DynamicTreeNode implements ITreeTableNode {

	/**
	 * Constructor. Creates a tree node with the given parent and initializes it with
	 * the specified user element.
	 *
	 * @param parent The parent of the tree element.
	 * @param userElement An element that constitutes the data of the tree element.
	 */
	public DynamicTreeTableNode( DynamicTreeTableNode parent, IUserTreeTableElement userElement ) {
		super( parent, userElement );
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.treetable.ITreeTableNode#getValueAt(int)
	 */
	public Object getValueAt( int column ) {

		if ( getUserObject() == null ) {
			return null;
		} // end if

		IUserTreeTableElement userElement = (IUserTreeTableElement) getUserObject();
		return userElement.getValueAt( column );
	}

	/**
	 * @see de.compeople.spirit.ui.swing.uicomponents.tree.DynamicTreeNode#createChildNode(de.compeople.spirit.ui.swing.uicomponents.tree.IUserTreeElement[], int)
	 */
	@Override
	protected DynamicTreeNode createChildNode() {
		return new DynamicTreeTableNode( this, null );
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DynamicTreeNode#createPlaceholderUserElement()
	 */
	@Override
	protected PlaceholderUserTreeElement createPlaceholderUserElement() {
		return new PlaceholderUserTreeTableElement();
	}

	private class PlaceholderUserTreeTableElement extends PlaceholderUserTreeElement implements IUserTreeTableElement {

		public Object getValueAt( int column ) {
			return null;
		}

	}

}
