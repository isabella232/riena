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
package org.eclipse.riena.objecttransaction.simple;

import org.eclipse.riena.objecttransaction.IObjectTransaction;
import org.eclipse.riena.objecttransaction.ObjectTransactionFactoryAccessor;
import org.eclipse.riena.objecttransaction.simple.value.Addresse;
import org.eclipse.riena.tests.RienaTestCase;

/**
 * TODO Fehlender Klassen-Kommentar
 * 
 * @author Christian Campo
 */
public class ObjectTransactionDisallowRegisterTest extends RienaTestCase {

	public void setUp() throws Exception {
		super.setUp();
		// this.setTraceOn(false);
		// trainModules("META-INF/hivetestmodule.xml");
		// replay();
	}

	public void tearDown() throws Exception {
		// verify();
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testSimpleAllowRegister() {
		IObjectTransaction objectTransaction = ObjectTransactionFactoryAccessor.fetchObjectTransactionFactory()
				.createObjectTransaction();
		objectTransaction.allowRegister(false);

		Addresse addresse = new Addresse(true);
		assertTrue("kunde must not be registered", !objectTransaction.isRegistered(addresse));

		objectTransaction.allowRegister(true);
		addresse = new Addresse(true);
		assertTrue("kunde must be registered", objectTransaction.isRegistered(addresse));

		showStatus("testSimpleAllNew", objectTransaction);
	}

	private void showStatus(String testName, IObjectTransaction objectTransaction) {
		System.out.println("testname >>>>>" + testName + "<<<<<");
		System.out.println(objectTransaction);
	}
}