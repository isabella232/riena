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

	public void afterBind() {
		ITreeRidget tree = (ITreeRidget) getRidget("tree");
		tree.bindToModel(createDummyModel(), 
				         ITreeNode.class, 
				         ITreeNode.PROPERTY_CHILDREN, 
				         ITreeNode.PROPERTY_PARENT, 
				         ITreeNode.PROPERTY_VALUE);
		tree.updateFromModel();
	}
	
	// helping methods
	//////////////////
	
    /**
     * We will set up a dummy model to initialize tree heararchy. In real
     * code, you will connect to a real model.
     * TODO [ev] more docs
     */
    private ITreeNode[] createDummyModel() {
    	TreeNode root1 = new TreeNode("me@this.com");
    	new TreeNode(root1, "Inbox");
    	new TreeNode(root1, "Drafts");
    	new TreeNode(root1, "Sent");

        TreeNode root2 = new TreeNode("other@aol.com");
        new TreeNode(root2, "Inbox");

        return new ITreeNode[] { root1, root2 };
    }

}
