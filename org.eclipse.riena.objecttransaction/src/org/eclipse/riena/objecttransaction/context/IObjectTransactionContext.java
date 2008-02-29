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
package org.eclipse.riena.objecttransaction.context;

import org.eclipse.riena.objecttransaction.IObjectTransaction;

/**
 * An extension to IContext being eable to hold an IObjectTransaction
 * 
 */
public interface IObjectTransactionContext extends IContext {

	/**
	 * @return Returns the objectTransaction.
	 */
	IObjectTransaction getObjectTransaction();

	/**
	 * @param objectTransaction
	 *            The objectTransaction to set.
	 */
	void setObjectTransaction(IObjectTransaction pObjectTransaction);

}
