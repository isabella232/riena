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
package org.eclipse.riena.core.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;

/**
 *
 */
public class PlatformVariableResolver {

	private static final String PLATFORM = "platform."; //$NON-NLS-1$

	public String resolveValue(IDynamicVariable variable, String argument) throws CoreException {
		if (variable.getName().startsWith(PLATFORM)) {
			return System.getProperty(variable.getName().substring(PLATFORM.length()));
		}
		return null;
	}

}
