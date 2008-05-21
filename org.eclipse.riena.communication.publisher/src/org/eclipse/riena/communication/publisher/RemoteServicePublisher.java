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
package org.eclipse.riena.communication.publisher;

import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.communication.publisher.Activator;

/**
 * Publishes OSGi Service as Webservice Endpoint
 * 
 */
public class RemoteServicePublisher {

	public void publish(String name, String url, String protocol) {
		Inject.service(name).useRanking().into(this).andStart(Activator.getDefault().getContext());
	}

	public void unpublish(String name) {

	}

	protected void publish(RemoteServiceDescription rsd) {

	}

	protected void bind(Object service) {

	}

	protected void unbind(Object service) {

	}

}
