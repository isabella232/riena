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
package org.eclipse.riena.communication.core.attachment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.easymock.MockControl;
import org.eclipse.riena.tests.RienaTestCase;

/**
 * Test the ByteArrayDataSource class.
 * 
 */
public class ByteArrayDataSourceTest extends RienaTestCase {

	private IDataSource dataSourceMock;
	private MockControl dataSourceControl;
	private final static String NAME = "Test";
	private final static String CONTENT_TYPE = "text/plain";

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	private void setUpDataSourceMock(int length) throws Exception {
		dataSourceControl = MockControl.createControl(IDataSource.class);
		dataSourceMock = (IDataSource) dataSourceControl.getMock();

		dataSourceMock.getName();
		dataSourceControl.setReturnValue(NAME);

		dataSourceMock.getInputStream();
		dataSourceControl.setReturnValue(getInputStream(length));

		dataSourceControl.replay();
	}

	private InputStream getInputStream(int length) {
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = (byte) i;
		}
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		if (dataSourceControl != null) {
			dataSourceControl.verify();
		}
		super.tearDown();
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testCreationEmpty() throws Exception {
		checkDataSource(0);
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testCreationLong() throws Exception {
		checkDataSource(10 * 1024);
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testCreationVeryLong() throws Exception {
		checkDataSource(1024 * 1024);
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testCreationVeryVeryLong() throws Exception {
		checkDataSource(10 * 1024 * 1024);
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testGetOutputStreamFail() throws Exception {
		setUpDataSourceMock(10);
		ByteArrayDataSource dataSource = new ByteArrayDataSource(dataSourceMock);
		try {
			dataSource.getOutputStream();
			fail("Should throw an IOException!");
		} catch (IOException ioe) {
			// ok();
		}
	}

	/**
	 * Nomen est Omen!
	 * 
	 * @throws Exception
	 */
	public void testNullDataSource() throws Exception {
		// This creation should not fail!
		ByteArrayDataSource dataSource = new ByteArrayDataSource(null);

		assertNull(dataSource.getName());
		assertNull(dataSource.getInputStream());
	}

	/**
	 * Check the ByteArrayDataSource with various lengths.
	 * 
	 * @param length
	 * @throws Exception
	 */
	private void checkDataSource(int length) throws Exception {
		setUpDataSourceMock(length);
		long before = System.currentTimeMillis();
		ByteArrayDataSource dataSource = new ByteArrayDataSource(dataSourceMock);
		println("Run with " + length + " bytes took " + (System.currentTimeMillis() - before) + " ms.");
		assertEquals(getInputStream(length), dataSource.getInputStream());
		assertEquals(NAME, dataSource.getName());
	}

	private void assertEquals(InputStream inputStream1, InputStream inputStream2) {
		int byte1 = 0;
		int byte2 = 0;
		while (byte1 != -1 && byte2 != -1) {
			try {
				byte1 = inputStream1.read();
				byte2 = inputStream2.read();
				assertEquals(byte1, byte2);
			} catch (IOException ioe) {
				fail(ioe.getMessage());
			}
		}
	}
}