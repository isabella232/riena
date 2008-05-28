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
package org.eclipse.riena.ui.ridgets;

/**
 * 
 */
public interface IGroupedTableRidget extends ITreeTableRidget {

	/**
	 * Property indicating whether rows associated with folder nodes in the tree
	 * should be rendered as grouping rows with the tree column spanning the
	 * entire table or rather as normal data rows like their children.
	 */
	String PROPERTY_GROUPING_ENABLED = "groupingEnabled";

	/**
	 * Indicates whether grouping is enabled. As grouped table without grouping
	 * looks like a normal table.
	 * 
	 * @return true if grouping is enabled, false otherwise.
	 */
	boolean isGroupingEnabled();

	/**
	 * Sets whether grouping is enabled. As grouped table without grouping looks
	 * like a normal table.
	 * 
	 * @param grouping
	 *            The new grouping state.
	 */
	void setGroupingEnabled(boolean grouping);
}
