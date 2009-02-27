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
package org.eclipse.riena.ui.ridgets;

/**
 * window event.
 */
public class WindowEvent {
	private Object source;

	/**
	 * constructor.
	 * 
	 * @param source
	 *            the event source.
	 */
	public WindowEvent(Object source) {
		this.source = source;
	}

	/**
	 * @return event source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @param source
	 */
	public void setSource(Object source) {
		this.source = source;
	}
}
