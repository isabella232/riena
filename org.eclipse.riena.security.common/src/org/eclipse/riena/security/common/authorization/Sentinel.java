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
package org.eclipse.riena.security.common.authorization;

import java.security.Permission;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.security.common.Activator;

/**
 * This class can be used as an alternative to the Java SecurityManager if you
 * don't like to switch java security on but still like to check on business
 * permissions in the code. The Sentinel also says true or false which is easier
 * to handle than an AccessControlException.
 */
public final class Sentinel {

	private Helper helper = new Helper();
	private ISentinelService sentinelService;
	private static Sentinel myself = new Sentinel();

	private Sentinel() {
		super();
		Inject.service(ISentinelService.class).useRanking().into(helper).andStart(Activator.getDefault().getContext());
	}

	public static Sentinel getInstance() {
		return myself;
	}

	/**
	 * checkAccess reads the current Subject from the SubjectHolderService,
	 * reads all its permissions from the cache and checks if this permission is
	 * allowed for this subject
	 * 
	 * @param permission
	 *            permission to be checked
	 * @return
	 */
	public boolean checkAccess(Permission permission) {
		if (sentinelService == null) {
			return false;
		}
		return sentinelService.checkAccess(permission);
	}

	private class Helper {

		public void bind(ISentinelService sentinelServiceParm) {
			sentinelService = sentinelServiceParm;
		}

		public void unbind(ISentinelService sentinelServiceParm) {
			sentinelService = null;
		}

	}

}
