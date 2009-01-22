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
package org.eclipse.riena.navigation.ui.swt.presentation;

/**
 * Gives the access to the global presentation manager casting it to the
 * SwtViewProvider
 * 
 */
public final class SwtViewProviderAccessor {

	private static volatile SwtViewProvider provider;

	private SwtViewProviderAccessor() {
		// utility
	}

	public static SwtViewProvider getViewProvider() {

		if (provider == null) {
			provider = new SwtViewProvider();
		}

		return provider;
	}

}
