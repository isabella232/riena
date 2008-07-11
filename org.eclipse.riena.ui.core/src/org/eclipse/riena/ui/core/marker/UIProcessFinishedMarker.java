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
package org.eclipse.riena.ui.core.marker;

import org.eclipse.riena.core.marker.AbstractMarker;

public class UIProcessFinishedMarker extends AbstractMarker {

	private static final String ATTRIBUTE_FINISHED = "uiprocess.finished"; //$NON-NLS-1$

	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return (Boolean) getAttribute(ATTRIBUTE_FINISHED);
	}

	/**
	 * @param finished
	 *            the finished to set
	 */
	public void setFinished(boolean finished) {
		setAttribute(ATTRIBUTE_FINISHED, finished);
	}

}
