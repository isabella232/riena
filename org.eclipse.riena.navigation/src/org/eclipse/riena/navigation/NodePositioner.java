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
package org.eclipse.riena.navigation;

import org.eclipse.riena.navigation.model.NavigationModelFailure;

/**
 * A NodePositioner object is responsible to add a NavigationNode as a child to
 * another NavigationNode at a specified index. NodePositioner is for example
 * used by the SimpleNavigationNodeProvider to add a subtree to a parent node.
 * You can add a NodePositioner to a NavigationArgument. Then, whenever new
 * nodes are created the NodePositioner will handle the addition of the new
 * subtree to the root node. Usually it should not be necessary to subclass
 * NodePositioner.
 */
@SuppressWarnings("unchecked")
public abstract class NodePositioner {

	/**
	 * Adds a node to another at index 0.
	 */
	public final static NodePositioner ADD_BEGINNING = new NodePositioner() {

		@Override
		public void addChildToParent(INavigationNode parent, INavigationNode child) {
			indexed(0).addChildToParent(parent, child);
		}
	};

	/**
	 * Adds a node to another as the last child.
	 */
	public final static NodePositioner ADD_END = new NodePositioner() {

		@Override
		public void addChildToParent(INavigationNode parent, INavigationNode child) {
			parent.addChild(child);
		}
	};

	/**
	 * Adds a node to another as a child at the specified index.
	 */
	public final static NodePositioner indexed(final int index) {

		return new NodePositioner() {

			@Override
			public void addChildToParent(INavigationNode parent, INavigationNode child) {
				if (index < 0) {
					throw new NavigationModelFailure("Cannot add child " + child + " to parent " + parent //$NON-NLS-1$//$NON-NLS-2$
							+ " at index " + index + ". Index must be >= 0"); //$NON-NLS-1$//$NON-NLS-2$
				}

				// Fallback for out of bound index
				if (index >= parent.getChildren().size()) {
					ADD_END.addChildToParent(parent, child);
					return;
				}
				parent.addChild(index, child);
			}

		};
	}

	/**
	 * Adds the child node to the parent node.
	 * 
	 * @param parent
	 *            the parent node
	 * @param child
	 *            the child node
	 */
	public abstract void addChildToParent(INavigationNode parent, INavigationNode child);

}
