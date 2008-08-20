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

import org.eclipse.riena.objecttransaction.delta.TransactionDelta;

/**
 * The interface for an extract that the ObjectTransaction imports and exports
 * and which contains the modififactions of the transactedObjects.
 * 
 */
public interface IObjectTransactionExtract {

	/**
	 * Returns the deltas stored in this objectransactionextract
	 * 
	 * @return array of deltas
	 */
	TransactionDelta[] getDeltas();

	/**
	 * Adds an existing transacted object to the extract with status clean.
	 * 
	 * @param transObject
	 * @pre !contains(transObject.getObjectId())
	 */
	void addCleanTransactedObject(ITransactedObject transObject);

	/**
	 * Method to check if an object is already part of an extract
	 * 
	 * @param objectid
	 * @return
	 */
	boolean contains(IObjectId objectid);

}