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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.riena.ui.ridgets.swt.MenuManagerHelper;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * This class stores the properties of a tool item.
 */
public class ToolItemProperties extends AbstractItemProperties {

	private ToolBar parent;
	private int index;

	/**
	 * @param item
	 */
	public ToolItemProperties(ToolItemRidget ridget) {

		super(ridget);

		ToolItem item = ridget.getUIControl();
		parent = item.getParent();
		index = parent.indexOf(item);

	}

	@Override
	protected ToolItemRidget getRidget() {
		return (ToolItemRidget) super.getRidget();
	}

	@Override
	protected ToolItem createItem() {

		IContributionItem contributionItem = getContributionItem();
		MenuManager menuManager = getMenuManager();
		ToolItem toolItem;
		if ((contributionItem != null) && (menuManager == null)) {
			contributionItem.fill(parent, index);
			toolItem = parent.getItem(index);
			toolItem.setEnabled(true);
			setAllProperties(toolItem, false);
			contributionItem.update();
		} else {
			toolItem = new ToolItem(parent, getStyle(), index);
			setAllProperties(toolItem, true);
			if (menuManager != null) {
				toolItem.setData(menuManager);
				MenuManagerHelper helper = new MenuManagerHelper();
				helper.addListeners(toolItem, menuManager.getMenu());
			}
		}
		getRidget().setUIControl(toolItem);
		return toolItem;

	}

	private MenuManager getMenuManager() {

		IContributionItem contributionItem = getContributionItem();
		if (contributionItem instanceof MenuManager) {
			return (MenuManager) contributionItem;
		}

		return null;

	}

}
