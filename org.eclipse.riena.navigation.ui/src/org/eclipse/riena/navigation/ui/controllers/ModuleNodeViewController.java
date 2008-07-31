/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.controllers;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.listener.ModuleNodeListener;
import org.eclipse.riena.ui.ridgets.IWindowRidget;

/**
 * Default implementation for a ModuleNodeViewController
 */
public class ModuleNodeViewController extends NavigationNodeViewController<IModuleNode> {

	private IWindowRidget windowRidget;
	private boolean closeable;
	private boolean dragEnabled;

	/**
	 * @param navigationNode
	 */
	public ModuleNodeViewController(IModuleNode navigationNode) {
		super(navigationNode);

		closeable = true;
		dragEnabled = true;
		getNavigationNode().addListener(new MyModuleNodeListener());
	}

	private final class MyModuleNodeListener extends ModuleNodeListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.navigation.model.NavigationNodeAdapter#labelChanged
		 * (org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void labelChanged(IModuleNode moduleNode) {
			updateLabel();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.navigation.model.NavigationNodeAdapter#iconChanged
		 * (org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void iconChanged(IModuleNode source) {
			updateIcon();
		}
	}

	/**
	 * @param windowRidget
	 *            the windowRidget to set
	 */
	public void setWindowRidget(IWindowRidget windowRidget) {
		this.windowRidget = windowRidget;
	}

	/**
	 * @return the windowRidget
	 */
	public IWindowRidget getWindowRidget() {
		return windowRidget;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.NavigationNodeViewController#afterBind()
	 */
	@Override
	public void afterBind() {

		super.afterBind();

		setCloseable(getNavigationNode().isCloseable());
		updateLabel();
		updateIcon();
	}

	private void updateIcon() {
		updateIcon(windowRidget);
	}

	private void updateLabel() {
		windowRidget.setTitle(getNavigationNode().getLabel());
	}

	public boolean isPresentGroupMember() {

		return ((IModuleGroupNode) getNavigationNode().getParent()).isPresentGroupNode();
	}

	public boolean hasSingleLeafChild() {

		return getNavigationNode().getChildren().size() == 1 && getNavigationNode().getChild(0).isLeaf();
	}

	public boolean isFirstChild() {

		return getNavigationNode().getParent().getChild(0) == getNavigationNode();
	}

	/**
	 * @return the closeable
	 */
	public boolean isCloseable() {
		return closeable;
	}

	/**
	 * @param closeable
	 *            the closeable to set
	 */
	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

	/**
	 * @return the dragEnabled
	 */
	public boolean isDragEnabled() {
		return dragEnabled;
	}

	/**
	 * @param dragEnabled
	 *            the dragEnabled to set
	 */
	public void setDragEnabled(boolean dragEnabled) {
		this.dragEnabled = dragEnabled;
	}
}
