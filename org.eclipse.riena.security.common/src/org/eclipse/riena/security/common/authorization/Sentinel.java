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
package org.eclipse.riena.security.common.authorization;

import java.security.Permission;
import java.security.Permissions;

import javax.security.auth.Subject;

import org.eclipse.riena.core.service.ServiceId;
import org.eclipse.riena.internal.security.common.Activator;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.ISubjectHolderService;

/**
 * This class can be used as an alternative to the Java SecurityManager if you
 * don't like to switch java security on but still like to check on business
 * permissions in the code. The Sentinel also says true or false which is easier
 * to handle than an AccessControlException.
 */
public class Sentinel {

	private static Sentinel singletonSentinel = new Sentinel();
	private IPermissionCache permCache;
	private ISubjectHolderService subjectHolderService;

	private Sentinel() {
		super();
		new ServiceId(IPermissionCache.ID).injectInto(this).andStart(Activator.getContext());
		new ServiceId(ISubjectHolderService.ID).injectInto(this).andStart(Activator.getContext());
	}

	public void bind(IPermissionCache permCache) {
		this.permCache = permCache;
	}

	public void unbind(IPermissionCache permCache) {
		if (permCache == this.permCache) {
			this.permCache = null;
		}
	}

	public void bind(ISubjectHolderService subjectHolderService) {
		this.subjectHolderService = subjectHolderService;
	}

	public void unbind(ISubjectHolderService subjectHolderService) {
		if (subjectHolderService == this.subjectHolderService) {
			this.subjectHolderService = null;
		}
	}

	protected static Sentinel getInstance() {
		return singletonSentinel;
	}

	protected IPermissionCache getPermissionCache() {
		return permCache;
	}

	protected ISubjectHolderService getSubjectHolderService() {
		return subjectHolderService;
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
	public static boolean checkAccess(Permission permission) {
		Sentinel sentinel = Sentinel.getInstance();
		ISubjectHolder subjectHolder = sentinel.getSubjectHolderService().fetchSubjectHolder();
		Subject subject = subjectHolder.getSubject();
		if (subject != null) {
			Permissions permissions = Sentinel.getInstance().getPermissionCache().getPermissions(subject);
			boolean result = permissions.implies(permission);
			return result;
		} else {
			return false;
		}
	}
}
