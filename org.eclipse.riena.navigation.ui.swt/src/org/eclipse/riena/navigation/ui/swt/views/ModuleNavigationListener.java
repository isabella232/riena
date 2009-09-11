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

import java.util.List;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.internal.navigation.ui.swt.handlers.SwitchModule;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;

/**
 * Navigation for the 'submodule' tree used in {@link ModuleView}. This includes
 * both mouse navigation (selection) and keyboard navigation (key down / up).
 * <p>
 * This class takes care of the following cases:
 * <ul>
 * <li>node clicked / selection change: activate the node</li>
 * <li>arrow up / down: activate the node</li>
 * <li>arrow up on the first node: activate the previous module, wrapping around
 * to the last, if there is no previous one</li>
 * <li>arrow down on the last node: activate the next module, wrapping around to
 * the 1st, if there is no next one</li>
 * </ul>
 * <p>
 * Some submodule nodes can be flagged as non-selecteble. If such a node is
 * selected by mouse, programmatically or arrow down movement, then the first
 * selectable child will be selected. If such a node is selected by arrow up
 * movement the previous selectable node (i.e. the node above) will be selected.
 */
class ModuleNavigationListener extends SelectionAdapter implements KeyListener {

	/** Keycode for 'arrow down' (16777218) */
	private static final int KC_ARROW_DOWN = 16777218;
	/** Keycode for 'arrow up' (16777217) */
	private static final int KC_ARROW_UP = 16777217;

	private volatile boolean keyPressed;

	private NodeSwitcher nodeSwitcher;
	private boolean isFirst;
	private boolean isLast;

	ModuleNavigationListener(Tree moduleTree) {
		moduleTree.addKeyListener(this);
		moduleTree.addSelectionListener(this);
	}

	public void widgetSelected(SelectionEvent event) {
		// System.out.println("widgetSelected() " + getSelection(event));
		cancelSwitch();
		if (!keyPressed) {
			startSwitch(getSelection(event));
		}
	}

	public void keyPressed(KeyEvent event) {
		cancelSwitch();
		keyPressed = true;
		TreeItem from = getSelection(event);
		isFirst = isFirst(from);
		isLast = isLast(from);
	}

	public void keyReleased(KeyEvent event) {
		// System.out.println("keyReleased() " + getSelection(event));
		// check stateMask is zero to ensure no modifier is pressed (i.e. ctrl or alt)
		if (event.stateMask == 0) {
			if (KC_ARROW_UP == event.keyCode && isFirst) {
				createSwitchModuleOp().doSwitch(false);
			} else if (KC_ARROW_DOWN == event.keyCode && isLast) {
				createSwitchModuleOp().doSwitch(true);
			} else {
				startSwitch(getSelection(event));
			}
		}
		keyPressed = false;
	}

	// helping methods
	//////////////////

	private void cancelSwitch() {
		if (nodeSwitcher != null) {
			nodeSwitcher.cancel();
		}
	}

	private SwitchModule createSwitchModuleOp() {
		SwitchModule result = new SwitchModule();
		result.setActivateSubmodule(true);
		return result;
	}

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

	private TreeItem getSelection(TypedEvent event) {
		Tree tree = (Tree) event.widget;
		TreeItem result = null;
		if (tree.getSelectionCount() > 0) {
			result = tree.getSelection()[0];
		}
		return result;
	}

	private boolean isFirst(TreeItem item) {
		boolean result = false;
		if (item != null) {
			Tree tree = item.getParent();
			if (tree.getItemCount() > 0) {
				result = tree.getItem(0) == item;
			}
		}
		return result;
	}

	private boolean isLast(TreeItem item) {
		if (null == item) {
			return false;
		}
		return item == findLast(item.getParent());
	}

	private void startSwitch(TreeItem item) {
		cancelSwitch();
		if (item != null) {
			nodeSwitcher = new NodeSwitcher(item.getDisplay(), (INavigationNode<?>) item.getData());
			nodeSwitcher.start();
		}
	}

	/**
	 * Non-API. Public for testing only.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	// TODO [ev] 286255 + tests
	private static ISubModuleNode findPreviousNode(ISubModuleNode node) {
		ISubModuleNode result = null;
		INavigationNode<?> parent = node.getParent();
		if (parent != null) {
			List<?> children = parent.getChildren();
			for (Object child : children) {
				if (child == node) {
					break;
				}
				result = (ISubModuleNode) child;
			}
		}
		return result;
	}

	// helping classes
	//////////////////

	/**
	 * Activates a navigation node after a timeout. Can be cancelled.
	 */
	private static class NodeSwitcher extends Thread {

		/**
		 * Wait this long (ms) before activating a node.
		 */
		private static final int TIMEOUT_MS = 700;

		private final INavigationNode<?> node;
		private final Display display;

		private volatile boolean isCancelled;

		NodeSwitcher(Display display, INavigationNode<?> node) {
			this.display = display;
			this.node = node;
		}

		@Override
		public void run() {
			try {
				sleep(TIMEOUT_MS);
			} catch (InterruptedException iex) {
				Nop.reason("ignore"); //$NON-NLS-1$
			}
			if (!isCancelled) {
				// node activation must be triggered from the UI thread:
				display.syncExec(new Runnable() {
					public void run() {
						node.activate();
					}
				});
			}
		}

		/**
		 * Cancels activation of the node, if called before the timeout.
		 */
		public void cancel() {
			isCancelled = true;
		}
	}
}
