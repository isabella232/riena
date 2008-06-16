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
 * Use for tree nodes which provide a tool tip.
 */
public interface ITreeNodeToolTip {

	/**
	 * @return the toolTipText for the node.
	 */
	String getToolTipText();

	/**
	 * @param toolTipText
	 *            The toolTipText to set.
	 */
	void setToolTipText(String toolTipText);

	/**
	 * @return
	 */
	boolean isSelectedAsDropTarget();

	/**
	 * @param selectedAsDropTarget
	 */
	void setSelectedAsDropTarget(boolean selectedAsDropTarget);

	/**
	 * @return
	 */
	boolean isDropOk();

	/**
	 * @param dropOk
	 */
	void setDropOk(boolean dropOk);
}