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
package org.eclipse.riena.security.common.authorization.internal;

import java.security.Permission;
import java.security.Permissions;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import javax.security.auth.Subject;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.cache.GenericObjectCache;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.ContainerModel;
import org.eclipse.riena.internal.security.common.Activator;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.common.authorization.IPermissionCache;
import org.osgi.service.log.LogService;

public class PermissionCache implements IPermissionCache {

	private GenericObjectCache permCache = new GenericObjectCache();
	private Logger LOGGER = Activator.getDefault().getLogger(PermissionCache.class.getName());

	public IAuthorizationService authService;

	public PermissionCache() {
		super();
		permCache.setName("PermissionCache");
		permCache.setHashMap(new HashMap<Subject, Permissions>());
		if (ContainerModel.isClient()) {
			permCache.setMinimumSize(1);
			permCache.setTimeout(999999000); // client permissions nearly
			// never timeout
		} else {
			permCache.setMinimumSize(100);
			permCache.setTimeout(360000);
		}
		Inject.service(IAuthorizationService.ID).useRanking().into(this).andStart(Activator.getContext());
	}

	public void bind(IAuthorizationService authService) {
		this.authService = authService;
	}

	public void unbind(IAuthorizationService authService) {
		if (authService == this.authService) {
			this.authService = null;
		}
	}

	public Permissions getPermissions(Subject subject) {
		Set<Principal> principals = subject.getPrincipals();
		Principal[] principalArray = principals.toArray(new Principal[principals.size()]);
		return getPermissions(principalArray);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.security.common.authorization.IPermissionCache#getPermissions(javax.security.auth.Subject)
	 */
	public Permissions getPermissions(Principal[] principals) {
		Permissions allPerms = new Permissions();
		ArrayList<Principal> missingPrincipals = new ArrayList<Principal>();

		for (Principal principal : principals) {
			Permissions perms = (Permissions) permCache.get(principal.getName());
			if (perms == null) {
				missingPrincipals.add(principal);
			} else {
				addPerms(allPerms, perms);
			}
		}
		if (missingPrincipals.size() > 0) {
			if (authService == null) {
				LOGGER.log(LogService.LOG_ERROR, "no authorization service to retrieve permissions");
				return null;
			}
			Principal[] mpArray = missingPrincipals.toArray(new Principal[missingPrincipals.size()]);
			Permissions[] permsArray = authService.getPermissions(mpArray);
			for (int i = 0; i < missingPrincipals.size(); i++) {
				permCache.put(missingPrincipals.get(i).getName(), permsArray[i]);
				addPerms(allPerms, permsArray[i]);
			}
		}
		// = authService.getPermissions(subject.getPrincipals().toArray(new
		// Principal[subject.getPrincipals().size()]));
		return allPerms;
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
