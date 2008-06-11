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
package org.eclipse.riena.navigation.ui.swt.lnf;

import java.util.Map;

/**
 * Theme (colors, fonts, etc.) of a look and feel.
 */
public interface ILnfTheme {

	/**
	 * Adds color resources to the given table.
	 * 
	 * @param table -
	 *            table with the resources of the look and feel
	 */
	void addCustomColors(Map<String, ILnfResource> table);

	/**
	 * Adds font resources to the given table.
	 * 
	 * @param table -
	 *            table with the resources of the look and feel
	 */
	void addCustomFonts(Map<String, ILnfResource> table);

	/**
	 * Adds image resources to the given table.
	 * 
	 * @param table -
	 *            table with the resources of the look and feel
	 */
	void addCustomImages(Map<String, ILnfResource> table);

	/**
	 * Adds settings to the given table.
	 * 
	 * @param table -
	 *            table with the settings of the look and feel
	 */
	void addCustomSettings(Map<String, Object> table);

}
