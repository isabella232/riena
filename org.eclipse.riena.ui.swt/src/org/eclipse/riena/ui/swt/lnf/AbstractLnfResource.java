/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
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

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.WeakRef;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Abstract wrapper for resources of look and feel.
 */
public abstract class AbstractLnfResource<T extends Resource> implements ILnfResource<T> {

	private WeakRef<T> resourceRef;

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfResource#dispose()
	 * 
	 * @deprecated it is no longer necessary to dispose {@code Resource}s
	 *             explicitly.
	 */
	@Deprecated
	public void dispose() {
		Nop.reason("deprecated"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfResource#getResource()
	 */
	public T getResource() {
		if (resourceRef == null) {
			return createRefedResource();
		}
		T resource = resourceRef.get();
		if (SwtUtilities.isDisposed(resource)) {
			return createRefedResource();
		}
		return resource;
	}

	private T createRefedResource() {
		final T resource = createResource();
		resourceRef = new WeakRef<T>(resource, new Runnable() {
			public void run() {
				SwtUtilities.disposeResource(resource);
			}
		});
		return resource;
	}

}
