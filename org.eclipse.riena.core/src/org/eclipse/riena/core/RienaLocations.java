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
package org.eclipse.riena.core;

import java.io.File;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.riena.core.util.StringUtils;
import org.osgi.framework.Bundle;

/**
 * Riena locations.
 */
public final class RienaLocations {

	public static final String RIENA_NAME = "riena"; //$NON-NLS-1$

	private RienaLocations() {
		// utility class
	}

	/**
	 * Return the data area.<br>
	 * <b>Note:</b> This version creates a special folder within the Eclipse
	 * install location.
	 * 
	 * @param bundle
	 * @return
	 */
	public static File getDataArea() {
		Location installLocation = Platform.getInstallLocation();
		Assert.isLegal(installLocation != null, "Platform.getInstallLocation() should not be null."); //$NON-NLS-1$
		File dataArea = new File(installLocation.getURL().getFile(), RIENA_NAME);
		if (dataArea.isDirectory()) {
			return dataArea;
		}
		boolean created = dataArea.mkdirs();
		Assert.isLegal(created, "data area is not a directory or does not exist."); //$NON-NLS-1$
		return dataArea;
	}

	/**
	 * Return the data area for the specified bundle.<br>
	 * <b>Note:</b> This version creates a special folder within the Eclipse
	 * install location.
	 * 
	 * @param bundle
	 * @return
	 */
	public static File getDataArea(Bundle bundle) {
		Assert.isLegal(bundle != null, "bundle must not be null."); //$NON-NLS-1$
		String symbolicName = bundle.getSymbolicName();
		Assert.isLegal(StringUtils.isGiven(symbolicName), "no symbolic name for bundle."); //$NON-NLS-1$
		File dataArea = new File(getDataArea(), symbolicName);
		if (dataArea.isDirectory()) {
			return dataArea;
		}
		boolean created = dataArea.mkdirs();
		Assert.isLegal(created, "data area is not a directory or does not exist."); //$NON-NLS-1$
		return dataArea;
	}
}
