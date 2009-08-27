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
package org.eclipse.riena.navigation.ui.swt.lnf;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Tests of the class <code>LnfManager</code>.
 */
@NonUITestCase
public class LnfManagerTest extends TestCase {

	/**
	 * Test of the method <code>getLnfClassName()</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetLnfClassName() throws Exception {

		String className = LnfManager.getLnfClassName();
		assertEquals(RienaDefaultLnf.class.getName(), className);

		LnfManager.setLnf(Object.class.getName());
		className = LnfManager.getLnfClassName();
		assertEquals(Object.class.getName(), className);

		LnfManager.setLnf((String) null);
		className = LnfManager.getLnfClassName();
		assertEquals(RienaDefaultLnf.class.getName(), className);

	}

	/**
	 * Test of the method <code>getLnf()</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetLnf() throws Exception {

		RienaDefaultLnf lnf = LnfManager.getLnf();
		assertNotNull(lnf);
		assertEquals(lnf.getClass().getName(), LnfManager.getLnfClassName());

	}

	/**
	 * Test of the method <code>dispose()</code>.
	 * 
	 * @throws Exception
	 */
	public void testDispose() throws Exception {

		RienaDefaultLnf lnf = LnfManager.getLnf();
		assertNotNull(lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND));
		LnfManager.dispose();
		assertNull(lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_FOREGROUND));

	}

}
