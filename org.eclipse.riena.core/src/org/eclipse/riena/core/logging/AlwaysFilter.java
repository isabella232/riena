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
package org.eclipse.riena.core.logging;

import org.eclipse.equinox.log.LogFilter;
import org.osgi.framework.Bundle;

public class AlwaysFilter implements LogFilter {

	public boolean isLoggable(Bundle b, String loggerName, int logLevel) {
		return true;
	}

}