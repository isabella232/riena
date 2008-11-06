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
 *
 */
public class MenuCoolBarComposite extends Composite {

	private List<Menu> menus;

	/**
	 * @param parent
	 * @param style
	 */
	public MenuCoolBarComposite(Composite parent, int style) {
		super(parent, style);
		menus = new ArrayList<Menu>();
	}

	public void addMenu(Menu menu) {
		menus.add(menu);
	}

	public List<Menu> getMenus() {
		return menus;
	}

}
