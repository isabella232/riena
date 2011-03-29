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
 * Accessor that gives access to the ObjectTransactionFactory
 * 
 * @deprecated use instead {@code ObjectTransactionFactory.getInstance()}
 */
@Deprecated
public final class ObjectTransactionFactoryAccessor {

	private ObjectTransactionFactoryAccessor() {
		super();
		// utility
	}

	/**
	 * Returns the correct ObjectTransactionFactory (currently defined as
	 * singleton)
	 * 
	 * @return ObjectTransactionFactory
	 * @deprecated use instead {@code ObjectTransactionFactory.getInstance()}
	 */
	@Deprecated
	public static IObjectTransactionFactory fetchObjectTransactionFactory() {
		return ObjectTransactionFactory.getInstance();
	}
}