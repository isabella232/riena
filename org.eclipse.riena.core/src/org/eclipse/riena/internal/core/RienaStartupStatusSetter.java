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
package org.eclipse.riena.internal.core;

import org.eclipse.riena.core.RienaStartupStatus;

/**
 * 
 */
public class RienaStartupStatusSetter extends RienaStartupStatus {

	private boolean started;

	public boolean isStarted() {
		return started;
	}

	void setStarted(boolean started) {
		this.started = started;
	}

}
