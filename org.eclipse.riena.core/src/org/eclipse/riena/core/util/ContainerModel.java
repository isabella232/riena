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

public class ContainerModel {

	private static final int CLIENT = 1;
	private static final int SERVER = 2;

	private static int containerModel = CLIENT;
	static {
		String s = System.getProperty("riena.container.model");
		if (s != null && s.equals("server")) {
			containerModel = SERVER;
		}
		Bundle[] bundles = Activator.getContext().getBundles();
		for (Bundle bundle : bundles) {
			if (bundle.getSymbolicName().startsWith("org.eclipse.equinox.http")) {
				containerModel = SERVER;
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
