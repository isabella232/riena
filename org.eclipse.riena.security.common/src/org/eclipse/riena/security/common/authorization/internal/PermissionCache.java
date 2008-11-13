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
package org.eclipse.riena.security.common.authorization.internal;

import java.security.Permission;
import java.security.Permissions;
import java.security.Principal;
import java.util.Enumeration;

import javax.security.auth.Subject;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.cache.GenericObjectCache;
import org.eclipse.riena.core.util.ContainerModel;
import org.eclipse.riena.internal.security.common.Activator;
import org.eclipse.riena.security.common.authorization.IPermissionCache;

public class PermissionCache implements IPermissionCache {

	private final GenericObjectCache<String, Permissions> permCache = new GenericObjectCache<String, Permissions>();

	private static final Logger LOGGER = Activator.getDefault().getLogger(PermissionCache.class);

	public PermissionCache() {
		super();
		permCache.setName("PermissionCache"); //$NON-NLS-1$
		if (ContainerModel.isClient()) {
			permCache.setMinimumSize(1);
			permCache.setTimeout(999999000); // client permissions nearly
			// never timeout
		} else {
			permCache.setMinimumSize(100);
			permCache.setTimeout(360000);
		}
	}

	public Permissions getPermissions(Principal principal) {
		return permCache.get(principal.getName());
	}

	public void putPermissions(Principal principal, Permissions permissions) {
		permCache.put(principal.getName(), permissions);

	}

	private Permissions addPerms(Permissions allPerms, Permissions perms) {
		Enumeration<Permission> enumPerms = perms.elements();
		while (enumPerms.hasMoreElements()) {
			Permission perm = enumPerms.nextElement();
			allPerms.add(perm);
		}

		return allPerms;
	}

	public void purgePermissions(Subject subject) {
	}

	public void purgePermissions(Principal principal) {
	}
}
