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

import java.security.Permissions;
import java.security.Principal;

import javax.security.auth.Subject;

public interface IPermissionCache {

	Permissions getPermissions(Subject subject);

	Permissions getPermissions(Principal[] principals);

	void purgePermissions(Principal principal);

	void purgePermissions(Subject subject);

}
