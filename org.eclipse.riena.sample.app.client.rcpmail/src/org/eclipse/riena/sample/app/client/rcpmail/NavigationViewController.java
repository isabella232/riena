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
package org.eclipse.riena.sample.app.client.rcpmail;

import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractRidgetController;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;

/**
 * TODO [ev] docs
 */
public class NavigationViewController extends AbstractRidgetController {

	@Override
	public void configureRidgets() {
		ITreeRidget tree = (ITreeRidget) getRidget("tree"); //$NON-NLS-1$
		tree.bindToModel(createDummyModel(), ITreeNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT,
				ITreeNode.PROPERTY_VALUE);
		tree.updateFromModel();
	}

	// helping methods
	// ////////////////

	/**
	 * We will set up a dummy model to initialize tree heararchy. In real code,
	 * you will connect to a real model. TODO [ev] more docs
	 */
	private ITreeNode[] createDummyModel() {
		TreeNode root1 = new TreeNode("me@this.com"); //$NON-NLS-1$
		new TreeNode(root1, "Inbox"); //$NON-NLS-1$
		new TreeNode(root1, "Drafts"); //$NON-NLS-1$
		new TreeNode(root1, "Sent"); //$NON-NLS-1$

		TreeNode root2 = new TreeNode("other@aol.com"); //$NON-NLS-1$
		new TreeNode(root2, "Inbox"); //$NON-NLS-1$

		return new ITreeNode[] { root1, root2 };
	}

}
