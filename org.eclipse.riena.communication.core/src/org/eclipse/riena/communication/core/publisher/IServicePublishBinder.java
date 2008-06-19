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
package org.eclipse.riena.communication.core.publisher;

import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.osgi.framework.ServiceReference;

/**
 * 
 */
public interface IServicePublishBinder {

	public abstract void publish(ServiceReference ref, String url, String protocol);

	public abstract void unpublish(ServiceReference serviceReference);

	public RemoteServiceDescription[] getAllServices();

}