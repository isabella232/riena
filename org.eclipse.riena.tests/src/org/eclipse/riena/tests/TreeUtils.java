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
package org.eclipse.riena.tests;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Utility class for tesing {@link Tree} widgets.
 */
public final class TreeUtils {

	private TreeUtils() {
		// prevent instantation
	}

	/**
	 * Returns the number of items in the tree starting from the root
	 * (included).
	 */
	public static int getItemCount(Tree control) {
		int count = 0;
		for (TreeItem root : control.getItems()) {
			count += getItemCount(root);
		}
		return count;
	}

	/**
	 * Returns the number of items in subtree starting from item (included).
	 */
	public static int getItemCount(TreeItem item) {
		int count = 0;
		if (item != null) {
			count++;
			if (item.getExpanded()) {
				for (TreeItem child : item.getItems()) {
					count += getItemCount(child);
				}
			}
		}
		return count;
	}
}
