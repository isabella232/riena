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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
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
class ModuleKeyboardNavigationListener extends SelectionAdapter implements TreeListener, KeyListener, MouseListener {

	/** Keycode for 'arrow down' (16777218) */
	private static final int KC_ARROW_DOWN = 16777218;
	/** Keycode for 'arrow up' (16777217) */
	private static final int KC_ARROW_UP = 16777217;

	private TreeItem selected;
	private boolean isFirst;
	private boolean isLast;
	private boolean isReady;

	ModuleKeyboardNavigationListener(Tree moduleTree) {
		moduleTree.addSelectionListener(this);
		moduleTree.addTreeListener(this);
		moduleTree.addKeyListener(this);
		moduleTree.addMouseListener(this);
	}

	public void keyPressed(KeyEvent event) {
		// unused
	}

	public void keyReleased(KeyEvent event) {
		if (isReady) {
			if (KC_ARROW_UP == event.keyCode && isFirst) {
				new SwitchModule().doSwitch(false);
			}
			if (KC_ARROW_DOWN == event.keyCode && isLast) {
				new SwitchModule().doSwitch(true);
			}
		}
		isReady = true;
	}

	public void mouseDoubleClick(MouseEvent e) {
		// unused
	}

	public void mouseDown(MouseEvent e) {
		// unused			
	}

	public void mouseUp(MouseEvent e) {
		isReady = true;
	}

	public void treeCollapsed(TreeEvent e) {
		e.display.asyncExec(new Runnable() {
			// #asyncExec ensures this is done AFTER the collapse operation has finished.
			// Otherwise this would run on the old data (i.e. before collapse) and return old result.
			public void run() {
				isLast = isLast(selected);
			}
		});
	}

	public void treeExpanded(TreeEvent e) {
		e.display.asyncExec(new Runnable() {
			// #asyncExec ensures this is done AFTER the collapse operation has finished.
			// Otherwise this would run on the old data (i.e. before collapse) and return old result.
			public void run() {
				isLast = isLast(selected);
			}
		});
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		selected = (TreeItem) event.item;
		isFirst = isFirst(selected);
		isLast = isLast(selected);
		isReady = false;
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
		Tree tree = item.getParent();
		if (tree.getItemCount() > 0) {
			result = tree.getItem(0) == item;
		}
		return result;
	}

	private boolean isLast(TreeItem item) {
		return item == findLast(item.getParent());
	}
}
