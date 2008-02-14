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
package org.eclipse.riena.core.util;

import org.eclipse.riena.internal.core.Activator;
import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

/**
 * This class can be used by other to find out whether the container they are
 * running in is a Riena client or a Riena server container. The consequence can
 * be things like storing objects in a singleton (for client) vs storing them in
 * a ThreadLocal (server) or others. The class can be driven be the
 * system.property "riena.container.model" that can be set to "client" or
 * "server". The ContainerModel also checks to see whether there is a bundle
 * called org.eclipse.equinox.http in which case it assumes a server model. In
 * any case, it creates a LOG_INFO entry to post what it has chosen.
 */
public class ContainerModel {

	private static final int CLIENT = 1;
	private static final int SERVER = 2;

	private static int containerModel = CLIENT;
	static {
		String s = System.getProperty("riena.container.model");
		if (s != null) {
			if (s.equals("server")) {
				containerModel = SERVER;
			}
		} else {
			Bundle[] bundles = Activator.getContext().getBundles();
			for (Bundle bundle : bundles) {
				if (bundle.getSymbolicName().startsWith("org.eclipse.equinox.http")) {
					containerModel = SERVER;
				}
			}
		}
		if (containerModel == SERVER) {
			Activator.getDefault().getLogger(ContainerModel.class.getName()).log(LogService.LOG_INFO,
					"!!! Riena is running in SERVERMODEL !!!");
		}
	}

	public static boolean isClient() {
		if (containerModel == CLIENT) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isServer() {
		if (containerModel == SERVER) {
			return true;
		} else {
			return false;
		}
	}

}
