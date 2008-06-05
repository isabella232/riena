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
package org.eclipse.riena.example.client.application;

import java.util.Map;

import org.eclipse.riena.navigation.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.FontLnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.ImageLnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.RienaDefaultTheme;
import org.eclipse.swt.SWT;

/**
 * 
 */
public class TestTheme extends RienaDefaultTheme {

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.RienaDefaultTheme#addCustomColors(java.util.Map)
	 */
	public void addCustomColors(Map<String, ILnfResource> table) {
		super.addCustomColors(table);

		table.put("EmbeddedTitlebar.activeBackgroundStartColor", new ColorLnfResource(234, 231, 158)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.activeBackgroundEndColor", new ColorLnfResource(225, 220, 114)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.activeBorderColor", new ColorLnfResource(171, 171, 174)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.passiveBackgroundStartColor", new ColorLnfResource(222, 224, 240)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.passiveBackgroundEndColor", new ColorLnfResource(186, 193, 225)); //$NON-NLS-1$
		table.put("EmbeddedTitlebar.passiveBorderColor", new ColorLnfResource(151, 150, 180)); //$NON-NLS-1$

		table.put("SubModuleTree.background", new ColorLnfResource(255, 255, 215)); //$NON-NLS-1$
		table.put("ModuleGroupWidget.background", new ColorLnfResource(255, 255, 215)); //$NON-NLS-1$
		table.put("SubApplication.background", new ColorLnfResource(255, 255, 215)); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.RienaDefaultTheme#addCustomFonts(java.util.Map)
	 */
	@Override
	public void addCustomFonts(Map<String, ILnfResource> table) {
		super.addCustomFonts(table);
		table.put("EmbeddedTitlebar.font", new FontLnfResource("Arial", 10, SWT.BOLD)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.RienaDefaultTheme#addCustomImages(java.util.Map)
	 */
	@Override
	public void addCustomImages(Map<String, ILnfResource> table) {
		super.addCustomImages(table);
		String imagePath = "org.eclipse.riena.example.client" + ":" + "/icons/ledred.png";
		table.put("treeDocumentLeaf.icon", new ImageLnfResource(imagePath)); //$NON-NLS-1$
		imagePath = "org.eclipse.riena.example.client" + ":" + "/icons/folder_favorite.png";
		table.put("treeFolderClosed.icon", new ImageLnfResource(imagePath)); //$NON-NLS-1$
		table.put("treeFolderOpen.icon", new ImageLnfResource(imagePath)); //$NON-NLS-1$
	}

}
