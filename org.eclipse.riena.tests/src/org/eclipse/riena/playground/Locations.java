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
package org.eclipse.riena.playground;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.riena.internal.tests.Activator;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 */
public class Locations extends TestCase {

	public void testLocations() {
		System.out.println("Platform.getConfigurationLocation: " + Platform.getConfigurationLocation().getURL());
		System.out.println("Platform.getInstallLocation: " + Platform.getInstallLocation().getURL());
		System.out.println("Platform.getInstanceLocation: " + Platform.getInstanceLocation().getURL());
		System.out.println("Platform.getLocation: " + Platform.getLocation());
		System.out.println("Platform.getLogFileLocation: " + Platform.getLogFileLocation());
		System.out.println("Platform.getUserLocation: " + Platform.getUserLocation().getURL());

		System.out.println("Location.CONFIGURATION_FILTER: " + getLocation(Location.CONFIGURATION_FILTER));
		System.out.println("Location.INSTALL_FILTER: " + getLocation(Location.INSTALL_FILTER));
		System.out.println("Location.INSTANCE_FILTER: " + getLocation(Location.INSTANCE_FILTER));
		System.out.println("Location.ECLIPSE_HOME_FILTER: " + getLocation(Location.ECLIPSE_HOME_FILTER));
		System.out.println("Location.USER_FILTER: " + getLocation(Location.USER_FILTER));

		File xXxData = Activator.getDefault().getBundle().getBundleContext().getDataFile("xXx");
		System.out.println(xXxData);
		System.out.println(Activator.getDefault().getBundle().getBundleContext().getProperty("osgi.install.area"));
	}

	private URL getLocation(String filterString) {
		Filter filter = null;
		try {
			filter = Activator.getDefault().getContext().createFilter(filterString);
		} catch (InvalidSyntaxException e) {
			// ignore this.  It should never happen as we have tested the above format.
		}
		ServiceTracker location = new ServiceTracker(Activator.getDefault().getContext(), filter, null);
		location.open();

		return (URL) ((Location) location.getService()).getURL();

	}
}
