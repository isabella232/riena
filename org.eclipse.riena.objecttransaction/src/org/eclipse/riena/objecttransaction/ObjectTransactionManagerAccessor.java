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
package org.eclipse.riena.objecttransaction;

import org.eclipse.riena.internal.objecttransaction.impl.ObjectTransactionManagerImpl;

/**
 * Accessor to get access to the ObjectTransactionManager
 * 
 */
public final class ObjectTransactionManagerAccessor {

	private static ThreadLocal<IObjectTransactionManager> threadedObjectTransactionManager = new ThreadLocal<IObjectTransactionManager>();

	private ObjectTransactionManagerAccessor() {
		super();
		// utility
	}

	/**
	 * Returns the ObjectTransactionManager (currently defined as threaded)
	 * 
	 * @return ObjectTransactionManager
	 */
	public static IObjectTransactionManager fetchObjectTransactionManager() {
		IObjectTransactionManager otm = threadedObjectTransactionManager.get();
		if (otm == null) {
			otm = new ObjectTransactionManagerImpl();
			threadedObjectTransactionManager.set(otm);
		}
		return otm;
	}
}