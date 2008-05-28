/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

/**
 * window event.
 * 
 * @author Juergen Becker
 */
public class WindowEvent {
	private Object source;

	/**
	 * constructor.
	 * 
	 * @param source the event source.
	 */
	public WindowEvent( Object source ) {
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
	public void setSource( Object source ) {
		this.source = source;
	}
}