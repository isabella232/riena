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
package org.eclipse.riena.objecttransaction.context;

/**
 * A helper supporting the activation and passivation of a context
 * 
 */

public final class ContextHelper {

	private ContextHelper() {
		// there is no instance of the Helper
	}

	/**
	 * Activates aContext
	 * 
	 * @param pContext
	 *            context
	 */
	public static void activateContext(IContext pContext) {
		if (pContext != null) {
			pContext.activate();
		}
	}

	/**
	 * Passivates aContext
	 * 
	 * @param pContext
	 *            context
	 */
	public static void passivateContext(IContext pContext) {
		if (pContext != null) {
			pContext.passivate();
		}
	}

	/**
	 * Ascertains the right context to set to a Context carrier. Possibly the
	 * setted context has to be activated, dependent on the state of the old
	 * context. This shuld be then done on this central place
	 * 
	 * @param pContext
	 *            context
	 */
	public static IContext setContext(IContext pOldContext, IContext pNewContext) {
		return pNewContext;
	}

}
