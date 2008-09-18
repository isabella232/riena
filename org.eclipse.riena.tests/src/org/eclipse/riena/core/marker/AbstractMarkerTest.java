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
package org.eclipse.riena.core.marker;

import junit.framework.TestCase;

/**
 * Tests of the class {@link AbstractMarker}.
 */
public class AbstractMarkerTest extends TestCase {

	/**
	 * Test of the constructor {@code AbstractMarker()}.
	 */
	public void testAbstractMarker() {

		AbstractMarker marker = new DummyMarker();
		assertTrue(marker.isUnique());

	}

	/**
	 * Test of the method {@code toString()}.
	 */
	public void testToString() {

		AbstractMarker marker = new DummyMarker();
		assertEquals("DummyMarker[attributes={}]", marker.toString());

		marker.setAttribute("att", "val");
		assertEquals("DummyMarker[attributes={att=val}]", marker.toString());

	}

	/**
	 * Test of the method {@code setAttribute}.
	 */
	public void testSetAttribute() {

		DummyMarker marker = new DummyMarker();
		assertNull(marker.getAttribute("lastname"));

		marker.reset();
		marker.setAttribute("lastname", "Gate");
		assertEquals("Gate", marker.getAttribute("lastname"));
		assertTrue(marker.isChangeFired());

		marker.reset();
		marker.setAttribute("lastname", "Ballmer");
		assertEquals("Ballmer", marker.getAttribute("lastname"));
		assertTrue(marker.isChangeFired());

		marker.reset();
		marker.setAttribute("lastname", null);
		assertNull(marker.getAttribute("lastname"));
		assertTrue(marker.isChangeFired());

		marker.reset();
		marker.setAttribute("lastname", 12);
		assertEquals(new Integer(12), marker.getAttribute("lastname"));
		assertTrue(marker.isChangeFired());

		marker.reset();
		try {
			marker.setAttribute(null, 12);
			fail("Exception expectd");
		} catch (Exception e) {
			// OK
		}
		assertFalse(marker.isChangeFired());

	}

	/**
	 * Test of the method {@code getAttribute(String, Object)}.
	 */
	public void testGetAttribute() {

		DummyMarker marker = new DummyMarker();

		assertEquals("unkown", marker.getAttribute("lastname", "unkown"));

		marker.setAttribute("lastname", "Gate");
		assertEquals("Gate", marker.getAttribute("lastname", "unkown"));

		marker.setAttribute("lastname", null);
		assertEquals("unkown", marker.getAttribute("lastname", "unkown"));

	}

	private class DummyMarker extends AbstractMarker implements IMarkerAttributeChangeListener {

		private boolean changeFired;

		public DummyMarker() {
			addAttributeChangeListener(this);
			reset();
		}

		public void attributesChanged() {
			changeFired = true;
		}

		public void reset() {
			changeFired = false;
		}

		public boolean isChangeFired() {
			return changeFired;
		}

	}

}
