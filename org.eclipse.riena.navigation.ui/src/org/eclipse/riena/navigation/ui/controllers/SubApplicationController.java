/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.controllers;

import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IUIProcessRidget;

/**
 * Implements the Controller for a Module Sub Application
 */
public class SubApplicationController extends NavigationNodeController<ISubApplicationNode> {

	private IUIProcessRidget uiProcessRidget;
	private final NodeEventDelegation contextUpdater = new NodeEventDelegation();

	/**
	 * Create a new Controller, find the corresponding subApplication for the
	 * passed ID
	 */
	public SubApplicationController(final ISubApplicationNode pSubApplication) {
		super(pSubApplication);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		// nothing to do
	}

	@Override
	public void afterBind() {
		super.afterBind();
		initUiProcessRidget();
	}

	private void initUiProcessRidget() {
		if (uiProcessRidget == null) {
			// fallback
			uiProcessRidget = getRidget("uiProcessRidget"); //$NON-NLS-1$
			if (uiProcessRidget == null) {
				return;
			}
		}
		uiProcessRidget.setContextLocator(contextUpdater);
	}

	/**
	 * Returns the ridget of a menu action.
	 * 
	 * @param id
	 *            id of the menu item
	 * @return action ridget; {@code null} if no action ridget was found
	 */
	public IActionRidget getMenuActionRidget(final String id) {

		final String menuItemId = IActionRidget.BASE_ID_MENUACTION + id;
		return getActionRidget(menuItemId);

	}

	/**
	 * Returns the ridget of a tool bar action.
	 * 
	 * @param id
	 *            id of the tool bar button
	 * @return action ridget; {@code null} if no action ridget was found
	 */
	public IActionRidget getToolbarActionRidget(final String id) {

		final String menuItemId = IActionRidget.BASE_ID_TOOLBARACTION + id;
		return getActionRidget(menuItemId);

	}

	/**
	 * Returns the action ridget with given id.
	 * 
	 * @param id
	 *            id of the ridget
	 * @return action ridget; {@code null} if no action ridget was found
	 */
	private IActionRidget getActionRidget(final String id) {

		final IRidget ridget = getRidget(id);
		if (ridget instanceof IActionRidget) {
			return (IActionRidget) ridget;
		} else {
			return null;
		}

	}

	/**
	 * @return the progressBoxRidget
	 */
	public IUIProcessRidget getUiProcessRidget() {
		return uiProcessRidget;
	}

	/**
	 * @param uiProcessRidget
	 *            the progressBoxRidget to set
	 */
	public void setUiProcessRidget(final IUIProcessRidget uiProcessRidget) {
		this.uiProcessRidget = uiProcessRidget;
	}

}
