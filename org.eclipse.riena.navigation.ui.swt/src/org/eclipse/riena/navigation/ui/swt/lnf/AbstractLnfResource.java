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
package org.eclipse.riena.navigation.ui.swt.lnf;

import org.eclipse.swt.graphics.Resource;

/**
 * Abstract wrapper for resources of look and feel.
 */
public abstract class AbstractLnfResource implements ILnfResource {

	private Resource resource;

	/**
	 * Creates a new instance.
	 * 
	 * @param resource -
	 *            resource to wrap
	 */
	protected AbstractLnfResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfResource#dispose()
	 */
	public void dispose() {
		getResource().dispose();
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfResource#getResource()
	 */
	public Resource getResource() {
		return resource;
	}

}
