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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A node of a tree that is dynamic.
 */
public class DynamicTreeNode extends DefaultTreeNode {

	private Iterator<? extends Collection<? extends IUserTreeElement>> unloadedChildren;

	/**
	 * Constructor. Creates a tree node with the given parent and initializes it
	 * with the specified user element.
	 * 
	 * @param parent
	 *            - the parent of the tree element.
	 * @param userElement
	 *            - an element provided by the user that constitutes the data of
	 *            the tree element.
	 */
	public DynamicTreeNode(DynamicTreeNode parent, IUserTreeElement userElement) {

		super(parent, userElement);
	}

	/**
	 * Returns whether the user object is loaded or not.
	 * 
	 * @return true if user object is loaded, false if it is not loaded or in
	 *         the process of being loaded.
	 */
	public boolean isLoaded() {
		return !(getUserObject() instanceof PlaceholderUserTreeElement);
	}

	/**
	 * Returns a tree structure with the value of this node a root element and
	 * the values of all loaded children, grandchildren, etc.
	 * 
	 * @return A tree structure with the loaded values of the subtree rooted in
	 *         this node.
	 */
	ValueTreeNode getLoadedSubTreeValues() {

		ValueTreeNode loadedSubTreeRoot = new ValueTreeNode(toString());

		for (int i = 0; i < getChildCount(); i++) {
			DynamicTreeNode childNode = ((DynamicTreeNode) children.get(i));
			if (childNode.isLoaded()) {
				ValueTreeNode childLoadedSubTreeValues = childNode.getLoadedSubTreeValues();
				if (childLoadedSubTreeValues != null) {
					loadedSubTreeRoot.add(childLoadedSubTreeValues);
				}
			}
		}

		return loadedSubTreeRoot;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DefaultTreeNode#setUserObject(java.lang.Object)
	 */
	@Override
	public void setUserObject(Object userObject) {
		super.setUserObject(userObject);

		resetChildIterator();
	}

	/**
	 * Resets the iterator of unloaded children.
	 */
	void resetChildIterator() {
		if (getUserObject() instanceof IUserTreeElement) {
			unloadedChildren = getUserTreeElement().getChildren();
		}
	}

	/**
	 * Checks whether there is another unloaded child to load.
	 * 
	 * @return true if the iterator of unloaded children has more, false
	 *         otherwise.
	 */
	boolean hasNextChildUserTreeElements() {
		if (unloadedChildren != null) {
			return unloadedChildren.hasNext();
		} else {
			return false;
		}
	}

	/**
	 * Loads the next child tree user element by retrieving it from the iterator
	 * of unloaded children.
	 * 
	 * @return The user tree element of the next unloaded child.
	 */
	Collection<? extends IUserTreeElement> getNextChildUserTreeElements() {
		if (unloadedChildren != null) {
			return unloadedChildren.next();
		} else {
			return null;
		}
	}

	protected IUserTreeElement createPlaceholderUserElement() {
		return new PlaceholderUserTreeElement();
	}

	protected DynamicTreeNode createChildNode() {
		return new DynamicTreeNode(this, null);
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNode#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		return getUserTreeElement().isLeaf();
	}

	private IUserTreeElement getUserTreeElement() {
		return ((IUserTreeElement) getUserObject());
	}

	/**
	 * A user tree element that is used as the placeholder for the next user
	 * tree element to be retrieved from the iterator of unloaded children.
	 */
	protected class PlaceholderUserTreeElement implements IUserTreeElement {

		@Override
		public String toString() {
			return getLoadingChildValue().toString();
		}

		/**
		 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElement#isLeaf()
		 */
		public boolean isLeaf() {
			return true;
		}

		/**
		 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElement#getChildren()
		 */
		public Iterator<Collection<IUserTreeElement>> getChildren() {
			List<Collection<IUserTreeElement>> noChildren = Collections.emptyList();
			return noChildren.iterator();
		}

		/**
		 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElement#getLoadingChildValue()
		 */
		public Object getLoadingChildValue() {
			return getUserTreeElement().getLoadingChildValue();
		}

		public void setFilter(IUserTreeElementFilter filter) {
			// ignore
		}

		public IUserTreeElementFilter getFilter() {
			return null;
		}

		public boolean hasFilter() {
			return false;
		}

	}

}
