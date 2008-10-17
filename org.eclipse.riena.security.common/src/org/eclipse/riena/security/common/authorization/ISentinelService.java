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

/**
 * Interface for an SentinelService (an OSGi Service) that is call by the
 * Sentinel and checks whether the current user has enough permission for a
 * certain operation.
 */
public interface ISentinelService {

	boolean checkAccess(Permission permission);

}
