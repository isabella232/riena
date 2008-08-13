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

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;

/**
 * Default implementation for a SubModuleNodeViewController
 */
/**
 * 
 */
public class SubModuleController extends NavigationNodeController<ISubModuleNode> {

	private static final String TITLE_SEPARATOR = " - "; //$NON-NLS-1$

	private IWindowRidget windowRidget;

	public SubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public SubModuleController() {
		this(null);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.NavigationNodeController#setNavigationNode(org.eclipse.riena.navigation.INavigationNode)
	 */
	@Override
	public void setNavigationNode(ISubModuleNode navigationNode) {
		super.setNavigationNode(navigationNode);

		getNavigationNode().addListener(new SubModuleNodeListener() {

			@Override
			public void labelChanged(ISubModuleNode subModuleNode) {
				updateLabel();
			}

			@Override
			public void iconChanged(ISubModuleNode source) {
				updateIcon();
			}

		});
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

	@Override
	public void afterBind() {
		super.afterBind();

		updateLabel();
		updateIcon();
		updateCloseable();
		updateActive();
	}

	/**
	 * @param actionRidget
	 */
	public void setDefaultButton(IActionRidget actionRidget) {
		if (windowRidget != null) {
			windowRidget.setDefaultButton(actionRidget.getUIControl());
		}
	}

	/**
	 * @return
	 */
	public Object getDefaultButton() {
		if (windowRidget != null) {
			return windowRidget.getDefaultButton();
		}
		return null;
	}

	private void updateLabel() {
		if (windowRidget != null) {
			windowRidget.setTitle(getFullTitle());
		}
	}

	private void updateCloseable() {
		if (windowRidget != null) {
			windowRidget.setCloseable(false);
		}
	}

	private void updateActive() {
		if (windowRidget != null) {
			windowRidget.setActive(getNavigationNode().isActivated());
		}
	}

	protected String getFullTitle() {
		String title = getNavigationNode().getLabel();
		if (!getModuleController().hasSingleLeafChild()) {
			INavigationNode<?> parent = getNavigationNode().getParent();
			while (!(parent instanceof IModuleNode)) {
				title = parent.getLabel() + TITLE_SEPARATOR + title;
				parent = parent.getParent();
			}
			title = parent.getLabel() + TITLE_SEPARATOR + title;
		}
		return title;
	}

	private void updateIcon() {
		updateIcon(windowRidget);
	}

	/**
	 * Returns the controller of the parent module.
	 * 
	 * @return module controller
	 */
	public ModuleController getModuleController() {
		return (ModuleController) getNavigationNode().getParentOfType(IModuleNode.class).getPresentation();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
	}

}
