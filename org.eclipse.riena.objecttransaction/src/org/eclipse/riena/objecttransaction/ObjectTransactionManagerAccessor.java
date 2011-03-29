/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.objecttransaction;

/**
 * Accessor to get access to the ObjectTransactionManager
 * 
 * @deprecated use instead {@code ObjectTransactionManager.getInstance()}
 */
@Deprecated
public final class ObjectTransactionManagerAccessor {

	private ObjectTransactionManagerAccessor() {
		// utility
	}

	/**
	 * Returns the ObjectTransactionManager (currently defined as threaded)
	 * 
	 * @return ObjectTransactionManager
	 * @deprecated use instead {@code ObjectTransactionManager.getInstance()}
	 */
	@Deprecated
	public static IObjectTransactionManager fetchObjectTransactionManager() {
		return ObjectTransactionManager.getInstance();
	}
}