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
package org.eclipse.riena.objecttransaction.context;

/**
 * Any action can/must be done in a context of something else. This interface
 * desribes a hook beening submitted to different places in the application
 * which must work in a context. A context mus be able to manage a replace of
 * another context, or build a context stack, when passivated than set the old
 * from the stact back.
 * 
 */

public interface IContext {

	/**
	 * Activates the related context
	 */
	void activate();

	/**
	 * Passivate the related context
	 */
	void passivate();

	/**
	 * @return true if the context is active otherwise false
	 */
	boolean isActivated();

}
