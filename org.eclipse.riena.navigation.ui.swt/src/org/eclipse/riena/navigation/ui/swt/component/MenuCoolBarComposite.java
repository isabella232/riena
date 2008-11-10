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
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

/**
 * This composites has a list of the top-level menus of the Riena menu bar (a
 * cool bar with an item for every top-level menu).
 */
public class MenuCoolBarComposite extends Composite {

	private List<Menu> menus;

	/**
	 * Creates an new instance of {@code MenuCoolBarComposite} given its parent
	 * and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            - a composite which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            - the style of widget to construct
	 */
	public MenuCoolBarComposite(Composite parent, int style) {
		super(parent, style);
		menus = new ArrayList<Menu>();
	}

	/**
	 * Adds the given menu to the list of top-level menus of the Riena
	 * application.
	 * 
	 * @param menu
	 *            - top-level menu to add
	 */
	public void addMenu(Menu menu) {
		menus.add(menu);
	}

	/**
	 * Returns the list of top-level menus of the Riena application.
	 * 
	 * @return list of menus
	 */
	public List<Menu> getMenus() {
		return menus;
	}

}
