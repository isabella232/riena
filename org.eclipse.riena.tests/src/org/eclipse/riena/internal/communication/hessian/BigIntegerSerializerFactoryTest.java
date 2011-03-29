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
package org.eclipse.riena.internal.communication.hessian;

import java.math.BigInteger;

import org.eclipse.riena.internal.communication.factory.hessian.BigIntegerSerializerFactory;

/**
 * Test the {@code BigIntegerSerializerFactory} class.
 */
public class BigIntegerSerializerFactoryTest extends AbstractSerializerFactoryTestCase {

	public void testBigIntegerFail() {
		assertFalse(isBackAndForthOk(new BigInteger("42"), HessianSerializerVersion.Two, BigInteger.class));
	}

	public void testBigInteger() {
		assertTrue(isBackAndForthOk(new BigInteger("42"), HessianSerializerVersion.Two, BigInteger.class,
				new BigIntegerSerializerFactory()));
	}
}
