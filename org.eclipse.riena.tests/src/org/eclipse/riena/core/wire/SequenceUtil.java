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
package org.eclipse.riena.core.wire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

/**
 *
 */
public final class SequenceUtil {

	private SequenceUtil() {
		// util
	}

	private static List<Class<? extends IWiring>> sequence;

	public static void init() {
		sequence = new ArrayList<Class<? extends IWiring>>();
	}

	/**
	 * @param clazz
	 */
	public static void add(Class<? extends IWiring> clazz) {
		if (sequence == null) {
			return;
		}
		sequence.add(clazz);
	}

	public static void assertExpected(Class<? extends IWiring>... wirings) {
		List<Class<? extends IWiring>> temp = Arrays.asList(wirings);
		Assert.assertEquals(temp, sequence);
	}
}
