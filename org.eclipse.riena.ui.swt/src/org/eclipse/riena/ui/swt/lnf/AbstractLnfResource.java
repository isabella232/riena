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
package org.eclipse.riena.ui.swt.lnf;

import org.eclipse.swt.graphics.Resource;

/**
 * Abstract wrapper for resources of look and feel.
 */
public abstract class AbstractLnfResource implements ILnfResource {

	private Resource resource;

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfResource#dispose()
	 */
	public void dispose() {
		if (resource != null) {
			resource.dispose();
			resource = null;
		}
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfResource#getResource()
	 */
	public Resource getResource() {
		if ((resource == null) || (resource.isDisposed())) {
			resource = createResource();
		}
		return resource;
	}

}
