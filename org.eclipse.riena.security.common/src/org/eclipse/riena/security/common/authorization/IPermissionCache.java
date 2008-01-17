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

import java.security.Permissions;
import java.security.Principal;

import javax.security.auth.Subject;

public interface IPermissionCache {

	public String ID = IPermissionCache.class.getName();

	public Permissions getPermissions(Subject subject);

	public Permissions getPermissions(Principal[] principals);

	public void purgePermissions(Principal principal);

	public void purgePermissions(Subject subject);

}