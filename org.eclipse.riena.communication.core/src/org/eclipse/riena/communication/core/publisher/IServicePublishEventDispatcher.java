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
package org.eclipse.riena.communication.core.publisher;

import org.eclipse.riena.communication.core.RemoteServiceDescription;

/**
 * Implementations of IServicePublishEventDispatcher provide a List of all
 * service end points for published OSGi Services within a container.
 * 
 * An implementation should be registered as OSGi Service and maybe published as
 * "remote" OSGi service itself.
 * 
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 * @see RemoteServiceDescription
 * 
 */
public interface IServicePublishEventDispatcher {

	/**
	 * 
	 * @return the list of service end points for published OSGi Services.
	 */
	RemoteServiceDescription[] getAllServices();
}
