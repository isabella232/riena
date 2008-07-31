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

import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.model.ApplicationModelListener;
import org.eclipse.riena.ui.ridgets.IWindowRidget;
import org.eclipse.riena.ui.ridgets.listener.IWindowRidgetListener;

/**
 * The controller for the application
 */
public class ApplicationViewController extends NavigationNodeViewController<IApplicationModel> {

	private IWindowRidget windowRidget;
	private IWindowRidgetListener windowRidgetListener;
	private boolean menuBarVisible;

	/**
	 * @param applicationModel
	 */
	public ApplicationViewController(IApplicationModel applicationModel) {
		super(applicationModel);
		applicationModel.addListener(new ApplicationModelListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.riena.navigation.model.NavigationNodeAdapter#labelChanged
			 * (org.eclipse.riena.navigation.INavigationNode)
			 */
			@Override
			public void labelChanged(IApplicationModel source) {
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
			public void iconChanged(IApplicationModel source) {
				updateIcon();
			}
		});
	}

	public void setWindowRidget(IWindowRidget pWindowRidget) {
		if (windowRidgetListener == null) {
			windowRidgetListener = new FrameListener();
		}
		windowRidget = pWindowRidget;
		if (windowRidget != null && windowRidgetListener != null) {
			windowRidget.addWindowRidgetListener(windowRidgetListener);
		}
	}

	public void setVisible(boolean pVisible) {
		if (windowRidget != null) {
			windowRidget.setVisible(pVisible);
		}
	}

	private static class FrameListener implements IWindowRidgetListener {
		public void closed() {
		}
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
		getNavigationNode().activate();
		updateLabel();
		updateIcon();
	}

	private void updateLabel() {
		windowRidget.setTitle(getNavigationNode().getLabel());
	}

	private void updateIcon() {
		updateIcon(windowRidget);
	}

	public void setMenubarVisible(boolean visible) {
		this.menuBarVisible = visible;
	}

	public boolean isMenubarVisible() {
		return menuBarVisible;
	}

}
