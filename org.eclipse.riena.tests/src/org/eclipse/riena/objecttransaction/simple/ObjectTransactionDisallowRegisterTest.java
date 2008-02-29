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