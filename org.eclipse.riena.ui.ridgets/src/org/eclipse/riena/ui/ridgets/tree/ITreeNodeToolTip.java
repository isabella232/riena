/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

/**
 * Use for tree nodes which provide a tool tip.
 * 
 * @author Frank Schepp
 */
public interface ITreeNodeToolTip {

	/**
	 * @return the toolTipText for the node.
	 */
	String getToolTipText();

	/**
	 * @param toolTipText The toolTipText to set.
	 */
	void setToolTipText( String toolTipText );

	/**
	 * @return
	 */
	boolean isSelectedAsDropTarget();

	/**
	 * @param selectedAsDropTarget
	 */
	void setSelectedAsDropTarget( boolean selectedAsDropTarget );

	/**
	 * @return
	 */
	boolean isDropOk();

	/**
	 * @param dropOk
	 */
	void setDropOk( boolean dropOk );
}