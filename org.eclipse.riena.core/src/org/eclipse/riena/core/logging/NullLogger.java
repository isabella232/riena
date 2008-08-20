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
package org.eclipse.riena.core.logging;

import org.eclipse.equinox.log.Logger;
import org.osgi.framework.ServiceReference;

class NullLogger implements Logger {

	public String getName() {
		return null;
	}

	public boolean isLoggable(int level) {
		return false;
	}

	public void log(int level, String message) {
	}

	public void log(int level, String message, Throwable exception) {
	}

	public void log(ServiceReference sr, int level, String message) {
	}

	public void log(ServiceReference sr, int level, String message, Throwable exception) {
	}

	public void log(Object context, int level, String message) {
	}

	public void log(Object context, int level, String message, Throwable exception) {
	}

}