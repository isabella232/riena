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
package org.eclipse.riena.core.injector.extension;

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.internal.core.injector.extension.ExtensionPointId;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 *
 */
@NonUITestCase
public class ExtensionPointIdTest extends RienaTestCase {

	public void testCreateEmptyExtensionPoint1() {
		ExtensionPointId id = new ExtensionPointId();
		try {
			id.normalize(IData.class);
		} catch (IllegalStateException e) {
			Nop.reason("expected");
		}
	}

	public void testCreateEmptyExtensionPoint2() {
		ExtensionPointId id = new ExtensionPointId(null);
		try {
			id.normalize(IData.class);
		} catch (IllegalStateException e) {
			Nop.reason("expected");
		}
	}

	public void testCreateEmptyExtensionPoint3() {
		ExtensionPointId id = new ExtensionPointId("");
		try {
			id.normalize(IData.class);
		} catch (IllegalStateException e) {
			Nop.reason("expected");
		}
	}

	public void testExtensionPointWithOneFQId() {
		ExtensionPointId id = new ExtensionPointId("id.one");
		id.normalize(IData.class);
		String result = "";
		for (String str : id.compatibleIds()) {
			result += str;
		}
		assertEquals("id.one", result);
	}

	public void testExtensionPointWithTwoFQId() {
		ExtensionPointId id = new ExtensionPointId("id.one,id.two");
		id.normalize(IData.class);
		String result = "";
		for (String str : id.compatibleIds()) {
			result += str;
		}
		assertEquals("id.oneid.two", result);
	}

	public void testExtensionPointWithTthreeFQId() {
		ExtensionPointId id = new ExtensionPointId("id.one,id.two,id.three");
		id.normalize(IData.class);
		String result = "";
		for (String str : id.compatibleIds()) {
			result += str;
		}
		assertEquals("id.oneid.twoid.three", result);
	}

	public void testExtensionPointWithOneSimpleId() {
		ExtensionPointId id = new ExtensionPointId("one");
		id.normalize(IData.class);
		String result = "";
		for (String str : id.compatibleIds()) {
			result += str;
		}
		String symbolicName = getContext().getBundle().getSymbolicName();
		assertEquals(symbolicName + ".one", result);
	}

	public void testExtensionPointWithTwoSimpleId() {
		ExtensionPointId id = new ExtensionPointId("one,two");
		id.normalize(IData.class);
		String result = "";
		for (String str : id.compatibleIds()) {
			result += str;
		}
		String symbolicName = getContext().getBundle().getSymbolicName();
		assertEquals(symbolicName + ".one" + symbolicName + ".two", result);
	}

	public void testExtensionPointWithThreeSimpleId() {
		ExtensionPointId id = new ExtensionPointId("one,two,three");
		id.normalize(IData.class);
		String result = "";
		for (String str : id.compatibleIds()) {
			result += str;
		}
		String symbolicName = getContext().getBundle().getSymbolicName();
		assertEquals(symbolicName + ".one" + symbolicName + ".two" + symbolicName + ".three", result);
	}

	public void testExtensionPointWithMixedId() {
		ExtensionPointId id = new ExtensionPointId("id.one,two,id.three");
		id.normalize(IData.class);
		String result = "";
		for (String str : id.compatibleIds()) {
			result += str;
		}
		String symbolicName = getContext().getBundle().getSymbolicName();
		assertEquals("id.one" + symbolicName + ".two" + "id.three", result);
	}

	public void testFailBecauseOfNoNormalize() {
		ExtensionPointId id = new ExtensionPointId("id.one,two,id.three");
		try {
			id.compatibleIds();
		} catch (IllegalStateException e) {
			Nop.reason("ok");
		}
	}
}
