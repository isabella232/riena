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
package org.eclipse.riena.core.contract;

/**
 * Runtime exception which indicates post condition violation.
 * 
 */
@Deprecated
public class PostConditionException extends RuntimeException {

	/**
	 * version ID (controlled by CVS).
	 */
	public static final String VERSION_ID = "$Id$";

	/**
	 * constructor.
	 * 
	 * @param msgText
	 *            the message text.
	 */
	@Deprecated
	public PostConditionException(String msgText) {
		super(msgText);
	}
}