/*******************************************************************************
 * Copyright (c) 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.objecttransaction;

import org.eclipse.riena.internal.objecttransaction.impl.ObjectTransactionFactoryImpl;

/**
 * Accessor that gives access to the ObjectTransactionFactory
 * 
 */
public final class ObjectTransactionFactoryAccessor {

	private static IObjectTransactionFactory singletonObjectTransactionFactory = null;

	private ObjectTransactionFactoryAccessor() {
		super();
		// utility
	}

	/**
	 * Returns the correct ObjectTransactionFactory (currently defined as
	 * singleton)
	 * 
	 * @return ObjectTransactionFactory
	 */
	public static IObjectTransactionFactory fetchObjectTransactionFactory() {
		if (singletonObjectTransactionFactory == null) {
			singletonObjectTransactionFactory = new ObjectTransactionFactoryImpl(ObjectTransactionManagerAccessor.fetchObjectTransactionManager());
		}
		return singletonObjectTransactionFactory;
	}
}