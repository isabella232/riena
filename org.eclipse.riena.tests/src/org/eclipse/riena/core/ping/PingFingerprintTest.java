/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.ping;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests class {@link PingFingerprint PingFingerprint}.
 */
@NonUITestCase
public class PingFingerprintTest extends TestCase {

	/**
	 * Tests method {@link PingFingerprint#PingFingerprint(IPingable)
	 * PingFingerprint(IPingable)}.
	 */
	public void testPingFingerprintIPingable() {
		try {
			new PingFingerprint(null);
			fail("Expected precondition violation");
		} catch (Exception e) {
		}
		IPingable pingable = new DefaultPingable() {
		};
		PingFingerprint fingerprint = new PingFingerprint(pingable);
		assertEquals(new PingFingerprint(pingable, true), fingerprint);
	}

	/**
	 * Tests method {@link PingFingerprint#PingFingerprint(IPingable, boolean)
	 * PingFingerprint(IPingable, boolean)}.
	 */
	public void testPingFingerprintIPingableBoolean() {
		try {
			new PingFingerprint(null, true);
			fail("Expected precondition violation");
		} catch (Exception e) {
		}
		IPingable pingable = new DefaultPingable() {
		};

		assertEquals("PingFingerprint[" + pingable.getClass().getName() + "]", new PingFingerprint(pingable, true)
				.toString());

		assertEquals(
				"PingFingerprint[" + pingable.getClass().getName() + "#" + System.identityHashCode(pingable) + "]",
				new PingFingerprint(pingable, false).toString());
	}

	/**
	 * Tests method {@link PingFingerprint#equals(Object) equals()} and
	 * {@link PingFingerprint#hashCode() hashCode()}.
	 */
	public void testEqualsAndHash() {
		IPingable pingable = new DefaultPingable() {
		};
		PingFingerprint fingerprintA = new PingFingerprint(pingable, "a");
		PingFingerprint fingerprintB = new PingFingerprint(pingable, "b");
		PingFingerprint fingerprintAOtherPingable = new PingFingerprint(new DefaultPingable() {
		}, "a");

		checkEqualsAndHashCode(true, fingerprintA, fingerprintA);
		checkEqualsAndHashCode(true, fingerprintA, new PingFingerprint(pingable, "a"));
		checkEqualsAndHashCode(false, fingerprintA, fingerprintB);
		checkEqualsAndHashCode(false, fingerprintA, fingerprintAOtherPingable);
		checkEqualsAndHashCode(false, fingerprintA, new Integer(3));
		checkEqualsAndHashCode(false, fingerprintA, null);
	}

	private void checkEqualsAndHashCode(boolean equalityExpected, Object first, Object second) {

		if (first == null) {
			assertEquals(equalityExpected, first == second);
			return;
		}
		assertEquals(equalityExpected, first.equals(second));
		if (second != null) {
			assertEquals(equalityExpected, first.hashCode() == second.hashCode());
		}
	}

}
