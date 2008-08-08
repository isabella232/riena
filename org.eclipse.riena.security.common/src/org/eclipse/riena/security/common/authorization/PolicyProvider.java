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

import java.io.FilePermission;
import java.security.PermissionCollection;
import java.security.Permissions;

import javax.security.auth.Subject;

public class PolicyProvider {

	public PolicyProvider() {
		super();
	}

	public PermissionCollection getPermissions(Subject subject) {
		System.out.println("PolicyProvider: getPermissions"); //$NON-NLS-1$
		Permissions permissions = new Permissions();
		permissions.add(new FilePermission("foo.txt", "read")); //$NON-NLS-1$ //$NON-NLS-2$
		return permissions;
	}

}
