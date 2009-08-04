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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.internal.navigation.ui.swt.handlers.SwitchModule;

/**
 * Keyboard navigation for the 'submodule' tree used in {@link ModuleView}.
 * <p>
 * When the first element of the tree is selected and ARROW_UP is pressed, it
 * will jump to the previous module / module group. When the last element of the
 * tree is selected and ARROR_DOWN is pressed it will jump to the next module /
 * module group.
 */
class ModuleKeyboardNavigationListener implements KeyListener {

	/** Keycode for 'arrow down' (16777218) */
	private static final int KC_ARROW_DOWN = 16777218;
	/** Keycode for 'arrow up' (16777217) */
	private static final int KC_ARROW_UP = 16777217;

	private TreeItem selected;
	private boolean isFirst;
	private boolean isLast;

	ModuleKeyboardNavigationListener(Tree moduleTree) {
		moduleTree.addKeyListener(this);
	}

	public void keyPressed(KeyEvent event) {
		Tree tree = (Tree) event.widget;
		if (tree.getSelectionCount() > 0) {
			selected = tree.getSelection()[0];
		} else {
			selected = null;
		}
		isFirst = isFirst(selected);
		isLast = isLast(selected);
	}

	public void keyReleased(KeyEvent event) {
		// check stateMask is zero to ensure no modifier is pressed (i.e. ctrl or alt)
		if (event.stateMask == 0) {
			if (KC_ARROW_UP == event.keyCode && isFirst) {
				// System.out.println("JUMP UP");
				new SwitchModule().doSwitch(false);
			}
			if (KC_ARROW_DOWN == event.keyCode && isLast) {
				// System.out.println("JUMP DOWN");
				new SwitchModule().doSwitch(true);
			}
		}
	}

	// helping methods
	//////////////////

	private TreeItem findLast(Tree tree) {
		if (tree.isDisposed()) {
			return null;
		}
		TreeItem[] items = tree.getItems();
		return items.length > 0 ? findLast(items[items.length - 1]) : null;
	}

	private TreeItem findLast(TreeItem item) {
		TreeItem result = item;
		if (item.getExpanded() && item.getItemCount() > 0) {
			TreeItem last = item.getItem(item.getItemCount() - 1);
			result = findLast(last);
		}
		return result;
	}

	private boolean isFirst(TreeItem item) {
		boolean result = false;

		if (null == item) {
			return false;
		}

		Tree tree = item.getParent();
		if (tree.getItemCount() > 0) {
			result = tree.getItem(0) == item;
		}
		return result;
	}

	private boolean isLast(TreeItem item) {
		if (null == item) {
			return false;
		}
		return item == findLast(item.getParent());
	}
}
