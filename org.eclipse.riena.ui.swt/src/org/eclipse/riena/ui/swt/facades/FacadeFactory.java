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
package org.eclipse.riena.ui.swt.facades;

import org.eclipse.osgi.util.NLS;

/**
 * TODO [ev] docs
 * 
 * @since 2.0
 */
final class FacadeFactory {

	static Object newFacade(final Class<?> type) {
		String name = type.getName();
		try {
			return type.getClassLoader().loadClass(name + "Impl").newInstance(); //$NON-NLS-1$
		} catch (Throwable throwable) {
			String msg = NLS.bind("Could not load implementation for {0}", name); //$NON-NLS-1$
			throw new RuntimeException(msg, throwable);
		}
	}

}
