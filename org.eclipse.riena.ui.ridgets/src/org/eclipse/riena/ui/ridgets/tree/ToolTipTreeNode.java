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

/**
 * Tree node providing a tooltip.
 */
public class ToolTipTreeNode extends DefaultTreeNode implements ITreeNodeToolTip {

	private String toolTipText;
	private boolean selectedAsDropTarget;
	private boolean dropOk;

	/**
	 * Constructor. Creates a tree node with no parent, no children and
	 * initializes it with the specified user object.
	 * 
	 * @param userObject -
	 *            an Object provided by the user that constitutes the node's
	 *            data.
	 */
	public ToolTipTreeNode(Object userObject) {

		super(userObject);
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNodeToolTip#getToolTipText()
	 */
	public String getToolTipText() {

		return toolTipText;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNodeToolTip#setToolTipText(java.lang.String)
	 */
	public void setToolTipText(String toolTipText) {

		this.toolTipText = toolTipText;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNodeToolTip#isSelectedAsDropTarget()
	 */
	public boolean isSelectedAsDropTarget() {

		return selectedAsDropTarget;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNodeToolTip#setSelectedAsDropTarget(boolean)
	 */
	public void setSelectedAsDropTarget(boolean selectedAsDropTarget) {

		this.selectedAsDropTarget = selectedAsDropTarget;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNodeToolTip#isDropOk()
	 */
	public boolean isDropOk() {

		return dropOk;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.ITreeNodeToolTip#setDropOk(boolean)
	 */
	public void setDropOk(boolean dropOk) {

		this.dropOk = dropOk;
	}
}